package com.zhiyou100.doccloudweb.Util;

import com.google.common.io.Resources;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/*
*@ClassName:HdfsUtil
 @Description:TODO
 @Author:
 @Date:2018/10/29 17:17 
 @Version:v1.0
*/
public class HdfsUtil {
    //文档上传工具类
    public static void upload(byte[] src, String docName, String dst) throws IOException {
        //加载配置文件
        Configuration coreSiteConf = new Configuration();
        //家在配置
        coreSiteConf.addResource(Resources.getResource("core-site.xml"));
        //获取文件系统客户端对象
        FileSystem fileSystem = FileSystem.get(coreSiteConf);
            //创建文件
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path(dst + "/" + docName));
        //把文件写入hdfs
        fsDataOutputStream.write(src);
        fsDataOutputStream.close();
        fileSystem.close();
    }
    public static void copyFromLocal(String src,String dst) throws IOException {
        Configuration coreSiteConf = new Configuration();
        coreSiteConf.addResource(Resources.getResource("core-site.xml"));
        //获取文件系统客户端对象
        FileSystem fileSystem = FileSystem.get(coreSiteConf);
        fileSystem.copyFromLocalFile(new Path(src),new Path(dst));
        fileSystem.close();
    }
    //文件下载
    public static void download(String input,String output) throws IOException {
        Configuration coreSiteConf = new Configuration();
        coreSiteConf.addResource(Resources.getResource("core-site.xml"));
        //获取文件系统客户端对象
        FileSystem fileSystem = FileSystem.get(coreSiteConf);
        //把文件从输出拷贝到输入
        fileSystem.copyToLocalFile(new Path(input),new Path(output));
        fileSystem.close();
    }

    public static void main(String[] args) throws IOException {
        String tmpWorkDirPath = "/tmp/docjobdaemon/" + UUID.randomUUID().toString() + "/";
        //创建临时工作目录
        File tmpWorkDir = new File(tmpWorkDirPath);
        tmpWorkDir.mkdirs();
        String input="hdfs://192.168.79.114:9000//doccloud/2018-10-31/a446e941-0ced-4f3f-ba6f-096c7e71ffed/进入IT企业必读的324个JAVA面试题.pdf";

        download(input,tmpWorkDirPath);
    }

}
