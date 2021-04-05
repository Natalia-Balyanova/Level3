package ru.geekbrains.balyanova.marchchat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnection {
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
            throw new RuntimeException("Невозможно подключиться к БД");
        }
    }

    public void close() {
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
