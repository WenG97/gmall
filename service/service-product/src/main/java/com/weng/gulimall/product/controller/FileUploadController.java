package com.weng.gulimall.product.controller;

import com.weng.gulimall.common.result.Result;
import com.weng.gulimall.product.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/admin/product/")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    // admin/product/fileUpload
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){

        String URL = fileUploadService.uploadFile(file);
        return Result.ok(URL);
    }
}
