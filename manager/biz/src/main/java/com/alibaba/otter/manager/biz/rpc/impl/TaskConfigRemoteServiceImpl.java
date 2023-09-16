package com.alibaba.otter.manager.biz.rpc.impl;

import com.alibaba.otter.manager.biz.config.channel.ChannelService;
import com.alibaba.otter.manager.biz.config.datamatrix.DataMatrixService;
import com.alibaba.otter.manager.biz.config.node.NodeService;
import com.alibaba.otter.manager.biz.config.pipeline.PipelineService;
import com.alibaba.otter.manager.biz.rpc.TaskConfigRemoteService;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.channel.ChannelStatus;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.communication.core.CommunicationClient;
import com.alibaba.otter.shared.communication.core.CommunicationRegistry;
import com.alibaba.otter.shared.communication.model.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author quanlinglong
 * @date 2023/8/28 18:52
 */
public class TaskConfigRemoteServiceImpl implements TaskConfigRemoteService {

    private static final Logger logger = LoggerFactory.getLogger(TaskConfigRemoteServiceImpl.class);
    private CommunicationClient communicationClient;
    private ChannelService channelService;
    private PipelineService pipelineService;
    private NodeService nodeService;
    private DataMatrixService dataMatrixService;

    public TaskConfigRemoteServiceImpl() {
        // 注册一下事件处理
        CommunicationRegistry.regist(ConfigEventType.saveChannel, this);
        CommunicationRegistry.regist(ConfigEventType.removeChannel, this);
        CommunicationRegistry.regist(ConfigEventType.savePipeline, this);
        CommunicationRegistry.regist(ConfigEventType.removePipeline, this);
        CommunicationRegistry.regist(ConfigEventType.listNode, this);
    }

    /**
     * 根据对应的工作节点机器id，获取相关的channel任务
     */
    @Override
    public Long onSaveChannel(SaveChannelEvent event) {
        Assert.notNull(event);
        Assert.notNull(event.getChannel());
        Channel oldChannel = null;
        if (event.getChannel().getId() != null) {
            oldChannel = channelService.findById(event.getChannel().getId());
        }

        if (oldChannel != null) {
            // todo 从 event 中取出具体参数
            if (event.getChannel() != null) {
                channelService.modify(event.getChannel());
            }
            return event.getChannel().getId();
        } else {
            Channel channel = event.getChannel();
            channel.setStatus(ChannelStatus.STOP);
            channelService.create(channel);
            logger.info("save channel, id " + channel.getId());
            return channel.getId();
        }
    }

    @Override
    public void onRemoveChannel(RemoveChannelEvent event) {
        Assert.notNull(event);
        Assert.notNull(event.getChannelId());
        channelService.remove(event.getChannelId());
    }

    @Override
    public Long onSavePipeline(SavePipelineEvent event) {
        Assert.notNull(event);
        Assert.notNull(event.getPipeline());
        Pipeline oldPipeline = null;
        if (event.getPipeline().getId() != null) {
            oldPipeline = pipelineService.findById(event.getPipeline().getId());
        }
        if (oldPipeline != null) {
            // todo 从 event 中取出具体参数
            if (event.getPipeline() != null) {
                pipelineService.modify(event.getPipeline());
            }
            return event.getPipeline().getId();
        } else {
            Pipeline pipeline = event.getPipeline();
            pipelineService.create(pipeline);
            logger.info("save pipeline, id " + pipeline.getId());
            return pipeline.getId();
        }
    }

    @Override
    public void onRemovePipeline(RemovePipelineEvent event) {
        Assert.notNull(event);
        Assert.notNull(event.getPipelineId());
        channelService.remove(event.getPipelineId());
    }

    @Override
    public List<Node> onListNode(ListNodeEvent event) {
        return nodeService.listAll();
    }

    // =============== setter / getter ===================

    public void setCommunicationClient(CommunicationClient communicationClient) {
        this.communicationClient = communicationClient;
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setDataMatrixService(DataMatrixService dataMatrixService) {
        this.dataMatrixService = dataMatrixService;
    }

    public void setPipelineService(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }
}
