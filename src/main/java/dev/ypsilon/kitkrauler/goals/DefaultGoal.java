package dev.ypsilon.kitkrauler.goals;

import dev.ypsilon.kitkrauler.tasks.CampusLoginTask;
import dev.ypsilon.kitkrauler.tasks.ExamUpdateTask;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DefaultGoal implements Consumer<WebDriver> {
    @Override
    public void accept(WebDriver driver) {
        CampusLoginTask.run(driver);
        Executors.newScheduledThreadPool(2)
                .scheduleAtFixedRate(() -> ExamUpdateTask.run(driver), 0, 20, TimeUnit.SECONDS);
    }
}
