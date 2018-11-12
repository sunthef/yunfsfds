package com.zhiyou100.util;

import lombok.extern.slf4j.Slf4j;
 ;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;


import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/*
*@ClassName:PdfUtil
 @Description:TODO
 @Author:
 @Date:2018/10/30 12:15 
 @Version:v1.0
*/
@Slf4j
public class PdfUtil {

    //获取页码
    public static int getNumberOfPages(String filePath) throws IOException, InterruptedException {
        int retry=0;
        while ((retry++)<70){
            File file = new File(filePath);
            if (!file.exists()){
                log.info("wait filesystem load file,retry time:{}",retry);
                Thread.sleep(2000);
            }else{
                PDDocument pdDocument = PDDocument.load(file);
                int pages = pdDocument.getNumberOfPages();
                pdDocument.close();
                return pages;
            }
        }
        throw new IOException("can not get "+filePath+"page");
    }

    //提取pdf中的文本
    public static  String getContent(String filePath) throws IOException {

        PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File(filePath), "rw"));
        pdfParser.parse();
        PDDocument pdDocument = pdfParser.getPDDocument();
        String text = new PDFTextStripper().getText(pdDocument);
        pdDocument.close();

        return text;
    }
    //获取缩略图

    public static void getThumbnails(String filePath,String outPath)throws IOException {
        // 利用PdfBox生成图像
        PDDocument pdDocument = PDDocument.load(new File(filePath));
        PDFRenderer renderer = new PDFRenderer(pdDocument);

// 构造图片
        BufferedImage img_temp = renderer.renderImageWithDPI(0, 30, ImageType.RGB);
// 设置图片格式
        Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix("jpg");
// 将文件写出
        ImageWriter writer =  it.next();
        ImageOutputStream imageout = ImageIO.createImageOutputStream(new FileOutputStream(outPath));
        writer.setOutput(imageout);
        writer.write(new IIOImage(img_temp, null, null));
        img_temp.flush();
        imageout.flush();
        imageout.close();
        pdDocument.close();

        //getPage();

    }

    public static void main(String[] args) throws IOException {
        String content = getContent("D:\\\\tmp\\\\docjobdaemon\\\\79d0ceba-87b2-4fc3-a7ce-e9eaa0466b4b/进入IT企业必读的324个JAVA面试题.pdf");
        System.out.println(content);
    }
}
