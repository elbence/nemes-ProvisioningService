import entity.Catastrophe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        Catastrophe catastrophe = new Catastrophe();
        catastrophe.setName("C1");

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(catastrophe);
        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
    }
}
