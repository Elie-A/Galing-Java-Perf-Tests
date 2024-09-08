package models;

import java.util.Map;

public class RequestModel {
    private String method;
    private String baseUrl; // New field for base URL
    private String endpoint;
    private Map<String, String> headers;
    private Map<String, Object> parameters;
    private Object body;
    private String description;
    private int expectedStatus;
    private boolean requiresToken; // New field to indicate if a token is required

    // Getters and setters
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getBaseUrl() { return baseUrl; } // Getter for baseUrl
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; } // Setter for baseUrl

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }

    public Map<String, Object> getParameters() { return parameters; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }

    public Object getBody() { return body; }
    public void setBody(Object body) { this.body = body; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getExpectedStatus() { return expectedStatus; }
    public void setExpectedStatus(int expectedStatus) { this.expectedStatus = expectedStatus; }

    public boolean isRequiresToken() { return requiresToken; } // Getter for requiresToken
    public void setRequiresToken(boolean requiresToken) { this.requiresToken = requiresToken; } // Setter for requiresToken
}