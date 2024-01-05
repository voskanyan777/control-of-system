/*
 * Класс для представление подробной информации об оперативной памяти
 * */

package com.example.test_javafx;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class RamFullInfo {
    @FXML
    private Text text_full_info;
    RamInfoProvider ramInfoProvider = new RamInfoProvider();

    @FXML
    void initialize() {
        text_full_info.setText(ramInfoProvider.fullRamInfo());
    }
}
