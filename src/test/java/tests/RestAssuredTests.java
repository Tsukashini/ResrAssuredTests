package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.UpdateUserModel;
import org.testng.annotations.Test;
import utilities.GenerateFakeMessage;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;


public class RestAssuredTests {

    @Test
    public void UpdateFieldsTest () {
        UpdateUserModel updateUserModel = new UpdateUserModel();
        updateUserModel.setName(GenerateFakeMessage.getFirstName());
        updateUserModel.setJob(GenerateFakeMessage.getJob());
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .and()
                .body(updateUserModel)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .log()
                .all()
                .statusCode(200);

    }

    @Test
    public void DeleteTest () {
        RestAssured.given()
                .log()
                .all()
                .and()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log()
                .all()
                .statusCode(204);


    }

    @Test
    public void RegisterSuccessfulTest () {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .and()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("id", equalTo(4))
                .body("token", instanceOf(String.class));

    }

    @Test
    public void RegisterUnSuccessfulTest () {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .and()
                .body("{\n" +
                        "    \"email\": \"sydney@fife\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log()
                .all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));

    }

    @Test
    public void LoginSuccessfulTest () {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .and()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("token", instanceOf(String.class));

    }

    @Test
    public void LoginUnSuccessfulTest () {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .and()
                .body("{\n" +
                        "    \"email\": \"peter@klaven\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log()
                .all()
                .statusCode(400)
                .body("error", equalTo("Missing password"));

    }

    @Test
    public void DelayedResponseTest () {
        RestAssured
                .given()
                .log()
                .all()
                .when()
                .get("https://reqres.in/api/users?delay=3")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", instanceOf(Integer.class))
                .body("data[0].id", instanceOf(Integer.class))
                .body("support.url", equalTo("https://reqres.in/#support-heading"))
                .body("support.text", equalTo("To keep ReqRes free, contributions towards server costs are appreciated!"))
                .log()
                .all();

    }
}
