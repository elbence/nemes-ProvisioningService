package debugutils;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpFakeResponse implements HttpResponse<String> {

    @Override
    public int statusCode() {
        return 0;
    }

    @Override
    public HttpRequest request() {
        return null;
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public String body() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<alert xmlns = \"urn:oasis:names:tc:emergency:cap:1.2\">\n" +
                "  <identifier>2.49.0.0.724.0.ES.20240405080113.622201VIRM06221712304073</identifier>\n" +
                "  <sender>http://www.aemet.es</sender>\n" +
                "  <sent>2024-04-05T08:01:13-00:00</sent>\n" +
                "  <status>Actual</status>\n" +
                "  <msgType>Update</msgType>\n" +
                "  <scope>Public</scope>\n" +
                "  <references>http://www.aemet.es,2.49.0.0.724.0.ES.20240404093441.622201VIRM06221712223281,2024-04-04T09:34:41-00:00</references>\n" +
                "  <info>\n" +
                "    <language>es-ES</language>\n" +
                "    <category>Met</category>\n" +
                "    <event>Aviso de vientos de nivel amarillo</event>\n" +
                "    <responseType>Monitor</responseType>\n" +
                "    <urgency>Future</urgency>\n" +
                "    <severity>Moderate</severity>\n" +
                "    <certainty>Possible</certainty>\n" +
                "    <eventCode>\n" +
                "      <valueName>AEMET-Meteoalerta fenomeno</valueName>\n" +
                "      <value>VI;Vientos</value>\n" +
                "    </eventCode>\n" +
                "    <effective>2024-04-05T10:01:13+02:00</effective>\n" +
                "    <onset>2024-04-06T00:00:00+02:00</onset>\n" +
                "    <expires>2024-04-06T23:59:59+02:00</expires>\n" +
                "    <senderName>AEMET. Agencia Estatal de Meteorología</senderName>\n" +
                "    <headline>Aviso de vientos de nivel amarillo. Pirineo oscense</headline>\n" +
                "    <description>Rachas máximas: 80 km/h. Aviso de baja probabilidad. Viento del sur. El aviso se refiere a cotas altas y zonas expuestas.</description>\n" +
                "    <instruction>Esté atento. Manténgase informado de la predicción meteorológica más actualizada. Se pueden producir daños moderados a personas y bienes, especialmente a aquellos vulnerables o en zonas expuestas al fenómeno.</instruction>\n" +
                "    <web>https://www.aemet.es/es/eltiempo/prediccion/avisos</web>\n" +
                "    <contact>AEMET</contact>\n" +
                "    <parameter>\n" +
                "      <valueName>AEMET-Meteoalerta nivel</valueName>\n" +
                "      <value>amarillo</value>\n" +
                "    </parameter>\n" +
                "    <parameter>\n" +
                "      <valueName>AEMET-Meteoalerta parametro</valueName>\n" +
                "      <value>RM;Rachas máximas;80 km/h</value>\n" +
                "    </parameter>\n" +
                "    <parameter>\n" +
                "      <valueName>AEMET-Meteoalerta probabilidad</valueName>\n" +
                "      <value>10%-40%</value>\n" +
                "    </parameter>\n" +
                "    <area>\n" +
                "      <areaDesc>Pirineo oscense</areaDesc>\n" +
                "      <polygon>42.74,-0.9 42.79,-0.85 42.85,-0.86 42.88,-0.81 42.9,-0.82 42.92,-0.78 42.92,-0.73 42.9,-0.73 42.83,-0.6 42.8,-0.6 42.78,-0.57 42.8,-0.52 42.82,-0.51 42.8,-0.44 42.8,-0.39 42.84,-0.35 42.84,-0.31 42.81,-0.24 42.78,-0.16 42.73,-0.11 42.72,-0.07 42.69,-0.06 42.69,0.0 42.72,0.09 42.72,0.14 42.74,0.18 42.72,0.26 42.67,0.3 42.72,0.36 42.69,0.4 42.69,0.58 42.7,0.59 42.69,0.66 42.66,0.7 42.62,0.71 42.61,0.77 42.55,0.75 42.53,0.73 42.5,0.73 42.47,0.7 42.44,0.71 42.39,0.75 42.35,0.76 42.25,0.74 42.17,0.7 42.12,0.7 42.08,0.68 42.03,0.65 41.96,0.58 41.97,0.55 41.97,0.47 41.99,0.47 42.03,0.44 42.07,0.41 42.06,0.35 42.06,0.32 42.16,0.33 42.17,0.3 42.23,0.28 42.22,0.19 42.24,0.15 42.25,0.1 42.28,0.11 42.3,0.07 42.3,0.01 42.26,0.01 42.26,-0.04 42.32,-0.04 42.36,-0.06 42.31,-0.14 42.3,-0.17 42.31,-0.22 42.34,-0.23 42.35,-0.36 42.34,-0.4 42.34,-0.47 42.36,-0.52 42.39,-0.58 42.39,-0.65 42.43,-0.65 42.48,-0.7 42.45,-0.73 42.46,-0.78 42.44,-0.85 42.46,-0.88 42.54,-0.92 42.59,-0.92 42.61,-0.9 42.66,-0.93 42.67,-0.89 42.7,-0.9 42.74,-0.9</polygon>\n" +
                "      <geocode>\n" +
                "        <valueName>AEMET-Meteoalerta zona</valueName>\n" +
                "        <value>622201</value>\n" +
                "      </geocode>\n" +
                "    </area>\n" +
                "  </info>\n" +
                "  <info>\n" +
                "    <language>en-GB</language>\n" +
                "    <category>Met</category>\n" +
                "    <event>Moderate wind warning</event>\n" +
                "    <responseType>Monitor</responseType>\n" +
                "    <urgency>Future</urgency>\n" +
                "    <severity>Moderate</severity>\n" +
                "    <certainty>Possible</certainty>\n" +
                "    <eventCode>\n" +
                "      <valueName>AEMET-Meteoalerta fenomeno</valueName>\n" +
                "      <value>VI;Vientos</value>\n" +
                "    </eventCode>\n" +
                "    <effective>2024-04-05T10:01:13+02:00</effective>\n" +
                "    <onset>2024-04-06T00:00:00+02:00</onset>\n" +
                "    <expires>2024-04-06T23:59:59+02:00</expires>\n" +
                "    <senderName>AEMET. State Meteorological Agency</senderName>\n" +
                "    <headline>Moderate wind warning. Pirineo oscense</headline>\n" +
                "    <description>Maximum gust of wind: 80 km/h. Aviso de baja probabilidad. Viento del sur. El aviso se refiere a cotas altas y zonas expuestas.</description>\n" +
                "    <instruction>Be aware, keep up to date with the latest weather forecast. Moderate damages to people and properties may occur, especially to those vulnerable or in exposed areas.</instruction>\n" +
                "    <web>https://www.aemet.es/en/eltiempo/prediccion/avisos</web>\n" +
                "    <contact>AEMET</contact>\n" +
                "    <parameter>\n" +
                "      <valueName>AEMET-Meteoalerta nivel</valueName>\n" +
                "      <value>amarillo</value>\n" +
                "    </parameter>\n" +
                "    <parameter>\n" +
                "      <valueName>AEMET-Meteoalerta parametro</valueName>\n" +
                "      <value>RM;Maximum gust of wind;80 km/h</value>\n" +
                "    </parameter>\n" +
                "    <parameter>\n" +
                "      <valueName>AEMET-Meteoalerta probabilidad</valueName>\n" +
                "      <value>10%-40%</value>\n" +
                "    </parameter>\n" +
                "    <area>\n" +
                "      <areaDesc>Pirineo oscense</areaDesc>\n" +
                "      <polygon>42.74,-0.9 42.79,-0.85 42.85,-0.86 42.88,-0.81 42.9,-0.82 42.92,-0.78 42.92,-0.73 42.9,-0.73 42.83,-0.6 42.8,-0.6 42.78,-0.57 42.8,-0.52 42.82,-0.51 42.8,-0.44 42.8,-0.39 42.84,-0.35 42.84,-0.31 42.81,-0.24 42.78,-0.16 42.73,-0.11 42.72,-0.07 42.69,-0.06 42.69,0.0 42.72,0.09 42.72,0.14 42.74,0.18 42.72,0.26 42.67,0.3 42.72,0.36 42.69,0.4 42.69,0.58 42.7,0.59 42.69,0.66 42.66,0.7 42.62,0.71 42.61,0.77 42.55,0.75 42.53,0.73 42.5,0.73 42.47,0.7 42.44,0.71 42.39,0.75 42.35,0.76 42.25,0.74 42.17,0.7 42.12,0.7 42.08,0.68 42.03,0.65 41.96,0.58 41.97,0.55 41.97,0.47 41.99,0.47 42.03,0.44 42.07,0.41 42.06,0.35 42.06,0.32 42.16,0.33 42.17,0.3 42.23,0.28 42.22,0.19 42.24,0.15 42.25,0.1 42.28,0.11 42.3,0.07 42.3,0.01 42.26,0.01 42.26,-0.04 42.32,-0.04 42.36,-0.06 42.31,-0.14 42.3,-0.17 42.31,-0.22 42.34,-0.23 42.35,-0.36 42.34,-0.4 42.34,-0.47 42.36,-0.52 42.39,-0.58 42.39,-0.65 42.43,-0.65 42.48,-0.7 42.45,-0.73 42.46,-0.78 42.44,-0.85 42.46,-0.88 42.54,-0.92 42.59,-0.92 42.61,-0.9 42.66,-0.93 42.67,-0.89 42.7,-0.9 42.74,-0.9</polygon>\n" +
                "      <geocode>\n" +
                "        <valueName>AEMET-Meteoalerta zona</valueName>\n" +
                "        <value>622201</value>\n" +
                "      </geocode>\n" +
                "    </area>\n" +
                "  </info>\n" +
                "</alert>";
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public HttpClient.Version version() {
        return null;
    }
}
