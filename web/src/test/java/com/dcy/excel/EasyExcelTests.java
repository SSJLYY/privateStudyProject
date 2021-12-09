package com.dcy.excel;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/18 9:51
 */
public class EasyExcelTests {

    @Test
    void importExcel() {
        String fileName = "D:\\商家信息.xlsx";
        final BufferedInputStream inputStream = FileUtil.getInputStream(fileName);

        try {
            // 构建导入参数
            ImportParams params = new ImportParams();
            params.setDataHandler(new ExcelDataHandlerDefaultImpl<Map<String, Object>>() {

                @Override
                public void setMapValue(Map<String, Object> map, String originKey, Object value) {
                    map.put(getRealKey(originKey), value != null ? value.toString() : null);
                }

                /**
                 * 中文key转换英文
                 * @param originKey
                 * @return
                 */
                private String getRealKey(String originKey) {
                    if (originKey.equals("序号")) {
                        return "id";
                    }
                    if (originKey.equals("名称")) {
                        return "name";
                    }
                    return originKey;
                }
            });
            // 执行导入
            final ExcelImportResult<Map<String, Object>> importResult = ExcelImportUtil.importExcelMore(inputStream, Map.class, params);
            final List<Map<String, Object>> list = importResult.getList();
            list.forEach(stringObjectMap -> {
                // map 转换 bean
                final Shop shop = BeanUtil.toBean(stringObjectMap, Shop.class);
                System.out.println(shop);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
