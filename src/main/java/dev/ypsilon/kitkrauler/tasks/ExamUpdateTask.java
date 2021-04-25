package dev.ypsilon.kitkrauler.tasks;

import dev.ypsilon.kitkrauler.CurrentProfile;
import dev.ypsilon.kitkrauler.Execution;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ExamUpdateTask implements Consumer<WebDriver> {

    public static final ExamUpdateTask INSTANCE = new ExamUpdateTask();

    public static void run(WebDriver driver) {
        INSTANCE.accept(driver);
    }

    @Override
    public void accept(WebDriver driver) {
        CurrentProfile.get().setGguid(driver.findElement(By.name("gguid")).getAttribute("value"));
        fetchData(driver);
    }

    private void fetchData(WebDriver driver) {
        // Load the new token
        driver.navigate().to("https://campus.studium.kit.edu/token.php");
        String tokenPageHtml = driver.getPageSource();
        // remove HTML
        tokenPageHtml = "{" + tokenPageHtml.split(">\\{")[1];
        tokenPageHtml = tokenPageHtml.split("\\}<")[0] + "}"; // TODO: check if the backslash is redundant
        // Parse as JSON
        JSONObject json = new JSONObject(tokenPageHtml);
        String token = json.getString("tokenA");

        // Navigation to campus.kit.edu required due to CORS
        driver.navigate().to("https://campus.kit.edu/");

        String tguid = ""; // TODO figure out what this is used for...
        String url = String.format("https://campus.kit.edu/sp/campus/student/contractview.asp?gguid=%s&tguid=%s&pguid=%s&lang=de&login-token=%s", CurrentProfile.get().getGguid(), tguid, CurrentProfile.get().getGguid(), token);

        // Load the table-HTML and put in DOM
        // String tableLoaderJS = "function replacePage(e){let t=document.open(\"text/html\",\"replace\");t.write(e),t.close()}function getData(e){let t=new XMLHttpRequest;return t.open(\"GET\",e,!1),t.send(),replacePage(t.responseText),t.responseText} return getData(\"%s\");";
        String tableLoaderJS = "function replacePage(e){let t=document.getElementsByTagName(\"html\")[0],n=document.createElement(\"HTML\");n.innerHTML=e,document.replaceChild(n,t)}function getData(e){let t=new XMLHttpRequest;return t.open(\"GET\",e,!1),t.send(),replacePage(t.responseText),t.responseText} return getData(\"%s\");";
        if (driver instanceof JavascriptExecutor jsDriver) {
            jsDriver.executeScript(String.format(tableLoaderJS, url));
        } else {
            throw new IllegalStateException("This driver does not support JavaScript!");
        }

        System.out.print("Waiting for elements to load");
        WebElement we = null;
        while (we == null) {
            try {
                we = driver.findElement(By.className("tablecontent"));
                Thread.sleep(50);
            } catch (org.openqa.selenium.NoSuchElementException | InterruptedException e) {
                System.out.print(".");
            }
        }
        System.out.println("\nElements loaded!");


        WebElement content = driver.findElement(By.className("tablecontent"));
        for (WebElement row : content.findElements(By.tagName("tr"))) {
            List<WebElement> tds = row.findElements(By.tagName("td"));
            String subj = tds.get(0).findElement(By.tagName("a")).getText();
            String grade = row.findElement(By.className("nowrap")).getText();

            addToHashMap(subj, grade);
            CurrentProfile.get().setFirstInsert(false);
        }
    }

    private void addToHashMap(String key, String value) {
        HashMap<String, String> data = CurrentProfile.get().getData();

        if (!data.containsKey(key)) {
            data.put(key, value);
        } else {
            if (!data.get(key).equals(value)) {
                data.put(key, value);
                if (!CurrentProfile.get().isFirstInsert()) {
                    String update = String.format("key updated (%s) to %s.%n", key, value);
                    if (SystemTray.isSupported()) {
                        Execution.getTrayIcon()
                                .displayMessage("KIT UPDATE!!11111", update, TrayIcon.MessageType.INFO);
                    }
                    System.out.println(update);
                }
            }
        }
    }
}
