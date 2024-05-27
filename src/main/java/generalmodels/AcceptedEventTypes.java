package generalmodels;

public enum AcceptedEventTypes {
    Costeros("Costeros", "Severe", 3),
    Vientos("Vientos", "Moderate", 2),
    Nevadas("Nevadas", "Moderate", 2),
    Lluvias("Lluvias", "Minor", 1),
    Tormentas("Tormentas", "Moderate", 2),
    Nieblas("Nieblas", "Moderate", 2);

    private final String name;
    private final String minimumSeverity;
    private final int severityValue;

    AcceptedEventTypes(String name, String minimumSeverity, int severityValue) {
        this.name = name;
        this.minimumSeverity = minimumSeverity;
        this.severityValue = severityValue;
    }

    public String getName() {
        return name;
    }
    public String getMinimumSeverity() {
        return minimumSeverity;
    }
    public int getSeverityValue() {
        return severityValue;
    }

    public static AcceptedEventTypes getByName(String name) {
        switch (name) {
            case "Costeros": return AcceptedEventTypes.Costeros;
            case "Vientos": return AcceptedEventTypes.Vientos;
            case "Nevadas": return AcceptedEventTypes.Nevadas;
            case "Lluvias": return AcceptedEventTypes.Lluvias;
            case "Tormentas": return AcceptedEventTypes.Tormentas;
            case "Nieblas": return AcceptedEventTypes.Nieblas;
        }
        return null;
    }

    public static int getSeverityValueFromName(String severityName) {
        switch (severityName.toLowerCase()) {
            case "severe": return 3;
            case "moderate": return 2;
            case "minor": return 1;
        }
        return -1;
    }

    public static boolean isAccepted(String name, String severity) {
        AcceptedEventTypes acceptedEventType = getByName(name);
        if (acceptedEventType == null) return false;
        return getSeverityValueFromName(severity) - acceptedEventType.getSeverityValue() >= 0;
    }

}
