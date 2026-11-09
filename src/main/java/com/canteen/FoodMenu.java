package com.canteen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class FoodMenu {

    public void show(Stage stage) {

        Label heading = new Label("SMART CANTEEN - FOOD MENU");
        heading.setStyle(
            "-fx-font-size: 25px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #17365D;"
        );

        String[] foodNames = {
            "Idli", "Dosa", "Veg Meals", "Fried Rice",
            "Chowmein", "Pasta", "Pizza", "Burger",
            "lemon soda", "coffee"
        };

        int[] prices = {
            30, 40, 80, 70, 60, 90, 150, 100, 20, 50
        };

        int[] cartQuantities = new int[foodNames.length];

        @SuppressWarnings("unchecked")
        Spinner<Integer>[] quantities = new Spinner[foodNames.length];

        VBox menu = new VBox(15);
        menu.setAlignment(Pos.CENTER);

        menu.getChildren().add(heading);

        // FOOD MENU
        for (int i = 0; i < foodNames.length; i++) {

            Label foodLabel = new Label(
                foodNames[i] + " - Rs. " + prices[i]
            );

            foodLabel.setPrefWidth(180);

            quantities[i] = new Spinner<>(0, 99, 0);
            quantities[i].setPrefWidth(90);

            HBox row = new HBox(20, foodLabel, quantities[i]);
            row.setAlignment(Pos.CENTER);

            menu.getChildren().add(row);
        }

        // BUTTONS
        Button addButton = new Button("ADD TO CART");
        Button totalButton = new Button("CALCULATE TOTAL");
        Button placeOrderButton = new Button("PLACE ORDER");
        Button clearButton = new Button("CLEAR CART");
        Button removeButton = new Button("REMOVE ITEM");
        Button backButton = new Button("BACK TO DASHBOARD");

        // REMOVE ITEM DROPDOWN
        ComboBox<String> removeItemBox = new ComboBox<>();
        removeItemBox.getItems().addAll(foodNames);
        removeItemBox.setPromptText("Select item to remove");

        // CART TABLE
        TableView<String[]> cartTable = new TableView<>();
        cartTable.setPrefWidth(600);
        cartTable.setPrefHeight(180);
        cartTable.setPlaceholder(new Label("Your cart is empty"));

        TableColumn<String[], String> itemColumn =
            new TableColumn<>("Food Item");
        itemColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue()[0])
        );

        TableColumn<String[], String> quantityColumn =
            new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue()[1])
        );

        TableColumn<String[], String> priceColumn =
            new TableColumn<>("Unit Price");
        priceColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue()[2])
        );

        TableColumn<String[], String> subtotalColumn =
            new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue()[3])
        );

        cartTable.getColumns().addAll(
            itemColumn, quantityColumn, priceColumn, subtotalColumn
        );

        itemColumn.setPrefWidth(180);
        quantityColumn.setPrefWidth(100);
        priceColumn.setPrefWidth(140);
        subtotalColumn.setPrefWidth(140);

        Label totalLabel = new Label("Total: Rs. 0");
        totalLabel.setStyle(
            "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: green;"
        );

        // UPDATE CART TABLE AND TOTAL
        Runnable updateCart = () -> {

            cartTable.getItems().clear();

            int total = 0;

            for (int i = 0; i < foodNames.length; i++) {

                if (cartQuantities[i] > 0) {

                    int subtotal = cartQuantities[i] * prices[i];

                    total += subtotal;

                    cartTable.getItems().add(new String[] {
                        foodNames[i],
                        String.valueOf(cartQuantities[i]),
                        "Rs. " + prices[i],
                        "Rs. " + subtotal
                    });
                }
            }

            totalLabel.setText("Total: Rs. " + total);
        };

        // ADD TO CART
        addButton.setOnAction(event -> {

            boolean added = false;

            for (int i = 0; i < foodNames.length; i++) {

                int quantity = quantities[i].getValue();

                if (quantity > 0) {

                    cartQuantities[i] += quantity;
                    quantities[i].getValueFactory().setValue(0);

                    added = true;
                }
            }

            if (!added) {
                new Alert(
                    Alert.AlertType.WARNING,
                    "Please select at least one food item!"
                ).showAndWait();

                return;
            }

            updateCart.run();
        });

        // CALCULATE TOTAL
        totalButton.setOnAction(event -> {

            updateCart.run();

            int total = 0;

            for (int i = 0; i < foodNames.length; i++) {
                total += cartQuantities[i] * prices[i];
            }

            if (total == 0) {
                new Alert(
                    Alert.AlertType.WARNING,
                    "Your cart is empty!"
                ).showAndWait();
            }
        });

        // REMOVE SELECTED ITEM
        removeButton.setOnAction(event -> {

            String selectedFood = removeItemBox.getValue();

            if (selectedFood == null) {
                new Alert(
                    Alert.AlertType.WARNING,
                    "Please select a food item to remove!"
                ).showAndWait();

                return;
            }

            for (int i = 0; i < foodNames.length; i++) {

                if (foodNames[i].equals(selectedFood)) {

                    if (cartQuantities[i] == 0) {
                        new Alert(
                            Alert.AlertType.WARNING,
                            "This item is not in your cart!"
                        ).showAndWait();

                        return;
                    }

                    cartQuantities[i] = 0;
                    break;
                }
            }

            updateCart.run();
            removeItemBox.setValue(null);

            new Alert(
                Alert.AlertType.INFORMATION,
                selectedFood + " removed from cart!"
            ).showAndWait();
        });

        // CLEAR CART
        clearButton.setOnAction(event -> {

            for (int i = 0; i < foodNames.length; i++) {

                cartQuantities[i] = 0;
                quantities[i].getValueFactory().setValue(0);
            }

            removeItemBox.setValue(null);
            updateCart.run();
        });

        // PLACE ORDER
        placeOrderButton.setOnAction(event -> {

            int total = 0;
            StringBuilder cartItems = new StringBuilder();

            for (int i = 0; i < foodNames.length; i++) {

                if (cartQuantities[i] > 0) {

                    total += cartQuantities[i] * prices[i];

                    if (cartItems.length() > 0) {
                        cartItems.append(", ");
                    }

                    cartItems.append(foodNames[i])
                             .append(" x ")
                             .append(cartQuantities[i]);
                }
            }

            if (cartItems.length() == 0) {
                new Alert(
                    Alert.AlertType.WARNING,
                    "Please add food items before placing an order."
                ).showAndWait();

                return;
            }

            OrderData.addOrder(cartItems.toString(), total);

            new Alert(
                Alert.AlertType.INFORMATION,
                "Order placed successfully!"
            ).showAndWait();

            for (int i = 0; i < foodNames.length; i++) {

                cartQuantities[i] = 0;
                quantities[i].getValueFactory().setValue(0);
            }

            removeItemBox.setValue(null);
            updateCart.run();
        });

        // BACK TO DASHBOARD
        backButton.setOnAction(event -> {
            new Dashboard().show(stage);
        });

        // BUTTON LAYOUT
        HBox buttonRow = new HBox(
            10,
            addButton,
            totalButton,
            placeOrderButton,
            clearButton,
            backButton
        );
        buttonRow.setAlignment(Pos.CENTER);

        HBox removeRow = new HBox(
            10,
            removeItemBox,
            removeButton
        );
        removeRow.setAlignment(Pos.CENTER);

        Label cartHeading = new Label("YOUR SHOPPING CART");
        cartHeading.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;"
        );

        VBox cartSection = new VBox(
            10,
            cartHeading,
            cartTable,
            totalLabel
        );
        cartSection.setAlignment(Pos.CENTER);

        menu.getChildren().addAll(
            buttonRow,
            removeRow,
            cartSection
        );

        menu.setPadding(new Insets(30));
        menu.setStyle("-fx-background-color: #F4F7FB;");

        ScrollPane scrollPane = new ScrollPane(menu);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 850, 650);

        stage.setTitle("Smart Canteen - Food Ordering");
        stage.setScene(scene);
        stage.show();
    }
}