package com.alibaba.otter.node.etl.common.datasource.impl;

import com.alibaba.otter.node.etl.common.datasource.DataSourceService;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;

public class RocketMQDataSourceService implements DataSourceService {




    @Override
    public <T> T getDataSource(long pipelineId, DataMediaSource dataMediaSource) {
        return null;
    }

    @Override
    public void destroy(Long pipelineId) {

    }
}
