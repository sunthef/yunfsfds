package com.zhiyou100.doccloudweb.dao;


import com.zhiyou100.doccloudweb.entity.DocJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
*@ClassName:DocJobRepository
 @Description:TODO
 @Author:
 @Date:2018/11/1 9:51 
 @Version:v1.0
*/
@Repository
public interface DocJobRepository extends JpaRepository<DocJobEntity,String> {
}
