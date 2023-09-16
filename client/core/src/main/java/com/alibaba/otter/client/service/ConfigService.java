package com.alibaba.otter.client.service;

import com.alibaba.otter.shared.common.model.config.channel.Channel;
import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;

import java.util.List;

/**
 * @author quanlinglong
 * @date 2023/8/29 09:40
 */
public interface ConfigService {

    Node findNode(Long nodeId);
    List<Node> listNodes();

    Channel findChannel(Long channelId, Long pipelineId);

    Long saveChannel(Channel channel);

    void removeChannel(Long channelId);

    Pipeline findPipeLine(Long pipelineId);

    Long savePipeline(Pipeline pipeline);

    void removePipeline(Long pipelineId);

}
