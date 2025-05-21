package com.mycompany.votingsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ResultsController {
    @FXML private TableView<Candidate> resultsTable;
    @FXML private TableColumn<Candidate, String> nameColumn;
    @FXML private TableColumn<Candidate, String> partyColumn;
    @FXML private TableColumn<Candidate, Integer> votesColumn;
    @FXML private Button refreshButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partyColumn.setCellValueFactory(new PropertyValueFactory<>("party"));
        votesColumn.setCellValueFactory(new PropertyValueFactory<>("voteCount"));
        refreshResults(null);
    }

    @FXML
    private void refreshResults(ActionEvent event) {
        resultsTable.setItems(FXCollections.observableArrayList(
            VotingSystemManager.getInstance().getCandidates().values()
        ));
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        App.setRoot("dashboard");
    }
} 