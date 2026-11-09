package com.canteen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GenerateReceipt {

    public void show(Stage stage) {

        Label heading = new Label("SMART CANTEEN - RECEIPT");
        heading.setStyle(
            "-fx-font-size: 25px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #17365D;"
        );

        ListView<String> receiptList = new ListView<>();

        for (var order : OrderData.orders) {

            String receipt =
                "Order ID: " + order.get(0) + "\n" +
                "Food Items: " + order.get(1) + "\n" +
                "Total Amount: " + order.get(2) + "\n" +
                "Status: " + order.get(3) + "\n" +
                "Date & Time: " + order.get(4);

            receiptList.getItems().add(receipt);
        }

        if (OrderData.orders.isEmpty()) {
            receiptList.getItems().add("No orders available.");
        }

        Button backButton = new Button("BACK TO DASHBOARD");

        backButton.setOnAction(e -> {
            new Dashboard().show(stage);
        });

        VBox layout = new VBox(
            20, heading, receiptList, backButton
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30));
        layout.setStyle("-fx-background-color: #F4F7FB;");

        Scene scene = new Scene(layout, 800, 550);

        stage.setTitle("Smart Canteen - Generate Receipt");
        stage.setScene(scene);
        stage.show();
    }
}
