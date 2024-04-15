import controllers.TimeController;

public class Main {
    public static void main(String[] args) {
        TimeController.startPeriodicProcess(1); // Update data every 5 hours
    }
}
