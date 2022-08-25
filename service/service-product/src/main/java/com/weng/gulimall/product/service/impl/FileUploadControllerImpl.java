package com.weng.gulimall.product.service.impl;

import com.weng.gulimall.common.util.DateUtil;
import com.weng.gulimall.product.config.minio.MinioProperties;
import com.weng.gulimall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadControllerImpl implements FileUploadService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;


    @Override
    public String uploadFile(MultipartFile file) {
        //生成随机文件名
        String filename = file.getOriginalFilename();
        filename = DateUtil.formatDate(new Date()) + "/" + UUID.randomUUID().toString().replace("-", "") + "_" + filename;


        try {
            PutObjectOptions options = new PutObjectOptions(file.getSize(), -1);
            options.setContentType(file.getContentType());

            minioClient.putObject(minioProperties.getBucketName(),
                    filename,
                    file.getInputStream(),
                    options);// 需要注入进来 @Resource private MinioClient minioClient;
            return minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + filename;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


