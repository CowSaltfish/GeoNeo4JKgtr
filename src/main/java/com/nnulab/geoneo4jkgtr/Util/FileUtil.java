package com.nnulab.geoneo4jkgtr.Util;

import com.nnulab.geoneo4jkgtr.Model.StratigraphicChronology;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class FileUtil {

    public static StratigraphicChronology getStratigraphicChronologyFromCSV(String stratigraphicChronologyPath) {
        Map<String, Integer> sc = new HashMap<>();
        List<String> scl = new ArrayList<>();

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
                if (columns[2].equals("")) {
                    continue;
                }
                sc.put(columns[2], index);
                scl.add(columns[2]);
                index++;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new StratigraphicChronology(sc, scl);
    }
}
