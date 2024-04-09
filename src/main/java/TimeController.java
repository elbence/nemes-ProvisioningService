import entity.Catastrophe;
import entity.Coordinate;
import entity.Event;
import entity.Zone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeController {

    // ============================================= ACTUAL CLASS RESPONSABILITIES =============================================

    public static long realStartTime;

    public static void main(String[] args) {
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                // ACTUAL EXECUTED TASKS HERE
                try {updateActiveAlertData();}
                catch (SQLException | IOException | InterruptedException e) {throw new RuntimeException(e);}
                controlRealTime();
            }
        };

        // schedule the task to run starting now and then every hour.
        realStartTime = System.nanoTime();
        timer.schedule (hourlyTask, 0L, 1000 * 60); // TODO change, currently working at minute intervals

    }

    public static void controlRealTime() {
        long realCurrentTime = System.nanoTime();
        System.out.println("A total " + (realCurrentTime - realStartTime) / 1000000000 + " seconds have passed since last execution");
        realStartTime = realCurrentTime;
    }



    // ============================================= MAIN PROCESSING METHOD =============================================

    // TODO separate responsabilities? maybe class for running and class for logics?
    public static void updateActiveAlertData() throws SQLException, IOException, InterruptedException {

        String apiUrl = "https://opendata.aemet.es/opendata/api/avisos_cap/ultimoelaborado/area/77";

        HttpClient httpClient = HttpClient.newHttpClient();

        System.out.println("\nFetching AEMET data...");
        HttpResponse<String> initialResponse = fetchInitialAemetResponse(apiUrl, httpClient);
        if (checkResponseStatus(initialResponse) != 200) throw new IOException("Data fetching gone wrong");

        System.out.println("Processing response and fetching actual data...");
        HttpResponse<String> downloadRes = fetchRealDataAfterRedirect(initialResponse, httpClient);
        if (downloadRes.statusCode() != 200) throw new IOException("Real data fetching gone wrong after redirect");

        System.out.println("Processing actual data (raw xml string -> json)...");
        List<JSONObject> alertsInJson = processResponseIntoJSONObjectList(downloadRes);
        List<Catastrophe> catastrophes = catastrophesFromJsonAlertList(alertsInJson);

        System.out.println("Saving data to db...");
        insertCatastrophesIntoDatabase(catastrophes);

    }

    private static List<Catastrophe> catastrophesFromJsonAlertList(List<JSONObject> alertsInJson) {
        List<Catastrophe> catastropheList = new ArrayList<>();
        for (JSONObject alert : alertsInJson) {

            JSONObject catastropheBaseInfoObject = alert.getJSONObject("alert").getJSONArray("info").getJSONObject(0);

            // Get shared properties (severity)
            String severity = catastropheBaseInfoObject.getString("severity");

            // Get event
            Event event;
            event = new Event();
            event.setName(catastropheBaseInfoObject.getJSONObject("eventCode").getString("value"));
            event.setSeverity(severity);
//            LocalDate dateStart = xxx;  // TODO
//            LocalDate endDate = xxx;  // TODO

            Object rawCatastrophes = catastropheBaseInfoObject.get("area");
            JSONArray alertCatastrophes = new JSONArray(0);
            if (rawCatastrophes instanceof JSONArray) alertCatastrophes = (JSONArray) rawCatastrophes;
            else if (rawCatastrophes instanceof JSONObject) alertCatastrophes.put(rawCatastrophes);

            for (int i = 0; i < alertCatastrophes.length(); i++) {
                JSONObject catastropheAct = alertCatastrophes.getJSONObject(i);

                // Get catastrophe name
                String name = catastropheAct.getString("areaDesc") + " - " + event.getName();

                // Get zones FIXME should do a for loop traversing zones...
                Object rawCoordinates = catastropheAct.get("polygon");
                String coordinatesAsString;
                if (rawCoordinates instanceof JSONArray) coordinatesAsString = ((JSONArray) rawCoordinates).getString(0);
                else coordinatesAsString = (String) rawCoordinates;
                Zone zone = zoneFromRawStringPolygon(coordinatesAsString);

                Catastrophe catastrophe = new Catastrophe();
                catastrophe.setEvent(event);
                catastrophe.setName(name);
                catastrophe.setDescription("-");
                catastrophe.setStartDate(LocalDate.now());
                catastrophe.setLastValidDate(LocalDate.now());
                catastrophe.setZone(zone);
                catastropheList.add(catastrophe);
            }
        }
        return catastropheList;
    }

    public static Zone zoneFromRawStringPolygon(String polygonFromAemetApi) {
        Coordinate center = new Coordinate(); // Assigning 0 to center as per your requirement
        int radius = 0; // Assigning 0 to radius as per your requirement
        List<Coordinate> coordinateList = parseCoordinates(polygonFromAemetApi);
        Zone zone = new Zone();
        zone.setCenter(center);
        zone.setRadius(radius);
        zone.setPolygons(coordinateList);
        return zone;
    }

    private static List<Coordinate> parseCoordinates(String polygonString) {
        List<Coordinate> coordinateList = new ArrayList<>();
        String[] coordinatesArray = polygonString.split(" ");
        for (String coordinate : coordinatesArray) {
            String[] latLon = coordinate.split(",");
            float lat = Float.parseFloat(latLon[0]);
            float lon = Float.parseFloat(latLon[1]);
            Coordinate coord = new Coordinate();
            coord.setLat(lat);
            coord.setLon(lon);
            coordinateList.add(coord);
        }
        return coordinateList;
    }



    // ============================================= DATABASE MANAGEMENT =============================================

    private static void insertCatastrophesIntoDatabase(List<Catastrophe> catastrophes) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        for (Catastrophe catastrophe : catastrophes) {
            System.out.println(" * Saving: " + catastrophe.toString());
            // Get event if in db
            Event catastropheEvent = catastrophe.getEvent();
            Event dbEvent =  entityManager.createNamedQuery("findById", Event.class)
                    .setParameter("name", catastropheEvent.getName())
                    .setParameter("severity", catastropheEvent.getSeverity())
                    .getSingleResult();
            if (dbEvent != null) {
                System.out.println("   - Updating event to found in db: " + dbEvent.toString());
                catastrophe.setEvent(dbEvent);
            } else {
                catastrophe.setEvent(catastropheEvent);
            }
            // Save catastrophe
            entityManager.getTransaction().begin();
            entityManager.persist(catastrophe);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
        entityManagerFactory.close();
    }



    // ============================================= PROCESSING DATA =============================================

    private static List<JSONObject> processResponseIntoJSONObjectList(HttpResponse<String> downloadRes) {
        String regex = "(?s)<alert[^>]*>.*?</alert>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(downloadRes.body());
        List<JSONObject> alertsInJson = new ArrayList<>();
        while (matcher.find()) {
            String alertInXml = matcher.group();
            JSONObject actualAlert = XML.toJSONObject(alertInXml);
            alertsInJson.add(actualAlert);
        }
        return alertsInJson;
    }



    // ============================================= SECOND REQUESTS =============================================

    private static HttpResponse<String> fetchRealDataAfterRedirect(HttpResponse<String> response, HttpClient httpClient) throws IOException, InterruptedException {
        JSONObject initialResponseAsJson = new JSONObject(response.body());
        String urlToRealData = initialResponseAsJson.getString("datos");
        HttpRequest realDataPreparedRequest = HttpRequest.newBuilder(URI.create(urlToRealData)).GET().build();
        return httpClient.send(realDataPreparedRequest, HttpResponse.BodyHandlers.ofString());
    }



    // ============================================= INITIAL REQUESTS =============================================

    private static HttpResponse<String> fetchInitialAemetResponse(String apiUrl, HttpClient httpClient) throws IOException, InterruptedException {
        // TODO api_key should be an env variable
        HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl))
                .header("accept", "application/json")
                .header("api_key", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbDQwNDE1NUB1amkuZXMiLCJqdGkiOiI2OTAzYWIyNy01YTg4LTRkMTYtOWIxZi04ZGU3MDE1ZmY5OWUiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTcxMDM0MTQ5NCwidXNlcklkIjoiNjkwM2FiMjctNWE4OC00ZDE2LTliMWYtOGRlNzAxNWZmOTllIiwicm9sZSI6IiJ9.rQ7p9coGd86oLqumfs5e53jawdAHG0Ihduruy6TxtQQ")
                .GET().build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static int checkResponseStatus(HttpResponse<String> response) {
        if (response.statusCode() < 200 || response.statusCode() > 300) return response.statusCode();
        JSONObject responseBody = new JSONObject(response.body());
        return responseBody.getInt("estado");
    }

}


