package com.guidini.guidini_desktop.authentication;

import com.guidini.guidini_desktop.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class AuthenticationController implements Initializable {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    @FXML
    private Text emailFieldErrorText;

    @FXML
    private Text passwordFieldErrorText;
    private boolean isEmailFieldDirty = false;
    private boolean isPasswordFieldDirty = false;

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private boolean isEmailControlValid() {
        return !emailField.getText().isEmpty() && emailField.getText().matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    }

    private boolean isPasswordControlValid() {
        return !passwordField.getText().isEmpty() && passwordField.getText().length() >= 8;
    }

    private boolean isFormValid() {
        return isEmailControlValid() && isPasswordControlValid();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            autoLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initForm();

        listenToKeyupEvent();
        listenToFocusOutEvent();
    }

    @FXML
    public void login(ActionEvent event) {
        if (!isFormValid()) {
            validatePassword();
            validateEmail();
            return;
        }

        String email = emailField.getText();
        String password = passwordField.getText();
        try {
            AuthenticationService authenticationService = new AuthenticationService();
            boolean flag = authenticationService.validate(email, password);
            if (flag) {
                Preferences preferences = Preferences.userRoot();
                preferences.put("email", email);
                preferences.put("password", password);
                preferences.putBoolean("isLoggedIn", true);


                redirectToHomePage(event);
            } else {
                infoBox("Veuillez verifier vos coordonnées", null, "Failed");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private void redirectToHomePage(ActionEvent event) throws IOException {
        // Switching Scenes
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("application/homepage.fxml")));
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // patch window size
        window.setWidth(1000);
        window.setHeight(750);
        window.setScene(scene);
        window.show();
    }


    private void autoLogin() throws IOException {
        Preferences userPreferences = Preferences.userRoot();
        String email = userPreferences.get("email", "");
        String password = userPreferences.get("password", "");
        if (!email.isEmpty() && !password.isEmpty()) {
            emailField.setText(email);
            passwordField.setText(password);
        }

    }

    private void initForm() {
        setFiledValidity(passwordField, passwordFieldErrorText, true);
        setFiledValidity(emailField, emailFieldErrorText, true);
    }

    private void listenToFocusOutEvent() {
        // add form event listeners
        emailField.focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (!isFocused) {
                if (!emailField.getText().isEmpty()) isEmailFieldDirty = true;
                validateEmail();
            }
        });
        passwordField.focusedProperty().addListener((observable, oldValue, isFocused) -> {
            if (!isFocused) {
                if (!passwordField.getText().isEmpty()) isPasswordFieldDirty = true;
                validatePassword();
            }
        });
    }

    private void validateEmail() {
        if (!emailField.getText().isEmpty()) setFiledValidity(emailField, emailFieldErrorText, isEmailControlValid());
    }

    private void validatePassword() {
        if (!passwordField.getText().isEmpty())
            setFiledValidity(passwordField, passwordFieldErrorText, isPasswordControlValid());
    }

    private void setFiledValidity(TextField formField, Text formFieldErrorText, boolean isValid) {
        formField.setStyle(isValid ? "-fx-border-color: none" : "-fx-border-color: red");
        formFieldErrorText.setVisible(!isValid);
    }

    private void listenToKeyupEvent() {
        // add form validation
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isEmailFieldDirty) validateEmail();
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isPasswordFieldDirty) validatePassword();
        });
    }
}
