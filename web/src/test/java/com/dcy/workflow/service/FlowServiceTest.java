//package com.dcy.workflow.service;
//
//import cn.hutool.core.collection.CollUtil;
//import com.dcy.common.model.flowable.ActProcessInstance;
//import com.google.common.collect.ImmutableList;
//import lombok.extern.slf4j.Slf4j;
//import org.flowable.bpmn.model.FlowElement;
//import org.flowable.bpmn.model.Process;
//import org.flowable.bpmn.model.UserTask;
//import org.flowable.engine.HistoryService;
//import org.flowable.engine.RepositoryService;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.TaskService;
//import org.flowable.engine.repository.Deployment;
//import org.flowable.engine.repository.ProcessDefinition;
//import org.flowable.task.api.Task;
//import org.flowable.task.api.history.HistoricTaskInstance;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Collection;
//import java.util.List;
//
///**
// * @Author：dcy
// * @Description:
// * @Date: 2021/6/7 9:50
// */
//@Slf4j
//@SpringBootTest
//public class FlowServiceTest {
//
//    @Autowired
//    private RepositoryService repositoryService;
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private TaskService taskService;
//    @Autowired
//    private ActTaskService actTaskService;
//    @Autowired
//    private HistoryService historyService;
//
//
//    @Test
//    void deploymentFlow() {
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("processes/学生请假.bpmn20.xml")
//                .addClasspathResource("processes/学生请假.svg")
//                .name("学生请假")
//                .deploy();
//        log.info("流程部署ID:{}", deployment.getId());
//        log.info("流程部署Name:{}", deployment.getName());
//    }
//
//    @Test
//    void startProcess() {
//        final ActProcessInstance processInstanceListOutputDTO = actTaskService.startProcess("leave", "a_leave", "1170896100656156674");
//        log.info("processInstanceListOutputDTO:{}", processInstanceListOutputDTO.toString());
//    }
//
//
//    @Test
//    void getTaskList() {
//        List<Task> taskList = taskService.createTaskQuery()
//                // 候选组
//                .taskCandidateGroupIn(ImmutableList.of("1171709223680184321", "1"))
////                .taskCandidateGroup("CandidateGroup")
//                // 候选用户 or Assigned
//                .taskCandidateOrAssigned("1170896100656156674")
//                .active()
//                .list();
//        for (Task task : taskList) {
//            log.info("任务ID:{}", task.getId());
//            log.info("任务名称：{}", task.getName());
//            log.info("任务创建时间：{}", task.getCreateTime());
//            log.info("任务委派人：{}", task.getAssignee());
//            log.info("流程实例ID:{}", task.getProcessInstanceId());
//        }
//    }
//
//    @Test
//    void getHisTaskList() {
//        final List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
//                .taskAssignee("1170896100656156674").finished()
//                .list();
//        for (HistoricTaskInstance historicTaskInstance : list) {
//            log.info("任务ID:{}", historicTaskInstance.getId());
//            log.info("任务名称：{}", historicTaskInstance.getName());
//            log.info("任务创建时间：{}", historicTaskInstance.getCreateTime());
//            log.info("任务委派人：{}", historicTaskInstance.getAssignee());
//            log.info("流程实例ID:{}", historicTaskInstance.getProcessInstanceId());
//        }
//    }
//
//    @Test
//    void completeTask() {
//        /*TaskInputDTO taskInputDTO = new TaskInputDTO();
//        taskInputDTO.setComment("审批内容完整，通过了");
//        taskInputDTO.setTaskId("fadb8b18-c90a-11eb-9c79-005056c00008");
//        taskInputDTO.setUserId("1170896100656156674");
//        actTaskService.complete(taskInputDTO);*/
//
////        taskService.complete("eabf08d0-c827-11eb-a4b7-005056c00008");
//    }
//
//
//    @Test
//    void getActivityList() {
//        String processDefinitionId = "leave:5:dfb70351-c77b-11eb-8a8f-005056c00008";
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
//        Process process = repositoryService.getBpmnModel(processDefinitionId).getProcessById(processDefinition.getKey());
//        Collection<FlowElement> flowElements = process.getFlowElements();
//        if (CollUtil.isNotEmpty(flowElements)) {
//            for (FlowElement flowElement : flowElements) {
//                if (flowElement instanceof UserTask) {
//                    log.info("节点ID:{}" + flowElement.getId());
//                    log.info("节点名称ID:{}" + flowElement.getName());
//                    log.info("========================");
//                }
//            }
//        }
//    }
//}
