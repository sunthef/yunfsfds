package com.zhiyou100.docservicedeamon;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/*
*@ClassName:DocConvertDemo
 @Description:TODO
 @Author:
 @Date:2018/10/30 9:31 
 @Version:v1.0
*/
public class DocConvertDemo {
    public static void main(String[] args) throws IOException {
        String command="soffice --headless --invisible --convert-to html d:\\a.txt";
        String workDir="d:\\tmp\\.stage\\"+UUID.randomUUID().toString()+"\\";
        //新建一个文件
        File file = new File(workDir);
        //如果不存在则创建目录
        file.mkdirs();
        //执行命令和文件
        Process process = Runtime.getRuntime().exec(command,null,file);

        InputStream errorStream = process.getErrorStream();

        InputStream inputStream = process.getInputStream();

        String error = IOUtils.toString(errorStream);

        String result = IOUtils.toString(inputStream);

        System.out.println(error);

        System.out.println(result);


    }
}
