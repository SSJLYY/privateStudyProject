package com.dcy.file.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dcy.common.annotation.Log;
import com.dcy.common.model.PageModel;
import com.dcy.common.model.PageResult;
import com.dcy.common.model.R;
import com.dcy.db.base.controller.BaseController;
import com.dcy.file.dto.input.FileInfoDownloadInputDTO;
import com.dcy.file.dto.input.FileInfoSearchInputDTO;
import com.dcy.file.dto.output.FileInfoListOutputDTO;
import com.dcy.file.dtomapper.MFileInfoMapper;
import com.dcy.file.model.FileInfo;
import com.dcy.file.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2019/9/18 14:02
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/system/file")
@Api(value = "FileController", tags = {"文件接口"})
public class FileController extends BaseController<FileInfoService, FileInfo> {

    private final MFileInfoMapper mFileInfoMapper;

    @Log
    @ApiOperation(value = "文件分页查询")
    @GetMapping(value = "/page")
    public R<PageResult<FileInfoListOutputDTO>> page(FileInfoSearchInputDTO fileInfoSearchInputDTO, PageModel pageModel) {
        FileInfo fileInfo = mFileInfoMapper.fileInfoSearchInputDTOToFileInfo(fileInfoSearchInputDTO);
        IPage<FileInfo> pageList = baseService.pageListByEntity(fileInfo, pageModel);
        List<FileInfoListOutputDTO> fileInfoListOutputDTOS = mFileInfoMapper.fileInfosToFileInfoListOutputDTOs(pageList.getRecords());
        return success(toPageDTO(pageList, fileInfoListOutputDTOS));
    }

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public R<FileInfoListOutputDTO> upload(@RequestParam("file") MultipartFile file) throws Exception {
        FileInfo fileInfo = baseService.upload(file);
        FileInfoListOutputDTO fileInfoListOutputDTO = mFileInfoMapper.fileInfoToFileInfoListOutputDTO(fileInfo);
        return success(fileInfoListOutputDTO);
    }

    @ApiOperation(value = "批量上传文件")
    @PostMapping(value = "/uploadFiles")
    public R<List<FileInfoListOutputDTO>> uploadFiles(@RequestParam(value = "files") MultipartFile[] files) {
        List<FileInfo> fileInfos = baseService.uploadFiles(files);
        List<FileInfoListOutputDTO> fileInfoListOutputDTOS = mFileInfoMapper.fileInfosToFileInfoListOutputDTOs(fileInfos);
        return success(fileInfoListOutputDTOS);
    }

    @Log
    @ApiOperation(value = "文件删除")
    @ApiImplicitParam(name = "id", value = "文件id", dataType = "String", paramType = "query")
    @PostMapping("/delete")
    public R<Boolean> delete(@NotBlank(message = "文件id不能为空") @RequestParam String id) {
        baseService.deleteFile(id);
        return success();
    }

    @Log
    @ApiOperation(value = "下载文件")
    @GetMapping("/download")
    public void download(@Validated FileInfoDownloadInputDTO fileInfoDownloadInputDTO, HttpServletResponse response) {
        baseService.downLoad(fileInfoDownloadInputDTO.getFileName(), fileInfoDownloadInputDTO.getFilePath(), response);
    }
}
