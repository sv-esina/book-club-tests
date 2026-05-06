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
import models.reviews.CreateReviewBodyModel;
import org.junit.jupiter.api.*;
import pages.ClubsPage;
import pages.components.ConvertRatingToStarsComponent;
import tests.TestBase;

import static testdata.TestData.*;


@Owner("Esina Svetlana")
@Feature("Работа с отзывами")
@DisplayName("[API+UI] Проверяем работу с отзывами")
public class ApiUIReviewsTests extends TestBase {

    ClubsPage clubsPage = new ClubsPage();
    ConvertRatingToStarsComponent convertRatingToStars = new ConvertRatingToStarsComponent();

    String username;
    String password;
    String newUsername;
    String newPassword;
    String bookTitle;
    String newBookTitle;
    String bookAuthor;
    String newBookAuthor;
    Integer publicationYear;
    Integer newPublicationYear;
    String description;
    String newDescription;
    String review;
    Integer assessment;
    Integer readPages;
    String newReview;
    Integer newAssessment;
    Integer newReadPages;

    @BeforeEach
    public void prepareTestData() {
        username = randomUsername();
        password = randomPassword();
        newUsername = randomUsername();
        newPassword = randomPassword();
        bookTitle = randomBookTitle();
        bookAuthor = randomBookAuthors();
        publicationYear = FAKER.number().numberBetween(1700, 2025);
        description = FAKER.book().title() + " - " + FAKER.book().author();
        newBookTitle = randomBookTitle();
        newBookAuthor = randomBookAuthors();
        newPublicationYear = FAKER.number().numberBetween(1700, 2025);
        newDescription = FAKER.book().title() + " - " + FAKER.book().author();
        review = FAKER.book().title() + " " + FAKER.mood() + " " + FAKER.random();
        assessment = FAKER.number().numberBetween(4, 5);
        readPages = FAKER.number().numberBetween(1, 300);
        newReview = FAKER.book().title() + " " + FAKER.brand() + " " + FAKER.random();
        newAssessment = FAKER.number().numberBetween(1, 3);
        newReadPages = FAKER.number().numberBetween(400, 500);

    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("reviews")})
    @Severity(SeverityLevel.MINOR)
    @Story("Реализовать создание отзыва на книжный клуб")
    @DisplayName("[API+UI] Проверка текста сообщения об отсутствии отзывов в новом книжном клубе")
    public void clubWithNoReviewsTest() {
        RegistrationAndAuthBodyModel usersRegData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersRegData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersRegData);

        String accessToken = "Bearer " + loginResponse.access();

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

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

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
                .noReviews(NO_REVIEWS_MESSAGE);

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать создание отзыва на книжный клуб")
    @DisplayName("[API+UI] Успешное добавление отзыва членом книжного клуба")
    public void successfulCreateReviewTest() {
        RegistrationAndAuthBodyModel usersRegData = new RegistrationAndAuthBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersRegData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersRegData);

        String accessToken = "Bearer " + loginResponse.access();

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

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        clubsPage.openFaviconPage(jsonStringLocalStorage)
                .openClubsPage(clubResponse.id())
                .checkingClubContent()
                .clickReviewButton()
                .checkingNewReviewContent()
                .enterAssessment(assessment)
                .enterReadPages(readPages)
                .enterReview(review)
                .clickSaveButton()
                .checkingReviewCardContent()
                .checkResult(clubsPage.getReviewerName(), username)
                .checkResult(clubsPage.getReviewRating(), convertRatingToStars.ratingValueToStars(assessment))
                .checkResult(clubsPage.getReadPages(), readPages + " стр.")
                .checkResult(clubsPage.getReviewContent(), review);

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать создание отзыва на книжный клуб")
    @DisplayName("[API+UI] Отмена добавления отзыва")
    public void cancelCreateReviewTest() {
        RegistrationAndAuthBodyModel usersRegData = new RegistrationAndAuthBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersRegData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersRegData);

        String accessToken = "Bearer " + loginResponse.access();

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

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        clubsPage.openFaviconPage(jsonStringLocalStorage)
                .openClubsPage(clubResponse.id())
                .checkingClubContent()
                .clickReviewButton()
                .checkingNewReviewContent()
                .enterAssessment(assessment)
                .enterReadPages(readPages)
                .enterReview(review)
                .clickCancelButton()
                .noReviews(NO_REVIEWS_MESSAGE);

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность редактирования отзыва на книжный клуб")
    @DisplayName("[API+UI] Успешное редактирование добавленного отзыва")
    public void successfulUpdateReviewTest() {
        RegistrationAndAuthBodyModel usersRegData = new RegistrationAndAuthBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersRegData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersRegData);

        String accessToken = "Bearer " + loginResponse.access();

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

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);

        api.reviews.createReview(reviewData, accessToken);

        clubsPage.openFaviconPage(jsonStringLocalStorage)
                .openClubsPage(clubResponse.id())
                .checkingClubContent()
                .checkingReviewCardContent()
                .clickEditReviewButton()
                .checkingEditReviewFormContent()
                .enterAssessment(newAssessment)
                .enterReadPages(newReadPages)
                .enterReview(newReview)
                .clickSaveButton()
                .checkResult(clubsPage.getReviewerName(), username)
                .checkResult(clubsPage.getReviewRating(), convertRatingToStars.ratingValueToStars(newAssessment))
                .checkResult(clubsPage.getReadPages(), newReadPages + " стр.")
                .checkResult(clubsPage.getReviewContent(), newReview);

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
    }

    @Test
    @Tags({@Tag("API+UI"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность редактирования отзыва на книжный клуб")
    @DisplayName("[API+UI] Попытка редактирования чужого отзыва")
    public void updateNotMineReviewByIdTest() {
        RegistrationAndAuthBodyModel usersRegData = new RegistrationAndAuthBodyModel(username, password);
        api.users.register(usersRegData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersRegData);

        String accessToken = "Bearer " + loginResponse.access();

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);

        api.reviews.createReview(reviewData, accessToken);

        RegistrationAndAuthBodyModel usersData2 = new RegistrationAndAuthBodyModel(newUsername, newPassword);

        SuccessfulRegistrationResponseModel registrationResponse2 = api.users.register(usersData2);

        SuccessfulLoginResponseModel loginResponse2 = api.auth.login(usersData2);

        String accessToken2 = "Bearer " + loginResponse2.access();

        UserDataModel userDataModel = new UserDataModel(registrationResponse2.id(),
                registrationResponse2.username(),
                registrationResponse2.firstName(),
                registrationResponse2.lastName(),
                registrationResponse2.email(),
                registrationResponse2.remoteAddr());

        LocalStorageAuthBodyModel localStorageAuthBody = new LocalStorageAuthBodyModel(userDataModel,
                loginResponse2.access(),
                loginResponse2.refresh(),
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
                .clickJoinClubButton()
                .checkingNotMineReviewCardContent()
                .findNotMineReviewCard(clubsPage.getReviewerName(), newUsername)
                .missEditReviewButton();

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
        api.users.deleteUser(accessToken2);
    }



    @Test
    @Tags({@Tag("API+UI"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возмонжость удаления отзыва на книжный клуб")
    @DisplayName("[API+UI] Успешное удаление добавленного отзыва")
    public void successfulDeleteReviewTest() {
        RegistrationAndAuthBodyModel usersRegData = new RegistrationAndAuthBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersRegData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersRegData);

        String accessToken = "Bearer " + loginResponse.access();

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

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);

        api.reviews.createReview(reviewData, accessToken);

        clubsPage.openFaviconPage(jsonStringLocalStorage)
                .openClubsPage(clubResponse.id())
                .checkingClubContent()
                .checkingReviewCardContent()
                .clickDeleteReviewButton()
                .confirmAlert()
                .noReviews(NO_REVIEWS_MESSAGE);

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
    }



    @Test
    @Tags({@Tag("API+UI"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возмонжость удаления отзыва на книжный клуб")
    @DisplayName("[API+UI] Попытка удаления чужого отзыва")
    public void deleteNotMineReviewByIdTest() {
        RegistrationAndAuthBodyModel usersRegData = new RegistrationAndAuthBodyModel(username, password);
        api.users.register(usersRegData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersRegData);

        String accessToken = "Bearer " + loginResponse.access();

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);

        api.reviews.createReview(reviewData, accessToken);

        RegistrationAndAuthBodyModel usersData2 = new RegistrationAndAuthBodyModel(newUsername, newPassword);

        SuccessfulRegistrationResponseModel registrationResponse2 = api.users.register(usersData2);

        SuccessfulLoginResponseModel loginResponse2 = api.auth.login(usersData2);

        String accessToken2 = "Bearer " + loginResponse2.access();

        UserDataModel userDataModel = new UserDataModel(registrationResponse2.id(),
                registrationResponse2.username(),
                registrationResponse2.firstName(),
                registrationResponse2.lastName(),
                registrationResponse2.email(),
                registrationResponse2.remoteAddr());

        LocalStorageAuthBodyModel localStorageAuthBody = new LocalStorageAuthBodyModel(userDataModel,
                loginResponse2.access(),
                loginResponse2.refresh(),
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
                .clickJoinClubButton()
                .checkingNotMineReviewCardContent()
                .findNotMineReviewCard(clubsPage.getReviewerName(), newUsername)
                .missDeleteReviewButton();

        api.clubs.deleteClub(clubResponse.id(), accessToken);
        api.users.deleteUser(accessToken);
        api.users.deleteUser(accessToken2);
    }

}
