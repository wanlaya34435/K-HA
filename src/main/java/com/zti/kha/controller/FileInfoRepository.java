package com.zti.kha.controller;

import com.zti.kha.model.Common.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileInfoRepository extends MongoRepository<FileInfo, String> {

    public List<FileInfo> findAll();
    public FileInfo findByFileName(String fileName);
    public List<FileInfo> findByFileType(String fileType);

}

