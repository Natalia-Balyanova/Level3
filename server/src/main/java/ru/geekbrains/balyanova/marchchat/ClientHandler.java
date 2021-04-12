package ru.geekbrains.balyanova.marchchat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler { //обработчик клиентов
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String username;
    private final Date date = new Date();

    public String getUsername() {
        return username;
    }

    public ClientHandler(Server server, Socket socket) throws IOException { //принимаем сокет
        this.server = server; //отдали ссылку на сервер
        this.socket = socket; //запоминаем его
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(() ->{
           try {
                // Цикл авторизации
                while (true) {
                    String msg = null;
                    try {
                        msg = in.readUTF();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    if (msg.startsWith("/login ")) {
                        String[] tokens = msg.split(("\\s+"));
                        if(tokens.length != 3) {
                            sendMessage("/login_failed Введите имя пользователя и пароль");
                            continue;
                        }
                        String login = tokens[1];
                        String password = tokens[2];
                        String userNickname = server.getAuthenticationProvider().getNicknameByLoginAndPassword(login, password);
                        if(userNickname == null) {
                            sendMessage("/login_failed Введен некорректный логин/пароль");
                            LOGGER.info("Клиент ввел некорректный логин/пароль");
                            continue;
                        }
                        if (server.isUserOnline(userNickname)) {
                            sendMessage("/login_failed Учетная запись уже используется");
                            continue;
                        }
                        username = userNickname;
                        sendMessage("/login_ok " + login + " " + username);
                        server.subscribe(this);
                        break;
                    }
                }
                // Цикл общения с клиентом
                while (true) {
                    String msg = in.readUTF();
                    if (msg.startsWith("/")) {
                        executeCommands(msg);
                        continue;
                    }
                    if (!msg.isEmpty() & !msg.startsWith(" ")) {
                        server.broadcastMessage("[ " + date + " ]\n" + username.toUpperCase(Locale.ROOT) + ": " + msg);
                        LOGGER.info("Клиент " + username + " отправил сообщение");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
               disconnect();
               LOGGER.warn("Связь клиента с сервером потеряна");
               executorService.shutdown();
            }
        });
    }

    private void executeCommands(String cmd) throws IOException {
        if (cmd.equals("/exit")) {
            server.broadcastMessage(username + " покинул чат");
            LOGGER.info(username + " покинул чат");
            disconnect();
        }
        if (cmd.startsWith("/w ")) {
            String[] tokens = cmd.split("\\s+", 3);
            server.sendPrivateMessage(this, tokens[1], tokens[2]);
            return;
        }
        if (cmd.startsWith("/change_nick ")) { //меняем ник
            String[] tokens = cmd.split("\\s+", 2);
            if (tokens.length != 2) {
                sendMessage("Server: Введена некорректная команда");
                return;
            }
            String newNickname = tokens[1];
            if (server.isUserOnline(newNickname)) {
                sendMessage("Server: Такой никнейм уже занят");
                return;
            }
            server.getAuthenticationProvider().changeNickname(username, newNickname);
            username = newNickname;
            server.broadcastMessage("Клиент " + username + " поменял имя на " + newNickname);
            LOGGER.info("Клиент " + username + " поменял имя на " + newNickname);
            server.broadcastClientsList();
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            disconnect();
        }
    }

    public void disconnect() {
        server.unsubscribe(this);//отписались от рассылки при закрытии
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}