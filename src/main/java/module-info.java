module project2.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires javafx.graphics;

    opens project2.project2 to javafx.fxml;

    exports project2.project2;
}