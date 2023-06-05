package com.nnulab.geoneo4jkgtr.Model.Entity.Nodes;

import com.nnulab.geoneo4jkgtr.Model.Entity.Enum.StratumType;
import lombok.Data;

/**
 * @author : LiuXianYu
 * @date : 2023/4/13 9:55
 */
@Data
public class Stratum extends Face{

    private StratumType stratumType;
    //地质年代序号（越小越老）
    private int AgeIndex;

    public Stratum() {
        setLabelName("Stratum");
    }

    @Override
    public String toString() {
        return "Stratum{" +
                "stratumType=" + stratumType +
                ", AgeIndex=" + AgeIndex +
                '}';
    }
}
