package com.dcy.file.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.dcy.common.model.PageModel;
import com.dcy.db.base.service.BaseService;
import com.dcy.file.mapper.FileInfoMapper;
import com.dcy.file.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/4/20 14:21
 */
@Service
public class FileInfoService extends BaseService<FileInfoMapper, FileInfo> {

    @Autowired
    private FileHandler fileHandler;

    /**
     * 分页查询
     *
     * @param fileInfo
     * @return
     */
    public IPage<FileInfo> pageListByEntity(FileInfo fileInfo, PageModel pageModel) {
        LambdaQueryWrapper<FileInfo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StrUtil.isNotBlank(fileInfo.getName()), FileInfo::getName, fileInfo.getName());
        return super.page(pageModel, queryWrapper);
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    public FileInfo upload(MultipartFile file) throws Exception {
        FileInfo fileInfo = fileHandler.upload(file);
        return save(fileInfo) ? fileInfo : null;
    }

    /**
     * 文件上传多个
     *
     * @param files
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<FileInfo> uploadFiles(MultipartFile[] files) {
        List<FileInfo> fileInfos = fileHandler.uploadFiles(files);
        SqlHelper.executeBatch(FileInfo.class, this.log, fileInfos, DEFAULT_BATCH_SIZE, (sqlSession, entity) -> {
            String sqlStatement = SqlHelper.getSqlStatement(FileInfoMapper.class, SqlMethod.INSERT_ONE);
            sqlSession.insert(sqlStatement, entity);
        });
        return fileInfos;
    }

    /**
     * 删除文件
     *
     * @param id
     */
    public void deleteFile(String id) {
        FileInfo fileInfo = getById(id);
        fileHandler.deleteFile(fileInfo);
    }

    /**
     * 下载文件
     *
     * @param fileName
     * @param filePath
     * @param response
     */
    public void downLoad(String fileName, String filePath, HttpServletResponse response) {
        fileHandler.downLoad(fileName, filePath, response);
    }

}
