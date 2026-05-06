package api;

import io.qameta.allure.Step;
import models.clubs.*;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.BaseSpec.baseResponseSpec;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {

    @Step("Отправка запроса POST на создание клуба")
    public SuccessfulCreateClubResponseModel createClub(CreateClubBodyModel clubData, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .body(clubData)
                .when()
                .post("/clubs/")
                .then()
                .spec(baseResponseSpec)
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(SuccessfulCreateClubResponseModel.class);
    }

    @Step("Отправка запроса POST на создание клуба c пустым body")
    public EmptyBodyCreateClubResponseModel emptyBodyCreateClub(EmptyBodyCreateClubRequestModel emptyClubData, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .body(emptyClubData)
                .when()
                .post("/clubs/")
                .then()
                .spec(baseResponseSpec)
                .spec(emptyBodyCreateClubResponseSpec)
                .extract()
                .as(EmptyBodyCreateClubResponseModel.class);
    }

    @Step("Поиск клуба по Id")
    public SuccessfulGetClubResponseModel getClub(Integer clubId, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .get("clubs/" + clubId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(successfulGetClubSpec)
                .extract().as(SuccessfulGetClubResponseModel.class);
    }

    @Step("Поиск клуба по Id")
    public NonExistentGetClubResponseModel getNonExistentClub(Integer clubId, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .get("clubs/" + clubId + 1 +"/")
                .then()
                .spec(baseResponseSpec)
                .spec(nonExistentClubSpec)
                .extract().as(NonExistentGetClubResponseModel.class);
    }

    @Step("Update данных клуба по Id")
    public SuccessfulUpdateClubResponseModel updateClub(Integer clubId, CreateClubBodyModel clubData, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .body(clubData)
                .when()
                .put("clubs/" + clubId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(successfulUpdateClubSpec)
                .extract().as(SuccessfulUpdateClubResponseModel.class);
    }

    @Step("Удаление клуба по Id")
    public void deleteClub(Integer clubId, String accessToken) {
         given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .delete("clubs/" + clubId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(deleteClubSpec);
    }

    @Step("Проверка ошибки при поиске удаленного клуба по Id")
    public NonExistentClubResponseModel noSuchClub(Integer clubId, String accessToken) {
        return given(baseRequestSpec)
                .header("Authorization", accessToken)
                .when()
                .delete("clubs/" + clubId + "/")
                .then()
                .spec(baseResponseSpec)
                .spec(nonExistentClubSpec)
                .extract().as(NonExistentClubResponseModel.class);
    }
}