package com.nnulab.geoneo4jkgtr.Util;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gdal.gdal.Band;
import org.gdal.gdal.ColorTable;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.ogr.*;
import org.gdal.osr.SpatialReference;

import java.io.*;
import java.util.List;
import java.util.Vector;

import static org.gdal.ogr.ogrConstants.*;

/**
 * @author : LiuXianYu
 * @date : 2022/4/1 11:43
 */
public class GdalUtil {

    private static final Logger log = LoggerFactory.getLogger(GdalUtil.class);

    public static void init() {
        loadGdalDll();
        gdal.AllRegister();
    }

    public static void loadGdalDll() {
        log.info("---LibraryUtil--gdal载入动态库");
        try {
            //根据系统环境加载资源
            String systemType = System.getProperty("os.name");
            String file = "";
            boolean isWin = systemType.toLowerCase().indexOf("win") != -1;
            if (isWin) {
                file = "/gdal/win32/gdalalljni.dll";
            } else {
                file = "/gdal/linux/libgdalalljni.so";
            }
            //从资源文件加载动态库
            loadFromResource(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从资源文件加载类
     * * @param clazz jar中类
     *
     * @param file resources目录下文件全路径：/gdal/win32/gdalalljni.dll
     */
    public static void loadFromResource(String file) throws IOException {
        try {
            //获取系统路径
//            String[] libraryPaths = initializePath("user.dir");
            String[] libraryPaths = initializePath("java.library.path");

            //log.info("---LibraryUtil-----java.library.path={}",StringUtil.join(";", libraryPaths));
            if (libraryPaths == null || libraryPaths.length == 0) {
                log.info("---LibraryUtil--请设置环境变量java.library.path");
                return;
            }
//            String nativeTempDir = libraryPaths[0]+ File.separator + "src\\main\\resources\\gdal\\win32";
            String nativeTempDir = libraryPaths[0];

            int sepIndex = file.lastIndexOf(File.separator);
            if (sepIndex == -1) {
                sepIndex = file.lastIndexOf("/");
            }
            String fileName = file.substring(sepIndex + 1);

            log.info("---LibraryUtil--从环境变量{}加载{}", nativeTempDir, fileName);
            //系统库不存在，就从资源文件复制
            File extractedLibFile = new File(nativeTempDir + File.separator + fileName);
            if (!extractedLibFile.exists()) {
                //file resources目录下文件全路径：/gdal/windows/gdalalljni.dll
                InputStream in = GdalUtil.class.getResourceAsStream(file);
                if (in == null) {
                    log.info("---LibraryUtil--资源文件不存在{}", file);
                    throw new FileNotFoundException(file);
                }
                saveFile(in, extractedLibFile);
                //保存文件到java.library.path
                log.info("---LibraryUtil--成功保存文件{}到{}", fileName, extractedLibFile.getPath());
            }
            //注意采用loadLibrary加载时mapLibraryName方法会根据系统补全名称
            int startIndex = fileName.startsWith("lib") ? 3 : 0;
            String libName = fileName.substring(startIndex, fileName.indexOf("."));
            String mapLibraryName = System.mapLibraryName(libName);
            log.info("---LibraryUtil--mapLibraryName={}", mapLibraryName);
            //输出调试信息
            log.info("---LibraryUtil--系统加载动态库{}开始", libName);
//            System.load("C:/Users/13222/.jdks/corretto-11.0.14.1/bin/" + libName + ".dll");
            System.loadLibrary(libName);
            log.info("---LibraryUtil--系统加载动态库{}完成", libName);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    private static String[] initializePath(String propname) {
        String ldpath = System.getProperty(propname, "");
        String ps = File.pathSeparator;
        int ldlen = ldpath.length();
        int i, j, n;
        // Count the separators in the path
        i = ldpath.indexOf(ps);
        n = 0;
        while (i >= 0) {
            n++;
            i = ldpath.indexOf(ps, i + 1);
        }

        // allocate the array of paths - n :'s = n + 1 path elements
        String[] paths = new String[n + 1];

        // Fill the array with paths from the ldpath
        n = i = 0;
        j = ldpath.indexOf(ps);
        while (j >= 0) {
            if (j - i > 0) {
                paths[n++] = ldpath.substring(i, j);
            } else if (j - i == 0) {
                paths[n++] = ".";
            }
            i = j + 1;
            j = ldpath.indexOf(ps, i);
        }
        paths[n] = ldpath.substring(i, ldlen);
        return paths;
    }

    public static void saveFile(InputStream in, File extractedLibFile) throws IOException {
        BufferedInputStream reader = null;
        FileOutputStream writer = null;
        try {
            //文件不存在创建文件，否则获取流报异常
            createFile(extractedLibFile);

            reader = new BufferedInputStream(in);
            writer = new FileOutputStream(extractedLibFile);

            byte[] buffer = new byte[1024];

            while (reader.read(buffer) > 0) {
                writer.write(buffer);
                buffer = new byte[1024];
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null)
                in.close();
            if (writer != null)
                writer.close();
        }
    }

    /**
     * 文件不存在创建文件，包括上级目录
     */
    public static void createFile(File destFile) throws IOException {
        File pfile = destFile.getParentFile();
        if (!pfile.exists()) {
            pfile.mkdirs();
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
    }

    /**
     * 由shp文件路径获取图层
     *
     * @param path
     * @return
     */
    public static Layer getLayerByPath(String path) {
        String strDriverName = "ESRI Shapefile";
        org.gdal.ogr.Driver oDriver = ogr.GetDriverByName(strDriverName);
        if (null == path)
            return null;
        DataSource dataSource = oDriver.Open(path);
        Layer layer = dataSource.GetLayer(0);
        //dataSource.delete.txt();
        return layer;
    }


    /**
     * 面转线
     *
     * @return boolean                    是否成功
     * @brief ConvertPolygonToPolyline        面图层转换为线图层
     * @param[in] tString polylinePath        转换后线图层文件路径
     * @param[in] OGRLayer* pLayer            要转换的面图层文件
     * @param[in] Envelope envelope            要转换数据的范围
     * @param[in] vector<long> vecFIDs        选中的要素ID列表
     * @param[in] pOGRSpatialReference        要转换的数据的空间参考(如果为空表示坐标系信息不变)
     * @author
     * @date
     */
    public static boolean ConvertPolygonToPolylineEx(String polylinePath, Layer pLayer, double[] envelope, Vector<Long> vecFIDs, SpatialReference pOGRSpatialReference) {
        // 判断
        if (pLayer == null) return false;
        if (polylinePath.isEmpty()) return false;

        // 坐标系读取
        SpatialReference pOGRSpatialReference_Source = pLayer.GetSpatialRef();
        int isSameCoordSystem = 0;
        if (pOGRSpatialReference == null) {
            pOGRSpatialReference = pOGRSpatialReference_Source;
            isSameCoordSystem = 1;
        } else if (pOGRSpatialReference_Source != null) {
            isSameCoordSystem = pOGRSpatialReference_Source.IsSame(pOGRSpatialReference);
        }

        // 创建Shape文件
        String strDriverName = "ESRI Shapefile";
        org.gdal.ogr.Driver oDriver = ogr.GetDriverByName(strDriverName);
        if (oDriver == null) {
            System.out.println(polylinePath + "驱动不可用！\n");
            return false;
        }

        DataSource pOGRDataSource = oDriver.CreateDataSource(polylinePath);
        if (pOGRDataSource == null) {
            System.out.println("创建矢量文件【" + polylinePath + "】失败！\n");
            return false;
        }

        Layer pOGRLayer = pOGRDataSource.CreateLayer("boundary", pOGRSpatialReference, wkbLineString);
        if (pOGRLayer == null) return false;

        // 面转线再合并
        Feature pOGRFeature_Old;
        Geometry pTempGeometry = null;
        Geometry pTempGeometryUnion = null;

        // 当前选择导出
        if (vecFIDs != null && !vecFIDs.isEmpty()) {
            for (Long vecFID : vecFIDs) {
                pOGRFeature_Old = pLayer.GetFeature(vecFID);
                pTempGeometry = pOGRFeature_Old.GetGeometryRef().GetLinearGeometry();
                pTempGeometry.AssignSpatialReference(pOGRSpatialReference_Source);
                if (0 == isSameCoordSystem) pTempGeometry.TransformTo(pOGRSpatialReference);

                if (pTempGeometryUnion == null) pTempGeometryUnion = pTempGeometry;
                else pTempGeometryUnion = pTempGeometryUnion.Union(pTempGeometry);
            }
        } else {
            //double xMin = envelope[0];
            //double xMax = envelope[1];
            //double yMin = envelope[2];
            //double yMax = envelope[3];
            if (0 != envelope.length && envelope[1] != envelope[0]) {
                pLayer.SetSpatialFilterRect(envelope[0], envelope[2], envelope[1], envelope[3]);
            }

            pOGRFeature_Old = pLayer.GetNextFeature();
            while (null != pOGRFeature_Old) {
                pTempGeometry = pOGRFeature_Old.GetGeometryRef();
                if (pTempGeometry.GetGeometryType() == wkbLineString)
                    pTempGeometry = pTempGeometry.GetLinearGeometry();
                else if (pTempGeometry.GetGeometryType() == wkbPolygon)
                    pTempGeometry = TransPoly2Line(pTempGeometry);
                pTempGeometry.AssignSpatialReference(pOGRSpatialReference_Source);
                if (0 == isSameCoordSystem) pTempGeometry.TransformTo(pOGRSpatialReference);

                if (pTempGeometryUnion == null)
                    pTempGeometryUnion = pTempGeometry;
                else
                    pTempGeometryUnion = pTempGeometryUnion.Union(pTempGeometry);
                pOGRFeature_Old = pLayer.GetNextFeature();
            }
        }

        // 获得Simply的距离
        double distanceValue = 0.0;
        if (null == pOGRSpatialReference) // 如果为空
        {
            double[] pTempOGREnvelope = new double[4];
            assert pTempGeometryUnion != null;
            pTempGeometryUnion.GetEnvelope(pTempOGREnvelope);
            if (pTempOGREnvelope[1] < 180) distanceValue = 0.00000001;
            else distanceValue = 0.01;
        } else if (1 == pOGRSpatialReference.IsProjected()) // 如果是Project的
        {
            distanceValue = 0.01;
        } else // 如果是Geo的
        {
            distanceValue = 0.00000001;
        }
        assert pTempGeometryUnion != null;
        pTempGeometryUnion = pTempGeometryUnion.Simplify(distanceValue);

        // 在ShapeFile文件中添加数据行
        Feature pOGRFeature_New;
        Geometry pOGRGeometry;
        FeatureDefn pOGRFeatureDefn = null;
        pOGRFeatureDefn = pOGRLayer.GetLayerDefn();

        int ogrGeometryType = pTempGeometryUnion.GetGeometryType();
//        ogrGeometryType = wkbFlatten(ogrGeometryType);

        if (ogrGeometryType == wkbMultiLineString) {
            Geometry pOGRGeometryCollectionTarget = pTempGeometryUnion;
            int geometryCount = pOGRGeometryCollectionTarget.GetGeometryCount();
            for (int i = 0; i < geometryCount; i++) {
                pOGRFeature_New = new Feature(pOGRFeatureDefn);
                pOGRGeometry = pOGRGeometryCollectionTarget.GetGeometryRef(i);
                pOGRFeature_New.SetGeometry(pOGRGeometry);
                pOGRLayer.CreateFeature(pOGRFeature_New);
                pOGRFeature_New.delete();//Frees the native resource associated to a DataSource object and close the file.
                pOGRFeature_New = null;
            }
        } else if (ogrGeometryType == wkbLineString || ogrGeometryType == wkbLineString25D) {
            pOGRFeature_New = new Feature(pOGRFeatureDefn);
            pOGRGeometry = pTempGeometryUnion;
            pOGRFeature_New.SetGeometry(pOGRGeometry);
            pOGRLayer.CreateFeature(pOGRFeature_New);
            pOGRFeature_New.delete();
            pOGRFeature_New = null;

        }
        pOGRDataSource.delete();

        // 销毁pTargetGeometrys
        pTempGeometryUnion.delete();
        pTempGeometryUnion = null;

        return true;
    }

    /**
     * 将单个面要素wkbPolygon转换为单个线要素wkbLineString
     *
     * @param pGeometry
     * @return
     */
    private static Geometry TransPoly2Line(Geometry pGeometry) {
        Geometry lGeometry = null;
        Geometry pTempGeometry = pGeometry.GetGeometryRef(0);
        int pGeometryPointsCount = pTempGeometry.GetPointCount();

        if (pGeometry.GetGeometryType() == wkbPolygon) {
            lGeometry = new Geometry(wkbLineString);
            for (int i = 0; i < pGeometryPointsCount; ++i) {
                lGeometry.AddPoint_2D(pTempGeometry.GetX(i), pTempGeometry.GetY(i));
            }
            lGeometry.AddPoint_2D(pTempGeometry.GetX(0), pTempGeometry.GetY(0));
        }
        return lGeometry;
    }

    /**
     * @return boolean                    是否成功
     * @brief ConvertPolygonToPolyline        检查面与面之间的拓扑问题：这个方法本来目的是面图层转换为线图层，虽然这个功能没有很好实现，但它也可以检查出面空隙
     * @param[in] tString polylinePath        转换后线图层文件路径
     * @param[in] OGRLayer* pLayer            要转换的面图层文件
     * @param[in] Envelope envelope            要转换数据的范围
     * @param[in] vector<long> vecFIDs        选中的要素ID列表
     * @param[in] pOGRSpatialReference        要转换的数据的空间参考(如果为空表示坐标系信息不变)
     * @author
     * @date
     */
    public static boolean CheckTopologyBetweenFaces(String polylinePath, Layer pLayer, double[] envelope, Vector<Long> vecFIDs, SpatialReference pOGRSpatialReference) {
        // 判断
        if (pLayer == null) return false;
        if (polylinePath.isEmpty()) return false;

        // 坐标系读取
        SpatialReference pOGRSpatialReference_Source = pLayer.GetSpatialRef();
        int isSameCoordSystem = 0;
        if (pOGRSpatialReference == null) {
            pOGRSpatialReference = pOGRSpatialReference_Source;
            isSameCoordSystem = 1;
        } else if (pOGRSpatialReference_Source != null) {
            isSameCoordSystem = pOGRSpatialReference_Source.IsSame(pOGRSpatialReference);
        }

        // 创建Shape文件
        String strDriverName = "ESRI Shapefile";
        org.gdal.ogr.Driver oDriver = ogr.GetDriverByName(strDriverName);
        if (oDriver == null) {
            System.out.println(polylinePath + "驱动不可用！\n");
            return false;
        }

        DataSource pOGRDataSource = oDriver.CreateDataSource(polylinePath);
        if (pOGRDataSource == null) {
            System.out.println("创建矢量文件【" + polylinePath + "】失败！\n");
            return false;
        }

        Layer pOGRLayer = pOGRDataSource.CreateLayer("boundary", pOGRSpatialReference, wkbLineString);
        if (pOGRLayer == null) return false;

        // 面转线再合并
        Feature pOGRFeature_Old;
        Geometry pTempGeometry = null;
        Geometry pTempGeometryUnion = null;

        // 当前选择导出
        if (vecFIDs != null && !vecFIDs.isEmpty()) {
            for (Long vecFID : vecFIDs) {
                pOGRFeature_Old = pLayer.GetFeature(vecFID);
                pTempGeometry = pOGRFeature_Old.GetGeometryRef().GetLinearGeometry();
                pTempGeometry.AssignSpatialReference(pOGRSpatialReference_Source);
                if (0 == isSameCoordSystem) pTempGeometry.TransformTo(pOGRSpatialReference);

                if (pTempGeometryUnion == null) pTempGeometryUnion = pTempGeometry;
                else pTempGeometryUnion = pTempGeometryUnion.Union(pTempGeometry);
            }
        } else {
            //double xMin = envelope[0];
            //double xMax = envelope[1];
            //double yMin = envelope[2];
            //double yMax = envelope[3];
            if (0 != envelope.length && envelope[1] != envelope[0]) {
                pLayer.SetSpatialFilterRect(envelope[0], envelope[2], envelope[1], envelope[3]);
            }

            pOGRFeature_Old = pLayer.GetNextFeature();
            while (null != pOGRFeature_Old) {
                pTempGeometry = pOGRFeature_Old.GetGeometryRef().GetLinearGeometry();
                pTempGeometry.AssignSpatialReference(pOGRSpatialReference_Source);
                if (0 == isSameCoordSystem) pTempGeometry.TransformTo(pOGRSpatialReference);

                if (pTempGeometryUnion == null) pTempGeometryUnion = pTempGeometry;
                else pTempGeometryUnion = pTempGeometryUnion.Union(pTempGeometry);

                pOGRFeature_Old = pLayer.GetNextFeature();
            }
        }

        // 获得Simply的距离
        double distanceValue = 0.0;
        if (null == pOGRSpatialReference) // 如果为空
        {
            double[] pTempOGREnvelope = new double[4];
            assert pTempGeometryUnion != null;
            pTempGeometryUnion.GetEnvelope(pTempOGREnvelope);
            if (pTempOGREnvelope[1] < 180) distanceValue = 0.00000001;
            else distanceValue = 0.01;
        } else if (1 == pOGRSpatialReference.IsProjected()) // 如果是Project的
        {
            distanceValue = 0.01;
        } else // 如果是Geo的
        {
            distanceValue = 0.00000001;
        }
        assert pTempGeometryUnion != null;
//        pTempGeometryUnion = pTempGeometryUnion.Simplify(0.1);

        // 在ShapeFile文件中添加数据行
        Feature pOGRFeature_New;
        Geometry pOGRGeometry;
        FeatureDefn pOGRFeatureDefn = null;
        pOGRFeatureDefn = pOGRLayer.GetLayerDefn();

        int ogrGeometryType = pTempGeometryUnion.GetGeometryType();
        ogrGeometryType = wkbFlatten(ogrGeometryType);

        if (ogrGeometryType == wkbMultiLineString) {
            Geometry pOGRGeometryCollectionTarget = pTempGeometryUnion;
            int geometryCount = pOGRGeometryCollectionTarget.GetGeometryCount();
            for (int i = 0; i < geometryCount; i++) {
                pOGRFeature_New = new Feature(pOGRFeatureDefn);
                pOGRGeometry = pOGRGeometryCollectionTarget.GetGeometryRef(i);
                pOGRFeature_New.SetGeometry(pOGRGeometry);
                pOGRLayer.CreateFeature(pOGRFeature_New);
                pOGRFeature_New.delete();//Frees the native resource associated to a DataSource object and close the file.
                pOGRFeature_New = null;
            }
        } else if (ogrGeometryType == wkbLineString) {
            pOGRFeature_New = new Feature(pOGRFeatureDefn);
            pOGRGeometry = pTempGeometryUnion;
            pOGRFeature_New.SetGeometry(pOGRGeometry);
            pOGRLayer.CreateFeature(pOGRFeature_New);
            pOGRFeature_New.delete();
            pOGRFeature_New = null;

        }
        pOGRDataSource.delete();

        // 销毁pTargetGeometrys
        pTempGeometryUnion.delete();
        pTempGeometryUnion = null;

        return true;
    }

    private static int wkbFlatten(int ogrGeometryType) {

        switch (ogrGeometryType) {
            case wkbMultiPolygon:
                return wkbMultiLineString;
            default:
                return wkbLineString;
        }
    }

    public static void CalAreaOfFace(Layer pLayer) {
        Feature feature = pLayer.GetNextFeature();
        while (null != feature) {
            double area = feature.GetGeometryRef().GetArea();
            feature.SetField("area", area);
            feature = pLayer.GetNextFeature();
        }
    }

    /**
     * 将shapefile转为GeoJson
     *
     * @param sourcePath
     * @param targetPath
     */
    public static void Shp2GeoJson(String sourcePath, String targetPath) {
//        // 支持中文路径
//        CPLSetConfigOption("GDAL_FILENAME_IS_UTF8", "NO");
//        // 属性表字段支持中文
//        CPLSetConfigOption("SHAPE_ENCODING", "");
        // 打开文件，读取数据
        org.gdal.ogr.Driver oDriver = ogr.GetDriverByName("ESRI Shapefile");
        if (null == oDriver)
            return;
        DataSource poSrcDS = oDriver.Open(sourcePath);
//        GDALDataset poSrcDS = GDALOpenEx(sourcePath, GDAL_OF_VECTOR, NULL, NULL, NULL);

        // 判断是否读取成功
        if (poSrcDS == null) {
            System.out.println("未能成功读取！");
            return;
        }

        // json 驱动
        org.gdal.ogr.Driver poDriver = ogr.GetDriverByName("GeoJSON");
//        GDALDriver poDriver = GetGDALDriverManager().GetDriverByName("GeoJSON");

        // 复制 shp 文件到指定输出的 json 文件
        DataSource poDstDS = poDriver.CopyDataSource(poSrcDS, targetPath);

        //释放内存
        if (poDstDS != null)
            poDstDS.delete();
    }
}
