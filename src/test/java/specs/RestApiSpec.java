package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class RestApiSpec {

    public static RequestSpecification listCheckPageRequestSpec = with()
            .filter(withCustomTemplates())
            .basePath("/api/unknown");

    public static RequestSpecification listAllLogsRequestSpec = with()
            .filter(withCustomTemplates())
            .log().all()
            .basePath("/api/unknown");

    public static RequestSpecification listRequestSpec = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().method()
            .log().body()
            .log().headers()
            .basePath("/api/unknown");

    public static ResponseSpecification listResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();

    public static RequestSpecification loginRequestSpec = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().method()
            .log().body()
            .log().headers()
            .contentType(JSON)
            .basePath("/api/login");

    public static ResponseSpecification loginResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .log(STATUS)
            .log(BODY)
            .build();

}
