package de.ypsilon.kitkrauler;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class Execution {
    private final WebDriver driver;
    private WebDriverWait wait;
    private String gguid;

    private String uName;
    private String pw;

    private TrayIcon trayIcon;

    private boolean firstInsert = true;
    private HashMap<String, String> data = new HashMap<>();

    public Execution(WebDriver driver) throws AWTException {
        this.driver = driver;

        execute();
    }

    public Execution(WebDriver driver, String[] credentials) throws AWTException {
        this.uName = credentials[0];
        this.pw = credentials[1];

        this.driver = driver;
        execute();
    }

    public void execute() throws AWTException {
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
            System.out.println(driver.getTitle() + " - Das KIT bleibt handlungsfähig");

            driver.findElement(By.id("hello")).findElement(By.className("login-link")).click();

            // Login
            login();

            wait.until(presenceOfElementLocated(By.id("hello")));

            driver.navigate().to("https://campus.studium.kit.edu/exams/registration.php");

            wait.until(presenceOfElementLocated(By.id("registration")));
            driver.switchTo().frame(driver.findElement(By.id("registration")));
            gguid = driver.findElement(By.name("gguid")).getAttribute("value");

            fetchData();

            while (true) {
                Thread.sleep(TimeUnit.SECONDS.toMillis(20));
                update();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private void login() {
        if (this.uName != null || this.pw != null) {
            driver.findElement(By.id("name")).sendKeys(this.uName);
            driver.findElement(By.id("password")).sendKeys(this.pw + Keys.ENTER);
        }
    }

    private void update() {
        driver.navigate().refresh();
        wait.until(presenceOfElementLocated(By.name("gguid")));
        gguid = driver.findElement(By.name("gguid")).getAttribute("value");
        fetchData();
    }

    private void fetchData() {
        WebElement content = driver.findElement(By.id("contract_" + gguid)).findElement(By.className("tablecontent"));
        for (WebElement row : content.findElements(By.tagName("tr"))) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            String subj = tds.get(0).findElement(By.tagName("a")).getText();
            String grade = row.findElement(By.className("nowrap")).getText();

            addToHashMap(subj, grade);
            firstInsert = false;
        }
    }

    private void addToHashMap(String key, String value) {
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