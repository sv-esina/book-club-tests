package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${env}.properties",
        "classpath:local.properties"

})


public interface WebDriverConfig extends Config {

    @Key("baseUrl")
    @DefaultValue("https://book-club.qa.guru")
    String getBaseUrl();

    @Key("baseURI")
    @DefaultValue("https://book-club.qa.guru")
    String getBaseURI();

    @Key("basePath")
    @DefaultValue("/api/v1")
    String getBasePath();

    @Key("browser")
    @DefaultValue("chrome")
    String getBrowser();

    @Key("browserVersion")
    @DefaultValue("147")
    String getBrowserVersion();

    @Key("browserSize")
    @DefaultValue("1920x1080")
    String getBrowserSize();

    @Key("isRemote")
    @DefaultValue("false")
    boolean isRemote();

    @Key("remoteUrl")
    String getRemoteUrl();

    @Key("remoteUsername")
    String getRemoteUsername();

    @Key("remotePassword")
    String getRemotePassword();


}
