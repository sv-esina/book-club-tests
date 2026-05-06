package tests.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.*;
import models.clubs.CreateClubBodyModel;
import models.clubs.SuccessfulCreateClubResponseModel;
import models.localStorage.LocalStorageAuthBodyModel;
import models.localStorage.UserDataModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationAndAuthBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.*;
import pages.ClubsPage;
import tests.TestBase;

import static testdata.TestData.*;


@Owner("Esina Svetlana")
@Feature("Работа с книжными клубами")
@DisplayName("[API+UI] Проверяем работу с книжными клубами")
public class ApiUIClubTests extends TestBase {

    ClubsPage clubsPage = new ClubsPage();

    String username;
    String password;
    String bookTitle;
    String newBookTitle;
    String bookAuthor;
    String newBookAuthor;
    Integer publicationYear;
    Integer newPublicationYear;
    String description;
    String newDescription;


    @BeforeEach
    public void prepareTestData() {
        username = randomUsername();
        password = randomPassword();
        bookTitle = randomBookTitle();
        bookAuthor = randomBookAuthors();
        publicationYear = FAKER.number().numberBetween(1700, 2025);
        description = FAKER.book().title() + " - " + FAKER.book().author();
        newBookTitle = randomBookTitle();
        newBookAuthor = randomBookAuthors();
        newPublicationYear = FAKER.number().numberBetween(1700, 2025);
        newDescription = FAKER.book().title() + " - " + FAKER.book().author();

    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("clubs")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать функционал создания клуба")
    @DisplayName("[API+UI] Успешное создание книжного клуба")
    public void successfulCreateClubUITest() {

        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        UserDataModel userDataModel = new UserDataModel(registrationResponse.id(),
                registrationResponse.username(),
                registrationResponse.firstName(),
                registrationResponse.lastName(),
                registrationResponse.email(),
                registrationResponse.remoteAddr());

        LocalStorageAuthBodyModel localStorageAuthBody = new LocalStorageAuthBodyModel(userDataModel,
                loginResponse.access(),
                loginResponse.refresh(),
                true);

        ObjectMapper mapper = new ObjectMapper();
        String jsonStringLocalStorage;
        try {
            // Преобразование объекта в JSON-строку
            jsonStringLocalStorage = mapper.writeValueAsString(localStorageAuthBody);
            System.out.println(jsonStringLocalStorage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(RUNTIME_EXCEPTION, e);
        }

        clubsPage.openFaviconPage(jsonStringLocalStorage)
                .openClubsPage(clubResponse.id())
                .checkingClubContent()
                .checkResult(clubsPage.getClubName(), bookTitle)
                .checkResult(clubsPage.getClubAuthors(), bookAuthor)
                .checkResult(clubsPage.getClubDescription(),description)
                .checkResult(clubsPage.getClubPublicationYear(), String.valueOf(publicationYear));

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("clubs")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать возможность редактирования книжного клуба")
    @DisplayName("[API+UI] Успешное редактирование книжного клуба")
    public void updateClubUITest() {

        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);


        UserDataModel userDataModel = new UserDataModel(registrationResponse.id(),
                registrationResponse.username(),
                registrationResponse.firstName(),
                registrationResponse.lastName(),
                registrationResponse.email(),
                registrationResponse.remoteAddr());

        LocalStorageAuthBodyModel localStorageAuthBody = new LocalStorageAuthBodyModel(userDataModel,
                loginResponse.access(),
                loginResponse.refresh(),
                true);

        ObjectMapper mapper = new ObjectMapper();
        String jsonStringLocalStorage;
        try {
            // Преобразование объекта в JSON-строку
            jsonStringLocalStorage = mapper.writeValueAsString(localStorageAuthBody);
            System.out.println(jsonStringLocalStorage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(RUNTIME_EXCEPTION, e);
        }

        clubsPage.openFaviconPage(jsonStringLocalStorage)
                .openClubsPage(clubResponse.id())
                .checkingClubContent()
                .checkResult(clubsPage.getClubName(), bookTitle)
                .checkResult(clubsPage.getClubAuthors(), bookAuthor)
                .checkResult(clubsPage.getClubDescription(),description)
                .checkResult(clubsPage.getClubPublicationYear(), String.valueOf(publicationYear));

        CreateClubBodyModel newClubData = new CreateClubBodyModel(newBookTitle, newBookAuthor, newPublicationYear,
                newDescription, TELEGRAM_LINK);

        api.clubs.updateClub(clubResponse.id(), newClubData, accessToken);

        clubsPage.refreshPage()
                .checkResult(clubsPage.getClubName(), newBookTitle)
                .checkResult(clubsPage.getClubAuthors(), newBookAuthor)
                .checkResult(clubsPage.getClubDescription(),newDescription)
                .checkResult(clubsPage.getClubPublicationYear(), String.valueOf(newPublicationYear));

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("clubs")})
    @Severity(SeverityLevel.CRITICAL)
    @Story("Реализовать возможность удаления книжного клуба")
    @DisplayName("[API+UI] Успешное удаление книжного клуба")
    public void deleteClubByIdTest() {

        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        UserDataModel userDataModel = new UserDataModel(registrationResponse.id(),
                registrationResponse.username(),
                registrationResponse.firstName(),
                registrationResponse.lastName(),
                registrationResponse.email(),
                registrationResponse.remoteAddr());

        LocalStorageAuthBodyModel localStorageAuthBody = new LocalStorageAuthBodyModel(userDataModel,
                loginResponse.access(),
                loginResponse.refresh(),
                true);

        ObjectMapper mapper = new ObjectMapper();
        String jsonStringLocalStorage;
        try {
            // Преобразование объекта в JSON-строку
            jsonStringLocalStorage = mapper.writeValueAsString(localStorageAuthBody);
            System.out.println(jsonStringLocalStorage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(RUNTIME_EXCEPTION, e);
        }

        clubsPage.openFaviconPage(jsonStringLocalStorage)
                .openClubsPage(clubResponse.id())
                .checkingClubContent()
                .checkResult(clubsPage.getClubName(), bookTitle)
                .checkResult(clubsPage.getClubAuthors(), bookAuthor)
                .checkResult(clubsPage.getClubDescription(),description)
                .checkResult(clubsPage.getClubPublicationYear(), String.valueOf(publicationYear));

        api.clubs.deleteClub(clubResponse.id(), accessToken);

        clubsPage.refreshPage()
                .checkingClubDetailsContent();

        api.users.deleteUser(accessToken);
    }
}
