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

public class APISimulation extends Simulation {
    // ObjectMapper instance for JSON serialization
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    {
        try {
            List<RequestModel> requestModels = JSONParser.parseJsonToRequests("C:\\Users\\eliea\\Documents\\intellij-workspace\\Galing-Java-Perf-Tests\\src\\test\\resources\\requests.json");

            HttpProtocolBuilder httpProtocol = http
                    .baseUrl("https://reqres.in")
                    .acceptHeader("application/json");

            ScenarioBuilder scn = scenario("API Test Scenario");

            for (RequestModel request : requestModels) {
                HttpRequestActionBuilder httpRequest;
                switch (request.getMethod().toUpperCase()) {
                    case "POST":
                        String postBody = request.getBody() != null ? OBJECT_MAPPER.writeValueAsString(request.getBody()) : null;
                        httpRequest = http(request.getDescription())
                                .post(request.getEndpoint())
                                .headers(request.getHeaders())
                                .body(postBody != null ? StringBody(postBody) : null)
                                .check(status().is(request.getExpectedStatus()))
                                .check(bodyString().saveAs("responseBody"));
                        break;
                    case "GET":
                        Map<String, Object> parameters = request.getParameters();
                        httpRequest = http(request.getDescription())
                                .get(request.getEndpoint())
                                .headers(request.getHeaders())
                                .queryParamMap(parameters != null ? parameters : new HashMap<>())
                                .check(status().is(request.getExpectedStatus()))
                                .check(bodyString().saveAs("responseBody"));
                        break;
                    case "PUT":
                        String putBody = request.getBody() != null ? OBJECT_MAPPER.writeValueAsString(request.getBody()) : null;
                        httpRequest = http(request.getDescription())
                                .put(request.getEndpoint())
                                .headers(request.getHeaders())
                                .body(putBody != null ? StringBody(putBody) : null)
                                .check(status().is(request.getExpectedStatus()))
                                .check(bodyString().saveAs("responseBody"));
                        break;
                    case "DELETE":
                        httpRequest = http(request.getDescription())
                                .delete(request.getEndpoint())
                                .headers(request.getHeaders())
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

            setUp(
                    scn.injectOpen(atOnceUsers(1))
            ).protocols(httpProtocol);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}