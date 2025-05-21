package com.mycompany.votingsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AuditLogController {
    @FXML private TableView<AuditLog> auditTable;
    @FXML private TableColumn<AuditLog, String> actionColumn;
    @FXML private TableColumn<AuditLog, String> userColumn;
    @FXML private TableColumn<AuditLog, String> timestampColumn;
    @FXML private TableColumn<AuditLog, String> detailsColumn;
    @FXML private Button refreshButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        refreshLog(null);
    }

    @FXML
    private void refreshLog(ActionEvent event) {
        auditTable.setItems(FXCollections.observableArrayList(
            VotingSystemManager.getInstance().getAuditLogs()
        ));
    }

    @FXML
    private void goBack(ActionEvent event) throws Exception {
        App.setRoot("dashboard");
    }
} 