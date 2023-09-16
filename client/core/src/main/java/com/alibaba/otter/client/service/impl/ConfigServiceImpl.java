package com.alibaba.otter.client.service.impl;

import com.alibaba.otter.client.communication.CoreCommunicationClient;
import com.alibaba.otter.client.service.ConfigService;
import com.alibaba.otter.shared.common.model.config.ConfigException;
import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import com.alibaba.otter.shared.common.utils.Assert;
import com.alibaba.otter.shared.communication.model.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author quanlinglong
 * @date 2023/8/29 09:41
 */
public class ConfigServiceImpl implements ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Resource(name = "coreCommunicationClient")
    private CoreCommunicationClient coreCommunicationClient;

    @Override
    public Node findNode(Long nodeId) {
        FindNodeEvent event = new FindNodeEvent();
        event.setNid(nodeId);
        Node node = null;
        try {
            Object obj = coreCommunicationClient.callManager(event);
            if (obj instanceof Node) {
                node = (Node) obj;
            } else {
                throw new ConfigException("No Such Node by nodeId[" + nodeId + "]");

            }
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event, e);
            throw new ConfigException("Call Manager Error by key[" + nodeId + "]");
        }
        logger.info("{}", node);
        return node;
    }

    @Override
    public List<Node> listNodes() {
        ListNodeEvent event = new ListNodeEvent();
        List<Node> nodes = null;
        try {
            Object obj = coreCommunicationClient.callManager(event);
            if (obj instanceof Node) {
                nodes = (List<Node>) obj;
            } else {
                throw new ConfigException("No Such Nodes ");

            }
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event, e);
            throw new ConfigException("Call Manager Error by key[" + event.getType() + "]");
        }
        return nodes;
    }

    @Override
    public Channel findChannel(Long channelId, Long pipelineId) {
        FindChannelEvent event = new FindChannelEvent();
        event.setChannelId(channelId);
        event.setPipelineId(pipelineId);
        Channel channel;
        try {
            Object obj = coreCommunicationClient.callManager(event);
            if (obj instanceof Channel) {
                channel =  (Channel) obj;
            } else {
                throw new ConfigException("No Such Channel by pipelineId[" + pipelineId + "]");
            }
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event.toString(), e);
            throw new ConfigException("Call Manager Error by key[" + channelId + "]");
        }
        return channel;
    }

    @Override
    public Long saveChannel(Channel channel) {
        SaveChannelEvent event = new SaveChannelEvent();
        event.setChannel(channel);
        Long channelId = null;
        try {
            Object obj = coreCommunicationClient.callManager(event);
            logger.info("obj name : {}, value: {}", obj.getClass().getName(), obj);
            if (obj instanceof Long) {
                channelId = (Long) obj;
            } else {
                throw new ConfigException("Not return channelId");
            }
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event, e);
            throw new RuntimeException(e);
        }
        return channelId;
    }

    @Override
    public void removeChannel(Long channelId) {
        RemoveChannelEvent event = new RemoveChannelEvent();
        event.setChannelId(channelId);
        try {
            Object obj = coreCommunicationClient.callManager(event);
            if (obj instanceof Long) {
                channelId = (Long) obj;
            }
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event, e);
        }
    }

    @Override
    public Pipeline findPipeLine(Long pipelineId) {
        FindPipelineEvent event = new FindPipelineEvent();
        event.setPipelineId(pipelineId);
        List<Pipeline> pipelines = new ArrayList<>();
        try {
            Object obj = coreCommunicationClient.callManager(event);
            if (obj instanceof List) {
                pipelines = (List<Pipeline>) obj;
            } else {
                 throw new ConfigException("No Such Pipeline by id[" + pipelineId + "]");
            }
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event, e);
            throw new RuntimeException(e);
        }
        for (Pipeline pipeline : pipelines) {
            if (pipeline.getId().equals(pipelineId)) {
                return pipeline;
            }
        }
        throw new ConfigException("No Such Pipeline by id[" + pipelineId + "]");
    }

    @Override
    public Long savePipeline(Pipeline pipeline) {
        Assert.assertNotNull(pipeline);
        SavePipelineEvent event = new SavePipelineEvent();
        event.setPipeline(pipeline);
        Long pipeLineId = null;
        // todo 检查一些必要参数，如名称，是否主节点等
        try {
            Object obj = coreCommunicationClient.callManager(event);
            if (obj instanceof Long) {
                pipeLineId = (Long) obj;
            } else {
                throw new ConfigException("Not return pipeLineId");
            }
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event, e);
            throw new RuntimeException(e);
        }
        return pipeLineId;
    }

    @Override
    public void removePipeline(Long pipelineId) {
        // todo 检查是否可删除
        RemovePipelineEvent event = new RemovePipelineEvent();
        event.setPipelineId(pipelineId);
        try {
            coreCommunicationClient.callManager(event);
        } catch (Exception e) {
            logger.error("call_manager_error: {}", event, e);
        }
    }
}
