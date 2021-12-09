package com.dcy.file.config;

import com.dcy.file.properties.MinioProperties;
import com.dcy.file.util.MinioUtil;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：dcy
 * @Description: minio配置类
 * @Date: 2021/4/20 14:13
 */
@Configuration
@ConditionalOnProperty(name = "file.type", havingValue = "minio")
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();
    }

    @Bean
    public MinioUtil minioUtil(MinioProperties minioProperties, MinioClient minioClient) {
        return new MinioUtil(minioProperties, minioClient);
    }

}
