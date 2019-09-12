package com.cn.xuyhproject.Excel2Data;

import java.util.List;

/**
 * @Author: cleve
 * @Description: 测试类
 * @Date: 2019/9/12
 * @Version:
 */

public class ImportExcelUtilTest {
    public static void main(String[] args) throws Exception{
        List<List<String[]>> list = ImportExcelUtil.readerExcel("E:\\QQDownload\\603559825\\FileRecv\\201706-专资平台票房对账测试1-5-2.xlsx", "");
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
