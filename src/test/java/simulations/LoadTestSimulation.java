package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import models.RequestModel;
import utils.JSONParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadTestSimulation extends Simulation {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    {
        try {
            // Parse the JSON file to get all request models
            List<RequestModel> requestModels = JSONParser.parseJsonToRequests("requests.json");

            // Create a scenario builder
            ScenarioBuilder scn = scenario("Load Test Scenario");

            // Execute login request if it is present in the request models
            RequestModel loginRequest = requestModels.stream()
                    .filter(req -> req.getDescription().equalsIgnoreCase("Login request"))
                    .findFirst()
                    .orElse(null);

            if (loginRequest != null) {
                HttpRequestActionBuilder loginHttpRequest = http(loginRequest.getDescription())
                        .post(loginRequest.getEndpoint())
                        .headers(loginRequest.getHeaders())
                        .body(StringBody(OBJECT_MAPPER.writeValueAsString(loginRequest.getBody())))
                        .check(status().is(loginRequest.getExpectedStatus()))
                        .check(jsonPath("$.token").saveAs("authToken"));

                scn = scn.exec(loginHttpRequest)
                        .exec(session -> {
                            System.out.println("Token: " + session.getString("authToken"));
                            return session;
                        });
            }

            // Handle other requests dynamically
            for (RequestModel request : requestModels) {
                if (loginRequest != null && request.getDescription().equalsIgnoreCase("Login request")) {
                    // Skip login request if it's already executed
                    continue;
                }

                HttpRequestActionBuilder httpRequest;
                Map<String, String> headers = new HashMap<>(request.getHeaders());

                // Replace Authorization header with the token from login if needed
                if (request.isRequiresToken()) {
                    scn = scn.exec(session -> {
                        String token = session.getString("authToken");
                        if (token != null) {
                            headers.put("Authorization", "Bearer " + token);
                        }
                        return session;
                    });
                }

                switch (request.getMethod().toUpperCase()) {
                    case "POST":
                        String postBody = request.getBody() != null ? OBJECT_MAPPER.writeValueAsString(request.getBody()) : null;
                        httpRequest = http(request.getDescription())
                                .post(request.getEndpoint())
                                .headers(headers)
                                .body(postBody != null ? StringBody(postBody) : null)
                                .check(status().is(request.getExpectedStatus()))
                                .check(bodyString().saveAs("responseBody"));
                        break;
                    case "GET":
                        Map<String, Object> parameters = request.getParameters();
                        httpRequest = http(request.getDescription())
                                .get(request.getEndpoint())
                                .headers(headers)
                                .queryParamMap(parameters != null ? parameters : new HashMap<>())
                                .check(status().is(request.getExpectedStatus()))
                                .check(bodyString().saveAs("responseBody"));
                        break;
                    case "PUT":
                        String putBody = request.getBody() != null ? OBJECT_MAPPER.writeValueAsString(request.getBody()) : null;
                        httpRequest = http(request.getDescription())
                                .put(request.getEndpoint())
                                .headers(headers)
                                .body(putBody != null ? StringBody(putBody) : null)
                                .check(status().is(request.getExpectedStatus()))
                                .check(bodyString().saveAs("responseBody"));
                        break;
                    case "DELETE":
                        httpRequest = http(request.getDescription())
                                .delete(request.getEndpoint())
                                .headers(headers)
                                .check(status().is(request.getExpectedStatus()))
                                .check(bodyString().saveAs("responseBody"));
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported HTTP method: " + request.getMethod());
                }

                scn = scn.exec(httpRequest)
                        .exec(session -> {
                            System.out.println("Response: " + session.getString("responseBody"));
                            return session;
                        })
                        .pause(1); // Pause between requests
            }

            // Set up the scenario
            setUp(
                    scn.injectOpen(rampUsers(10).during(5 * 60))
            ).protocols(http.baseUrl("https://reqres.in")); // Set a default base URL if needed

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}