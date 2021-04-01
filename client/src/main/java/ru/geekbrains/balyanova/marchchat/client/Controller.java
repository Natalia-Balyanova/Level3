package ru.geekbrains.balyanova.marchchat.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //File msgHistoryFile = new File("history.txt");
    @FXML
    TextField msgField, loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    Button logout;

    @FXML
    TextArea chatArea;

    @FXML
    HBox loginPanel, msgPanel;

    @FXML
    ListView<String> clientsList;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;

    public void setUsername(String username) {
        this.username = username;
        boolean usernameIsNull = username == null;

        loginPanel.setVisible(usernameIsNull);
        loginPanel.setManaged(usernameIsNull);
        logout.setManaged(!usernameIsNull);
        logout.setVisible(!usernameIsNull);
        msgPanel.setVisible(!usernameIsNull);
        msgPanel.setManaged(!usernameIsNull);
        clientsList.setVisible(!usernameIsNull);
        clientsList.setManaged(!usernameIsNull);
        chatArea.clear();//очистили историю чата, чтобы при выходе ее не видеть
        loginField.clear();//очистили поле логин
        passwordField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUsername(null);
    }

    public void login() {
        if (loginField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showErrorAlert("Имя пользователя или пароль не могут быть пустыми");
            return;
        }
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF("/login " + loginField.getText() + " " + passwordField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread t = new Thread(() -> {
                try {
                    //Цикл авторизации
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/login_ok ")) {
                            setUsername(msg.split("\\s")[1]);
                            showMessageHistory("history.txt");//показываем историю сообщений
                            break;
                        }
                        if (msg.startsWith("/login_failed ")) {
                            String cause = msg.split("\\s", 2)[1];
                            chatArea.appendText(cause + "\n");
                        }
                    }
                    //Цикл общения
                    while (true) {
                        //showMessageHistory();
                        String msg = in.readUTF();
                        if (msg.startsWith("/")) {
                            if (msg.startsWith("/clients_list ")) {
                                String[] tokens = msg.split("\\s");
                                Platform.runLater(() -> {
                                    clientsList.getItems().clear();
                                    for (int i = 1; i < tokens.length; i++) {
                                        clientsList.getItems().add(tokens[i]);
                                    }
                                });
                            }
                            continue;
                        }
                        chatArea.appendText(msg + "\n");
                        createAndSaveHistory();//записываем историю
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    disconnect();
                }
            });
            t.start();
        } catch (IOException e) {
            showErrorAlert("Невозможно подключиться к серверу");
        }
    }

    public void reply() {
        try {
            out.writeUTF(msgField.getText());
            msgField.clear();
            msgField.requestFocus();//переключение фокуса на текстовое поле
        } catch (IOException e) {
            showErrorAlert("Невозможно отправить сообщение");

        }
    }

    private void createAndSaveHistory() {//создание файла и запись сообщений
        try {
            File msgHistoryFile = new File("history.txt");
            if (!msgHistoryFile.exists()) {//если не существует создаем
                msgHistoryFile.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(msgHistoryFile, true));
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(chatArea.getText());//добавляем текст из чата
            bw.close();// нужно ли закрывать...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //недоделала, история загружается, но при последующих коннект/дисконнект история дублируется
    //попробовала несколькими способами
    private void showMessageHistory(String file) {
        try {
            FileReader reader = new FileReader("history.txt");
            char[] historyData = new char[10240];
            int len = reader.read(historyData);
            while (len != -1) {
                len = reader.read(historyData);
            }
            chatArea.appendText(String.valueOf(historyData));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//        try (BufferedReader br = new BufferedReader(new FileReader("history.txt"))) {
//            StringBuilder msgHistory = new StringBuilder();
//            String temp = br.readLine();
//
//            while (temp != null) {
//                msgHistory.append(temp + "\n");
//                temp = br.readLine();
//            }
//            String historyForClient = msgHistory.toString();
//            chatArea.appendText(historyForClient + "\n");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void disconnect() {
        setUsername(null);
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {//кнопка перерегистрации
        disconnect();
    }

    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("March Chat FX");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();

    }
}
