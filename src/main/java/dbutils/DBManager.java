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
            // Get event if in db
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
