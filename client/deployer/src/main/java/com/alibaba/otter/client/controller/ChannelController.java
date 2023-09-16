package com.alibaba.otter.client.controller;

import com.alibaba.otter.client.communication.CoreCommunicationClient;
import com.alibaba.otter.client.model.BaseModel;
import com.alibaba.otter.client.service.ConfigService;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.channel.ChannelParameter;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author quanlinglong
 * @date 2023/8/24 17:15
 */
@RequestMapping("/channeltest")
@Controller
public class ChannelController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Resource(name = "coreCommunicationClient")
    private CoreCommunicationClient coreCommunicationClient;

    @ResponseBody
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }


    @Resource
    private ConfigService taskService;

    @ResponseBody
    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.GET)
    public BaseModel findChannel(@PathVariable("channelId") Long key) {
        Channel channel = taskService.findChannel(key, null);
        if (channel != null) {
            return BaseModel.getInstance(channel);
        }
        return BaseModel.getInstance(key + " channel not exist");
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseModel saveChannel(@RequestBody Channel channel) {
        channel.setStatus(ChannelStatus.STOP);
        channel.setDescription("测试");
        channel.setParameters(genParameter());

        Long channelId = taskService.saveChannel(channel);
        if (channelId != null) {
            return BaseModel.getInstance(channelId);
        }
        return BaseModel.getInstance("success");
    }

    private ChannelParameter genParameter() {
        // 前端可配置三个参数：同步一致性、同步模式、是否开启数据一致性
        ChannelParameter parameter = new ChannelParameter();
        parameter.setEnableRemedy(false);//是否开启数据一致性
        parameter.setRemedyAlgorithm(ChannelParameter.RemedyAlgorithm.LOOPBACK);
        parameter.setRemedyDelayThresoldForMedia(60);
        parameter.setSyncConsistency(ChannelParameter.SyncConsistency.BASE);//同步一致性
        parameter.setSyncMode(ChannelParameter.SyncMode.FIELD);//同步模式
        return parameter;
    }



    @ResponseBody
    @RequestMapping(value = "/channel/{channelId}", method = RequestMethod.DELETE)
    public BaseModel removeChannel(@PathVariable("channelId") Long key) {
        taskService.removeChannel(key);
        // todo 校验对应的pipeline要被删除
        return BaseModel.getInstance("remove channel, channelId: " + key);
    }


}
