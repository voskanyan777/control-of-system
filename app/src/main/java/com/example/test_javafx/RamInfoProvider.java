/*
 * Класс реализует получение данных об оперативной памяти
 * */

//  Interface Segregation Principle - принцип разделения интерфейсов

package com.example.test_javafx;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;

import java.text.DecimalFormat;

interface RamInfo {
    double fullnessRam();

    double getUsedRam();

    String fullRamInfo();
}

public class RamInfoProvider {
    SystemInfo systemInfo = new SystemInfo();
    GlobalMemory memory = systemInfo.getHardware().getMemory();

    // Форматирование для оперативной памяти
    private DecimalFormat FormatRam = new DecimalFormat("####.#");
    // Свободная оперативная память
    protected double remainingRam = memory.getAvailable() / (1024.0 * 1024.0 * 1024.0);

    // Полная оперативная память
    protected double availableRam = (memory.getTotal() / (1024.0 * 1024.0 * 1024.0));


    protected String formattedAvailableRam = FormatRam.format(availableRam);


    public double fullnessRam() {
      // Метод возвращает заполнненость оперативной памяти
      // в диапозоне от 0.0 до 1.0. Грубо говоря в диапозоне от 0% до 100%
      // Это нужно для корректного отображения в прогресс баре
        double v = 1 - (this.remainingRam / this.availableRam);
        return v;
    }

    public double getUsedRam() {
      // Метод возвращает количество используемой оперативной памяти
        double remainingRam = memory.getAvailable() / (1024.0 * 1024.0 * 1024.0);

        double availableRam = (memory.getTotal() / (1024.0 * 1024.0 * 1024.0));

        double result = availableRam - remainingRam;
        return result;
    }

    public String fullRamInfo() {
      // Подрообная информация об оператвиной памяти
        String clockSpeed =
                "Clock speed: " + memory.getPhysicalMemory().get(0).getClockSpeed() / 1000000 + "Mhz\n";
        String memoryType = "Memory type: " + memory.getPhysicalMemory().get(0).getMemoryType() + "\n";
        String countOfRam = "Count: " + memory.getPhysicalMemory().size() + "\n";
        String result = clockSpeed + memoryType + countOfRam;
        return result;
    }
}
