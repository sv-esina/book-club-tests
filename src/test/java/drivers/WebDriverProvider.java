package drivers;

import com.codeborne.selenide.Configuration;
import config.WebDriverConfig;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;
import java.util.Objects;


public class WebDriverProvider {
    private final WebDriverConfig config;

    public WebDriverProvider() {

        this.config = ConfigFactory.create(WebDriverConfig.class, System.getProperties());
    }


    public WebDriverConfig getConfig() {
        return config;
    }

    public void configure() {
        Configuration.browser = config.getBrowser();

        String browserVersion = config.getBrowserVersion();
        if (Objects.nonNull(browserVersion)) {
            Configuration.browserVersion = config.getBrowserVersion();
        }

        Configuration.browserSize = config.getBrowserSize();
        Configuration.baseUrl = config.getBaseUrl();

        if (config.isRemote()) {
            Configuration.remote = String.valueOf(remoteUrl());
            Configuration.browserCapabilities = getCapabilities();
        }

        RestAssured.baseURI = config.getBaseURI();
        RestAssured.basePath = config.getBasePath();

    }

    private String remoteUrl() {
        var url  = config.getRemoteUrl();
        if (Objects.nonNull(config.getRemoteUsername()) && Objects.nonNull(config.getRemotePassword())) {
            url = url.replace("://", "://" + config.getRemoteUsername() + ":" + config.getRemotePassword() + "@");
        }
        return url;
    }


    private DesiredCapabilities getCapabilities() {
        var capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));

        return capabilities;
    }

}
