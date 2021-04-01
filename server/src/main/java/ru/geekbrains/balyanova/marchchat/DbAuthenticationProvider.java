package ru.geekbrains.balyanova.marchchat;

import java.sql.*;
/*
CREATE TABLE chatclients (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    login    STRING,
    password STRING,
    nickname STRING UNIQUE
);
Добавить в сетевой чат аутентификацию через базу данных SQLite.
* Добавить в сетевой чат возможность смены ника.

 */
public class DbAuthenticationProvider implements AuthenticationProvider {
    private static Connection connection;//объявили соединение
    private static Statement stmt;//для отправик запросов в БД
    private static PreparedStatement psInsert;//подготовленные запросы

    public static void connect() {//подключаемся к БД
        try {
            Class.forName("org.sqlite.JDBC");//загрузили драйвер в память из pom.xml//срабатывает блок инициализации
            connection = DriverManager.getConnection("jdbc:sqlite:chatlogin.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Невозможно подключиться к БД");
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
            try (ResultSet rs = stmt.executeQuery("select nickname from chatclients where login = '" + login + "' and password = '" + password + "';")) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return null;
    }

    @Override
    public void changeNickname(String oldNickname, String newNickname) {
        try {//замена ника работает только в чате, в БД ник остается прежним. при повторном заходе в чат, ник первоначальный
            stmt.executeUpdate("update chatclients set nickname = 'newNickname', where nickname = oldNickname");
            //stmt.executeUpdate("insert into chatclients set nickname = 'newNickname'");
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new UnsupportedOperationException();
        }
    }

    public static void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (psInsert != null) {
                psInsert.close();
            }
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