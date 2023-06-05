package com.nnulab.geoneo4jkgtr.Util;

import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSShapefileDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author : LiuXianYu
 * @date : 2023/5/16 17:15
 */
@Data
@Component
public class GeoServerUtil {

    @Value("${geoserver.url}")
    private String url;
    @Value("${geoserver.username}")
    private String username;
    @Value("${geoserver.password}")
    private String password;
    @Value("${geoserver.workspacename}")
    private String workspaceName;
    @Value("${geoserver.storename}")
    private String storeName;
    @Value("${geoserver.layername}")
    private String layerName;

    /**
     * 获取geoserver图层管理
     *
     * @param address  geoserver服务地址
     * @param account  账号
     * @param passWord 密码
     * @return it.geosolutions.geoserver.rest.GeoServerRESTManager
     * @author qqz
     * @date 17:07 2022/2/22
     **/
    public static GeoServerRESTManager getManager(String address, String account, String passWord) throws MalformedURLException {
        URL u = new URL(address);
        //获取管理对象
        return new GeoServerRESTManager(u, account, passWord);
    }

    /**
     * 创建数据库存储
     *
     * @param manager  geoserver管理类
     * @param workArea 工作区
     * @param dataPool 数据存储
     * @return boolean
     * @author qqz
     * @date 15:12 2022/2/19
     **/
    public static boolean createDBDataPool(GeoServerRESTManager manager, String workArea, String dataPool, String dbHost, int dbPort, String dbUser, String dbPassWord, String dbDataBase, String dbSchema) {
        RESTDataStore restStore = manager.getReader().getDatastore(workArea, dataPool);
        if (restStore == null) {
            GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(dataPool);

            store.setHost(dbHost);//设置url
            store.setPort(dbPort);//设置端口
            store.setUser(dbUser);// 数据库的用户名
            store.setPassword(dbPassWord);// 数据库的密码
            store.setDatabase(dbDataBase);// 那个数据库;
            store.setSchema(dbSchema); //当前先默认使用public这个schema
            store.setConnectionTimeout(20);// 超时设置
            store.setMaxConnections(20); // 最大连接数
            store.setMinConnections(1);     // 最小连接数
            store.setExposePrimaryKeys(true);
            boolean createStore = manager.getStoreManager().create(workArea, store);
            System.out.println("create store : " + createStore);
            return createStore;
        }
        return true;
    }

    /**
     * 创建shp数据存储
     *
     * @param manager  geoserver图层管理类
     * @param workArea 工作区间
     * @param dataPool 数据存储
     * @param geoPath  图层服务存放地址
     * @param shpName  shp文件名称
     * @return boolean
     * @author qqz
     * @date 14:14 2022/2/22
     **/
    public static boolean createShpDataPool(GeoServerRESTManager manager, String workArea, String dataPool, String geoPath, String shpName) throws MalformedURLException {
        String fileUrl = "file://" + geoPath + "/" + workArea + "/" + dataPool + "/" + shpName;
        URL urlShapeFile = new URL(fileUrl);
        boolean existsDatastore = manager.getReader().existsDatastore(workArea, dataPool);
        if (!existsDatastore) {
            //创建shape文件存储
            GSShapefileDatastoreEncoder store = new GSShapefileDatastoreEncoder(dataPool, urlShapeFile);
            store.setCharset(Charset.forName("GBK"));
            boolean createStore = manager.getStoreManager().create(workArea, store);
            return createStore;
        }
        return true;
    }

    /**
     * 创建工作区间
     *
     * @param manager  geoserver图层管理类
     * @param workArea 工作区间
     * @return boolean
     * @author qqz
     * @date 14:15 2022/2/22
     **/
    public static boolean createWorkArea(GeoServerRESTManager manager, String workArea) {
        boolean existsWorkspace = manager.getReader().existsWorkspace(workArea);
        //判断工作空间是否存在
        if (!existsWorkspace) {
            //创建一个新的存储空间
            boolean createWs = manager.getPublisher().createWorkspace(workArea);
            System.out.println("create ws : " + createWs);
            return createWs;
        }
        return true;
    }

