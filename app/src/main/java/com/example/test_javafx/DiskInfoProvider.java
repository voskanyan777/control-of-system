/*
 * Класс реализует получение данных об дисковых накопителях
 * */
package com.example.test_javafx;

import oshi.SystemInfo;

import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// Интерфейс для класса
interface DiskInfo {
  String[] getDisksName();

  long getSizeOfDisk(int index);

  String usedDiskMemory(int disk_index);
}

public class DiskInfoProvider implements DiskInfo {

  // Паттерн singleton
  private static DiskInfoProvider instance;

  private DiskInfoProvider() {}

  public static DiskInfoProvider getInstance() {
    if (instance == null) {
      instance = new DiskInfoProvider();
    }
    return instance;
  }

  SystemInfo systemInfo = new SystemInfo();

  // Форматирование объема накопителя
  private DecimalFormat FormatDisk = new DecimalFormat("####");

  public String[] getDisksName() {
    // Метод для полчения названавии дисковвых накопителей
    int count_of_disks = systemInfo.getHardware().getDiskStores().size();
    String[] disks = new String[count_of_disks];
    for (int i = 0; i < count_of_disks; i++) {
      disks[i] =
          systemInfo.getHardware().getDiskStores().get(i).getPartitions().get(0).getMountPoint();
    }
    return disks;
  }

  public long getSizeOfDisk(int index) {
    // Метод для полчения размера диска
    long disk_size = systemInfo.getHardware().getDiskStores().get(index).getSize();
    return disk_size / (1024 * 1024 * 1024);
  }

  public String usedDiskMemory(int disk_index) {
    // Метод для получения используемой памяти в накопителе
    Iterable<FileStore> fileStores = FileSystems.getDefault().getFileStores();
    List<String> usedSpaceList = new ArrayList<>();

    for (FileStore store : fileStores) {
      try {
        // Получаем доступное пространство на диске
        long usableSpace = store.getUsableSpace();
        // Получаем общий размер диска
        long totalSpace = store.getTotalSpace();
        // Вычисляем занятое пространство
        long usedSpace = totalSpace - usableSpace;

        // Переводим в гигабайты и добавляем в ArrayList
        double usedSpaceInGB = (double) usedSpace / (1024 * 1024 * 1024);
        usedSpaceList.add(FormatDisk.format(usedSpaceInGB));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return usedSpaceList.get(disk_index);
  }
}
