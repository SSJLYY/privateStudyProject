package com.dcy.file.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.file.model.FileInfo;
import com.dcy.file.service.FileHandler;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/12/17 9:03
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "file.type", havingValue = "fastdfs")
public class FastDFSServiceImpl implements FileHandler {

    private static final String FILE_SPLIT = ".";

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Override
    public String fileType() {
        return "fastdfs";
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
        if (fileInfo != null && StrUtil.isNotBlank(fileInfo.getPath())) {
            StorePath storePath = StorePath.parseFromUrl(fileInfo.getPath());
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        }
    }

    @Override
    public void downLoad(String fileName, String filePath, HttpServletResponse response) {
        if (StrUtil.isNotBlank(fileName) && StrUtil.isNotBlank(filePath)) {
            StorePath storePath = StorePath.parseFromUrl(filePath);
            DownloadByteArray callback = new DownloadByteArray();
            byte[] bytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), callback);
            try {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(fileName, CharsetUtil.UTF_8));
                response.setCharacterEncoding(CharsetUtil.UTF_8);
                IoUtil.write(response.getOutputStream(), true, bytes);
            } catch (IOException e) {
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
        if (!fileInfo.getName().contains(FILE_SPLIT)) {
            throw new IllegalArgumentException("缺少后缀名");
        }
        uploadFile(file, fileInfo);
        // 设置文件来源
        fileInfo.setSource(fileType());
        return fileInfo;
    }

    private void uploadFile(MultipartFile file, FileInfo fileInfo) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()), null);
        fileInfo.setUrl(fdfsWebServer.getWebServerUrl() + "/" + storePath.getFullPath());
        fileInfo.setPath(storePath.getFullPath());
    }
}
