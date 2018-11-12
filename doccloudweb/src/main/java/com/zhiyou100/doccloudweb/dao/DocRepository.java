package com.zhiyou100.doccloudweb.dao;

import com.zhiyou100.doccloudweb.entity.Doc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//只需要集成接口
//高级自动化
@Repository
public interface DocRepository extends JpaRepository<Doc,Integer> {
    Optional<Doc> findByMd5(String md5);
}
