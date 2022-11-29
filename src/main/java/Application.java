import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matcher;

import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Application implements JsonStrings {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private static String token;
    private static int categoryId;
    private static String packageCode;

    public static void main(String[] args) {
        token = getAuthorizationToken();
        categoryId = createCategory(false);
//        System.out.println("Category ID: " + categoryId);
//        System.out.println("Category by ID:");
//        System.out.println(getCategoryById(categoryId).getBody().asString());
//        System.out.println("Visible categories:");
//        getVisibleCategories();
//        System.out.println("Delete category:");
//        deleteCategoryById(categoryId);
//        verifyNoCategories();
        verifyLineage(categoryId);
        getAllGroupsWithPreemptiveBasicAuth();
        getAllGroupsWithChallengedBasicAuth();
        packageCode = createShippingPackage();
        System.out.println(getPackageByCode(packageCode).getCode());
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
     * @return the Response from the server
     */
    public static Response getCategoryById(int id) {
        return given()
                .header(
                        "Authorization",
                        "Bearer " + token)
                .contentType(ContentType.JSON)
                .pathParam("categoryId", id)
                .when().get(BASE_URL + "/category/{categoryId}")
                .then().extract().response();
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
                .then().statusCode(HttpStatus.SC_OK).log().all();
    }

    /**
     * Verifies there are no categories.
     * Uses headers and response body assertion.
     */
    public static void verifyNoCategories() {
        given()
                .header(
                        "Authorization",
                        "Bearer " + token)
                .contentType(ContentType.JSON)
                .when().get(BASE_URL + "/category")
                .then().assertThat().body("recordsTotal", equalTo(0));
    }

    // -------------------------------------------------------------------------
    // DEMO:
    /**
     * Verifies part of the response by using other parts of the response.
     * Uses lambda expression.
     *
     * "lineage" is a property of a category which is always in the format "/<category_id>/".
     */
    public static void verifyLineage(int id) {
        getCategoryById(id)
                .then().body("lineage", response -> equalTo(String.format("/%s/", response.path("id").toString())));
    }

    /**
     * Retrieve all groups using preemptive basic authentication.
     */
    public static void getAllGroupsWithPreemptiveBasicAuth() {
        given()
                .auth().preemptive().basic("admin@shopizer.com", "password")
                .when().get(BASE_URL + "/sec/private/groups")
                .then().statusCode(200).and().log().all();
    }

    /**
     * Retrieve all groups using challenged basic authentication.
     */
    public static void getAllGroupsWithChallengedBasicAuth() {
        given()
                .auth().basic("admin@shopizer.com", "password")
                .when().get(BASE_URL + "/sec/private/groups")
                .then().statusCode(200).and().log().all();
    }

    /**
     * Creates a shipping package by serialization of a ShippingPackage object.
     *
     * @return the code of the newly created package
     */
    public static String createShippingPackage() {
        ShippingPackage shippingPackage = new ShippingPackage();
        given()
                .header(
                        "Authorization",
                        "Bearer " + token)
                .contentType("application/json")
                .body(shippingPackage)
                .when().post(BASE_URL + "/private/shipping/package")
                .then().statusCode(HttpStatus.SC_OK);

        return shippingPackage.getCode();
    }

    /**
     * Retrieves a shipping package by a given code using deserialization.
     *
     * @param code the code of the shipping package
     * @return a deserialized ShippingPackage object
     */
    public static ShippingPackage getPackageByCode(String code) {
        return given()
                .header(
                        "Authorization",
                        "Bearer " + token)
                .contentType(ContentType.JSON)
                .pathParam("packageCode", code)
                .when().get(BASE_URL + "/private/shipping/package/{packageCode}")
                .as(ShippingPackage.class);
    }
}