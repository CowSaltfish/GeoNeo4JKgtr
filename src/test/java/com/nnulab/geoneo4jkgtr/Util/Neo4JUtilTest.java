package com.nnulab.geoneo4jkgtr.Util;

import junit.framework.TestCase;

public class Neo4JUtilTest extends TestCase {

    public void testGetNodesInfo() {
        Neo4JUtil.getNodesInfo("match (n:Face) return n");
    }
}