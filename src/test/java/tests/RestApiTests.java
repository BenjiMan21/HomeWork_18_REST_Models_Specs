package tests;

import io.restassured.response.Response;
import models.lombok.LoginBodyModel;
import models.lombok.LoginMissingPasswordModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.RestApiSpec.*;


public class RestApiTests extends TestBase {

    @Test
    void checkThePageTest() {
        step ("Проверка page:1", ()-> {
            given(listCheckPageRequestSpec)

                    .when()
                    .get()

                    .then()
                    .body("page", is(1));
        });
    }

    @Test
    void checkThePageWithAllLogsTest() {
        step ("Проверка page:1 с логами", ()->{
        given(listAllLogsRequestSpec)

                .when()
                .get()

                .then()
                .log().all()
                .body("page", is(1));
        });
    }

    @Test
    void checkThePageWithSomeLogsTest() {
        step ("Проверка page:1 с частичными логами", ()->{
        given(listRequestSpec)

                .when()
                .get()

                .then()
                .log().status()
                .log().body()
                .body("page", is(1));
        });
    }

    @Test
    void checkThePageWithStatusCodeTest() {
        step ("Проверка page:1 со статус кодом 200", ()->{
        given(listRequestSpec)

                .when()
                .get()

                .then()
                .spec(listResponseSpec)
                .body("page", is(1));
        });
    }

    @Test
    void checkThePageWithJsonSchemaTest() {
        step ("Проверка page:1 со схемой", ()->{
        given(listRequestSpec)

                .when()
                .get()

                .then()
                .spec(listResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/homework-schema.json"))
                .body("page", is(1));
        });
    }

    @Test
    void checkThePageWithJsonSchemaWithHamcrestTest() {
        Response statusResponse = step ("Запрос с Hamcrest", ()->{
            return given(listRequestSpec)

                .when()
                .get()

                .then()
                .spec(listResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/homework-schema.json"))
                .extract().response();
        });
        step ("Ответ", ()->
        assertThat(statusResponse.path("page"), is(1)));
    }

    @Test
    void checkThePageWithJsonSchemaWithAssertJTest() {
        String response = step ("Запрос с AssertJ", ()->{
            return given(listRequestSpec)

                .when()
                .get()

                .then()
                .spec(listResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/homework-schema.json"))
                .extract().response().asString();
        });
        step ("Ответ", ()->{
        assertThat(response)
                .contains("\"page\":1")
                .contains("\"total\":12")
                .contains("\"total_pages\":2");
        });
    }

    @Test
    void checkWithAssertJFromMassiveTest() {
        String response = step ("Запрос из массива", ()->{
        return given(listRequestSpec)

                .when()
                .get()

                .then()
                .spec(listResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/homework-schema.json"))
                .extract().response().asString();
        });
        step ("Ответ", ()->{
        assertThat(response)
                .contains("\"name\":\"cerulean\"")
                .contains("\"pantone_value\":\"15-4020\"");
        });
    }

    @Test
    void checkUnsuccessfulLoginTest() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("peter@klaven");

        LoginMissingPasswordModel response = step ("Запрос с логином", ()->{
        return given(loginRequestSpec)

                .body(authData)

                .when()
                .post()

                .then()
                .spec(loginResponseSpec)
                .body(matchesJsonSchemaInClasspath("schemas/homework-login-schema.json"))
                        .extract().as(LoginMissingPasswordModel.class);
        });
        step ("Ответ", ()->
                assertEquals("Missing password", response.getError()));
    }
}
