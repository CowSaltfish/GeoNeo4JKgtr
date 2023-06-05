package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.nnulab.geoneo4jkgtr.Service.GeoServerService;
import com.nnulab.geoneo4jkgtr.Util.GeoServerUtil;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;

/**
 * @author : LiuXianYu
 * @date : 2023/5/16 19:51
 */
public class GeoServerServiceImpl implements GeoServerService {

    @Resource
    GeoServerUtil geoServerUtil;

    /**
     * 发布shp
     * @return
     */
    @Override
    public boolean publishShp() {
        String url = geoServerUtil.getUrl();//geoserver的地址
        String un = geoServerUtil.getUsername();//geoserver的账号
        String pw = geoServerUtil.getPassword();//geoserver的密码
        String workspace = geoServerUtil.getWorkspaceName();//工作区名称
        String storeName = geoServerUtil.getStoreName();//数据源名称
        String layerName = geoServerUtil.getLayerName();//发布的图层名称，此名称必须和压缩包的名称一致

        //shp文件压缩包，必须是zip压缩包，且shp文件(.shp、.dbf、.shx等)外层不能有文件夹，且压缩包名称需要与shp图层名称一致
        String zipFilePath = "D:\\data\\shapefile\\1125shpfile\\temp.zip";

        try {
            //  1、获取geoserver连接对象
            GeoServerRESTManager manager = null;

            try {
                manager = new GeoServerRESTManager(new URL(url), un, pw);
                System.out.println("连接geoserver服务器成功");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("geoserver服务器连接失败");
                return false;
            }

            GeoServerRESTReader reader = manager.getReader();
            GeoServerRESTPublisher publisher = manager.getPublisher();
            GeoServerRESTStoreManager storeManager = manager.getStoreManager();

            //  2、判断是否有工作区，没有则创建
            boolean b2 = reader.existsWorkspace(workspace);
            if (!b2) {
                boolean b = publisher.createWorkspace(workspace);
                if (!b) {
                    System.out.println("工作区创建失败");
                    return false;
                }
            }

            //  3、判断是否有数据源，没有则创建
            //  4、发布图层，如果存在就不发布
            //  创建数据源 和 发布图层服务可以一步进行
            RESTDataStore datastore = reader.getDatastore(workspace, storeName);
            RESTLayer layer = reader.getLayer(workspace, layerName);
            if (layer == null && datastore == null) {
                File file = new File(zipFilePath);
                // 进行发布；参数依次为：工作区名称、数据源名称、图层名称、shp文件压缩文件对象、坐标系
                boolean b = publisher.publishShp(workspace, storeName, layerName, file, "EPSG:4326");
                if (!b) {
                    System.out.println("shp图层发布失败");
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
