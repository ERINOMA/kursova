package com.kursova;

import com.kursova.model.Enrollment;
import com.kursova.network.HttpCourseClient;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private final HttpCourseClient client = new HttpCourseClient();

    private final TableView<Enrollment> table = new TableView<>();

    private final TextField f1Field = new TextField();
    private final TextField f2Field = new TextField();
    private final TextField f3Field = new TextField();

    private final ComboBox<String> statusCombo = new ComboBox<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("ІС Керування навчальними курсами та зарахуванням");

        // 1. Налаштування колонок таблиці TableView
        TableColumn<Enrollment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Enrollment, Integer> courseCol = new TableColumn<>("ID Курсу");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Enrollment, Integer> studentCol = new TableColumn<>("ID Студента");
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Enrollment, String> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Enrollment, Integer> f1Col = new TableColumn<>("F1 (Завдання)");
        f1Col.setCellValueFactory(new PropertyValueFactory<>("factorF1"));

        TableColumn<Enrollment, Integer> f2Col = new TableColumn<>("F2 (Тести)");
        f2Col.setCellValueFactory(new PropertyValueFactory<>("factorF2"));

        TableColumn<Enrollment, Integer> f3Col = new TableColumn<>("F3 (Активність)");
        f3Col.setCellValueFactory(new PropertyValueFactory<>("factorF3"));

        TableColumn<Enrollment, Double> scoreCol = new TableColumn<>("Інтегральний бал");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("integralScore"));

        table.getColumns().addAll(idCol, courseCol, studentCol, statusCol, f1Col, f2Col, f3Col, scoreCol);
        table.setPrefHeight(260);

        f1Field.setPrefWidth(60);
        f2Field.setPrefWidth(60);
        f3Field.setPrefWidth(60);

        statusCombo.getItems().addAll("REQUESTED", "APPROVED", "ACTIVE", "COMPLETED");

        Button btnRefresh = new Button("Оновити дані");
        Button btnSaveFactors = new Button("Зберегти оцінки");
        Button btnChangeStatus = new Button("Змінити статус");

        btnRefresh.setOnAction(e -> refreshData());

        btnSaveFactors.setOnAction(e -> {
            Enrollment selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Увага", "Спочатку виберіть рядок у таблиці!");
                return;
            }
            try {
                int f1 = Integer.parseInt(f1Field.getText());
                int f2 = Integer.parseInt(f2Field.getText());
                int f3 = Integer.parseInt(f3Field.getText());

                client.updateFactors(selected.getId(), f1, f2, f3);
                refreshData();
            } catch (NumberFormatException ex) {
                showError("Помилка вводу", "Оцінки F1, F2, F3 повинні бути цілими числами!");
            } catch (Exception ex) {
                showError("Помилка сервера", ex.getMessage());
            }
        });

        btnChangeStatus.setOnAction(e -> {
            Enrollment selected = table.getSelectionModel().getSelectedItem();
            String newStatus = statusCombo.getValue();
            if (selected == null || newStatus == null) {
                showError("Увага", "Виберіть запис у таблиці та новий статус зі списку!");
                return;
            }
            try {

                client.changeStatus(selected.getId(), newStatus);
                refreshData();
            } catch (Exception ex) {
                showError("Порушення циклу!", ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                f1Field.setText(String.valueOf(newSelection.getFactorF1()));
                f2Field.setText(String.valueOf(newSelection.getFactorF2()));
                f3Field.setText(String.valueOf(newSelection.getFactorF3()));
                statusCombo.setValue(newSelection.getStatus());
            }
        });

        HBox factorsPanel = new HBox(10,
                new Label("F1 (Завдання):"), f1Field,
                new Label("F2 (Тести):"), f2Field,
                new Label("F3 (Активність):"), f3Field,
                btnSaveFactors);
        factorsPanel.setPadding(new Insets(5));

        HBox statusPanel = new HBox(10,
                new Label("Новий стан:"), statusCombo,
                btnChangeStatus,
                btnRefresh);
        statusPanel.setPadding(new Insets(5));

        VBox root = new VBox(10, table, factorsPanel, statusPanel);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 850, 420);
        stage.setScene(scene);

        refreshData();
        stage.show();
    }

    private void refreshData() {
        try {
            table.setItems(FXCollections.observableArrayList(client.getEnrollments()));
        } catch (Exception e) {
            showError("Помилка зв'язку із сервером",
                    "Не вдалося отримати дані. Переконайтеся, що Apache Tomcat запущено!\nДеталі: " + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
