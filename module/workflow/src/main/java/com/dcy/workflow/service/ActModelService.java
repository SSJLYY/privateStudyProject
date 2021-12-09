package com.dcy.workflow.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author：dcy
 * @Description:
 * @Date: 2021/6/7 13:26
 */
@Slf4j
@Service
public class ActModelService {

    private static final String MODEL_ID = "modelId";
    private static final String MODEL_NAME = "name";
    private static final String MODEL_REVISION = "revision";
    private static final String MODEL_DESCRIPTION = "description";

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 创建模型
     *
     * @param modelName
     * @param category
     * @param description
     * @param jsonXml
     * @param svgXml
     * @return
     */
    public boolean createModel(String key, String modelName, String category, String description, String jsonXml, String svgXml) {
        return saveModel(null, key, modelName, category, description, jsonXml, svgXml);
    }

    /**
     * 修改模型
     *
     * @param modelId
     * @param modelName
     * @param category
     * @param description
     * @param jsonXml
     * @param svgXml
     * @return
     */
    public boolean updateModel(String key, String modelId, String modelName, String category, String description, String jsonXml, String svgXml) {
        return saveModel(modelId, key, modelName, category, description, jsonXml, svgXml);
    }

    /**
     * 部署模型
     *
     * @param modelId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deployModel(String modelId) {
        Model model = repositoryService.getModel(modelId);
        String processName = model.getName();
        final String suffix = ".bpmn20.xml";
        if (!StrUtil.endWith(processName, suffix)) {
            processName += suffix;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(repositoryService.getModelEditorSource(model.getId()));
        Deployment deployment = repositoryService.createDeployment().name(model.getName())
                .addInputStream(processName, in).deploy();
        // 设置流程分类
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
        for (ProcessDefinition processDefinition : list) {
            repositoryService.setProcessDefinitionCategory(processDefinition.getId(), model.getCategory());
        }
        return true;
    }

    /**
     * 根据流程定义id获取流程图
     *
     * @param processDefinitionId
     */
    public InputStream getBpmnModel(String processDefinitionId) {
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration processEngineConfig = processEngine.getProcessEngineConfiguration();

        ProcessDiagramGenerator diagramGenerator = processEngineConfig.getProcessDiagramGenerator();

        return diagramGenerator.generateDiagram(bpmnModel, "bmp", processEngineConfig.getActivityFontName(),
                processEngineConfig.getLabelFontName(), processEngineConfig.getAnnotationFontName(), processEngineConfig.getClassLoader(), true);
    }

    /**
     * 根据模型id查询json
     *
     * @param modelId
     * @return
     */
    public String getModelJsonByModelId(String modelId) {
        Model model = repositoryService.getModel(modelId);
        if (model != null) {
            return new String(repositoryService.getModelEditorSource(model.getId()), StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * 保存模型
     *
     * @param modelId
     * @param modelName
     * @param category
     * @param description
     * @param jsonXml
     * @param svgXml
     * @return
     */
    private boolean saveModel(String modelId, String key, String modelName, String category, String description, String jsonXml, String svgXml) {
        try {
            if (StrUtil.isBlank(jsonXml) && StrUtil.isBlank(svgXml) && StrUtil.isBlank(modelName)) {
                return false;
            }
            Model model;
            if (StrUtil.isNotBlank(modelId)) {
                // 根据modelId 查询对象
                model = repositoryService.getModel(modelId);
            } else {
                // 创建新的model
                model = repositoryService.newModel();
            }
            model.setName(modelName);
            model.setCategory(category);

            // 构建源信息
            ObjectNode modelJson = (ObjectNode) objectMapper.reader().createObjectNode();
            modelJson.put(MODEL_NAME, modelName);
            modelJson.put(MODEL_DESCRIPTION, description);
            model.setMetaInfo(modelJson.toString());
            model.setKey(StringUtils.defaultString(key));
            model.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(model.getKey()).count() + 1)));
            // 保存model
            repositoryService.saveModel(model);
            //保存json
            repositoryService.addModelEditorSource(model.getId(), jsonXml.getBytes(StandardCharsets.UTF_8));

            // svg 转换 流
            InputStream svgStream = new ByteArrayInputStream(svgXml.getBytes(StandardCharsets.UTF_8));
            TranscoderInput input = new TranscoderInput(svgStream);
            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);
            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            IoUtil.close(outStream);
            IoUtil.close(svgStream);
        } catch (TranscoderException e) {
            e.printStackTrace();
            log.error("saveModel {}", e.getMessage());
            return false;
        }
        return true;
    }
}
