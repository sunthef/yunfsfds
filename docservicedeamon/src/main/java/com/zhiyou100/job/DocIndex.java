package com.zhiyou100.job;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

//用来封装索引字段
@Data
public class DocIndex {
@Field
    private String id;
    @Field
    private String docName;
    @Field
    private  String url;
    @Field
    private  String docContent;
    @Field
    private  String docType;



}
