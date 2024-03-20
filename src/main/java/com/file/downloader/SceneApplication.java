package com.file.downloader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.stream.IntStream;

public class SceneApplication extends Application {

    private static final String PROGRESS_BAR_LABEL_TEMPLATE = "Loading: %s%s";

    private final DownloadController downloadController = new DownloadController();

    private final Button executeButton = new Button("Execute");
    private final ProgressBar progressBar = new ProgressBar();
    private final Label progressLabel = new Label(PROGRESS_BAR_LABEL_TEMPLATE.formatted(0, "%"));

    private BigDecimal progress = BigDecimal.ZERO;

    @Override
    public void start(Stage stage) {

        final var gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        final var urlLabel = new Label("URL:");
        final var urlTextField = new TextField();
        gridPane.add(urlLabel, 0, 0);
        gridPane.add(urlTextField, 1, 0);

        final var idFromLabel = new Label("ID from:");
        final var idFromTextField = new TextField();
        gridPane.add(idFromLabel, 0, 1);
        gridPane.add(idFromTextField, 1, 1);

        final var idToLabel = new Label("ID to:");
        final var idToTextField = new TextField();

        gridPane.add(idToLabel, 0, 2);
        gridPane.add(idToTextField, 1, 2);
        gridPane.add(executeButton, 2, 3);
        gridPane.add(progressLabel, 1, 4);
        gridPane.add(progressBar, 1, 5);

        executeButton.setOnAction(event -> {

            final var url = urlTextField.getText();
            final var idFrom = Integer.parseInt(idFromTextField.getText());
            final var idTo = Integer.parseInt(idToTextField.getText());

            IntStream.range(idFrom, idTo)
                    .forEach(id -> {

                        if (progress.doubleValue() < 1) {
                            final var val = (id - idFrom) / (idTo - idFrom);

                            progress = progress.add(BigDecimal.valueOf(val));
                            progressBar.setProgress(progress.doubleValue());
                            progressLabel.setText(PROGRESS_BAR_LABEL_TEMPLATE.formatted(Math.round(progress.doubleValue() * 100), "%"));
                        }

                        downloadController.onButtonClick(url, id);
                    });
        });

        final var scene = new Scene(gridPane);

        stage.setScene(scene);
        stage.setTitle("JavaFX Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}