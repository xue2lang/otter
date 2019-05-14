package com.alibaba.otter.node.etl.transform.transformer;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.shared.etl.model.EventData;

import java.util.HashMap;
import java.util.List;

public class RocketMQTransformer implements OtterTransformer<EventData, EventData> {
    @Override
    public EventData transform(EventData data, OtterTransformerContext context) {
        List<CanalEntry.Column> beforeColumns = data.getBeforeColumns();
        List<CanalEntry.Column> afterColumns = data.getAfterColumns();

        HashMap<String, Object> before = new HashMap<String, Object>();
        HashMap<String, Object> after = new HashMap<String, Object>();

        for (CanalEntry.Column column : beforeColumns) {
            before.put(column.getName(), column.getValue());
        }
        for (CanalEntry.Column column : afterColumns) {
            after.put(column.getName(), column.getValue());
        }

        data.setAfter(after);
        data.setBefore(before);
        data.setAfterColumns(null);
        data.setBeforeColumns(null);
        if (data.getKeys().size() > 0) {
            data.setPrimaryKey(data.getKeys().get(0).getColumnValue());
        }
        return data;
    }
}
