package org.example;

public enum BrowserType {
    CHROME("webdriver.chrome.driver"),
    FIREFOX("webdriver.firefox.driver");

    private final String propertyKey;
    BrowserType(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyKey() {
        return propertyKey;
    }
}
