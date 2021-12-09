package com.dcy.file.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.file.model.FileInfo;
import com.dcy.file.service.FileHandler;
import com.dcy.file.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/20 14:30
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "file.type", havingValue = "minio")
public class MinioServiceImpl implements FileHandler {

    @Autowired
    private MinioUtil minioUtil;


    @Override
    public String fileType() {
        return "minio";
    }

    @Override
    public FileInfo upload(MultipartFile file) throws Exception {
        return uploadCommonFile(file);
    }

    @Override
    public List<FileInfo> uploadFiles(MultipartFile[] files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        Arrays.stream(files).forEach(multipartFile -> {
            try {
                FileInfo fileInfo = uploadCommonFile(multipartFile);
                fileInfos.add(fileInfo);
            } catch (Exception e) {
                log.error("uploadFiles [{}]", e.getMessage());
            }
        });
        return fileInfos;
    }

    @Override
    public void deleteFile(FileInfo fileInfo) {
        try {
            if (fileInfo != null && StrUtil.isNotBlank(fileInfo.getPath())) {
                minioUtil.removeFile(fileInfo.getPath());
            }
        } catch (Exception e) {
            log.error("deleteFile [{}]", e.getMessage());
        }
    }

    @Override
    public void downLoad(String fileName, String filePath, HttpServletResponse response) {
        if (StrUtil.isNotBlank(fileName) && StrUtil.isNotBlank(filePath)) {
            try {
                InputStream inputStream = minioUtil.downloadFile(filePath);
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileName, CharsetUtil.UTF_8));
                response.setCharacterEncoding(CharsetUtil.UTF_8);
                IoUtil.copy(inputStream, response.getOutputStream());
            } catch (Exception e) {
                log.error("downLoad [{}]", e.getMessage());
            }
        }
    }


    private FileInfo uploadCommonFile(MultipartFile file) throws Exception {
        FileInfo fileInfo = new FileInfo()
                .setName(file.getOriginalFilename())
                .setContentType(file.getContentType())
                .setFileSize(file.getSize())
                .setCreateDate(new Date());
        uploadFile(file, fileInfo);
        // 设置文件来源
        fileInfo.setSource(fileType());
        return fileInfo;
    }

    /**
     * 上传文件
     *
     * @param file
     * @param fileInfo
     */
    private void uploadFile(MultipartFile file, FileInfo fileInfo) {
        try {
            String filePath = minioUtil.uploadFile(file);
            fileInfo.setUrl(minioUtil.getMinioProperties().getWebUrl() + filePath);
            fileInfo.setPath(filePath);
        } catch (Exception e) {
            log.error("uploadFile [{}]", e.getMessage());
        }
    }
}
