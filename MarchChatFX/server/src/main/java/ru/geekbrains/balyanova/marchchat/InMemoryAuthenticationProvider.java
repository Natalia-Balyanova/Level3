package ru.geekbrains.balyanova.marchchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InMemoryAuthenticationProvider implements AuthenticationProvider {
    private class User {
        private String login;
        private String password;
        private String nickname;

        public User (String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }
    private List<User> users;

    public InMemoryAuthenticationProvider() {
        this.users = new ArrayList<>(Arrays.asList(
                new User("Bob", "100", "MegaBob"),
                new User("Max", "100", "MaxMax"),
                new User("Jack", "100", "Wizard")
        ));
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for(User u : users) {
            if(u.login.equals(login) && u.password.equals(password)) {
                return u.nickname;
            }
        }
        return null;
    }

    @Override
    public void changeNickname(String oldNickname, String newNickname) {
        for(User u : users) {
            if(u.nickname.equals(oldNickname)) {
                u.nickname = newNickname;
                return;
            }
        }
    }

    public boolean isNickBusy(String nickname) {
        for(User u : users) {
            if(u.nickname.equals(nickname)) {
                return true;//ник занятт
            }
        }
        return false;//ник свободен
    }

    @Override
    public void init() {
    }

    @Override
    public void shutdown() {
    }

}
