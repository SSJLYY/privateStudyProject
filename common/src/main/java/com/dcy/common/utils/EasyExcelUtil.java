package com.dcy.common.utils;

import cn.afterturn.easypoi.entity.vo.BigExcelConstants;
import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.entity.vo.TemplateExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.ModelMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/5/7 9:28
 */
public class EasyExcelUtil {

    private static final String HSSF = ".xls";
    private static final String XSSF = ".xlsx";

    /**
     * 注解导出
     *
     * @param dataList 数据
     * @param aClass   类对象
     * @param params   excel参数
     * @param fileName 文件名称
     * @param modelMap
     * @param request
     * @param response
     */
    public static void normalExcel(List<?> dataList,
                                   Class<?> aClass,
                                   ExportParams params,
                                   String fileName,
                                   ModelMap modelMap,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        modelMap.put(NormalExcelConstants.DATA_LIST, dataList);
        modelMap.put(NormalExcelConstants.CLASS, aClass);
        modelMap.put(NormalExcelConstants.PARAMS, params);
        modelMap.put(NormalExcelConstants.FILE_NAME, fileName);
        PoiBaseView.render(modelMap, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    /**
     * 注解导出
     *
     * @param dataList 数据
     * @param aClass   类对象
     * @param params   excel参数
     * @param fileName 文件名称
     * @param response
     */
    @Deprecated
    public static void normalExcel(List<?> dataList,
                                   Class<?> aClass, ExportParams params,
                                   String fileName, HttpServletResponse response) {

        try {
            Workbook workbook = ExcelExportUtil.exportExcel(params, aClass, dataList);
            if (workbook instanceof HSSFWorkbook) {
                fileName += HSSF;
            } else {
                fileName += XSSF;
            }
            ServletOutputStream outputStream = response.getOutputStream();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, StrUtil.format("attachment;filename={}", URLUtil.encode(fileName, CharsetUtil.CHARSET_UTF_8)));
            response.setContentType(CharsetUtil.UTF_8);
            workbook.write(outputStream);
            IoUtil.close(outputStream);
            IoUtil.close(workbook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模板导出
     *
     * @param map
     * @param params
     * @param fileName
     * @param modelMap
     * @param request
     * @param response
     */
    public static void templateExcel(Map<String, Object> map, TemplateExportParams params, String fileName, ModelMap modelMap, HttpServletRequest request,
                                     HttpServletResponse response) {
        modelMap.put(TemplateExcelConstants.FILE_NAME, fileName);
        modelMap.put(TemplateExcelConstants.PARAMS, params);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        PoiBaseView.render(modelMap, request, response,
                TemplateExcelConstants.EASYPOI_TEMPLATE_EXCEL_VIEW);
    }

    /**
     * map 导出
     *
     * @param list
     * @param entity
     * @param params
     * @param fileName
     * @param modelMap
     * @param request
     * @param response
     */
    public static void mapExcel(List<Map<String, Object>> list, List<ExcelExportEntity> entity, ExportParams params, String fileName, ModelMap modelMap, HttpServletRequest request,
                                HttpServletResponse response) {
        modelMap.put(MapExcelConstants.MAP_LIST, list);
        modelMap.put(MapExcelConstants.ENTITY_LIST, entity);
        modelMap.put(MapExcelConstants.PARAMS, params);
        modelMap.put(MapExcelConstants.FILE_NAME, fileName);
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);
    }

    /**
     * 大数据导出
     * <p>http://doc.wupaas.com/docs/easypoi/easypoi-1c10lbsojh62f</p>
     *
     * @param aClass
     * @param params
     * @param dataParams
     * @param excelExportServer
     * @param modelMap
     * @param request
     * @param response
     */
    public static void bigExcel(Class<?> aClass, ExportParams params, Map<String, Object> dataParams, IExcelExportServer excelExportServer, ModelMap modelMap, HttpServletRequest request,
                                HttpServletResponse response) {
        modelMap.put(BigExcelConstants.CLASS, aClass);
        modelMap.put(BigExcelConstants.PARAMS, params);
        //就是我们的查询参数,会带到接口中,供接口查询使用
        modelMap.put(BigExcelConstants.DATA_PARAMS, dataParams);
        modelMap.put(BigExcelConstants.DATA_INTER, excelExportServer);
        PoiBaseView.render(modelMap, request, response, BigExcelConstants.EASYPOI_BIG_EXCEL_VIEW);
    }
}
