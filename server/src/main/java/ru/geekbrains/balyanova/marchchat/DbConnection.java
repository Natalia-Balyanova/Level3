package ru.geekbrains.balyanova.marchchat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
    private static final Logger LOGGER = LogManager.getLogger(DbConnection.class);
    private Connection connection;//объявили соединение
    private Statement stmt;//для отправик запросов в БД

    public Statement getStmt() {
        return stmt;
    }

    public DbConnection() {
        try {
            Class.forName("org.sqlite.JDBC");//загрузили драйвер в память из pom.xml//срабатывает блок инициализации
            this.connection = DriverManager.getConnection("jdbc:sqlite:chatlogin.db");
            this.stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LOGGER.error("Невозможно подключиться к БД");
            throw new RuntimeException("Невозможно подключиться к БД");
        }
    }

    public void close() {
        LOGGER.error("Соединение с БД приостановлено");
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
