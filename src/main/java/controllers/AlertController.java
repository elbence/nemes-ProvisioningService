package controllers;


import externaldatafetch.RequestManagers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AlertController {

    public static void alertUsersOfActiveCatastrophes() {
        System.out.println("Update active alert data");
        System.out.println("Sending update notification");

        String apiUrl = "http://localhost:8080/alerts/sendalerts";

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl))
                .header("accept", "application/json")
                .GET().build();

        HttpResponse<String> response = null;
        try {response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());}
        catch (IOException | InterruptedException e) {System.out.println("! Update active alert data failed: " + e.getMessage());}

        if (response == null || response.statusCode() != 200)
            System.out.println("! Update active alert data failed");
        else System.out.println("Update active alert data successful");
    }

}
