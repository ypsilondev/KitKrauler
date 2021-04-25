package dev.ypsilon.kitkrauler;

import java.util.Optional;

public class KitCrawler {
    public static void main(String[] args) {
        updateSystemProperties();

        String engine = System.getProperty("engine");
        Optional<Engine> foundEngine = Engine.parse(engine);

        if (engine == null || foundEngine.isEmpty()) {
            Engine.EDGE.initialize(args);
        } else {
            foundEngine.get().initialize(args);
        }
    }

    private static void updateSystemProperties() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
            System.setProperty("webdriver.firefox.driver", "geckodriver.exe");
        } else if (osName.contains("mac") || osName.contains("dawrin")) {
            System.setProperty("webdriver.edge.driver", "msedgedriver");
            System.setProperty("webdriver.firefox.driver", "geckodriver");
        } else if (osName.contains("linux")) {
            System.setProperty("webdriver.edge.driver", "msedgedriver");
            System.setProperty("webdriver.firefox.driver", "geckodriver");
        } else {
            System.out.println("Not running in a supported environment: " + osName);
            System.exit(2);
        }
    }

}
