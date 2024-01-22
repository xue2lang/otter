/*
 * Copyright (C) 2010-2101 Alibaba Group Holding Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.otter.shared.etl.model;

import com.alibaba.otter.shared.common.utils.OtterToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.io.Serializable;

/**
 * 基于MQ的同步记录对象
 * 
 * @author Li Zhengxian 2019.5.9
 */
public class MqBatch implements Serializable {

    private static final long serialVersionUID = 1716704802567430638L;

    private RowBatch          rowBatch;                               // 如果目标端是mq，则一定不为空

    public MqBatch(){

    }

    public MqBatch(RowBatch rowBatch){
        this.rowBatch = rowBatch;
    }

    public MqBatch(RowBatch rowBatch, FileBatch fileBatch, File root){
        this.rowBatch = rowBatch;
    }

    public RowBatch getRowBatch() {
        return rowBatch;
    }

    public void setRowBatch(RowBatch rowBatch) {
        this.rowBatch = rowBatch;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, OtterToStringStyle.DEFAULT_STYLE);
    }

}
