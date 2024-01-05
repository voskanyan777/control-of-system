/*
 * Класс реализует получение данных о видеокарте
 * */

package com.example.test_javafx;

import oshi.SystemInfo;

public class GraphicCardProvider {

  // Паттерн singleton
  private static GraphicCardProvider instance;

  private GraphicCardProvider() {}

  public static GraphicCardProvider getInstance() {
    if (instance == null) {
      instance = new GraphicCardProvider();
    }
    return instance;
  }

  SystemInfo systemInfo = new SystemInfo();

  // Название видеокарты
  protected String nameOfGpu =
      String.valueOf(systemInfo.getHardware().getGraphicsCards().get(0).getName());
}
