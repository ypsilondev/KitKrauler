package dev.ypsilon.kitkrauler;

import dev.ypsilon.kitkrauler.goals.DefaultGoal;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class Execution {

    private static TrayIcon trayIcon = null;

    private static final Class<? extends Consumer<WebDriver>> DEFAULT_GOAL = DefaultGoal.class;

    public Execution(WebDriver driver) throws AWTException, ReflectiveOperationException {
        setupTray();
        executeDefaultGoal(driver);
    }

    public Execution(WebDriver driver, String[] credentials) throws AWTException, ReflectiveOperationException {
        login(credentials);
        setupTray();
        executeDefaultGoal(driver);
    }

    private void login(String[] credentials) {
        String username = credentials[0];

        System.out.println("KIT-Account-Passwort für " + username);
        String password = new String(System.console().readPassword(""));

        CurrentProfile.get().setLoginInformation(username, password);
    }

    private void setupTray() throws AWTException {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image img = null;
            try {
                InputStream imageStream = this.getClass().getClassLoader().getResourceAsStream("images/img.jpg");
                if(imageStream != null) {
                    img = ImageIO.read(imageStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert img != null;
            trayIcon = new TrayIcon(img, "KIT Krauler");
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> trayIcon.displayMessage(
                    "KIT",
                    "Ich funktioniere. Aber keine Noten :(",
                    TrayIcon.MessageType.INFO
            ));

            tray.add(trayIcon);
        }
    }

    private void executeDefaultGoal(WebDriver driver) throws ReflectiveOperationException {
        DEFAULT_GOAL.getConstructor().newInstance().accept(driver);
    }

    public static synchronized TrayIcon getTrayIcon() {
        return trayIcon;
    }
}
