/*
 * Класс для отображения информации об процессоре
 * */
// Single Responsibility Principle - принцип единственной ответственности
package com.example.test_javafx;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CpuFullInfo {
    @FXML
    private Text text_full_info;

    // Экземпляр класса для получения информации о процессоре
    ProcessorInfoProvider processorInfoProvider = new ProcessorInfoProvider();

    // При открытий окна данный метод в поле `text_full_info` установит текст с
    // подробной информации о процессоре
    @FXML
    void initialize() {
        text_full_info.setText(processorInfoProvider.fullCpuInfo);
    }
}
