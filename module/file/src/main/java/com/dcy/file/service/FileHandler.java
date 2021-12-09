package com.dcy.file.service;

import com.dcy.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/5/7 8:23
 */
public interface FileHandler {

    /**
     * 文件来源
     *
     * @return
     */
    String fileType();

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    FileInfo upload(MultipartFile file) throws Exception;

    /**
     * 文件上传多个
     *
     * @param files
     * @return
     */
    List<FileInfo> uploadFiles(MultipartFile[] files);

    /**
     * 删除文件
     *
     * @param fileInfo
     */
    void deleteFile(FileInfo fileInfo);

    /**
     * 下载文件
     *
     * @param fileName
     * @param filePath
     * @param response
     */
    void downLoad(String fileName, String filePath, HttpServletResponse response);
}
