package org.example;

import org.example.entities.ClientEntity;
import org.example.entities.OrderStatusEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //insertData();
        //selectList();
        var entity = getById(1);
        System.out.println(entity.getFirstName()+ " " + entity.getLastName());

        //insertOrderStatus();

    }

    private static void insertData() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        ClientEntity entity = new ClientEntity();
        entity.setFirstName("Іван");
        entity.setLastName("Барабашка");
        entity.setPhone("+38 068 47 85 458");
        entity.setCar_model("Volkswagen Beetle A5");
        entity.setCar_year(2003);
        session.save(entity);

        transaction.commit();
        session.close();
    }

    private static void selectList() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        List<ClientEntity> results = session.createQuery("from ClientEntity", ClientEntity.class)
                .getResultList();

        //System.out.println("Count = "+ results.size());

        for (ClientEntity client : results) {
            System.out.println(client);
        }
        session.close();
    }

    private static ClientEntity getById(int id) {
        // Obtain a session from the SessionFactory
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        ClientEntity entity = session.get(ClientEntity.class, id);
        transaction.commit();
        session.close();
        return entity;
    }

    private static void insertOrderStatus() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String[] list = {
                "Нове замовлення",
                "В процесі виконання",
                "Виконано",
                "Скасовано клієнтом"
        };
        for (var item : list) {
            OrderStatusEntity entity = new OrderStatusEntity();
            entity.setName(item);
            session.save(entity);
        }
        transaction.commit();
        session.close();
    }


}

