package de.ypsilon.kitkrauler;

import org.openqa.selenium.edge.EdgeOptions;

import java.awt.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KitCrawler {
    private static final String U_PATTERN = "^(u[a-z]{4})$";

    public static void main(String[] args) {
        System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
        try {
            EdgeOptions edgeOptions = new EdgeOptions();

            if (args.length == 2) {
                validateInput(args);
                edgeOptions.addArguments("headless");
                edgeOptions.addArguments("disable-gpu");

                new Execution(edgeOptions, args);
            } else {
                new Execution(edgeOptions);
            }
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static void validateInput(String[] args) {
        Matcher uMatcher = Pattern.compile(U_PATTERN).matcher(args[0]);
        Scanner scanner = new Scanner(System.in);

        while (!uMatcher.matches()) {
            System.out.println("Inkorrektes U-Kürzel");
            args[0] = scanner.nextLine();
            uMatcher = Pattern.compile(U_PATTERN).matcher(args[0]);
        }
    }
}
