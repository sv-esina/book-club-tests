package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import pages.components.CheckReviewCardComponent;

import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;

public class ClubsPage {

    CheckReviewCardComponent checkResultValue = new CheckReviewCardComponent();

    private final SelenideElement
                    clubContent = $(".club-content"),
                    noReviewsForm = $(".no-reviews"),
                    createReviewButton = $(".add-review-btn"),
                    newReviewContent = $(".reviews-header"),
                    assessmentInput = $("#assessment"),
                    readPagesInput = $("#readPages"),
                    reviewInput = $("#review"),
                    saveButton = $(".save-btn"),
                    cancelButton = $(".cancel-btn"),
                    reviewCardUserReview = $(".review-card.user-review"),
                    reviewerName = $(".reviewer-name"),
                    reviewContent = $(".review-content"),
                    readPages = $(".read-pages"),
                    reviewRating = $(".stars"),
                    editReviewButton = $(".edit-review-btn"),
                    reviewForm = $(".review-form"),
                    deleteReviewButton = $(".delete-review-btn"),
                    joinClubButton = $(".join-btn"),
                    reviewCard = $(".review-card"),
                    clubName = $(".club-header"),
                    clubAuthors = $(".authors"),
                    clubDescription = $(".description"),
                    clubPublicationYear = $(".year"),
                    clubDetails = $(".club-details"),
                    joinClub = $(".club-card").$(".join-btn"),
                    currentLoginPage = $("[data-testid=login-page]"),
                    searchInput = $(".search-input");

                ElementsCollection clubNames = $$(".club-header");

    @Step("Открываем сайт book-club")
    public ClubsPage openMainPage() {
        open(baseUrl);
        return this;
    }

    @Step("Нажимаем кнопку Присоединиться на выбранном клубе")
    public ClubsPage joinAnyClub() {
        joinClub.click();
        currentLoginPage.shouldBe(visible);
        return this;
    }

    @Step("Выполняем поиск по рандомному наименованию клуба")
    public ClubsPage searchRandomClub() {
        clubNames.shouldHave(sizeGreaterThan(0));
        int randomIndex = new Random().nextInt(clubNames.texts().size());
        String randomClubName = clubNames.get(randomIndex).getText();

        searchInput.setValue(randomClubName).pressEnter();
        clubNames.findBy(text(randomClubName));

        return this;
    }


    @Step("Открываем браузер для передачи авторизационных данных")
    public ClubsPage openFaviconPage(String value) {
        open("/favicon.ico");
        localStorage().setItem("book_club_auth", value);
        return this;
    }

    @Step("Открываем форму созданного клуба {value}")
    public ClubsPage openClubsPage(Integer value) {
        open("/clubs/" + value);
        return this;
    }

    @Step("Проверка отображения на экране формы клуба")
    public ClubsPage checkingClubContent() {
        clubContent.shouldBe(visible);
        return this;
    }

    @Step("Проверка отображения на экране формы клуба")
    public ClubsPage noReviews(String value) {
        noReviewsForm.shouldHave(text(value));
        return this;
    }

    @Step("Нажимаем кнопку Написать отзыв")
    public ClubsPage clickReviewButton() {
        createReviewButton.click();
        return this;
    }

    @Step("Проверка отображения формы Новый отзыв")
    public ClubsPage checkingNewReviewContent() {
        newReviewContent.shouldBe(visible);
        return this;
    }

    @Step("Ввод значения в поле Оценка")
    public ClubsPage enterAssessment(Integer value) {
        assessmentInput.setValue(String.valueOf(value));
        return this;
    }

    @Step("Ввод значения в поле Прочитано страниц")
    public ClubsPage enterReadPages(Integer value) {
        readPagesInput.setValue(String.valueOf(value));
        return this;
    }

    @Step("Ввод значения в поле Ваш отзыв")
    public ClubsPage enterReview(String value) {
        reviewInput.setValue(value);
        return this;
    }

    @Step("Нажимаем кнопку Опубликовать")
    public ClubsPage clickSaveButton() {
        saveButton.click();
        return this;
    }

    @Step("Нажимаем кнопку Отменить")
    public ClubsPage clickCancelButton() {
        cancelButton.click();
        return this;
    }

    @Step("Проверка отображения карточки отзыва")
    public ClubsPage checkingReviewCardContent() {
        reviewCardUserReview.shouldBe(visible);
        return this;
    }

    @Step("Проверка значений в добавленной сущности")
    public ClubsPage checkResult(SelenideElement key, String value) {
        checkResultValue.checkingResultValues(key, value);

        return this;
    }

    @Step("Выбор карточки чужого отзыва")
    public ClubsPage findNotMineReviewCard(SelenideElement key, String value) {
        checkResultValue.findNotMineReviewCard(key, value);

        return this;
    }

    public SelenideElement getReviewerName() {
        return reviewerName;
    }

    public SelenideElement getReviewRating() {

        return reviewRating;
    }

    public SelenideElement getReadPages() {
        return readPages;
    }

    public SelenideElement getReviewContent() {
        return reviewContent;
    }

    public SelenideElement getClubName() {
        return clubName;
    }

    public SelenideElement getClubAuthors() {

        return clubAuthors;
    }

    public SelenideElement getClubDescription() {
        return clubDescription;
    }

    public SelenideElement getClubPublicationYear() {
        return clubPublicationYear;
    }

    @Step("Нажимаем кнопку Редактировать")
    public ClubsPage clickEditReviewButton() {
        editReviewButton.click();
        return this;
    }

    @Step("Проверка отображения карточки отзыва")
    public ClubsPage checkingEditReviewFormContent() {
        reviewForm.shouldBe(visible).shouldHave(text("Редактирование отзыва"));
        return this;
    }

    @Step("Нажимаем кнопку Удалить")
    public ClubsPage clickDeleteReviewButton() {
        deleteReviewButton.click();
        return this;
    }

    @Step("Нажимаем ОК в алерте Подтвердите действие ")
    public ClubsPage confirmAlert() {
        confirm();
        return this;
    }

    @Step("Нажимаем кнопку Присоединиться")
    public ClubsPage clickJoinClubButton() {
        joinClubButton.click();
        return this;
    }

    @Step("Проверка отображения карточки чужого отзыва")
    public ClubsPage checkingNotMineReviewCardContent() {
        reviewCard.shouldBe(visible);
        return this;
    }

    @Step("Проверяем отсутствие кнопки Редактировать")
    public ClubsPage missEditReviewButton() {
        editReviewButton.shouldNotBe(visible);
        return this;
    }

    @Step("Проверяем отсутствие кнопки Редактировать")
    public ClubsPage missDeleteReviewButton() {
        deleteReviewButton.shouldNotBe(visible);
        return this;
    }

    @Step("Обновляем страницу браузера")
    public ClubsPage refreshPage() {
        refresh();
        return this;
    }

    @Step("Проверка отображения сообщения об отсутствии клуба")
    public ClubsPage checkingClubDetailsContent() {
        clubDetails.shouldHave(text("Не удалось загрузить информацию о клубе"));
        return this;
    }

}
