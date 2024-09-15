package org.example;

import com.github.javafaker.Faker;
import org.example.entities.*;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //insertData();
        //selectList();
        //var entity = getById(1);
        //System.out.println(entity.getFirstName()+ " " + entity.getLastName());

        //insertServices();
        //insertOrderServices();
        //insertOrderStatus();

        //insert all random
        //insertRandomAll();
        //show orders all
        displayAllOrders();

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

    private static void insertRandomClient(Faker faker) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        ClientEntity client = new ClientEntity();
        client.setFirstName(faker.name().firstName());
        client.setLastName(faker.name().lastName());
        client.setPhone(faker.phoneNumber().phoneNumber());
        client.setCar_model(faker.company().name() + " " + faker.letterify("????")); // Наприклад, Volkswagen Beetle
        client.setCar_year(faker.number().numberBetween(1990, 2022)); // Рік від 1990 до 2022

        session.save(client);
        transaction.commit();
        session.close();
    }

    private static void insertRandomService(Faker faker) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        ServiceEntity service = new ServiceEntity();
        service.setName(faker.commerce().productName()); // Випадкова назва послуги
        service.setPrice(faker.number().randomDouble(2, 100, 5000)); // Випадкова ціна від 100 до 5000

        session.save(service);
        transaction.commit();
        session.close();
    }

    private static void insertRandomOrder(Faker faker) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        // Отримуємо випадкових клієнта та статус
        Random random = new Random();
        ClientEntity client = session.get(ClientEntity.class, random.nextInt(10) + 1); // Випадковий клієнт
        OrderStatusEntity status = session.get(OrderStatusEntity.class, random.nextInt(4) + 1); // Випадковий статус

        OrderEntity order = new OrderEntity();
        order.setClient(client);
        order.setStatus(status);
        order.setOrderDate(faker.date().past(30, java.util.concurrent.TimeUnit.DAYS)); // Випадкова дата за останні 30 днів

        session.save(order);
        transaction.commit();

        // Додаємо випадкові послуги до замовлення
        transaction = session.beginTransaction();
        for (int i = 0; i < random.nextInt(5) + 1; i++) { // Від 1 до 5 послуг на замовлення
            ServiceEntity service = session.get(ServiceEntity.class, random.nextInt(10) + 1);
            OrderServiceEntity orderService = new OrderServiceEntity();
            orderService.setOrder(order);
            orderService.setService(service);
            session.save(orderService);
        }

        transaction.commit();
        session.close();
    }

    private static void insertRandomAll(){
        Faker faker = new Faker();

        for (int i = 0; i < 10; i++) {
            insertRandomClient(faker);
        }
        for (int i = 0; i < 10; i++) {
            insertRandomService(faker);
        }
        for (int i = 0; i < 10; i++) {
            insertRandomOrder(faker);
        }
        System.out.println("Data update complete.");
    }

    private static void displayAllOrders() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<OrderEntity> orders = session.createQuery("from OrderEntity", OrderEntity.class).getResultList();

        for (OrderEntity order : orders) {
            System.out.println("Замовлення ID: " + order.getId());
            System.out.println("Дата замовлення: " + order.getOrderDate());
            System.out.println("Клієнт: " + order.getClient().getFirstName() + " " + order.getClient().getLastName());
            System.out.println("Статус: " + order.getStatus().getName());

            if (!order.getOrderServices().isEmpty()) {
                System.out.println("Послуги:");
                for (OrderServiceEntity orderService : order.getOrderServices()) {
                    System.out.println("- " + orderService.getService().getName() + " (Ціна: " + orderService.getService().getPrice() + ")");
                }
            } else {
                System.out.println("Послуги не додані.");
            }

            System.out.println("--------------------------------------------------");
        }

        transaction.commit();
        session.close();
    }
}

