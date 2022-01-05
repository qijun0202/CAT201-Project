module com.project.cat201project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.project.cat201project to javafx.fxml;
    exports com.project.cat201project;
}