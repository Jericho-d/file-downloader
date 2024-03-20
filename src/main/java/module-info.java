module com.file.downloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires org.apache.tika.core;
    requires static lombok;

    opens com.file.downloader to javafx.fxml;
    exports com.file.downloader;
}