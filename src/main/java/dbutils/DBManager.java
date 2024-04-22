package dbutils;

import entity.Catastrophe;
import entity.Event;
import entity.Zone;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DBManager {

    public static void insertCatastrophesIntoDatabase(List<Catastrophe> catastrophes) {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryGenerator.getFactory();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        for (Catastrophe catastrophe : catastrophes) {
            System.out.println(" * Saving: " + catastrophe.toString());
            // Get catastrophe if in db
            catastrophe = getCatastropheFromDb(catastrophe, entityManager);

            // Get Event if in db - maybe do only if catastrophe was not in db ? TODO Consider
            Event catastropheEvent = getEventFromDb(catastrophe, entityManager);
            catastrophe.setEvent(catastropheEvent);

            // Get Zone if in db
            Zone catastropheZone = getZoneFromDb(catastrophe, entityManager);
            catastrophe.setZone(catastropheZone);

            // Save catastrophe
            entityManager.getTransaction().begin();
            entityManager.persist(catastrophe);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
    }

    private static Zone getZoneFromDb(Catastrophe catastrophe, EntityManager entityManager) {
        Zone catastropheZone = catastrophe.getZone();
        try {
            Zone dbZone;
            dbZone =  entityManager.createNamedQuery("findByCenter", Zone.class)
                    .setParameter("lat", catastropheZone.getCenterLat())
                    .setParameter("lon", catastropheZone.getCenterLon())
                    .getSingleResult();
            System.out.println("   - Updating Zone to found in db: " + dbZone.toString());
            catastropheZone = dbZone;
        } catch (Exception ignored) {
            System.out.println("     > Zone not found or not included yet");
        }
        return catastropheZone;
    }

    private static Event getEventFromDb(Catastrophe catastrophe, EntityManager entityManager) {
        Event catastropheEvent = catastrophe.getEvent();
        try {
            Event dbEvent;
            dbEvent =  entityManager.createNamedQuery("findById", Event.class)
                    .setParameter("name", catastropheEvent.getEventName())
                    .setParameter("severity", catastropheEvent.getSeverity())
                    .getSingleResult();
            System.out.println("   - Updating event to found in db: " + dbEvent.toString());
            catastropheEvent = dbEvent;
        } catch (Exception ignored) {
            System.out.println("     > Event not found or not included yet");
        }
        return catastropheEvent;
    }

    private static Catastrophe getCatastropheFromDb(Catastrophe catastrophe, EntityManager entityManager) {
        Catastrophe dbCatastrophe;
        try {
            dbCatastrophe = entityManager.createNamedQuery("findActiveById", Catastrophe.class)
                    .setParameter("name", catastrophe.getName())
                    .setParameter("startdate", catastrophe.getStartDate())
                    .setParameter("eventname", catastrophe.getEvent().getEventName())
                    .setParameter("severity", catastrophe.getEvent().getSeverity())
                    .getSingleResult(); // TODO Should get all similars...
            System.out.println("   ** Found similar catastrophe in db " + dbCatastrophe.toString());
            catastrophe = dbCatastrophe; // TODO should update catastrophe if found in db and actually similar, compare zones iou values or something as such
        } catch (Exception ignored) {
            System.out.println("    > No matching catastrophe found, inserting new");
        }
        return catastrophe;
    }

}
