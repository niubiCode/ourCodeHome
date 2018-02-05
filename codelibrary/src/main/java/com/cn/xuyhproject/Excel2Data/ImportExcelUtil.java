package com.cn.xuyhproject.Excel2Data;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuyh
 * @Description: Excel导入，支持2007以上版本，文件后缀.xlsx的文档，支持数据量大的处理
 * @Date: 2018/2/5
 * @Version:
 */
public class ImportExcelUtil {

    private OPCPackage  xlsxPackage = null;
    private String      sheetName   = "";

    public ImportExcelUtil(OPCPackage pkg, String sheetName) {
        this.xlsxPackage = pkg;
        this.sheetName   = sheetName;
    }

    public static ImportExcelUtil getInstance(OPCPackage pkg, String sheetName){
        return new ImportExcelUtil(pkg, sheetName);
    }

    /**
     * 读取xlsx文件
     * @param path 文件路径
     * @param sheetName sheet页名称
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static List<List<String[]>> readerExcel(String path, String sheetName) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException{
        return readerExcel(new File(path), sheetName);
    }

    /**
     * 读取xlsx文件
     * @param file File对象
     * @param sheetName sheet页名称
     * @return
     * @throws IOException
     * @throws OpenXML4JException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public static List<List<String[]>> readerExcel(File file, String sheetName) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        OPCPackage              opcPackage      = OPCPackage.open(file, PackageAccess.READ);
        ImportExcelUtil         importExcel     = getInstance(opcPackage, sheetName);
        List<List<String[]>>    excelDatelist   = importExcel.process();
        opcPackage.close();
        return excelDatelist;
    }
    private List<List<String[]>> process() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        XSSFReader                  xssfReader    = new XSSFReader(this.xlsxPackage);
        List<String[]>              sheetDatalist = new ArrayList<>();
        List<List<String[]>>        excelDataList = new ArrayList<>();
        ReadOnlySharedStringsTable  strings       = new ReadOnlySharedStringsTable(this.xlsxPackage);

        StylesTable              styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter   = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iter.hasNext()) {
            InputStream stream          = iter.next();
            String      sheetNameTemp   = iter.getSheetName();
            if (null != this.sheetName && !"".equals(this.sheetName)) {
                if(this.sheetName.equals(sheetNameTemp)){
                    sheetDatalist = processSheet(styles, strings, stream);
                    stream.close();
                }
            } else {
                sheetDatalist = processSheet(styles, strings, stream);
                stream.close();
            }
            excelDataList.add(sheetDatalist);
        }
        return excelDataList;
    }
    private List<String[]> processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {
        InputSource        sheetSource  = new InputSource(sheetInputStream);
        MyXSSFSheetHandler handler      = new MyXSSFSheetHandler(styles, strings);

        SAXParserFactory   saxFactory   = SAXParserFactory.newInstance();
        SAXParser          saxParser    = saxFactory.newSAXParser();
        XMLReader          sheetParser  = saxParser.getXMLReader();
        handler.setDataFormat("yyyyMMdd");
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
        return handler.getRows();
    }
//    public static void main(String[] args) throws Exception{
//        List<List<String[]>> list = readerExcel("E:\\QQDownload\\603559825\\FileRecv\\201706-专资平台票房对账测试1-5 -2.xlsx", "专资数据-票房导入模板");
//        System.out.println(list.size()+"---"+list.get(0).size());
//    }
    public static void main(String[] args) throws Exception{
        List<List<String[]>> list = readerExcel("E:\\QQDownload\\603559825\\FileRecv\\201706-专资平台票房对账测试1-5-2.xlsx", "");
        System.out.println(list.size()+"---"+list.get(0).size());
        for(int i =0;i<list.size();i++){
            System.out.println("------------第"+ i+1 +"页数据------------");
            List<String[]> sheetDataList = list.get(i);
            for(int j=0;j<sheetDataList.size();j++){
                String[] rowData = sheetDataList.get(j);
                for(int m=0;m<rowData.length;m++){
                    System.out.print("|"+rowData[m]+"|");
                }
                System.out.println();
            }
        }
    }
}
