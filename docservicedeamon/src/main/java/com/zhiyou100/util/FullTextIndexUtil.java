package com.zhiyou100.util;


import com.zhiyou100.job.DocIndex;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;


import java.io.IOException;



public class FullTextIndexUtil   {
    static SolrClient solrClient;
    static final String SOLR_URL="http://master:8983/solr/doccloud";
    static {

        solrClient = new HttpSolrClient(SOLR_URL);

    }
    public static void add(DocIndex docIndex) throws IOException, SolrServerException {
        solrClient.addBean(docIndex);
        solrClient.commit();
    }

    public static void main(String[] args) throws IOException, SolrServerException {
    /*     DocIndex docIndex = new DocIndex();*/

    /*    docIndex.setDocContent("中国人当自强");
        docIndex.setId(1);
    docIndex.setDocName("英雄");
        docIndex.setUrl("hhh");
        docIndex.setDocType("docx");
        add(docIndex);*/
        SolrQuery params = new SolrQuery();
        System.out.println("======================query=============================");
        params.set("q", "docContent:中国* or id:bigdata");
        params.set("start", 0);
        params.set("rows", 20);
        params.set("sort", "id asc");
        SolrDocumentList docs = query(params);
        for (SolrDocument doc : docs) {
            // 多值查询
            @SuppressWarnings("unchecked")

            String id = (String) doc.getFieldValue("id");
            String docName = (String) doc.getFieldValue("docName");
            String docContent = String.valueOf(doc.getFieldValue("docContent"));
            String docType = (String) doc.getFieldValue("docType");

            System.out.println("id:"+id+"\t name:" + docName + "\t description:"+docContent+"\t price:"+docType );
        }
    }

    private static SolrDocumentList query(SolrQuery params) {

        try {
            QueryResponse rsp = solrClient.query(params);
            SolrDocumentList docs = rsp.getResults();
            System.out.println("查询内容:" + params);
            System.out.println("文档数量：" + docs.getNumFound());
            System.out.println("查询花费时间:" + rsp.getQTime());

            System.out.println("------query data:------");
            return docs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
