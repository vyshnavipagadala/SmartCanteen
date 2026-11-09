package com.canteen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageOrders {

    public void show(Stage stage) {

        Label heading = new Label("SMART CANTEEN - MANAGE ORDERS");
        heading.setStyle(
            "-fx-font-size: 25px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #17365D;"
        );

        Label subtitle = new Label("Order Management System");

        TableView<ObservableList<String>> table =
            new TableView<>();

        TableColumn<ObservableList<String>, String> idColumn =
            new TableColumn<>("Order ID");

        TableColumn<ObservableList<String>, String> itemsColumn =
            new TableColumn<>("Food Items");

        TableColumn<ObservableList<String>, String> totalColumn =
            new TableColumn<>("Total Amount");

        TableColumn<ObservableList<String>, String> statusColumn =
            new TableColumn<>("Order Status");

        idColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().get(0)
            )
        );

        itemsColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().get(1)
            )
        );

        totalColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().get(2)
            )
        );

        statusColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().get(3)
            )
        );

        table.getColumns().addAll(
            idColumn, itemsColumn, totalColumn, statusColumn
        );

        table.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY
        );
        table.setItems(OrderData.orders);

        Label message = new Label("No orders available yet.");
        message.setStyle("-fx-font-size: 15px;");

        Button backButton = new Button("BACK TO DASHBOARD");
        Label statusLabel = new Label("Select Order Status:");

ComboBox<String> statusBox = new ComboBox<>();

statusBox.getItems().addAll(
    "Pending",
    "Preparing",
    "Completed"
);

statusBox.setPromptText("Choose Status");

Button updateButton = new Button("UPDATE STATUS");
updateButton.setOnAction(e -> {

    ObservableList<String> selectedOrder =
        table.getSelectionModel().getSelectedItem();

    if (selectedOrder == null) {
        new Alert(
            Alert.AlertType.WARNING,
            "Please select an order first!"
        ).showAndWait();
        return;
    }

    String newStatus = statusBox.getValue();

    if (newStatus == null) {
        new Alert(
            Alert.AlertType.WARNING,
            "Please select a status!"
        ).showAndWait();
        return;
    }

    selectedOrder.set(3, newStatus);

    OrderData.saveOrders();

    table.refresh();

    new Alert(
        Alert.AlertType.INFORMATION,
        "Order status updated successfully!"
    ).showAndWait();

    statusBox.setValue(null);
});

        backButton.setOnAction(e -> {
            new Dashboard().show(stage);
        });

        VBox root = new VBox(
    20,
    heading,
    subtitle,
    message,
    table,
    statusLabel,
    statusBox,
    updateButton,
    backButton
);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F4F7FB;");

        Scene scene = new Scene(root, 800, 550);

        stage.setTitle("Smart Canteen - Manage Orders");
        stage.setScene(scene);
        stage.show();
    }
}
