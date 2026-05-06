package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class ReviewsSpec {

    public static ResponseSpecification successfulCreateReviewResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/successful_create_review_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .expectBody("user", notNullValue())
            .expectBody("user.id", notNullValue())
            .expectBody("user.username", notNullValue())
            .expectBody("review", notNullValue())
            .expectBody("assessment", notNullValue())
            .expectBody("readPages", notNullValue())
            .expectBody("created", notNullValue())
            .expectBody("modified", nullValue())
            .build();

    public static ResponseSpecification successfulGetNullReviewSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/successful_get_null_review_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("next", nullValue())
            .expectBody("previous", nullValue())
            .expectBody("results", empty())
            .build();

    public static ResponseSpecification successfulGetReviewSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/successful_get_review_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("next", nullValue())
            .expectBody("previous", nullValue())
            .expectBody("results", notNullValue())
            .build();

    public static ResponseSpecification successfulUpdateReviewSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/successful_put_update_review_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .expectBody("user", notNullValue())
            .expectBody("user.id", notNullValue())
            .expectBody("user.username", notNullValue())
            .expectBody("review", notNullValue())
            .expectBody("assessment", notNullValue())
            .expectBody("readPages", notNullValue())
            .expectBody("created", notNullValue())
            .expectBody("modified", notNullValue())
            .build();

    public static ResponseSpecification deleteReviewSpec = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .build();

    public static ResponseSpecification nonExistentReviewSpec = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/successful_deleted_review_response_schema.json"))
            .build();

    public static ResponseSpecification noPermissionSpec = new ResponseSpecBuilder()
            .expectStatusCode(403)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/successful_deleted_review_response_schema.json"))
            .build();
}
