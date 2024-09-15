package org.example;

import org.example.entities.*;
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

        //insertServices();
        //insertOrderServices();
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

    private static void insertServices() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Object[][] services = {
                {"Діагностика автомобіля", 500.00},
                {"Заміна масла", 1200.00},
                {"Заміна гальмівних колодок", 850.00},
                {"Ремонт двигуна", 5000.00}
        };

        for (Object[] serviceData : services) {
            ServiceEntity service = new ServiceEntity();
            service.setName((String) serviceData[0]);
            service.setPrice((Double) serviceData[1]);
            session.save(service);
        }

        transaction.commit();
        session.close();
    }

    private static void insertOrderServices() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Отримуємо замовлення та послугу з бази даних
        OrderEntity order = session.get(OrderEntity.class, 1L); // Приклад: отримати замовлення з ID = 1
        ServiceEntity service = session.get(ServiceEntity.class, 1); // Приклад: отримати послугу з ID = 1

        // Додаємо зв'язок між замовленням і послугою
        OrderServiceEntity orderService = new OrderServiceEntity();
        orderService.setOrder(order);
        orderService.setService(service);

        session.save(orderService);

        transaction.commit();
        session.close();
    }

}

