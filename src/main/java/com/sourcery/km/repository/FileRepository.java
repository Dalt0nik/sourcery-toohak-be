package com.sourcery.km.repository;

import com.sourcery.km.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileRepository {

    @Insert("""
            INSERT INTO files (id, file_type, created_at, created_by, is_temporary) VALUES
            (#{id}, #{fileType}, #{createdAt}, #{createdBy}, #{isTemporary})
            """)
    void insertNewFile(File file);
}
