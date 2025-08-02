package com.example.jewelry_management.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeImage(MultipartFile file, String subFolder);

    void deleteFileByUrl(String url);

    boolean isValidImage(MultipartFile file);
}
