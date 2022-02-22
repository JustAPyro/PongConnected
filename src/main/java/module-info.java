module com.pyredevelopment.pongonline {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.scripting;

    opens com.pyredevelopment.pongonline to javafx.fxml;
    exports com.pyredevelopment.pongonline;
}