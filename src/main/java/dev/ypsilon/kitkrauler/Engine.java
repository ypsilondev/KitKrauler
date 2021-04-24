package dev.ypsilon.kitkrauler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Engine {
    EDGE("edge", EdgeDriver.class),
    FIREFOX("firefox", FirefoxDriver.class);

    private static final String U_PATTERN = "^(u[a-z]{4})$";
    private final String id;
    private final Class<? extends WebDriver> driver;

    Engine(String id, Class<? extends WebDriver> driver) {
        this.id = id;
        this.driver = driver;
    }

    public static Optional<Engine> parse(String id) {
        return Arrays.stream(values()).filter(engine -> engine.id.equals(id)).findFirst();
    }

    public void initialize(String[] args) {
        try {
            AbstractDriverOptions driverOptions = null;

            if (args.length == 1) {
                validateInput(args);

                switch (this) {
                    case FIREFOX -> {
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.setHeadless(true);
                        driverOptions = firefoxOptions;
                    }
                    default -> {
                        EdgeOptions edgeOptions = new EdgeOptions();
                        edgeOptions.setHeadless(true);
                        driverOptions = edgeOptions;
                    }
                }

                new Execution(this.driver.getConstructor(driverOptions.getClass()).newInstance(driverOptions), args);
            } else {
                new Execution(this.driver.getConstructor().newInstance());
            }
        } catch (AWTException ignored) { } catch (InvocationTargetException |
                InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void validateInput(String[] args) {
        Matcher uMatcher = Pattern.compile(U_PATTERN).matcher(args[0]);
        Scanner scanner = new Scanner(System.in);

        while (!uMatcher.matches()) {
            System.out.println("Inkorrektes U-Kürzel");
            args[0] = scanner.nextLine();
            uMatcher = Pattern.compile(U_PATTERN).matcher(args[0]);
        }
    }
}
