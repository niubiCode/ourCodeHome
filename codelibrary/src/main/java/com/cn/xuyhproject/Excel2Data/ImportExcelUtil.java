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
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 山人
 * @Description: Excel导入，支持2007以上版本，文件后缀.xlsx的文档，支持数据量大的处理
 * @Date: 2017/7/26
 * @Version:
 */

public class ImportExcelUtil {

    private OPCPackage xlsxPackage;
    private int minColumns;
    private PrintStream output;
    private String sheetName;

    public ImportExcelUtil(OPCPackage pkg, PrintStream output, String sheetName, int minColumns) {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
        this.sheetName = sheetName;
    }

    public static ImportExcelUtil getInstance(OPCPackage pkg, String sheetName, int minColumns){
        return new ImportExcelUtil(pkg, System.out, sheetName, minColumns);
    }

    private static List<List<String[]>> readerExcel(String path, String sheetName, int minColumns) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        OPCPackage opcPackage = OPCPackage.open(path, PackageAccess.READ);
        ImportExcelUtil importExcel = getInstance(opcPackage, sheetName, minColumns);
        List<List<String[]>> list = importExcel.process();
        opcPackage.close();
        return list;
    }

    public List<List<String[]>> process() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        List<List<String[]>> pDataList = new ArrayList();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        List<String[]> list = null;
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            String sheetNameTemp = iter.getSheetName();
            if (null != this.sheetName && "".equals(this.sheetName)) {
                if(this.sheetName.equals(sheetNameTemp)){
                    list = processSheet(styles, strings, stream);
                    stream.close();
                    ++index;
                }
            } else {
                list = processSheet(styles, strings, stream);
                stream.close();
                ++index;
            }
            pDataList.add(list);
        }
        return pDataList;
    }
    public List<String[]> processSheet(StylesTable styles, ReadOnlySharedStringsTable strings, InputStream sheetInputStream) throws IOException, ParserConfigurationException, SAXException {

        InputSource sheetSource = new InputSource(sheetInputStream);
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader sheetParser = saxParser.getXMLReader();
        MyXSSFSheetHandler handler = new MyXSSFSheetHandler(styles, strings, this.minColumns, this.output);
        sheetParser.setContentHandler(handler);
        sheetParser.parse(sheetSource);
        return handler.getRows();
    }
    public static void main(String[] args) throws Exception{
        List<List<String[]>> list = readerExcel("E:\\QQDownload\\603559825\\FileRecv\\201706-专资平台票房对账测试1-5 -2.xlsx", "专资数据-票房导入模板", 13);
        System.out.println(list.get(0).size());
    }
}
