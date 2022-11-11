import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

public class Application implements JsonStrings {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static String token;
    private static int categoryId;

    public static void main(String[] args) {
        token = getAuthorizationToken();
        categoryId = createCategory(false);
        getCategoryById(categoryId);
        deleteCategoryById(categoryId);
        getVisibleCategories();
    }

    /**
     * Retrieves an authorization token by sending a POST request.
     * Uses header, request body, response body extraction and path searching.
     *
     * @return authorization token
     */
    public static String getAuthorizationToken() {
        return given()
                .contentType(ContentType.JSON)
                .body(CREDENTIALS)
                .when().post(BASE_URL + "/private/login")
                .then().extract().response().getBody().path("token");
    }

    /**
     * Creates a new category.
     * Uses headers, request body and response body extraction.
     *
     * @param visibility whether the category should be visible or not
     * @return the id of the newly created category
     */
    public static int createCategory(boolean visibility) {
        return given()
                .header(
                        "Authorization",
                        "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(String.format(CREATE_CATEGORY_BODY, ThreadLocalRandom.current().nextInt(), visibility))
                .when().post(BASE_URL + "/private/category")
                .then().extract().response().getBody().path("id");
    }

    /**
     * Retrieves category by a given id.
     * Uses path parameter.
     *
     * @param id the id of the category
     */
    public static void getCategoryById(int id) {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token)
                .contentType(ContentType.JSON)
                .pathParam("categoryId", id)
                .when().get(BASE_URL + "/category/{categoryId}")
                .then().log().all();
    }

    /**
     * Retrieves only visible categories.
     * Uses multiple headers and a query parameter.
     */
    public static void getVisibleCategories() {
        given()
                .headers(
                        "Authorization",
                        "Bearer " + token, "Content-Type", "application/json")
                .queryParam("filter", "visible")
                .when().get(BASE_URL + "/category")
                .then().log().all();
    }

    /**
     * Deletes category by given id.
     * Uses .param and path parameter.
     *
     * @param id id of the category to be deleted
     */
    public static void deleteCategoryById(int id) {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token)
                .param("Content-Type", "application/json")
                .pathParam("categoryId", id)
                .when().delete(BASE_URL + "/private/category/{categoryId}")
                .then().statusCode(HttpStatus.SC_OK);
    }
}