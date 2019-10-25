package com.spurspro.option.utils.excel.sax;

import java.util.ArrayList;
import java.util.List;

/**
 * 名称: ExcelReaderUtil.java<br>
 * 描述: <br>
 * 类型: JAVA<br>
 *
 * @since 2.01.06
 */
public class ExcelReaderUtil {
    // excel2003扩展名
    public static final String EXCEL03_EXTENSION = ".xls";
    // excel2007扩展名
    public static final String EXCEL07_EXTENSION = ".xlsx";

    /**
     * 读取Excel文件，可能是03也可能是07版本
     *
     * @param fileName
     * @throws Exception
     */
    public static List<String> readExcel(IExcelRowReader reader, String fileName) throws Exception {
        List<String> rowList = new ArrayList<>();
        // 处理excel2003文件
        if (fileName.endsWith(EXCEL03_EXTENSION)) {
            ExcelXlsReader exceXls = new ExcelXlsReader();
            exceXls.setRowReader(reader);
            exceXls.process(fileName);
            rowList = exceXls.getRowlist();
            // 处理excel2007文件
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {
            ExcelXlsxReader exceXlsx = new ExcelXlsxReader();
            exceXlsx.setRowReader(reader);
            exceXlsx.process(fileName);
            rowList = exceXlsx.getRowlist();
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
        return rowList;
    }

    /**
     * 测试
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        IExcelRowReader rowReader = new ExcelRowReader();
        List<String> rowList = ExcelReaderUtil.readExcel(rowReader, "D:\\dataImport\\tongluowang\\medik-buy.xlsx");
        System.out.println("行数: " + rowList.size());
    }
}
