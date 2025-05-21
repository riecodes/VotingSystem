module com.mycompany.votingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.votingsystem to javafx.fxml;
    exports com.mycompany.votingsystem;
}
