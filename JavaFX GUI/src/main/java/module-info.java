module com.example.extracredit {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.extracredit to javafx.fxml;
    exports com.example.extracredit;
}