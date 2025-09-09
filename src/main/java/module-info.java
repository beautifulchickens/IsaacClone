module com.example.isaacclone {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.isaacclone to javafx.fxml;
    exports com.example.isaacclone;
}