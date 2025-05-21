package com.mycompany.votingsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminController {
    @FXML private TableView<Candidate> candidatesTable;
    @FXML private TableColumn<Candidate, String> idColumn;
    @FXML private TableColumn<Candidate, String> nameColumn;
    @FXML private TableColumn<Candidate, String> partyColumn;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField partyField;
    @FXML private Button addButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("candidateID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partyColumn.setCellValueFactory(new PropertyValueFactory<>("party"));
        refreshCandidates();
    }

    private void refreshCandidates() {
        candidatesTable.setItems(FXCollections.observableArrayList(
            VotingSystemManager.getInstance().getCandidates().values()
        ));
    }

    @FXML
    private void addCandidate(ActionEvent event) {
        String id = idField.getText();
        String name = nameField.getText();
        String party = partyField.getText();
        if (id.isEmpty() || name.isEmpty() || party.isEmpty()) return;
        try {
            boolean success = VotingSystemManager.getInstance().addCandidate(id, name, party);
            if (success) {
                refreshCandidates();
                idField.clear();
                nameField.clear();
                partyField.clear();
            }
        } catch (Exception e) {
            // Optionally show error
        }
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        App.setRoot("dashboard");
    }
} 