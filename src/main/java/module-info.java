module com.pyredevelopment.pongonline {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pyredevelopment.pongonline to javafx.fxml;
    exports com.pyredevelopment.pongonline;
}