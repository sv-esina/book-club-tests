package api;

import io.qameta.allure.Step;
import models.reviews.*;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.BaseSpec.baseResponseSpec;
import static specs.reviews.ReviewsSpec.*;

public class ReviewsApiClient {

    @Step("Отправка запроса POST на добавление отзыва")
    public SuccessfulCreateAndUpdateReviewResponseModel createReview(CreateReviewBodyModel reviewData, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .body(reviewData)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(baseResponseSpec)
                .spec(successfulCreateReviewResponseSpec)
                .extract()
                .as(SuccessfulCreateAndUpdateReviewResponseModel.class);
    }

    @Step("Просмотр отсутствия отзывов по Id клуба")
    public SuccessfulGetNullReviewResponseModel getNullReview(Integer clubId, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .get("clubs/reviews/" + "?club=" + clubId)
                .then()
                .spec(baseResponseSpec)
                .spec(successfulGetNullReviewSpec)
                .extract().as(SuccessfulGetNullReviewResponseModel.class);
    }

    @Step("Просмотр отзывов по Id клуба")
    public SuccessfulGetReviewResponseModel getReview(Integer clubId, Integer page_size, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .get("clubs/reviews/" + "?club=" + clubId + "&page_size=" + page_size)
                .then()
                .spec(baseResponseSpec)
                .spec(successfulGetReviewSpec)
                .extract().as(SuccessfulGetReviewResponseModel.class);
    }

    @Step("Update отзыва по Id")
    public SuccessfulCreateAndUpdateReviewResponseModel updateReview(Integer clubId, CreateReviewBodyModel reviewData, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .body(reviewData)
                .when()
                .put("clubs/reviews/" + clubId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(successfulUpdateReviewSpec)
                .extract().as(SuccessfulCreateAndUpdateReviewResponseModel.class);
    }

    @Step("Удаление своего отзыва по Id")
    public void deleteReview(Integer reviewId, String accessToken) {
        given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .delete("clubs/reviews/" + reviewId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(deleteReviewSpec);
    }

    @Step("Проверка ошибки при поиске удаленного отзыва по Id")
    public NonExistentDeleteReviewResponseModel noSuchReview(Integer reviewId, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .delete("clubs/reviews/" + reviewId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(nonExistentReviewSpec)
                .extract().as(NonExistentDeleteReviewResponseModel.class);
    }

    @Step("Проверка отсутствия прав пользователя")
    public NoPermissionUpdateOrDeleteReviewResponseModel notMineReview(Integer reviewId, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .delete("clubs/reviews/" + reviewId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(noPermissionSpec)
                .extract().as(NoPermissionUpdateOrDeleteReviewResponseModel.class);
    }
}
