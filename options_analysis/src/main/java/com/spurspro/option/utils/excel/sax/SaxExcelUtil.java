package com.spurspro.option.utils.excel.sax;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * SaxExcelUtil
 * Created by Jimmy.Liu on 2018-10-18 08:35
 *
 * @since 2.01.06
 */
public class SaxExcelUtil {
    private static List<Map<String,String>> dataListT;
    private final int startRow;
    private final int endRow;
    private int currentRow = 0;
    private final String filename;
    private static Map<String, String> map;
    static char[] strChar ;
    /**
     * 构造方法
     */
    public SaxExcelUtil(String filename, int startRow, int endRow) throws Exception{
        dataListT = new ArrayList<>();
        if(StringUtils.isEmpty(filename)) throw new Exception("文件名不能空");
        this.filename = filename;
        this.startRow = startRow;
        this.endRow = endRow+1;
        processSheet();
    }
    /**
     * 指定获取第一个sheet
     *
     * @throws Exception
     */
    private void processSheet() throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> it = r.getSheetsData();
        while(it.hasNext()){
            map = null;
            InputStream sheet1 = it.next();
            InputSource sheetSource = new InputSource(sheet1);
            parser.parse(sheetSource);
            sheet1.close();
        }
    }
    /**
     * 加载sax 解析器
     * @param sst
     * @return
     * @throws SAXException
     */
    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "org.apache.xerces.parsers.SAXParser"
                );
        ContentHandler handler = new PagingHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private  class PagingHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private String index = null;
        private PagingHandler(SharedStringsTable sst) {
            this.sst = sst;
        }
        /**
         * 开始元素 （获取key 值）
         */
        @Override
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            if(name.equals("c")) {
                index = attributes.getValue("r");
                //判断是否是新的一行
                if(Pattern.compile("^A[0-9]+$").matcher(index).find()){
                    if(map!=null&&isAccess()&&!map.isEmpty()){
                        dataListT.add(map);
                    }
                    map = new LinkedHashMap<>();
                    currentRow++;
                }
                if(isAccess()){
                    String cellType = attributes.getValue("t");
                    if(cellType != null && cellType.equals("s")) {
                        nextIsString = true;
                    } else {
                        nextIsString = false;
                    }
                }
            }
            lastContents = "";
        }
        /**
         * 获取value
         */
        @Override
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            if(isAccess()){
                if(nextIsString) {
                    int idx = Integer.parseInt(lastContents);
                    lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                    nextIsString = false;
                }
                if(name.equals("v")) {
                    map.put(index, lastContents);
                }
            }

        }
        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if(isAccess()){
                lastContents += new String(ch, start, length);
            }
        }
        @Override
        public void endDocument ()throws SAXException{
            if(map!=null&&isAccess()&&!map.isEmpty()){
                dataListT.add(map);
            }
        }

    }
    private boolean isAccess(){
        if(currentRow>=startRow && currentRow<=endRow){
            return true;
        }
        return false;
    }
    /**
     * 获取数据 并且填补字段值为空的数据
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> getRowDataList() throws Exception{
        List<Map<String,String>> list = dataListT.subList(startRow, dataListT.size());
        if(!list.isEmpty()){
            Map<String,String> map = dataListT.get(0);
            List<String> com = data("A",map.size()-1);
            for(int i=0;i<list.size();i++){
                Map<String,String> returnMap = list.get(i);
                for(String str:com){
                    boolean flag = true;
                    for(Entry<String,String> entry:returnMap.entrySet()){
                        if(entry.getKey().contains(str)){
                            //有
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        //没有
                        returnMap.put(str+(i+2), null);
                    }
                }
            }
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        com.spurspro.option.utils.excel.sax.SaxExcelUtil reader = new com.spurspro.option.utils.excel.sax.SaxExcelUtil("D:\\dataImport\\zuodun-updated.xlsx",1,30000);
        System.out.println("行数: " + reader.getRowDataList().size());
        for (Map<String, String> row : reader.getRowDataList()) {
            List<String> rowValues = row.values().stream().collect(Collectors.toList());
            rowValues.get(70);
            System.out.println("列数: " + rowValues.size());
        }
    }
    /**
     * 封装数据
     * @param str
     * @param counts
     * @return
     */
    public static List<String> data(String str,int counts){
        List<String> list = new ArrayList<>();
        list.add(str);
        for(int i=0;i<counts;i++){
            strChar = str.toCharArray();
            jinwei(0);
            str = new String(strChar);
            list.add(str);
        }
        return list;
    }
    //数字进位
    public static void jinwei(int index){
        char a = 'A';
        int aint =(int)('A');
        if((strChar.length-1)-index>=0){
            int sc = (int)strChar[(strChar.length-1)-index];
            if(sc- 25 >= aint){
                jinwei(index+1);
                strChar[(strChar.length-1)-index] = a;
            }else{
                strChar[strChar.length-1-index] = (char)(sc+1);
            }
        }else{
            strChar[(strChar.length-1)-index+1] = a;
            StringBuilder str = new StringBuilder();
            str.append('A');
            str.append(strChar);
            strChar = str.toString().toCharArray();
        }
    }

    /**
     * SAX 模式解析excel表
     * @param path
     * @return
     * @throws Exception
     */
    public static List<String[]> parse(String path) throws Exception {
        File tempFile = new File(path);
        //传入一个路径产生流再将流传入工具类，返回解析对象，Excel的所有数据就被解析到List<String[]> 里面，遍历list任由你处置。
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);
            ExcelParser parse = new ExcelParser().parse(inputStream);
            List<String[]> rows = parse.getDatas();
            return rows;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {

            }
        }

    }
}
