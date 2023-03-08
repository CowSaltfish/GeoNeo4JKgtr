package com.nnulab.geoneo4jkgtr.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

    public static Map<String, Integer> getStratigraphicChronologyFromCSV(String stratigraphicChronologyPath) {
        Map<String, Integer> sc = new HashMap<>();
        // 创建 reader
        try (BufferedReader br = Files.newBufferedReader(Paths.get(stratigraphicChronologyPath))) {
            int index = 0;
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line;
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                // 打印行
//                System.out.println("User["+ String.join(", ", columns) +"]");
                if (index <= 0) {
                    ++index;
                    continue;
                }
                if(columns[2].equals("")){
                    continue;
                }
                sc.put(columns[2], index);
                index++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sc;
    }
}
