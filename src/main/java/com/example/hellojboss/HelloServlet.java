package com.example.hellojboss;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {

    @Resource(mappedName = "java:/PostgresDS") // Replace with your datasource JNDI name
    private DataSource dataSource;

    private String message;

    public void init() {
        message = "Hello World!";
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        try (Session session = createHibernateSession()) {
            session.beginTransaction();

            // Query the database
            List<Person> persons = session.createQuery("SELECT * FROM person", Person.class).getResultList();

            // Hello
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            for (Person person : persons) {
                out.println("<p>Name: " + person.getName() + "</p>");
                // Add more fields here if needed
            }
            out.println("</body></html>");

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Session createHibernateSession() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.datasource", "java:/PostgresDS"); // Replace with your datasource JNDI name
        configuration.configure();

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory.openSession();
    }

    public void destroy() {
    }
}
