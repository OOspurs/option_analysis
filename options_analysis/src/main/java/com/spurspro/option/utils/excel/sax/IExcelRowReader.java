package com.spurspro.option.utils.excel.sax;

import java.util.List;

/**
 * IExcelRowReader
 * Created by Jimmy.Liu on 2018-10-17 10:12
 *
 * @since 2.01.06
 */
public interface IExcelRowReader {
    /**
     * 业务逻辑实现方法
     *
     * @param sheetIndex
     * @param curRow
     * @param rowlist
     */
    void getRows(int sheetIndex, int curRow, List<String> rowlist);
}
