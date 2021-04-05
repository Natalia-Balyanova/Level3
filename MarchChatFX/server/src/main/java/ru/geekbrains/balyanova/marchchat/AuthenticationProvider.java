package ru.geekbrains.balyanova.marchchat;

public interface AuthenticationProvider {
    void init();
    String getNicknameByLoginAndPassword(String login, String password);
    void changeNickname(String oldNickname, String newNickname);
    boolean isNickBusy(String nickname);
    void shutdown();

}