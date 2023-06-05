package com.nnulab.geoneo4jkgtr.Model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2023/4/18 11:24
 */
@Data
public class StratigraphicChronology {
    private Map<String, Integer> stratigraphicChronologyMap;
    private List<String> stratigraphicChronologyList;

    public StratigraphicChronology(Map<String, Integer> stratigraphicChronologyMap, List<String> stratigraphicChronologyList) {
        this.stratigraphicChronologyMap = stratigraphicChronologyMap;
        this.stratigraphicChronologyList = stratigraphicChronologyList;
    }
}
