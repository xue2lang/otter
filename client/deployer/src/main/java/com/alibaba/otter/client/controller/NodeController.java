package com.alibaba.otter.client.controller;

import com.alibaba.otter.client.model.BaseModel;
import com.alibaba.otter.client.service.ConfigService;
import com.alibaba.otter.shared.common.model.config.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 测试类
 *
 * @author quanlinglong
 * @date 2023/8/28 19:17
 */
@RequestMapping("/nodetest")
@RestController
public class NodeController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Resource
    private ConfigService taskService;

    @ResponseBody
    @RequestMapping(value = "/node/{nodeId}", method = RequestMethod.GET)
    public String findNode(@PathVariable("nodeId") Long key) {
        Node node = taskService.findNode(key);
        if (node != null) {
            return node.toString();
        }
        return key + " node not exist";
    }

    @ResponseBody
    @RequestMapping(value = "/node/list", method = RequestMethod.GET)
    public BaseModel listNode() {
        return BaseModel.getInstance(taskService.listNodes());
    }
}
