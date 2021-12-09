package com.dcy.file.dtomapper;

import com.dcy.file.dto.input.FileInfoSearchInputDTO;
import com.dcy.file.dto.output.FileInfoListOutputDTO;
import com.dcy.file.model.FileInfo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2020/11/12 11:20
 */
@Mapper(componentModel = "spring")
public interface MFileInfoMapper {

    FileInfo fileInfoSearchInputDTOToFileInfo(FileInfoSearchInputDTO fileInfoSearchInputDTO);

    FileInfoListOutputDTO fileInfoToFileInfoListOutputDTO(FileInfo fileInfo);

    List<FileInfoListOutputDTO> fileInfosToFileInfoListOutputDTOs(List<FileInfo> fileInfos);
}
