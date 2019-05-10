package com.alibaba.otter.node.etl.transform.transformer;

import com.alibaba.otter.shared.etl.model.EventData;

public class RocketMQTransformer implements OtterTransformer<EventData, EventData> {
    @Override
    public EventData transform(EventData data, OtterTransformerContext context) {
        return null;
    }
}
