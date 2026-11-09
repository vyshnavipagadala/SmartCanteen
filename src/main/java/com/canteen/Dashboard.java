package com.canteen;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Dashboard {

    
    public void show(Stage stage) {

        Label heading = new Label("SMART CANTEEN");
        heading.setStyle(
            "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #17365D;"
        );

        Label subtitle = new Label("Food Ordering Management System");
        subtitle.setStyle("-fx-font-size: 16px;");

        Button foodButton = new Button("Food Menu");
        Button orderButton = new Button("Manage Orders");
        Button receiptButton = new Button("Generate Receipt");
        Button excelButton = new Button("Export to Excel");
        Button logoutButton = new Button("Logout");

        foodButton.setPrefWidth(220);
        orderButton.setPrefWidth(220);
        receiptButton.setPrefWidth(220);
        excelButton.setPrefWidth(220);
        logoutButton.setPrefWidth(220);

        foodButton.setOnAction(e -> {
    new FoodMenu().show(stage);
});

      orderButton.setOnAction(e -> {
    new ManageOrders().show(stage);
});

       receiptButton.setOnAction(e -> {
    new GenerateReceipt().show(stage);
});
excelButton.setOnAction(e -> {

    FileChooser fileChooser = new FileChooser();

    fileChooser.setTitle("Save Canteen Orders");

    fileChooser.setInitialFileName("CanteenOrders.xlsx");

    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter(
            "Excel Files", "*.xlsx"
        )
    );

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
        try {
            ExcelExporter.exportOrders(file.getAbsolutePath());

            new Alert(
                Alert.AlertType.INFORMATION,
                "Excel file exported successfully!"
            ).showAndWait();

        } catch (IOException ex) {
            new Alert(
                Alert.AlertType.ERROR,
                "Error exporting Excel file: " + ex.getMessage()
            ).showAndWait();
        }
    }
});

        VBox root = new VBox(
            20,
            heading,
            subtitle,
            foodButton,
            orderButton,
            receiptButton,
            excelButton,
            logoutButton
        );

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F4F7FB;");

        Scene scene = new Scene(root, 800, 550);

        stage.setTitle("Smart Canteen - Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}