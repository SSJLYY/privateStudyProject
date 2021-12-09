package com.dcy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.alibaba.fastjson.JSON;
import com.dcy.common.constant.Constant;
import com.dcy.system.model.Resource;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class ResourceServiceTest {

    @Autowired
    private ResourceService resourceService;

    @Test
    void treeList() {
        final List<Resource> resourceList = resourceService.list();

        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
        resourceList.forEach(resource -> {
            final TreeNode<String> treeNode = new TreeNode<>(resource.getId(), resource.getParentId(), resource.getTitle(), resource.getResSort());
            treeNode.setExtra(ImmutableMap.of("data", resource));
            nodeList.add(treeNode);
        });
        final List<Tree<String>> treeList = TreeUtil.build(nodeList, Constant.COMMON_PARENT_ID);

        log.info("treeList {}", JSON.toJSONString(treeList));
    }
}