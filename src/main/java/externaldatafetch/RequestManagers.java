package externaldatafetch;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestManagers {

    public static HttpResponse<String> fetchRealDataAfterRedirect(HttpResponse<String> response, HttpClient httpClient) throws IOException, InterruptedException {
        JSONObject initialResponseAsJson = new JSONObject(response.body());
        String urlToRealData = initialResponseAsJson.getString("datos");
        HttpRequest realDataPreparedRequest = HttpRequest.newBuilder(URI.create(urlToRealData)).GET().build();
        return httpClient.send(realDataPreparedRequest, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> fetchInitialAemetResponse(String apiUrl, HttpClient httpClient) throws IOException, InterruptedException {
        // TODO api_key should be an env variable
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl))
                .header("accept", "application/json")
                .header("api_key", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbDQwNDE1NUB1amkuZXMiLCJqdGkiOiI2OTAzYWIyNy01YTg4LTRkMTYtOWIxZi04ZGU3MDE1ZmY5OWUiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTcxMDM0MTQ5NCwidXNlcklkIjoiNjkwM2FiMjctNWE4OC00ZDE2LTliMWYtOGRlNzAxNWZmOTllIiwicm9sZSI6IiJ9.rQ7p9coGd86oLqumfs5e53jawdAHG0Ihduruy6TxtQQ")
                .GET().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static int checkResponseStatus(HttpResponse<String> response) {
        if (response.statusCode() < 200 || response.statusCode() > 300) return response.statusCode();
        JSONObject responseBody = new JSONObject(response.body());
        return responseBody.getInt("estado");
    }


}
