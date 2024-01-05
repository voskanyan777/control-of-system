/*
 1
*/

/*
 * Класс реализует получение данных об процессоре
 * */

package com.example.test_javafx;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.text.DecimalFormat;

interface ProcessorInfo {
  String getLoad();

  double formattedCpuLoad(double cpuLoad);
}

public class ProcessorInfoProvider implements ProcessorInfo {
  SystemInfo systemInfo = new SystemInfo();
  CentralProcessor processor = systemInfo.getHardware().getProcessor();

  // Форматирование для загрузки процессора
  private DecimalFormat FormatCpuLoad = new DecimalFormat("###");
  protected String nameOfCpu = processor.getProcessorIdentifier().getName();
  protected String fullCpuInfo = processor.toString();

  public String getLoad() {
    // Метод возвращает загруженность процессора в реальном времени
    float load = (float) (processor.getProcessorCpuLoad(1000)[0] * 100);
    String result = FormatCpuLoad.format(load);
    return result;
  }

  public double formattedCpuLoad(double cpuLoad) {
    // Метод возвращает загруженность процессора в форматированном виде
    double newMin = 0.1;
    double newMax = 1.0;

    double newValue = (cpuLoad / 100.0) * (newMax - newMin) + newMin;
    return newValue;
  }
}
