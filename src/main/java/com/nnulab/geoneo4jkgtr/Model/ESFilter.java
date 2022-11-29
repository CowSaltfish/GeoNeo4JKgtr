package com.nnulab.geoneo4jkgtr.Model;

import lombok.Data;

@Data
public class ESFilter {

    private String es_url;

    private String index_name;

    private String query;
}
