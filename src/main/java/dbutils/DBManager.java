package dbutils;

import entity.Catastrophe;
import entity.Event;
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


            // Get event if in db - TODO do only if catastrophe was not in db
            Event catastropheEvent = catastrophe.getEvent();
            Event dbEvent;
            try {
                dbEvent =  entityManager.createNamedQuery("findById", Event.class)
                        .setParameter("name", catastropheEvent.getEventName())
                        .setParameter("severity", catastropheEvent.getSeverity())
                        .getSingleResult();
                System.out.println("   - Updating event to found in db: " + dbEvent.toString());
                catastrophe.setEvent(dbEvent);
            } catch (Exception ignored) {
                System.out.println("     > Event not found or not included yet");
                catastrophe.setEvent(catastropheEvent);
            }
            // Save catastrophe
            entityManager.getTransaction().begin();
            entityManager.persist(catastrophe);
            entityManager.getTransaction().commit();
        }
        entityManager.close();
    }

}
