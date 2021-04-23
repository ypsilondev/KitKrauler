package de.ypsilon.kitkrauler;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class KitCrawler {

    private static boolean firstInsert = true;
    private static HashMap<String, String> data = new HashMap<>();
    private static TrayIcon trayIcon;

    private static WebDriver driver;
    private static WebDriverWait wait;

    private static boolean update = false;
    private static String gguid;

    public static void main(String[] args) throws AWTException {
        driver = new EdgeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(60).getSeconds());

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            Image img = Toolkit.getDefaultToolkit().getImage("images/img.jpg");
            trayIcon = new TrayIcon(img, "KIT Krauler");
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> {
                trayIcon.displayMessage("KIT", "Ich gehe noch aber beim KIT arbeitet keiner", TrayIcon.MessageType.INFO);
            });

            tray.add(trayIcon);
        }


        try {
            driver.get("https://campus.studium.kit.edu/");
            System.out.println(driver.getTitle());

            driver.findElement(By.id("hello")).findElement(By.className("login-link")).click();

            // Login

            wait.until(presenceOfElementLocated(By.id("hello")));

            driver.navigate().to("https://campus.studium.kit.edu/exams/registration.php");

            wait.until(presenceOfElementLocated(By.id("registration")));
            driver.switchTo().frame(driver.findElement(By.id("registration")));
            gguid = driver.findElement(By.name("gguid")).getAttribute("value");

            System.out.println(gguid);

            fetchData();

            while (true) {
                Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void update() throws InterruptedException {
        update = true;
        driver.navigate().refresh();
        wait.until(presenceOfElementLocated(By.name("gguid")));
        update = false;
        gguid = driver.findElement(By.name("gguid")).getAttribute("value");
        fetchData();
    }

    private static void fetchData() {
        WebElement content = driver.findElement(By.id("contract_" + gguid)).findElement(By.className("tablecontent"));
        for (WebElement row : content.findElements(By.tagName("tr"))) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            String subj = tds.get(0).findElement(By.tagName("a")).getText();
            String grade = row.findElement(By.className("nowrap")).getText();

            addToHashMap(subj, grade);
            firstInsert = false;
        }
    }

    private static void addToHashMap(String key, String value) {
        if (!data.containsKey(key)) {
            data.put(key, value);
        } else {
            if (!data.get(key).equals(value)) {
                data.put(key, value);
                if (!firstInsert) {
                    String update = String.format("key updated (%s) to %s.%n", key, value);
                    if (trayIcon != null) {
                        trayIcon.displayMessage("KIT UPDATE!!11111", update, TrayIcon.MessageType.INFO);
                    }
                    System.out.println(update);
                }
            }
        }
    }

}
