module project2.project2 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires javafx.graphics;

    opens project2.project2 to javafx.fxml;
    opens project2.project2.controllers to javafx.fxml;

    exports project2.project2;
    exports project2.project2.controllers to javafx.fxml;
}