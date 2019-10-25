package com.spurspro.option.utils.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * CSV工具类
 *
 * @author：  Rodge
 * @time：    2017年12月25日 下午10:02:08
 * @version： V1.0.0
 */
public final class CSVUtils {

    /** 日志对象 **/
    private static final Logger logger = LoggerFactory.getLogger(CSVUtils.class);

    /**
     * 将数据写入CSV
     *
     * @param fileName
     * @param data
     */
    public static void writeCSV(final String fileName, final List<String[]> data) {
        CSVWriter writer = null;
        try {
            // 校验文件后缀名
            String csvName = StringUtils.join(FilenameUtils.getFullPath(fileName), FilenameUtils.getBaseName(fileName));
            // 创建文件所在目录
            File file = new File(FilenameUtils.getFullPath(csvName));
            if (!file.exists())
                file.mkdirs();

            writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csvName), StandardCharsets.UTF_8.name()), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            writer.writeAll(data);
        } catch (Exception e) {
            logger.error("将数据写入CSV出错", e);
        } finally {
            if (null != writer) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    logger.error("关闭文件输出流出错", e);
                }
            }
        }
    }

    /**
     * 读取CSV数据
     *
     * @param fileName
     * @return
     */
    public static List<String[]> readCSV(final String fileName) {
        List<String[]> list = null;
        CSVReader reader = null;
        try {
            reader = new CSVReaderBuilder(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8.name())).build();

            list = reader.readAll();
        } catch (Exception e) {
            logger.error("读取CSV数据出错", e);
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("关闭文件输入流出错", e);
                }
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        String filePath = "/home/oliver/jimmy/sources/github/options_analysis/src/main/resources/data/50etf.csv";
        List<String[]> datas = CSVUtils.readCSV(filePath);
        logger.info("data lengths: " + datas.size());
    }

}