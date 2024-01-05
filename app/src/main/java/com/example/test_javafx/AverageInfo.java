/*
 * Класс для отображения информации о средних значениях
 * */
// Open closed Principle - принцип открытости-закрытости
package com.example.test_javafx;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AverageInfo implements InformationFormatter {
    @FXML
    private Text average_info;

    @Override
    public String formatInformation(String cpuValue, String ramValue) {
       return cpuValue + "\n" + ramValue;
    }

    // Устанавливаем информацию с средним значением
    public void setValue(String cpuValue, String ramValue) {
        String formattedInfo = formatInformation(cpuValue, ramValue);
        average_info.setText(formattedInfo);
    }
}