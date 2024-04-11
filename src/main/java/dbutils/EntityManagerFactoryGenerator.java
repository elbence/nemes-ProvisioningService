package dbutils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryGenerator {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getFactory() {
        if (emf == null) emf = Persistence.createEntityManagerFactory("default");
        return emf;
    }

}
