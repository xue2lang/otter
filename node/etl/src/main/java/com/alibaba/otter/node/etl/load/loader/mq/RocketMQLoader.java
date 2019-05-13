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

package com.alibaba.otter.node.etl.load.loader.mq;

import com.alibaba.otter.node.common.config.ConfigClientService;
import com.alibaba.otter.node.etl.OtterConstants;
import com.alibaba.otter.node.etl.load.exception.LoadException;
import com.alibaba.otter.node.etl.load.loader.LoadContext;
import com.alibaba.otter.node.etl.load.loader.OtterLoader;
import com.alibaba.otter.node.etl.load.loader.db.DbLoadAction;
import com.alibaba.otter.node.etl.load.loader.db.FileLoadAction;
import com.alibaba.otter.node.etl.load.loader.db.context.DbLoadContext;
import com.alibaba.otter.node.etl.load.loader.db.context.FileLoadContext;
import com.alibaba.otter.node.etl.load.loader.interceptor.LoadInterceptor;
import com.alibaba.otter.node.etl.load.loader.weight.WeightController;
import com.alibaba.otter.shared.common.model.config.ConfigHelper;
import com.alibaba.otter.shared.common.model.config.data.DataMedia;
import com.alibaba.otter.shared.common.model.config.data.DataMediaSource;
import com.alibaba.otter.shared.etl.model.*;
import com.google.common.base.Function;
import com.google.common.collect.OtterMigrateMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 针对RowData的数据载入实现
 * 
 * @author jianghang 2019-5-13 上午11:15:48
 * @version 4.0.0
 */
public class RocketMQLoader implements OtterLoader<DbBatch, List<LoadContext>>, BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQLoader.class);
    private ExecutorService     executorService;
    private BeanFactory         beanFactory;
    private ConfigClientService configClientService;
    private LoadInterceptor     dbInterceptor;

    public List<LoadContext> load(DbBatch data) {
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }
}
