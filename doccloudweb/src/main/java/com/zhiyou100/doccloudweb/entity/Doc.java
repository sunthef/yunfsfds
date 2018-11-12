package com.zhiyou100.doccloudweb.entity;

import lombok.Data;


import javax.persistence.*;
import java.util.Date;

//jpa
@Entity
//映射数据库中的表
//此注解表示实体映射到数据库
@Table(name="doc")
@Data
public class Doc {

        //固定写法
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private  int id;
    @Column(name="md5")//如果数据苦字段与entity实体一样
    // 则不用加侧注解

    private String md5;
    @Column(name = "doc_name")
    private String docName;
    @Column(name = "doc_type")
    private String docType;
    @Column(name = "doc_status")
    private String docStatus;
    @Column(name = "doc_size")
    private Integer docSize;
    @Column(name = "doc_path")
    private String docPath;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "doc_create_time")
    private Date docCreateTime;
    @Column(name = "doc_comment")
    private String docComment;
    @Column(name = "doc_permission")
    private String docPermission;


    @Column(name="num_of_page")
    private int numOfPage;
}
