package tests.api;

import io.qameta.allure.*;
import models.clubs.CreateClubBodyModel;
import models.clubs.SuccessfulCreateClubResponseModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationAndAuthBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import models.reviews.*;
import org.junit.jupiter.api.*;
import tests.TestBase;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.TestData.*;


@Owner("Esina Svetlana")
@Feature("Работа с отзывами")
@DisplayName("[API] Проверяем работу с отзывами")
public class ReviewsTests  extends TestBase {
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
    @Tags({@Tag("API"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать создание отзыва на книжный клуб")
    @DisplayName("[API] Успешное добавление отзыва членом книжного клуба")
    public void successfulCreateReviewTest() {
        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);

        SuccessfulCreateAndUpdateReviewResponseModel reviewResponse = api.reviews.createReview(reviewData, accessToken);

        step("Проверка значений созданного отзыва в полученном ответе", () -> {
            assertThat(reviewResponse.id()).isGreaterThan(0);
            assertThat(reviewResponse.club()).isEqualTo(clubResponse.id());
            assertThat(reviewResponse.user().get("id")).isEqualTo(registrationResponse.id());
            assertThat(reviewResponse.user().get("username")).isEqualTo(registrationResponse.username());
            assertThat(reviewResponse.review()).isEqualTo(review);
            assertThat(reviewResponse.assessment()).isEqualTo(assessment);
            assertThat(reviewResponse.readPages()).isEqualTo(readPages);
            assertThat(reviewResponse.created()).isNotNull();
            assertThat(reviewResponse.modified()).isNull();
        });

    }

    @Test
    @Tags({@Tag("API"), @Tag("reviews")})
    @Severity(SeverityLevel.MINOR)
    @Story("Реализовать создание отзыва на книжный клуб")
    @DisplayName("[API] Просмотр отсутствия отзывов в только что созданном клубе")
    public void getNullReviewByIdClubTest() {
        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        SuccessfulGetNullReviewResponseModel getReviewResponse = api.reviews.getNullReview(clubResponse.id(), accessToken);

        step("Проверка значений созданного отзыва в полученном ответе", () -> {
            assertThat(getReviewResponse.count()).isEqualTo(0);
            assertThat(getReviewResponse.next()).isNull();
            assertThat(getReviewResponse.previous()).isNull();
            assertThat(getReviewResponse.results()).isEmpty();
        });

    }

    @Test
    @Tags({@Tag("API"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать создание отзыва на книжный клуб")
    @DisplayName("[API] Успешный просмотр добавленного отзыва по Id клуба")
    public void getReviewByIdClubTest() {
        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);

        SuccessfulCreateAndUpdateReviewResponseModel reviewResponse = api.reviews.createReview(reviewData, accessToken);

        SuccessfulGetReviewResponseModel getReviewResponse = api.reviews.getReview(clubResponse.id(), reviewResponse.readPages(), accessToken);

        step("Проверка значений созданного отзыва в полученном ответе", () -> {
            assertThat(getReviewResponse.count()).isGreaterThan(0);
            assertThat(getReviewResponse.next()).isNull();
            assertThat(getReviewResponse.previous()).isNull();
            assertThat(getReviewResponse.results()).isNotNull();
            assertThat(getReviewResponse.results().get(0).user().get("id")).isEqualTo(registrationResponse.id());
            assertThat(getReviewResponse.results().get(0).user().get("username")).isEqualTo(registrationResponse.username());
            assertThat(getReviewResponse.results().get(0).review()).isEqualTo(reviewResponse.review());
            assertThat(getReviewResponse.results().get(0).assessment()).isEqualTo(reviewResponse.assessment());
            assertThat(getReviewResponse.results().get(0).readPages()).isEqualTo(reviewResponse.readPages());
            assertThat(getReviewResponse.results().get(0).created()).isNotNull();
            assertThat(getReviewResponse.results().get(0).modified()).isNull();
        });

    }

    @Test
    @Tags({@Tag("API"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность редактирования отзыва на книжный клуб")
    @DisplayName("[API] Успешный update добавленного отзыва по Id")
    public void updateReviewByIdTest() {
        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);
        SuccessfulCreateAndUpdateReviewResponseModel reviewResponse = api.reviews.createReview(reviewData, accessToken);

        CreateReviewBodyModel newReviewData = new CreateReviewBodyModel(clubResponse.id(), newReview, newAssessment, newReadPages);
        SuccessfulCreateAndUpdateReviewResponseModel updateResponse = api.reviews.updateReview(reviewResponse.id(), newReviewData, accessToken);

        step("Проверка значений созданного отзыва в полученном ответе", () -> {
            assertThat(updateResponse.id()).isEqualTo(reviewResponse.id());
            assertThat(updateResponse.club()).isEqualTo(reviewResponse.club());
            assertThat(updateResponse.user().get("id")).isEqualTo(registrationResponse.id());
            assertThat(updateResponse.user().get("username")).isEqualTo(registrationResponse.username());
            assertThat(updateResponse.review()).isEqualTo(newReview);
            assertThat(updateResponse.assessment()).isEqualTo(newAssessment);
            assertThat(updateResponse.readPages()).isEqualTo(newReadPages);
            assertThat(updateResponse.created()).isEqualTo(reviewResponse.created());
            assertThat(updateResponse.modified()).isNotEqualTo(reviewResponse.modified());
        });

    }

    @Test
    @Tags({@Tag("API"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность редактирования отзыва на книжный клуб")
    @DisplayName("[API] Попытка редактирования чужого отзыва по Id")
    public void updateNotMineReviewByIdTest() {
        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);
        SuccessfulCreateAndUpdateReviewResponseModel reviewResponse = api.reviews.createReview(reviewData, accessToken);

        RegistrationAndAuthBodyModel usersData2 = new RegistrationAndAuthBodyModel(newUsername, newPassword);

        api.users.register(usersData2);

        SuccessfulLoginResponseModel loginResponse2 = api.auth.login(usersData2);

        String accessToken2 = "Bearer " + loginResponse2.access();

        NoPermissionUpdateOrDeleteReviewResponseModel updateResponse = api.reviews.notMineReview(reviewResponse.id(), accessToken2);

        step("Проверка текста полученной ошибки", () -> {
            assertThat(updateResponse.detail()).isEqualTo(PERMISSION_ERROR);
        });

    }

    @Test
    @Tags({@Tag("API"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возможность удаления отзыва на книжный клуб")
    @DisplayName("[API] Успешное удаление добавленного отзыва по Id")
    public void deleteReviewByIdTest() {
        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);
        SuccessfulCreateAndUpdateReviewResponseModel reviewResponse = api.reviews.createReview(reviewData, accessToken);

        api.reviews.deleteReview(reviewResponse.id(), accessToken);

        NonExistentDeleteReviewResponseModel nonExistentReviewResponse = api.reviews.noSuchReview(reviewResponse.id(), accessToken);

        step("Проверка фактического удаления отзыва", () -> {
            assertThat(nonExistentReviewResponse.detail()).isEqualTo(ERROR_REVIEW_MATCHES);
        });

    }

    @Test
    @Tags({@Tag("API"), @Tag("reviews")})
    @Severity(SeverityLevel.NORMAL)
    @Story("Реализовать возмонжость удаления отзыва на книжный клуб")
    @DisplayName("[API] Удаление чужого отзыва по Id")
    public void deleteNotMineReviewByIdTest() {
        RegistrationAndAuthBodyModel usersData = new RegistrationAndAuthBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(usersData);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(usersData);

        CreateClubBodyModel clubData = new CreateClubBodyModel(bookTitle, bookAuthor, publicationYear,
                description, TELEGRAM_LINK);

        String accessToken = "Bearer " + loginResponse.access();

        SuccessfulCreateClubResponseModel clubResponse = api.clubs.createClub(clubData, accessToken);

        CreateReviewBodyModel reviewData = new CreateReviewBodyModel(clubResponse.id(), review, assessment, readPages);
        SuccessfulCreateAndUpdateReviewResponseModel reviewResponse = api.reviews.createReview(reviewData, accessToken);

        RegistrationAndAuthBodyModel usersData2 = new RegistrationAndAuthBodyModel(newUsername, newPassword);

        SuccessfulRegistrationResponseModel registrationResponse2 = api.users.register(usersData2);

        SuccessfulLoginResponseModel loginResponse2 = api.auth.login(usersData2);

        String accessToken2 = "Bearer " + loginResponse2.access();

        NoPermissionUpdateOrDeleteReviewResponseModel deleteResponse = api.reviews.notMineReview(reviewResponse.id(), accessToken2);

        step("Проверка текста полученной ошибки", () -> {
            assertThat(deleteResponse.detail()).isEqualTo(PERMISSION_ERROR);
        });

    }
}
