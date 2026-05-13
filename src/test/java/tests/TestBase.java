package tests;

import api.ApiClient;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import drivers.WebDriverProvider;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    private static WebDriverProvider driver;

    @BeforeAll
    static void configureSelenide() {

        driver = new WebDriverProvider();
        driver.configure();
    }

    @BeforeEach
    void addListenerToContainStepByStepInformationInAllureReport() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    protected static final ApiClient api = new ApiClient();


    @AfterEach
    void addAttachmentsForAllureReport() {
        if (driver.getConfig().isRemote()) {
            if (WebDriverRunner.hasWebDriverStarted()) {
                Attach.screenshotAs("Last screenshot");
                Attach.pageSource();
                Attach.browserConsoleLogs();
                Attach.addVideo();
            }
        }
        closeWebDriver();
    }

}