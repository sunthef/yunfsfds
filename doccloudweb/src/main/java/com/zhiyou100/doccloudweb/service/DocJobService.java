package com.zhiyou100.doccloudweb.service;


import com.zhiyou100.doccloudweb.dao.DocJobRepository;
import com.zhiyou100.doccloudweb.dao.DocRepository;
import com.zhiyou100.doccloudweb.entity.Doc;
import com.zhiyou100.doccloudweb.entity.DocJobEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
*@ClassName:DocJobService
 @Description:TODO
 @Author:
 @Date:2018/11/1 9:56 
 @Version:v1.0
*/
@Service
public class DocJobService {

    @Autowired
    private DocJobRepository docJobRepository;

    public Optional<DocJobEntity> findById(String id){
        return docJobRepository.findById(id);
    }
    public DocJobEntity save(DocJobEntity docJobEntity){
        return docJobRepository.save(docJobEntity);
    }



}
