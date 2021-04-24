package dev.ypsilon.kitkrauler;

import java.util.Optional;

public class KitCrawler {
    public static void main(String[] args) {
        System.setProperty("webdriver.edge.driver", "msedgedriver.exe");
        System.setProperty("webdriver.firefox.driver", "geckodriver.exe");

        String engine = System.getProperty("engine");
        Optional<Engine> foundEngine = Engine.parse(engine);

        if (engine == null || foundEngine.isEmpty()) {
            Engine.EDGE.initialize(args);
        } else {
            foundEngine.get().initialize(args);
        }
    }
}
