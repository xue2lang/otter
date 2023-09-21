package com.alibaba.otter.client.controller;

import com.alibaba.otter.shared.common.model.config.data.DataMediaPair;
import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter.ArbitrateMode;
import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter.PipeChooseMode;
import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter.LoadBanlanceAlgorithm;
import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter.SelectorMode;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

import com.alibaba.otter.shared.common.model.config.pipeline.PipelineParameter;

import com.alibaba.otter.client.model.BaseModel;
import com.alibaba.otter.client.service.ConfigService;
import com.alibaba.otter.shared.common.model.config.pipeline.Pipeline;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author quanlinglong
 * @date 2023/8/24 17:15
 */
@RequestMapping("/pipelinetest")
@Controller
public class PipelineController {

    private static final Logger logger = LoggerFactory.getLogger(PipelineController.class);

    @Resource
    private ConfigService taskService;

    @ResponseBody
    @RequestMapping(value = "/pipeline/{pipelineId}", method = RequestMethod.GET)
    public BaseModel findPipeline(@PathVariable("pipelineId") Long key) {
        Pipeline pipeline = taskService.findPipeLine(key);
        if (pipeline != null) {
            return BaseModel.getInstance(pipeline);
        }
        return BaseModel.getInstance(key + " pipeline not exist");
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseModel savePipeline(@RequestBody Pipeline pipeline) {
        // todo channel id 要记下来，因为后续创建各项配置
        if (pipeline.getChannelId() == null) {
            pipeline.setChannelId(9L);
        }
        if (StringUtils.isBlank(pipeline.getName())) {
            pipeline.setName("test-pipeline");
        }
        pipeline.setDescription("测试pipeline创建");//描述
        // todo 获取node列表，而且要知道哪些已经被占用。otter是怎么判断被占用的
//        List<Node> nodes = taskService.listNodes();

        pipeline.setSelectNodes(Lists.newArrayList());//Select机器
        pipeline.setExtractNodes(Lists.newArrayList());//Extract机器，前端不展示
        pipeline.setLoadNodes(Lists.newArrayList());//Load机器
        // 设置同步数据库
        pipeline.setPairs(getPairs());
        pipeline.setGmtCreate(new Date());
        pipeline.setGmtModified(new Date());
        pipeline.setParameters(genParameter());

        taskService.savePipeline(pipeline);

        return BaseModel.getInstance("success");
    }

    private List<DataMediaPair> getPairs() {
        return Lists.newArrayList();
    }

    private PipelineParameter genParameter() {
        PipelineParameter parameter = new PipelineParameter();
        parameter.setPipelineId(null);
        parameter.setParallelism(5L);//并行度，默认是5

        parameter.setHome(false);// 是否主站点
        parameter.setExtractPoolSize(10);//数据反查线程数，默认10
        parameter.setLoadPoolSize(15);//数据载入线程数，默认15
        parameter.setFileLoadPoolSize(15);// 文件载入线程数，默认15
        parameter.setSelectorMode(SelectorMode.Canal); //数据同步来源
        parameter.setDestinationName("");// Canal名字，todo 根据名字找到canal id ？
        parameter.setMainstemClientId((short) 1001);//消费端ID，默认1001，前端不展示
        parameter.setMainstemBatchsize(6000);//消费批次大小，默认6000
        parameter.setBatchTimeout(-1L);//获取批次数据超时时间(毫秒)，默认-1，格式: -1不进行控制，0代表永久，>0则按照指定时间控制
        parameter.setLoadBatchsize(50);//Load批次大小，默认50

        //  高级设置
        parameter.setUseBatch(true);//使用batch，默认true
        parameter.setSkipSelectException(false);//跳过select异常，默认false
        parameter.setSkipLoadException(false);//跳过load异常，默认false
        parameter.setArbitrateMode(ArbitrateMode.AUTOMATIC);//仲裁器调度模式，默认自动
        parameter.setLbAlgorithm(LoadBanlanceAlgorithm.Stick);// 负载均衡算法
        parameter.setPipeChooseType(PipeChooseMode.AUTOMATIC);//传输模式，默认自动选择

        parameter.setDumpSelector(true);//记录selector日志
        parameter.setDumpSelectorDetail(false);//记录selector详细日志
        parameter.setDumpEvent(false);//记录load日志，默认false
        parameter.setDryRun(false);//dryRun模式
        parameter.setDdlSync(false);//支持ddl同步，默认true，此处用false
        parameter.setSkipDdlException(false);//是否跳过ddl异常
        parameter.setFileDetect(false); // 文件重复同步对比，默认false
        parameter.setUseFileEncrypt(false);//文件传输加密，默认false
        parameter.setUseExternalIp(false); //启用公网同步，默认false

        parameter.setSkipFreedom(false);//跳过自由门数据
        parameter.setSkipNoRow(false);//跳过反查无记录数据
        parameter.setUseTableTransform(false);// 启用数据表类型转化，默认false
        parameter.setEnableCompatibleMissColumn(true);//兼容字段新增同步，默认true
        parameter.setChannelInfo(null);//自定义同步标记
        parameter.setUseLocalFileMutliThread(false);

        // channel parameter
        parameter.setEnableRemedy(null);
        parameter.setSyncMode(null);
        parameter.setSyncConsistency(null);
        parameter.setRemedyAlgorithm(null);
        parameter.setMainstemClientId((short) 0);
        parameter.setSystemSchema(null);
        parameter.setSystemMarkTable(null);
        parameter.setSystemBufferTable(null);
        parameter.setRetriever(null);//下载方式
        parameter.setSystemMarkTableColumn(null);
        parameter.setSystemDualTable(null);
        parameter.setSystemMarkTableInfo(null);
        parameter.setRemedyDelayThresoldForMedia(null);//反差时间阈值
        return parameter;
    }

    @ResponseBody
    @RequestMapping(value = "/pipeline/{pipelineId}", method = RequestMethod.DELETE)
    public BaseModel removeChannel(@PathVariable("pipelineId") Long key) {
        taskService.removePipeline(key);
        return BaseModel.getInstance("remove pipeline, pipelineId: " + key);
    }


}
