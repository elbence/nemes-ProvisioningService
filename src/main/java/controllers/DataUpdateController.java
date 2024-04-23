package controllers;

import dataprocessors.ResponseProcessors;
import dbutils.DBManager;
import entity.Catastrophe;
import externaldatafetch.RequestManagers;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.List;

public class DataUpdateController {

    public static String API_URL = "https://opendata.aemet.es/opendata/api/avisos_cap/ultimoelaborado/area/esp"; // FETCH events in CValenciana, change 77 to esp for SPAIN range

    // ============================================= MAIN PROCESSING METHOD =============================================

    public static void updateActiveAlertData() throws SQLException, IOException, InterruptedException {

        System.out.println("\n============================================================================================");
        System.out.println("UPDATING DATA WITH NEWEST ALERTS");
        System.out.println("============================================================================================\n");

        HttpClient httpClient = HttpClient.newHttpClient();

        System.out.println("Fetching AEMET data...");
        HttpResponse<String> initialResponse = RequestManagers.fetchInitialAemetResponse(API_URL, httpClient);
        if (RequestManagers.checkResponseStatus(initialResponse) != 200) throw new IOException("Data fetching gone wrong");

        System.out.println("Processing response and fetching actual data...");
        HttpResponse<String> downloadRes = RequestManagers.fetchRealDataAfterRedirect(initialResponse, httpClient);
        if (downloadRes.statusCode() != 200) throw new IOException("Real data fetching gone wrong after redirect");

        System.out.println("Processing actual data (raw xml string -> json)...");
        List<JSONObject> alertsInJson = ResponseProcessors.processAllAlertsInXmlToJsonList(downloadRes);
        System.out.print("Found " + alertsInJson.size() + " alerts with following numbers of catastrophes each: ");
        List<Catastrophe> catastrophes = ResponseProcessors.catastrophesFromJsonAlertList(alertsInJson);
        System.out.println("\nFound total catastrophes: " + catastrophes.size());

        System.out.println("Saving data to db...");
        DBManager.insertCatastrophesIntoDatabase(catastrophes);

        System.out.println("End of update.");

    }

}