    /**
     *  创建样式
     * @param manager geoserver图层管理类
     * @param obj    反射所需类
     * @param styleName 样式名称
     * @param geoStylePath
     * @return
     */
    public static boolean createStyle(GeoServerRESTManager manager, Object obj, String styleName, String geoStylePath) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        boolean existsStyle = manager.getReader().existsStyle(styleName);
        if (!existsStyle) {
            //获取样式格式
            String style = FileUtils.readFileToString(new File(geoStylePath));
            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                //获取属性名
                String fieldName = field.getName();
                //首字母转大写
                fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method m = clazz.getMethod("get" + fieldName);  // 获取getter方法
                // 执行方法提提取数值
                if (m.invoke(obj) == null) {
                    style = style.replaceAll("@" + fieldName.toLowerCase(), "");
                } else {
                    String value = m.invoke(obj).toString();    // 执行获取数值
                    // 将对应的样式格式，设置为提取到的数值
                    style = style.replaceAll("@" + fieldName.toLowerCase(), value);
                }
            }
            //设置样式名称
            style = style.replaceAll("@sldName", styleName);
            //创建样式
            existsStyle = manager.getPublisher().publishStyle(style);
        }
        return existsStyle;
    }

    /**
     * 发布数据库图层 === 发布数据库
     *
     * @param manager    geoserver图层管理
     * @param workArea   工作区间
     * @param dataPool   数据存储
     * @param styleName  样式名称
     * @param name       图层标题名称
     * @param nativeName 图层名称
     * @param srs        坐标系
     * @return boolean
     * @author qqz
     * @date 14:17 2022/2/22
     **/
    public static boolean publishDBLayer(GeoServerRESTManager manager, String workArea, String dataPool, String styleName, String name, String nativeName, String srs) {
        boolean existsLayer = manager.getReader().existsLayer(workArea, name);
        if (!existsLayer) {
            GSLayerEncoder layerEncoder = new GSLayerEncoder();
            layerEncoder.setEnabled(true);
            //设置图层默认样式
            layerEncoder.setDefaultStyle(styleName);
            GSFeatureTypeEncoder gsFeatureTypeEncoder = new GSFeatureTypeEncoder();
            if (StringUtil.isBlank(name) && StringUtil.isBlank(nativeName)) {
//                ExceptionFactory.throwException("表名和标识码为空!");
                System.out.println("表名和标识码为空!");
            } else if (StringUtil.isNotBlank(name) && StringUtil.isNotBlank(nativeName)) {
                gsFeatureTypeEncoder.setName(name);
                gsFeatureTypeEncoder.setNativeName(nativeName);
            } else if (StringUtil.isNotBlank(name)) {
                gsFeatureTypeEncoder.setName(name);
                gsFeatureTypeEncoder.setNativeName(name);
            } else {
                gsFeatureTypeEncoder.setName(nativeName);
                gsFeatureTypeEncoder.setNativeName(nativeName);
            }
            gsFeatureTypeEncoder.setSRS(srs);
            boolean publish = manager.getPublisher().publishDBLayer(workArea, dataPool, gsFeatureTypeEncoder, layerEncoder);
            return publish;
        }
        return true;
    }



    /**
     * 发布shp图层=====shp文件
     *
     * @param manager   geoserver图层管理类型
     * @param zipFile   zip文件
     * @param workArea  工作区间
     * @param dataPool  数据存储
     * @param styleName 样式名称
     * @param layerName 图层名称
     * @param srs       坐标系
     * @return boolean
     * @author qqz
     * @date 14:18 2022/2/22
     **/
    public static boolean publishShpLayer(GeoServerRESTManager manager, File zipFile, String workArea, String dataPool, String styleName, String layerName, String srs)  {
        boolean existsLayer = manager.getReader().existsLayer(workArea, layerName);
        if (!existsLayer) {
            boolean publish = false;
            try {
                publish = manager.getPublisher().publishShp(workArea, dataPool, layerName, zipFile, srs, styleName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return publish;
        }
        return true;
    }


}
