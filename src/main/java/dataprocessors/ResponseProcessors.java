package dataprocessors;

import entity.Catastrophe;
import entity.Coordinate;
import entity.Event;
import entity.Zone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseProcessors {

    public static List<JSONObject> processAllAlertsInXmlToJsonList(HttpResponse<String> downloadRes) {
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

    public static List<Catastrophe> catastrophesFromJsonAlertList(List<JSONObject> alertsInJson) {
        List<Catastrophe> catastropheList = new ArrayList<>();
        for (JSONObject alert : alertsInJson) {

            JSONObject catastropheBaseInfoObject = alert.getJSONObject("alert").getJSONArray("info").getJSONObject(0);

            // Get shared properties (severity)
            String severity = catastropheBaseInfoObject.getString("severity");

            // Get event
            Event event;
            event = new Event();
            event.setEventName(catastropheBaseInfoObject.getJSONObject("eventCode").getString("value"));
            event.setSeverity(severity);

            // Get dates
            String startDateString = catastropheBaseInfoObject.getString("effective");
            String endDateString = catastropheBaseInfoObject.getString("expires");
            LocalDate startDate = extractLocalDateFromString(startDateString);
            LocalDate endDate = extractLocalDateFromString(endDateString);

            Object rawCatastrophes = catastropheBaseInfoObject.get("area");
            JSONArray alertCatastrophes = new JSONArray(0);
            if (rawCatastrophes instanceof JSONArray) alertCatastrophes = (JSONArray) rawCatastrophes;
            else if (rawCatastrophes instanceof JSONObject) alertCatastrophes.put(rawCatastrophes);

            System.out.print(alertCatastrophes.length() + ", ");

            for (int i = 0; i < alertCatastrophes.length(); i++) {
                JSONObject catastropheAct = alertCatastrophes.getJSONObject(i);

                // Get catastrophe name
                String name = catastropheAct.getString("areaDesc") + " - " + event.getEventName();

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
                catastrophe.setStartDate(startDate);
                catastrophe.setLastValidDate(endDate);
                catastrophe.setZone(zone);
                catastropheList.add(catastrophe);
            }
        }
        return catastropheList;
    }

    public static LocalDate extractLocalDateFromString(String dateString) {
        // Parse the input string to an OffsetDateTime
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateString);

        // Extract LocalDate from OffsetDateTime
        return offsetDateTime.toLocalDate();
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

    public static List<Coordinate> parseCoordinates(String polygonString) {
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

}
