package com.tl.toc.stepDefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.tl.toc.utils.CommonUtil;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.ArrayList;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class SatelliteInformationStepDefinitions {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;
    private String requestUrl;
    private String satelliteId;
    private Scenario scenario = null;
    private RequestSpecification requestSpecification;
    private String tleFormat;
    private String timeStamps;
    private String units;
    private String baseUrl;
    private Properties props = null;
    private String propertiesFileName;

    @Before
    public void initialization(Scenario scenario) {
        this.scenario = scenario;
        String profile = System.getProperty("profile") != null ? System.getProperty("profile") : "e1";
        propertiesFileName = "test-config-" + profile + ".properties";
        if (props == null) {
            props = new CommonUtil().readPropertiesFile(propertiesFileName);
        }
        baseUrl = props.getProperty(profile + ".url");
    }

    @Given("User is having a satellite NORMAD id as {string}")
    public void user_is_having_a_satellite_normad_id_as(String id) {
        satelliteId = id;
        RestAssured.baseURI = baseUrl;
        request = RestAssured.given();
    }

    @When("the user will call the {string} with {string}, {string} and {string}")
    public void the_user_will_call_the_with_id(String endpoint, String timeStamps, String units, String indent) {
        try {
            this.timeStamps = timeStamps;
            this.units = units;
            requestUrl = endpoint.replace("id", satelliteId);
            scenario.log(requestUrl);
            response = request.param("timestamps", this.timeStamps)
                    .param("units", this.units)
                    .param("indent", indent)
                    .contentType("applicaiton/json")
                    .get(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the status code is {int}")
    public void the_status_code_is(Integer statusCode) {
        scenario.log(response.statusLine());
        response.then()
                .assertThat()
                .statusCode(statusCode);
        JsonNode responseBody = new CommonUtil().getJsonBody(response.asPrettyString());
        assertThat("Validating name", responseBody.get(0).get("name").asText().equals("iss"));
        assertThat("Validating id", responseBody.get(0).get("id").intValue() == Integer.parseInt(satelliteId));
        assertThat("Validating latitude", responseBody.get(0).get("latitude").asInt() >= -90 && responseBody.get(0).get("latitude").asInt() <= 90);
        assertThat("Validating longitude", responseBody.get(0).get("longitude").asInt() >= -180 && responseBody.get(0).get("longitude").asInt() <= 180);
        assertThat("Validating altitude", responseBody.get(0).get("altitude").asInt() >= 0);
        assertThat("Validating velocity", responseBody.get(0).get("velocity").asInt() >= 0);
        //visibility validation should be dynamic. As this api provide only once the expected value is being hard coded
        assertThat("Validating visibility", !responseBody.get(0).get("visibility").asText().isEmpty());
        assertThat("Validating footprint", responseBody.get(0).get("footprint").intValue() >= 0);
        assertThat("Validating timestamp", responseBody.get(0).get("timestamp").asText().equals(timeStamps.split(",")[0]));
        assertThat("Validating daynum", responseBody.get(0).get("daynum").asInt() >= 0);
        assertThat("Validating solar_lat", responseBody.get(0).get("solar_lat").asInt() >= -90 && responseBody.get(0).get("solar_lat").asInt() <= 90);
        assertThat("Validating solar_lon", responseBody.get(0).get("solar_lon").asInt() >= -360 && responseBody.get(0).get("solar_lon").asInt() <= 360);
        if (units.equals("miles")) {
            //this will cover if unit is in miles
            assertThat("Validating units", responseBody.get(0).get("units").asText().equals(units));
        } else {
            //this will cover if unit is kilometers or any other units
            assertThat("Validating units", responseBody.get(0).get("units").asText().equals("kilometers"));
        }

    }

    @Then("should receive the status code is {int}")
    public void should_receive_the_status_code_is(Integer statusCode) {
        scenario.log(response.statusLine());
        response.then()
                .assertThat()
                .statusCode(statusCode);
        JsonNode responseBody = new CommonUtil().getJsonBody(response.asPrettyString());

    }

    @Then("receive a valid response")
    public void receive_a_valid_response() {
        scenario.log(response.asPrettyString());
        response.then()
                .assertThat()
                .header("X-Rate-Limit-Limit", notNullValue())
                .header("X-Rate-Limit-remaining", notNullValue())
                .header("X-Rate-Limit-interval", notNullValue());
    }

    @Then("should return status code as {string}")
    public void should_return_status_code_as(String statusCode) {
        scenario.log(response.statusLine());
        response.then()
                .assertThat()
                .statusCode(Integer.parseInt(statusCode));
    }

    @Then("user should receive an error message as like {string}")
    public void user_should_receive_an_error_message_as_like(String errorMessage) {
        JsonNode responseBody = new CommonUtil().getJsonBody(response.asPrettyString());
        assertThat("Validating error message", responseBody.get("error").asText().trim().equals(errorMessage));
    }

    @When("the user will call the TLE {string} with {string}")
    public void the_user_will_call_the_tle_with(String endpoint, String format) {
        try {
            tleFormat = format;
            requestUrl = endpoint.replace("id", satelliteId);
            scenario.log(requestUrl);
            response = request.param("format", tleFormat)
                    .get(requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("should receive a valid data for the satellite {string}")
    public void should_receive_a_valid_data(String satteliteName) {
        scenario.log(response.asPrettyString());
        boolean isJson = new CommonUtil().isJson(response.asPrettyString());
        response.then()
                .assertThat()
                .header("X-Rate-Limit-Limit", notNullValue())
                .header("X-Rate-Limit-remaining", notNullValue())
                .header("X-Rate-Limit-interval", notNullValue());
        switch (tleFormat) {
            case "text":
                assertThat("Validating data format", !isJson);
                break;
            case "json":
                assertThat("Validating data format", isJson);
                break;
            default:
                assertThat("Validating data format", isJson);
                break;
        }
        if (isJson) {
            response.then()
                    .assertThat()
                    .body("header", equalTo(satteliteName))
                    .body("id", equalTo(satelliteId))
                    .body("name", equalTo("iss"))
                    .body("requested_timestamp", notNullValue())
                    .body("tle_timestamp", notNullValue())
                    .body("line1", notNullValue())
                    .body("line2", notNullValue());
            //More validation can be added here for each field of data
        } else {
            //More validation can be added here for text formatted response for each line of data
            ArrayList<String> textLinesAsList = new CommonUtil().readTextAsList(response.asPrettyString());
            assertThat("Validating ISS name.", satteliteName.equals(textLinesAsList.get(0).trim()));
        }
    }

}



