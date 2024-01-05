/*
 * Класс GUI программы
 * */

package com.example.test_javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Gui extends Application {
  @Override
  public void start(Stage stage) throws IOException {

    FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource("hello-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
    stage.setTitle("Monitoring");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();

  }

  public DecimalFormat userRam = new DecimalFormat("####.#");
  public DecimalFormat FormatRam = new DecimalFormat("####.#");

  ArrayList cpuLoads = new ArrayList();
  ArrayList ramUsed = new ArrayList();

  @FXML private CheckBox super_mode_button;
  @FXML public Text main_text;
  @FXML private Text ram_text;
  @FXML private Text cpu_text;
  @FXML public Text ram_info_text;
  @FXML private Text cpu_load_text;

  @FXML private Button button_full_info;
  @FXML private Button button_full_info1;
  @FXML private ProgressBar cpu_bar;

  @FXML public ProgressBar ram_bar;

  @FXML
  public ListView<String> listView; // Замените String на тип данных, который вы хотите использовать

  private ObservableList<String> dataList = FXCollections.observableArrayList();

  private Timeline timeline;
  private Timeline timeline_1;

  @FXML private Text gpu_text;
  ProcessorInfoProvider processorInfoProvider = new ProcessorInfoProvider();
  RamInfoProvider ramInfoProvider = new RamInfoProvider();

  // Применение паттерна Singleton
  GraphicCardProvider graphicCardProvider = GraphicCardProvider.getInstance();
  DiskInfoProvider diskInfoProvider = DiskInfoProvider.getInstance();

  @FXML
  void initialize() {
    cpu_text.setText("CPU: " + processorInfoProvider.nameOfCpu);
    gpu_text.setText("GPU: " + graphicCardProvider.nameOfGpu);
    ram_text.setText("RAM:");
    for (int i = 0; i < diskInfoProvider.getDisksName().length; i++) {
      dataList.add(
          diskInfoProvider.getDisksName()[i]
              + " "
              + diskInfoProvider.usedDiskMemory(i)
              + " GB / "
              + diskInfoProvider.getSizeOfDisk(i)
              + " GB");
    }
    listView.setItems(dataList);


    button_full_info.setOnAction(event -> openRamFullInfoWindow());
    button_full_info1.setOnAction(event -> openCpuFullInfoWindow());
    super_mode_button.setOnAction(event -> StartSuperMod());

    // Создаем таймер, который будет выполняться каждую секунду
    timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateProgressBar()));
    timeline.setCycleCount(Timeline.INDEFINITE); // Запускаем таймер бесконечное количество раз
    timeline.play(); // Запускаем таймер

    timeline_1 = new Timeline(new KeyFrame(Duration.seconds(0.8), event -> upgradeCpuLoad()));
    timeline_1.setCycleCount(Timeline.INDEFINITE);
    timeline_1.play();
  }

  private void averageWindow() {
    /*
     * Метод открывает окно с информацией о средних значениях
     * нагрузки процессора и потребляемой оперативной памяти
     * */
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("average_value.fxml"));
      Parent root = fxmlLoader.load();
      AverageInfo averageInfo = fxmlLoader.getController();

      // Передаем данные контроллеру
      averageInfo.setValue(getAverageMeaning(cpuLoads), getUsedRam(ramUsed));

      Stage settingsStage = new Stage();
      settingsStage.setTitle("Full info");
      settingsStage.setScene(new Scene(root, 571, 327));
      settingsStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getAverageMeaning(ArrayList<Integer> loads) {
    /*
     * Метод считает средную нагрузку процессора
     * */
    String result;
    int total = 0;
    if (!loads.isEmpty()) { // Проверяем, что список не пустой, чтобы избежать деления на ноль
      for (Integer load : loads) {
        total += load;
      }
      int average = total / loads.size();
      result = "Average cpu load: " + average + "%";
    } else {
      result = "No data available"; // Добавьте обработку случая, когда список пустой
    }
    cpuLoads.clear();
    return result;
  }

  private String getUsedRam(ArrayList<Double> usedRam) {
    /*
     * Метод считает средную потребляемую оперативную память
     * */
    Double total = 0.0;
    Double average;
    String result;
    for (Double used : usedRam) {
      total += used;
    }
    average = total / usedRam.size();
    result = " Average ram used: " + userRam.format(average) + " Gb";
    ramUsed.clear();
    return result;
  }

  private void StartSuperMod() {
    /*
     * Метод для запуска супер мода
     * */
    new Thread(
            () -> {
              String result;
              while (super_mode_button.isSelected()) {
                if (super_mode_button.isSelected()) {
                  int load = Integer.parseInt(processorInfoProvider.getLoad());
                  cpuLoads.add(load);
                  double usedRam = ramInfoProvider.getUsedRam();
                  ramUsed.add(usedRam);
                }
              }
            })
        .start();
    if (!super_mode_button.isSelected()) {
      averageWindow();
    }
  }

  private void upgradeCpuLoad() {
    /*
     * Метод для обновления данных о процессоре
     * */
    new Thread(
            () -> {
              int load = Integer.parseInt(processorInfoProvider.getLoad());
              cpu_load_text.setText("Load: " + load + "%");
              cpu_bar.setProgress(processorInfoProvider.formattedCpuLoad(load));
            })
        .start();
  }

  private void openRamFullInfoWindow() {
    /*
     * Метод для открытия окна с информацией об оперативной памяти
     * */
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("full_ram_info.fxml"));
      Parent root = fxmlLoader.load();

      Stage settingsStage = new Stage();
      settingsStage.setTitle("Full info");
      settingsStage.setScene(new Scene(root, 571, 327));
      settingsStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void openCpuFullInfoWindow() {
    /*
     * Метод для открытия окна с информацией об процессоре
     * */
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("full_cpu_info.fxml"));
      Parent root = fxmlLoader.load();

      Stage settingsStage = new Stage();
      settingsStage.setTitle("Full info");
      settingsStage.setScene(new Scene(root, 571, 327));
      settingsStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  private void updateProgressBar() {
    /*
     * Метод для обновления прогресс бара
     * */
    new Thread(
            () -> {
              ram_bar.setProgress(
                  ramInfoProvider.fullnessRam()); // Устанавливаем прогресс-бару новое
              ram_info_text.setText(
                  FormatRam.format(ramInfoProvider.getUsedRam())
                      + "/"
                      + ramInfoProvider.formattedAvailableRam
                      + "GB");

              listView.refresh();
            })
        .start();
  }

  public static void main(String[] args) {
    launch();
  }
}
