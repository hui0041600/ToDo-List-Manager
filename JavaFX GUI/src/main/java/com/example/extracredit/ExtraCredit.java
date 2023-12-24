package com.example.extracredit;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;

public class ExtraCredit extends Application {
    private static final String FILE_NAME = "data.txt";
    private static final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private static TableView<Task> tableView;
    private static TextField taskInput;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        createDataFileIfNotExists();
        loadDataFromFile();
        tableView = createTableView();

        primaryStage.setTitle("ToDo JavaFX");
        primaryStage.setOnCloseRequest(event -> saveDataToFile());

        BorderPane borderPane = createBorderPane();
        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createBorderPane() {
        BorderPane borderPane = new BorderPane();

        VBox vBox = createVBox();
        borderPane.setCenter(vBox);

        HBox hBox = createHBox();
        borderPane.setBottom(hBox);
        BorderPane.setMargin(hBox, new Insets(10, 10, 10, 10));

        return borderPane;
    }

    private VBox createVBox() {
        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(10));

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        });

        vBox.getChildren().add(tableView);
        return vBox;
    }

    private HBox createHBox() {
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10));

        taskInput = new TextField();
        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Button completeButton = new Button("Mark Complete");
        Button incompleteButton = new Button("Mark Incomplete");

        addButton.setOnAction(e -> addTask(taskInput.getText()));
        deleteButton.setOnAction(e -> deleteTask());
        completeButton.setOnAction(e -> markTask(true));
        incompleteButton.setOnAction(e -> markTask(false));
        hBox.getChildren().addAll(taskInput, addButton, deleteButton, completeButton, incompleteButton);

        return hBox;
    }

    private static void createDataFileIfNotExists() {
        File file = new File(FILE_NAME);

        try {
            if (file.createNewFile()) {
                System.out.println("data.txt created successfully.");
            } else {
                System.out.println("data.txt already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    Task task = new Task(parts[0]);
                    task.setComplete(parts[2].equals("Complete"));
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + " | " + task.getStatus() + " | " + (task.isComplete() ? "Complete" : "Incomplete"));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void addTask(String description) {
        Task task = new Task(description);
        tasks.add(task);
        updateTableView();
        saveDataToFile();
        taskInput.clear();
    }

    private static void deleteTask() {
        Task selectedTask = getSelectedTask();
        if (selectedTask != null) {
            tasks.remove(selectedTask);
            updateTableView();
            saveDataToFile();
        }
    }

    private static void markTask(boolean complete) {
        Task selectedTask = getSelectedTask();
        if (selectedTask != null) {
            selectedTask.setComplete(complete);
            updateTableView();
            saveDataToFile();
        }
    }

    private static void updateTableView() {
        tableView.refresh();
    }

    private static Task getSelectedTask() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    private static TableView<Task> createTableView() {
        TableView<Task> table = new TableView<>();
        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<Task, String> statusColumn = new TableColumn<>("Status");

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(descriptionColumn, statusColumn);
        table.setItems(tasks);

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        return table;
    }

    public static class Task implements Serializable {
        private static final long serialVersionUID = 1L;
        private String description;
        private boolean complete;
        private String status;

        public Task(String description) {
            this.description = description;
            this.complete = false;
            this.status = "Not Started";
        }

        public boolean isComplete() {
            return complete;
        }

        public void setComplete(boolean complete) {
            this.complete = complete;
            updateStatus();
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

        private void updateStatus() {
            this.status = complete ? "Complete" : "Incomplete";
        }

        @Override
        public String toString() {
            return description + " (" + status + ")";
        }
    }
}



