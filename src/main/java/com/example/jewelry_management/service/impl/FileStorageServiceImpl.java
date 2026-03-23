package com.example.jewelry_management.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.jewelry_management.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Cloudinary cloudinary;

    private final List<String> VALID_IMAGE_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/webp"
    );

    public FileStorageServiceImpl(
            @Value("${CLOUDINARY_NAME}") String cloudName,
            @Value("${CLOUDINARY_API_KEY}") String apiKey,
            @Value("${CLOUDINARY_API_SECRET}") String apiSecret
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    @Override
    public String storeImage(MultipartFile file, String subFolder) {
        if (file == null || file.isEmpty()) {
            log.warn("File ảnh rỗng hoặc null");
            return null;
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", subFolder)
            );

            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("Đã upload ảnh lên Cloudinary: {}", secureUrl);
            return secureUrl;

        } catch (IOException e) {
            log.error("Lỗi khi upload ảnh lên Cloudinary: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi upload ảnh lên Cloudinary!", e);
        }
    }

    @Override
    public void deleteFileByUrl(String url) {
        if (url == null || url.isBlank()) {
            log.warn("URL ảnh không hợp lệ: {}", url);
            return;
        }

        try {
            String publicId = extractPublicId(url);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Đã xóa ảnh trên Cloudinary với publicId: {}", publicId);
        } catch (IOException e) {
            log.error("Lỗi khi xóa ảnh trên Cloudinary: {}. Lý do: {}", url, e.getMessage(), e);
        }
    }

    @Override
    public boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        return VALID_IMAGE_TYPES.contains(file.getContentType());
    }

    /**
     * Extracts the Cloudinary public_id from a secure URL.
     * Example URL: https://res.cloudinary.com/{cloud_name}/image/upload/v1234567890/folder/filename.jpg
     * Extracted publicId: folder/filename
     */
    private String extractPublicId(String url) {
        // Remove everything up to and including "/upload/"
        String afterUpload = url.substring(url.indexOf("/upload/") + "/upload/".length());

        // Remove the version segment if present (e.g., "v1234567890/")
        if (afterUpload.matches("v\\d+/.*")) {
            afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
        }

        // Remove the file extension
        int dotIndex = afterUpload.lastIndexOf(".");
        if (dotIndex != -1) {
            afterUpload = afterUpload.substring(0, dotIndex);
        }

        return afterUpload;
    }
}
