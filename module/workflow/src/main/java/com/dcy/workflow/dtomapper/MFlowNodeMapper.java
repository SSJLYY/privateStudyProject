package com.dcy.workflow.dtomapper;

import com.dcy.workflow.dto.input.FlowNodeSaveInputDTO;
import com.dcy.workflow.model.FlowNode;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author：dcy
 * @Description: 流程节点表 转换类
 * @Date: 2021-06-08
 */
@Mapper(componentModel = "spring")
public interface MFlowNodeMapper {

    FlowNode toFlowNode(FlowNodeSaveInputDTO flowNodeSaveInputDTO);

    List<FlowNode> toFlowNode(List<FlowNodeSaveInputDTO> flowNodeSaveInputDTOS);

}