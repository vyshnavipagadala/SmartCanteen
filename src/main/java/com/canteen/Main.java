package com.canteen;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        OrderData.loadOrders();

        Label title = new Label("SMART CANTEEN");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitle = new Label("Welcome to Food Ordering System");

        TextField username = new TextField();
        username.setPromptText("Enter Username");
        username.setMaxWidth(250);

        PasswordField password = new PasswordField();
        password.setPromptText("Enter Password");
        password.setMaxWidth(250);

        Button login = new Button("LOGIN");
        Button clear = new Button("CLEAR");

        Label message = new Label();

      login.setOnAction(e -> {
    if (username.getText().equals("admin")
            && password.getText().equals("1234")) {

        message.setText("Login Successful!");

        Stage dashboardStage = new Stage();
        new Dashboard().show(dashboardStage);

        stage.close();

    } else {
        message.setText("Invalid Username or Password!");
    }
});

        clear.setOnAction(e -> {
            username.clear();
            password.clear();
            message.setText("");
        });

        VBox root = new VBox(15);

        root.getChildren().addAll(
                title, subtitle, username,
                password, login, clear, message
        );

        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #FFF4E6; -fx-padding: 30;");

        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("Smart Canteen - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}