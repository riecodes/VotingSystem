package com.mycompany.votingsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList("ADMIN", "VOTER"));
        roleComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        errorLabel.setText("");
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }
        User user = VotingSystemManager.getInstance().authenticateUser(username, password);
        if (user == null || !user.getRole().equals(role)) {
            errorLabel.setText("Invalid credentials or role.");
            return;
        }
        AppState.setCurrentUser(user);
        try {
            App.setRoot("dashboard");
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Failed to load dashboard.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        errorLabel.setText("");
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }
        if (!role.equals("VOTER")) {
            errorLabel.setText("Only voter registration is allowed here.");
            return;
        }
        try {
            // Use username as voterID for simplicity
            boolean success = VotingSystemManager.getInstance().registerVoter(username, password, username);
            if (success) {
                errorLabel.setText("Registration successful! You can now log in.");
            } else {
                errorLabel.setText("Username already exists.");
            }
        } catch (Exception e) {
            errorLabel.setText("Registration failed: " + e.getMessage());
        }
    }
} 