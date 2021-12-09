package com.dcy.file.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author dcy
 * @since 2019-09-18
 */
@Data
@Accessors(chain = true)
@TableName("sys_file_info")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件md5 主键id
     */
    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 物理路径
     */
    private String path;

    /**
     * url地址
     */
    private String url;

    /**
     * 来源
     */
    private String source;

    /**
     * 创建时间
     */
    private Date createDate;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String FILE_FLAG = "file_flag";

    public static final String CONTENT_TYPE = "content_type";

    public static final String FILE_SIZE = "file_size";

    public static final String PATH = "path";

    public static final String URL = "url";

    public static final String SOURCE = "source";

    public static final String CREATE_DATE = "createDate";


}
