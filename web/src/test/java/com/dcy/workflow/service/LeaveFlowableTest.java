//package com.dcy.workflow.service;
//
//import com.dcy.common.model.flowable.ActProcessInstance;
//import com.dcy.workflow.dto.output.ActivityInstanceListOutputDTO;
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//import lombok.extern.slf4j.Slf4j;
//import org.flowable.bpmn.model.FlowElement;
//import org.flowable.bpmn.model.Process;
//import org.flowable.bpmn.model.UserTask;
//import org.flowable.engine.HistoryService;
//import org.flowable.engine.RepositoryService;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.TaskService;
//import org.flowable.engine.repository.ProcessDefinition;
//import org.flowable.task.api.Task;
//import org.flowable.task.api.history.HistoricTaskInstance;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
///**
// * @Author：dcy
// * @Description:
// * @Date: 2021/6/7 9:50
// */
//@Slf4j
//@SpringBootTest
//public class LeaveFlowableTest {
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
//    void startProcess() {
////        final ProcessInstanceListOutputDTO processInstanceListOutputDTO = actTaskService.startProcess("leave2", "a_leave", "1170896100656156674");
//        final ActProcessInstance processInstanceListOutputDTO = actTaskService.startProcessAndCompleteFirstTask("leave", "a_leave", "1170896100656156674");
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
//
//        final String taskId = "db12c96c-c99a-11eb-a704-005056c00008";
//        final String userId = "1170896100656156674";
//        final String comment = "审批内容完整，通过了";
////        final String comment = "驳回了";
//
//        // 通过
//        actTaskService.complete(taskId, userId, comment, ImmutableMap.of("status", "success"));
//        // 驳回
////        actTaskService.complete(taskId, userId, comment,false);
//    }
//
//    @Test
//    void getTaskComments() {
//        final List<ActivityInstanceListOutputDTO> runTaskInsList = actTaskService.getRunTaskInsList("daee9f8c-c99a-11eb-a704-005056c00008");
//        for (ActivityInstanceListOutputDTO activityInstance : runTaskInsList) {
//            log.info("环节名称:{}", activityInstance.getActivityName());
//            log.info("开始时间:{}", activityInstance.getStartTime());
//            log.info("结束时间:{}", activityInstance.getEndTime());
//            log.info("任务历时:{}", activityInstance.getDuration());
//            log.info("操作人:{}", activityInstance.getAssignee());
//            log.info("审批意见:{}", activityInstance.getComment());
//            log.info("============================================");
//        }
//
//    }
//
//    @Test
//    void getHisActivityInsList() {
//        final List<ActivityInstanceListOutputDTO> hisTaskInsList = actTaskService.getHisTaskInsListByProInsId("daee9f8c-c99a-11eb-a704-005056c00008");
//        for (ActivityInstanceListOutputDTO historicTaskInstance : hisTaskInsList) {
//            log.info("环节名称:{}", historicTaskInstance.getActivityName());
//            log.info("开始时间:{}", historicTaskInstance.getStartTime());
//            log.info("结束时间:{}", historicTaskInstance.getEndTime());
//            log.info("任务历时:{}", historicTaskInstance.getDuration());
//            log.info("操作人:{}", historicTaskInstance.getAssignee());
//            log.info("审批意见:{}", historicTaskInstance.getComment());
//            log.info("============================================");
//        }
//    }
//
//    @Test
//    void getActivityList() {
//        String processDefinitionId = "leave:1:e3f8c9f9-c993-11eb-a5ee-005056c00008";
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
//        Process process = repositoryService.getBpmnModel(processDefinitionId).getProcessById(processDefinition.getKey());
//        final List<UserTask> flowElements = process.findFlowElementsOfType(UserTask.class);
//        for (FlowElement flowElement : flowElements) {
//            log.info("节点ID:{}" + flowElement.getId());
//            log.info("节点名称ID:{}" + flowElement.getName());
//            log.info("========================");
//        }
//    }
//}
