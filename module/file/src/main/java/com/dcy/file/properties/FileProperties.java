package com.dcy.file.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author：dcy
 * @Description: 配置启用文件上传类型
 * @Date: 2021/4/20 14:12
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /**
     * 文件存储类型：fastdfs，minio
     */
    private String type;

}
