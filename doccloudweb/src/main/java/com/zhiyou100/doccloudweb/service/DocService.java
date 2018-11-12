package com.zhiyou100.doccloudweb.service;

import com.zhiyou100.doccloudweb.dao.DocRepository;
import com.zhiyou100.doccloudweb.entity.Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DocService {
    @Autowired
  private   DocRepository docRepository;

/*
public  Doc fingByid(Integer id){
    //optional 只有一个元素的集合
    Optional<Doc> doc = docRepository.findById(id);
    if (doc.isPresent()){
        return doc.get();
    }
    return null;
}*/
    public  Optional<Doc> findByMd5(String md5) {
        //optional 只有一个元素的集合
            return   docRepository.findByMd5(md5);

}

 /*   //删
    public void delete(int id){
    docRepository.deleteById(id);

    }*/
//增
 public Doc save(Doc docEntity) {
     return docRepository.save(docEntity);

 }
//校验
public Optional<Doc> findbyId(int id){
    return docRepository.findById(id);
}


}
