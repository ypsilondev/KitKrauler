package dev.ypsilon.kitkrauler.tasks;

import dev.ypsilon.kitkrauler.CurrentProfile;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Consumer;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class CampusLoginTask implements Consumer<WebDriver> {

    private static final CampusLoginTask INSTANCE = new CampusLoginTask();

    public static void run(WebDriver driver) {
        INSTANCE.accept(driver);
    }

    @Override
    public void accept(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.get("https://campus.studium.kit.edu/");
            System.out.println(driver.getTitle() + " - Das KIT bleibt handlungsfähig");

            driver.findElement(By.id("hello")).findElement(By.className("login-link")).click();
            wait.ignoring(UnhandledAlertException.class).until(presenceOfElementLocated(By.id("sbmt")));

            insertLoginInformation(driver);

            wait.until(presenceOfElementLocated(By.id("hello")));
            driver.navigate().to("https://campus.studium.kit.edu/exams/registration.php");

            wait.until(presenceOfElementLocated(By.id("registration")));
            driver.switchTo().frame(driver.findElement(By.id("registration")));

            wait.ignoring(UnhandledAlertException.class).until(presenceOfElementLocated(By.name("gguid")));
            CurrentProfile.get().setGguid(driver.findElement(By.name("gguid")).getAttribute("value"));

            System.out.println("Login erfolgreich");
        } catch (WebDriverException exception) {
            exception.printStackTrace();
        }
    }

    private void insertLoginInformation(WebDriver driver) {
        if (CurrentProfile.get().usingConsoleAuthentication()) {
            driver.findElement(By.id("name")).sendKeys(CurrentProfile.get().getUsername());
            driver.findElement(By.id("password")).sendKeys(CurrentProfile.get().getPassword() + Keys.ENTER);
        }
    }
}
