package com.sourcery.km.repository;

import com.sourcery.km.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileRepository {

    @Insert("""
            INSERT INTO files (id, file_type, file_url, created_at) VALUES
            (#{id}, #{fileType}, #{fileUrl}, #{createdAt})
            """)
    void insertNewFile(File file);
}
