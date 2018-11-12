package com.zhiyou100.job;

import com.zhiyou100.job.callback.DocJobCallback;
import com.zhiyou100.job.callback.DocJobResponse;
import com.zhiyou100.util.FullTextIndexUtil;
import com.zhiyou100.util.HdfsUtil;
import com.zhiyou100.util.PdfUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

/*
*@ClassName:DocJobHandler
 @Description:TODO
 @Author:
 @Date:2018/10/30 17:09 
 @Version:v1.0
*/
@Slf4j
public class DocJobHandler implements Runnable {
    private DocJob docJob;

    public DocJobHandler(DocJob docJob) {
        this.docJob = docJob;
        log.info("start to deal job {}",docJob);
    }

    public void run() {
        //得到输入路径
        String input = docJob.getInput();
        String tmpWorkDirPath = "\\tmp\\docjobdaemon\\" + UUID.randomUUID().toString() + "/";
        //创建临时工作目录
        File tmpWorkDir = new File(tmpWorkDirPath);
        tmpWorkDir.mkdirs();
        //下载文件到临时目录
        try {
            HdfsUtil.download(input, tmpWorkDirPath);
            log.info("download file to {}", tmpWorkDirPath);
            //step1：文档转换成html
            String fileName = docJob.getFileName();
            convertToHtml(fileName, tmpWorkDir);


            //step2 转换成pdf
            convertToPdf(fileName, tmpWorkDir);
            Thread.sleep(2000);
            log.info("sleep for a while");


            //pdf路径
            String pdfPath = tmpWorkDirPath + docJob.getFileName().substring(0, docJob.getFileName().indexOf(".")) + ".pdf";
            //html路径
            String htmlPath = tmpWorkDirPath + docJob.getFileName().substring(0, docJob.getFileName().indexOf(".")) + ".html";
            //缩略图路径
            String thumbnailsPath = tmpWorkDirPath + docJob.getFileName().substring(0, docJob.getFileName().indexOf(".")) + "png";
            log.info("pdfPath :{}", pdfPath);
            //step3 提取页码
            int numberOfPages = PdfUtil.getNumberOfPages(pdfPath);
            //step4 提取首页缩略图
            PdfUtil.getThumbnails(pdfPath, thumbnailsPath);
            //step5 利用solr建立索引
            //提取文档内容
            String content = PdfUtil.getContent(pdfPath);
            //建立稳当对象索引
            DocIndex docIndex = new DocIndex();
            docIndex.setId(docJob.getId());
            docIndex.setUrl(docJob.getInput() + "/" + docJob.getFileName());
            docIndex.setDocName(docJob.getFileName());
            docIndex.setDocContent(content);
            String[] strings = docJob.getFileName().split("\\.");
            docIndex.setDocType(strings[1]);
            FullTextIndexUtil.add(docIndex);
            //step6 上传结果
            HdfsUtil.copyFromLocal(htmlPath, docJob.getOutput());
            log.info("upload {} to hdfs:", htmlPath);
            HdfsUtil.copyFromLocal(pdfPath, docJob.getOutput());
            log.info("upload {} to hdfs:", pdfPath);
            HdfsUtil.copyFromLocal(thumbnailsPath, docJob.getOutput());
            log.info("upload {} to hdfs:", thumbnailsPath);
            //step7 清理临时目录
            log.info("clear tmpworkdir : {}", tmpWorkDir.getAbsolutePath());
            //java 自带file不能递归删除 使用commons工具类
            FileUtils.deleteDirectory(tmpWorkDir);
            //step8 任务成功回调
            reportDocJob(numberOfPages, docJob, true, "success");
        } catch (Exception e) {
            //失败处理
            log.error("docjob {} failed deal to {}", docJob, e);
            try {
                FileUtils.deleteDirectory(tmpWorkDir);
                reportDocJob(0, docJob, false, e.getMessage());
            } catch (IOException e1) {
                log.error("failed: docjob  {}  deal to:{}", docJob, e1.getMessage());
                //e1.printStackTrace();
            }
        }


    }

    private void reportDocJob(int numberOfPages, DocJob docJob,  boolean success, String message) throws IOException {

        DocJobResponse docJobResponse = new DocJobResponse();
        docJobResponse.setDocJobId(docJob.getId());
        docJobResponse.setSuccess(success);
        docJobResponse.setMessage(message);
        docJobResponse.setFinishTime(System.currentTimeMillis());
        docJobResponse.setNumOfPage(numberOfPages);
        if (!success) {
            docJobResponse.setRetryTime(1);
        }
        DocJobCallback jobCallback = RPC.getProxy(DocJobCallback.class, DocJobCallback.versionID, new InetSocketAddress("127.0.0.1", 8877), new Configuration());
        log.info("report job:{} to web : {}", docJob, docJobResponse);
        jobCallback.reportDocJob(docJobResponse);
    }

    private void convertToPdf(String fileName, File tmpWorkDir) throws IOException {
        String command = "soffice --headless --invisible --convert-to pdf " + fileName;
        Process process = Runtime.getRuntime().exec(command, null, tmpWorkDir);
        System.out.println(IOUtils.toString(process.getInputStream()));
        System.out.println(IOUtils.toString(process.getErrorStream()));

    }

    private void convertToHtml(String fileName, File tmpWorkDir) throws IOException {
        String command = "soffice --headless --invisible --convert-to html " + fileName;
        Process process = Runtime.getRuntime().exec(command, null, tmpWorkDir);
        System.out.println(IOUtils.toString(process.getInputStream()));
        System.out.println(IOUtils.toString(process.getErrorStream()));


    }

}
