package tests.api;

import io.qameta.allure.*;
import models.login.*;
import models.registration.RegistrationAndAuthBodyModel;
import org.junit.jupiter.api.*;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.TestData.*;



@Owner("Esina Svetlana")
@Feature("Авторизация пользователя")
@DisplayName("[API] Проверяем работу авторизации")
public class LoginTests  extends TestBase {

    String emptyUsername = EMPTY_STRING;
    String emptyPassword = EMPTY_STRING;
    String invalidUsername;
    String nullUsername = null;
    String nullPassword = null;

    @BeforeEach
    public void prepareTestData() {
        invalidUsername = randomUsername();
    }

    @Test
    @Tags({@Tag("API"), @Tag("login")})
    @Severity(SeverityLevel.BLOCKER)
    @Story("Реализовать авторизацию пользователя")
    @DisplayName("Успешная авторизация пользователя")
    public void successfulLoginTest() {

        RegistrationAndAuthBodyModel loginData = new RegistrationAndAuthBodyModel(VALID_USERNAME, VALID_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        step("Проверка корректности полученых токенов", () -> {
            assertThat(loginResponse.access()).startsWith(JWT_TOKEN_PREFIX);
            assertThat(loginResponse.refresh()).startsWith(JWT_TOKEN_PREFIX);
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("login")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать авторизацию пользователя")
    @DisplayName("Проверка ввода некорректного пароля")
    public void wrongPasswordLoginTest() {

        RegistrationAndAuthBodyModel loginData = new RegistrationAndAuthBodyModel(VALID_USERNAME, WRONG_PASSWORD);

        WrongPasswordLoginResponseModel loginResponse = api.auth.wrongPassword(loginData);

        step("Проверка текста полученной ошибки", () -> {
            assertThat(loginResponse.detail()).isEqualTo(ERROR_INVALID_CREDENTIALS);
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("login")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать авторизацию пользователя")
    @DisplayName("Авторизация незарегистрированным пользователем")
    public void wrongUsernameLoginTest() {
        RegistrationAndAuthBodyModel loginData = new RegistrationAndAuthBodyModel(invalidUsername, VALID_PASSWORD);

        WrongPasswordLoginResponseModel loginResponse = api.auth.wrongPassword(loginData);

        step("Проверка текста полученной ошибки", () -> {
            assertThat(loginResponse.detail()).isEqualTo(ERROR_INVALID_CREDENTIALS);
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("login")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать авторизацию пользователя")
    @DisplayName("Авторизация пользователя c пустыми параметрами")
    public void emptyUsernameAndPasswordLoginTest() {
        RegistrationAndAuthBodyModel loginData = new RegistrationAndAuthBodyModel(emptyUsername, emptyPassword);

        EmptyParamsLoginResponseModel loginResponse = api.auth.emptyParams(loginData);

        step("Проверка текста полученных ошибок", () -> {
            assertThat(loginResponse.username().get(0)).isEqualTo(ERROR_BLANK_FIELD);
            assertThat(loginResponse.password().get(0)).isEqualTo(ERROR_BLANK_FIELD);
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("login")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать авторизацию пользователя")
    @DisplayName("Авторизация пользователя с пустым Request body")
    public void emptyRequestBodyRegistrationTest() {

        WrongLoginBodyModel wrongLoginData = new WrongLoginBodyModel();

        EmptyBodyLoginResponseModel loginResponse = api.auth.emptyBody(wrongLoginData);

        step("Проверка текста полученных ошибок", () -> {
            assertThat(loginResponse.username().get(0)).isEqualTo(ERROR_REQUIRED_FIELD);
            assertThat(loginResponse.password().get(0)).isEqualTo(ERROR_REQUIRED_FIELD);
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("login")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать авторизацию пользователя")
    @DisplayName("Авторизация пользователя c null в параметрах")
    public void nullRequestBodyRegistrationTest() {

        RegistrationAndAuthBodyModel loginData = new RegistrationAndAuthBodyModel(nullUsername, nullPassword);

        NullParamsLoginResponseModel loginResponse = api.auth.nullParams(loginData);

        step("Проверка текста полученных ошибок", () -> {
            assertThat(loginResponse.username().get(0)).isEqualTo(ERROR_NULL_FIELD);
            assertThat(loginResponse.password().get(0)).isEqualTo(ERROR_NULL_FIELD);
        });
    }
}
