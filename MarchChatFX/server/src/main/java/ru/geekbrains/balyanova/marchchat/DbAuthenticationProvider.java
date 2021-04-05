package ru.geekbrains.balyanova.marchchat;

import java.sql.*;

public class DbAuthenticationProvider implements AuthenticationProvider {

    private DbConnection dbConnection;//объявили соединение

    @Override
    public void init() {
        dbConnection = new DbConnection();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        String query = String.format("select nickname from chatclients where login = '%s' and password = '%s';", login, password);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void changeNickname(String oldNickname, String newNickname) {
        String query = String.format("update chatclients set nickname = '%s' where nickname = '%s';", oldNickname, newNickname);
        try {
            dbConnection.getStmt().executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isNickBusy(String nickname) {
        String query = String.format("select id from chatclients where nickname = '%s';", nickname);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void shutdown() {
        dbConnection.close();
    }
}
