package com.dcy.file.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.dcy.file.properties.MinioProperties;
import io.minio.*;
import io.minio.messages.Bucket;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：dcy
 * @Description: minio 工具类
 * @Date: 2021/4/20 14:35
 */
public class MinioUtil {

    public static final int DEFAULT_MULTIPART_SIZE = 10 * 1024 * 1024;

    private MinioProperties minioProperties;

    private MinioClient minioClient;

    public MinioUtil(MinioProperties minioProperties, MinioClient minioClient) {
        this.minioProperties = minioProperties;
        this.minioClient = minioClient;
    }

    public MinioProperties getMinioProperties() {
        return minioProperties;
    }

    /**
     * 创建桶
     *
     * @param bucket
     * @throws Exception
     */
    public void createBucket(String bucket) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    /**
     * 查看所有桶
     *
     * @return
     * @throws Exception
     */
    public List<String> getBuckets() throws Exception {
        return minioClient.listBuckets().stream().map(Bucket::name).collect(Collectors.toList());
    }

    /**
     * 删除桶
     *
     * @param bucket
     * @throws Exception
     */
    public void deleteBucket(String bucket) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    public String uploadFile(MultipartFile file) throws Exception {
        // 获取文件后缀名
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        // 2021-04-21/20210421121212
        String filePath = StrUtil.builder()
                .append(DateUtil.today())
                .append("/")
                .append(DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN))
                .append(".")
                .append(suffix)
                .toString();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(filePath)
                .stream(file.getInputStream(), -1, DEFAULT_MULTIPART_SIZE)
                .build());
        return filePath;
    }

    /**
     * 下载文件
     *
     * @param fileName
     * @return
     * @throws Exception
     */
    public InputStream downloadFile(String fileName) throws Exception {
        InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileName).build());
        return stream;
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @throws Exception
     */
    public void removeFile(String fileName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileName).build());
    }
}
