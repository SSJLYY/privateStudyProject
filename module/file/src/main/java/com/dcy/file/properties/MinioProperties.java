package com.dcy.file.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author：dcy
 * @Description: Minio配置文件
 * @Date: 2021/4/20 14:14
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file.minio")
public class MinioProperties {

    /**
     * minio 地址
     */
    private String url;

    /**
     * minio accessKey
     */
    private String accessKey;

    /**
     * minio secretKey
     */
    private String secretKey;

    /**
     * 桶
     */
    private String bucket;

    /**
     * 文件访问地址（nginx地址）
     * 注意：Bucket Policy：* Read Only策略 才可以访问
     * 访问地址格式：地址:端口/Bucket名称/路径地址/文件名称
     */
    private String webUrl;
}
