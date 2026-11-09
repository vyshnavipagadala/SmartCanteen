package com.canteen;

import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelExporter {

    public static void exportOrders(String filePath) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Canteen Orders");

            String[] headers = {
                "Order ID",
                "Food Items",
                "Total Amount",
                "Order Status",
                "Date and Time"
            };

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNumber = 1;

            for (ObservableList<String> order : OrderData.orders) {

                Row row = sheet.createRow(rowNumber++);

                for (int i = 0; i < order.size(); i++) {
                    row.createCell(i).setCellValue(order.get(i));
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream output =
                    new FileOutputStream(filePath)) {
                workbook.write(output);
            }
        }
    }
}
