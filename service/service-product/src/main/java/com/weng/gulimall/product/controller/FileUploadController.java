package com.weng.gulimall.product.controller;

import com.weng.gulimall.common.result.Result;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/product/")
public class FileUploadController {

    @Value("${minio.bucketName}")
    private String bucketName;

    @Autowired
    private MinioClient minioClient;

    // admin/product/fileUpload
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        //生成随机文件名
        String filename = file.getOriginalFilename();
        filename = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));


        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().object(filename)
                    .bucket(bucketName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(),file.getSize(),-1).build();

            minioClient.putObject(objectArgs);// 需要注入进来 @Resource private MinioClient minioClient;
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
}
