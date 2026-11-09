package com.canteen;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderData {

    public static ObservableList<ObservableList<String>> orders =
            FXCollections.observableArrayList();

    private static int nextOrderId = 1;

    private static final String FILE_NAME = "CanteenOrders.xlsx";

    // Add a new order
    public static void addOrder(String items, double total) {

        ObservableList<String> order =
                FXCollections.observableArrayList();

        order.add(String.valueOf(nextOrderId++));
        order.add(items);
        order.add("Rs. " + total);

        order.add("Pending");

        String dateTime = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        );

        order.add(dateTime);

        orders.add(order);

        saveOrders();
    }

    // Save all orders to Excel
    public static void saveOrders() {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Orders");

            String[] headers = {
                "Order ID",
                "Food Items",
                "Total Amount",
                "Order Status",
                "Date and Time"
            };

            Row header = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowNumber = 1;

            for (ObservableList<String> order : orders) {

                Row row = sheet.createRow(rowNumber++);

                for (int i = 0; i < order.size(); i++) {
                    row.createCell(i).setCellValue(order.get(i));
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream output =
                    new FileOutputStream(FILE_NAME)) {

                workbook.write(output);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load previously saved orders
    public static void loadOrders() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        orders.clear();
        nextOrderId = 1;

        try (FileInputStream input = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(input)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                ObservableList<String> order =
                        FXCollections.observableArrayList();

                for (int j = 0; j < 5; j++) {

                    Cell cell = row.getCell(j);

                    if (cell == null) {
                        order.add("");
                    } else {
                        order.add(cell.toString());
                    }
                }

                orders.add(order);

                int id = Integer.parseInt(order.get(0));

                if (id >= nextOrderId) {
                    nextOrderId = id + 1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}