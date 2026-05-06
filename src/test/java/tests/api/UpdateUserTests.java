package tests.api;

import io.qameta.allure.*;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationAndAuthBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.update.*;
import org.junit.jupiter.api.*;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.TestData.*;


@Owner("Esina Svetlana")
@Feature("Работа с пользователями")
@DisplayName("[API] Проверяем обновление данных пользователя через метод Put")
public class UpdateUserTests extends TestBase {

    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String emptyUsername = EMPTY_STRING;



    @BeforeEach
    public void prepareTestData() {
        username = randomUsername();
        password = randomPassword();
        firstName = randomFirstName();
        lastName = randomLastName();
        email = randomEmail();
    }

    @Test
    @Tags({@Tag("API"), @Tag("users")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность редактирования данных пользователя")
    @DisplayName("[API] Успешный update данных пользователя с помощью метода PUT")
    public void successfulPutUpdateUserTest() {

        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        step("Проверка полученного ответа", () -> {
            assertThat(registrationResponse.id()).isGreaterThan(0);
            assertThat(registrationResponse.username()).isEqualTo(username);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");
            assertThat(registrationResponse.remoteAddr()).matches(IP_ADDRESS_REGEXP);

        });

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        step("Проверка полученных токенов", () -> {
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });

        UpdateBodyModel updateData = new UpdateBodyModel(username, firstName, lastName, email);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulUpdateUserResponseModel updateResponse = api.users.updatePut(updateData, accessToken);

        step("Проверка обновленных данных", () -> {
            assertThat(updateResponse.id()).isEqualTo(registrationResponse.id());
            assertThat(updateResponse.username()).isEqualTo(username);
            assertThat(updateResponse.firstName()).isEqualTo(firstName);
            assertThat(updateResponse.lastName()).isEqualTo(lastName);
            assertThat(updateResponse.email()).isEqualTo(email);
            assertThat(registrationResponse.remoteAddr()).matches(IP_ADDRESS_REGEXP);
            assertThat(registrationResponse.remoteAddr()).isEqualTo(updateResponse.remoteAddr());
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("users")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность редактирования данных пользователя")
    @DisplayName("[API] Update данных пользователя с отсутствием обязательных параметров (PUT)")
    public void wrongParamsPutUpdateUserTest() {

        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        String accessToken = "Bearer " + loginResponse.access();

        step("Проверка полученных токенов", () -> {
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });

        WrongParamsUpdateBodyModel wrongParamsUpdateData = new WrongParamsUpdateBodyModel(username);

        WrongParamsUpdateUserResponseModel updateResponse = api.users.setOnlyUsernameUpdatePut(wrongParamsUpdateData, accessToken);

        step("Проверка текста полученных ошибок", () -> {
            assertThat(updateResponse.firstName().get(0)).isEqualTo(ERROR_REQUIRED_FIELD);
            assertThat(updateResponse.lastName().get(0)).isEqualTo(ERROR_REQUIRED_FIELD);
            assertThat(updateResponse.email().get(0)).isEqualTo(ERROR_REQUIRED_FIELD);
        });
    }

    @Test
    @Tags({@Tag("API"), @Tag("users")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность редактирования данных пользователя")
    @DisplayName("[API] Update данных пользователя с пустым username (PUT)")
    public void emptyUsernamePutUpdateUserTest() {

        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        String accessToken = "Bearer " + loginResponse.access();

        step("Проверка полученных токенов", () -> {
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });

        UpdateBodyModel updateData = new UpdateBodyModel(emptyUsername, firstName, lastName, email);

        EmptyUsernameUpdateUserResponseModel updateResponse = api.users.setEmptyUsernameUpdatePut(updateData, accessToken);

        step("Проверка текста полученной ошибки", () -> {
            assertThat(updateResponse.username().get(0)).isEqualTo(ERROR_BLANK_FIELD);
        });
    }

}

