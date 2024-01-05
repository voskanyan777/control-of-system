module com.example.test_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.oshi;

    opens com.example.test_javafx to javafx.fxml;
    exports com.example.test_javafx;
}