package specs.registration;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;

public class RegistrationSpec {

    public static ResponseSpecification successRegistrationResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/successful_registration_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("username", notNullValue())
            .expectBody("remoteAddr", notNullValue())
            .build();

    public static ResponseSpecification wrongCredentialsRegisterResponseSpec  = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/existing_user_registration_response_schema.json"))
            .expectBody("username", notNullValue())
            .build();

    public static ResponseSpecification wrongParamsRegistrationResponseSpec  = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/empty_params_registration_response_schema.json"))
            .expectBody("username", notNullValue())
            .expectBody("password", notNullValue())
            .build();

    public static ResponseSpecification invalidUsernameRegistrationResponseSpec  = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/registration/invalid_username_registration_response_schema.json"))
            .expectBody("username", notNullValue())
            .build();

    public static ResponseSpecification deleteUserSpec  = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .build();
}
