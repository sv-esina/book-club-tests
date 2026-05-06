package tests.api;

import io.qameta.allure.*;
import models.logout.EmptyOrNullParamLogoutResponseModel;
import models.logout.InvalidTokenLogoutModel;
import models.logout.LogoutBodyModel;
import models.registration.RegistrationAndAuthBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.TestData.*;


@Owner("Esina Svetlana")
@Feature("Logout пользователя")
@DisplayName("[API] Проверяем работу выхода пользователя из сессии")
public class LogoutTests extends TestBase {

    String emptyRefreshToken = EMPTY_STRING;
    String nullRefreshToken = null;

    @Test
    @Tags({@Tag("API"), @Tag("logout")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность выхода пользователя из сессии")
    @DisplayName("Успешный выход пользователя из сессии")
    public void successfulLogoutTest() {

        RegistrationAndAuthBodyModel loginData = new RegistrationAndAuthBodyModel(VALID_USERNAME, VALID_PASSWORD);

        String getRefreshToken = api.auth.getRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(getRefreshToken);
        api.auth.logout(logoutData);

    }

    @Test
    @Tags({@Tag("API"), @Tag("logout")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность выхода пользователя из сессии")
    @DisplayName("Выполнение запроса logout с некорректным токеном")
    public void invalidTokenLogoutTest() {

        LogoutBodyModel logoutData = new LogoutBodyModel(WRONG_REFRESH_TOKEN);

        InvalidTokenLogoutModel logoutResponse = api.auth.setInvalidRefreshToken(logoutData);

        step("Проверка текста полученных ошибок", () -> {
            assertThat(logoutResponse.detail()).isEqualTo(ERROR_INVALID_TOKEN);
            assertThat(logoutResponse.code()).isEqualTo(ERROR_TOKEN_NOT_VALID_CODE);
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("logout")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность выхода пользователя из сессии")
    @DisplayName("Выполнение запроса logout с null в параметре refresh")
    public void nullTokenLogoutTest() {

        LogoutBodyModel logoutData = new LogoutBodyModel(nullRefreshToken);

        EmptyOrNullParamLogoutResponseModel logoutResponse = api.auth.setNullOrEmptyRefreshToken(logoutData);

        step("Проверка текста полученных ошибок", () -> {
            assertThat(logoutResponse.refresh().get(0)).isEqualTo(ERROR_NULL_FIELD);
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("logout")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность выхода пользователя из сессии")
    @DisplayName("Выполнение запроса logout с пустым параметром refresh")
    public void emptyTokenLogoutTest() {

        LogoutBodyModel logoutData = new LogoutBodyModel(emptyRefreshToken);

        EmptyOrNullParamLogoutResponseModel logoutResponse = api.auth.setNullOrEmptyRefreshToken(logoutData);

        step("Проверка текста полученной ошибки", () -> {
            assertThat(logoutResponse.refresh().get(0)).isEqualTo(ERROR_BLANK_FIELD);
        });
    }
}
