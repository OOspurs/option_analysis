package com.spurspro.option.utils.excel.sax;

import java.util.List;

/**
 * ExcelRowReader
 * Created by Jimmy.Liu on 2018-10-17 10:13
 *
 * @since 2.01.06
 */
public class ExcelRowReader implements IExcelRowReader {

    @Override
    public void getRows(int sheetIndex, int curRow, List<String> rowlist) {
        System.out.print(curRow+" ");
        for (int i = 0; i < rowlist.size(); i++) {
            System.out.print(rowlist.get(i)==""?"*":rowlist.get(i) + " ");
        }
        System.out.println();
    }

}
