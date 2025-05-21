package com.mycompany.votingsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class VotingController {
    @FXML private ComboBox<String> candidateComboBox;
    @FXML private Label statusLabel;
    @FXML private Button voteButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        candidateComboBox.setItems(FXCollections.observableArrayList(
            VotingSystemManager.getInstance().getCandidates().keySet()
        ));
        statusLabel.setText("");
    }

    @FXML
    private void handleVote(ActionEvent event) {
        String candidateID = candidateComboBox.getValue();
        if (candidateID == null) {
            statusLabel.setText("Please select a candidate.");
            return;
        }
        String username = AppState.getCurrentUser().getUsername();
        try {
            boolean success = VotingSystemManager.getInstance().vote(username, candidateID);
            if (success) {
                statusLabel.setText("Vote cast successfully!");
                voteButton.setDisable(true);
                candidateComboBox.setDisable(true);
            } else {
                statusLabel.setText("You have already voted or an error occurred.");
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        App.setRoot("dashboard");
    }
} 