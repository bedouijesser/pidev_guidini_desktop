package com.guidini.guidini_desktop.application;

import com.guidini.guidini_desktop.MainApplication;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class NavbarController implements Initializable {

    @FXML
    private Button commentsBtn;

    @FXML
    private Button contactBtn;

    @FXML
    private Button dashBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Button tablesBtn;

    @FXML
    private Button usersBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // add event listeners to buttons
        Map<Button, String> buttonsPathMap = new HashMap<>();
        buttonsPathMap.put(commentsBtn, "comments");
        buttonsPathMap.put(contactBtn, "contact");
        buttonsPathMap.put(dashBtn, "dashboard");
        buttonsPathMap.put(profileBtn, "profile");
        buttonsPathMap.put(tablesBtn, "tables");
        buttonsPathMap.put(usersBtn, "users");

        for (Map.Entry<Button, String> entry : buttonsPathMap.entrySet()) {
            Button button = entry.getKey();
            String path = entry.getValue();
            button.setOnAction(event -> {
                try {
                    redirect(path, event);
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            });
        }
    }

    private void redirect(String route, Event event) throws IOException {

        // Switching Scenes
        System.out.println("Redirecting to " + route);
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("application/" + route + ".fxml")));
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // patch window size
        window.setWidth(1000);
        window.setHeight(750);
        window.setScene(scene);
        window.show();
    }

    public void logout(Event event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "êtes vous sur de vouloir vous déconnecter?",
                new ButtonType("Oui", ButtonBar.ButtonData.YES),
                new ButtonType("Non", ButtonBar.ButtonData.NO));
        alert.setTitle("Logout");
        alert.showAndWait();
        ButtonType result = alert.getResult();
        System.out.println(result.getButtonData());
        if (result.getButtonData().isDefaultButton()) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("login.fxml")));
            Scene scene = new Scene(root);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // patch window size
            window.setWidth(1000);
            window.setHeight(750);
            window.setScene(scene);
            window.show();
        }
    }

}
