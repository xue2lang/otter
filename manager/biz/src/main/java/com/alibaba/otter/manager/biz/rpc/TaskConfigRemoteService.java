package com.alibaba.otter.manager.biz.rpc;

import com.alibaba.otter.shared.common.model.config.node.Node;
import com.alibaba.otter.shared.communication.model.config.*;

import java.util.List;

/**
 *   manager 远程服务接口
 *
 * @author quanlinglong
 * @date 2023/8/28 18:51
 */
public interface TaskConfigRemoteService {

    /**
     * 接收客户端的创建/更新channel请求
     */
    public Long onSaveChannel(SaveChannelEvent event);


    public void onRemoveChannel(RemoveChannelEvent event);


    public Long onSavePipeline(SavePipelineEvent event);


    public void onRemovePipeline(RemovePipelineEvent event);

    public List<Node> onListNode(ListNodeEvent event);
}
