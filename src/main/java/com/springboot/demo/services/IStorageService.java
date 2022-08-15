package com.springboot.demo.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    public String storeFile(MultipartFile file);
    public Stream<Path> loadAllFiles();
    public byte[] readFileContent(String fileNames);
    public void deleteAllFiles();
}
