package com.alibaba.otter.client.communication;

import com.alibaba.otter.shared.communication.core.CommunicationClient;
import com.alibaba.otter.shared.communication.core.exception.CommunicationException;
import com.alibaba.otter.shared.communication.core.impl.DefaultCommunicationClientImpl;
import com.alibaba.otter.shared.communication.core.model.Callback;
import com.alibaba.otter.shared.communication.core.model.Event;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.util.Arrays;
import java.util.List;

/**
 * 封装了基于communication通讯的工具
 *
 * @author quanlinglong
 * @date 2023/8/28 11:23
 */
public class CoreCommunicationClient implements DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(CoreCommunicationClient.class);

    private CommunicationClient delegate;

    private List<String> managerAddress;
    private volatile int index = 0;

    public Object callManager(final Event event) {
        CommunicationException ex = null;
        Object object = null;
        logger.info("managerAddress print=" + managerAddress);
        for (int i = index; i < index + managerAddress.size(); i++) { // 循环一次manager的所有地址
            String address = managerAddress.get(i % managerAddress.size());
            logger.info("address print =" + address);
            try {
                object = delegate.call(address, event);
                index = i; // 更新一下上一次成功的地址
                return object;
            } catch (CommunicationException e) {
                // retry next address;
                ex = e;
            }
        }

        throw ex; // 走到这一步，说明肯定有出错了
    }

    /**
     * 指定manager，进行event调用
     *
     * <pre>
     * 注意：该方法为异步调用
     * </pre>
     */
    public void callManager(final Event event, final Callback callback) {
        if (delegate instanceof DefaultCommunicationClientImpl) {
            ((DefaultCommunicationClientImpl) delegate).submit(new Runnable() {

                public void run() {
                    Object obj = callManager(event);
                    callback.call(obj);
                }
            });
        }
    }

    public void destroy() throws Exception {
    }

    // ================== setter / getter =====================

    public void setDelegate(CommunicationClient delegate) {
        this.delegate = delegate;
    }

    public void setManagerAddress(String managerAddress) {
        String server = StringUtils.replace(managerAddress, ";", ",");
        String[] servers = StringUtils.split(server, ',');
        this.managerAddress = Arrays.asList(servers);
        this.index = RandomUtils.nextInt(this.managerAddress.size()); // 随机选择一台机器
    }


    public String getManagerAddress() {
        return StringUtils.join(managerAddress, ",");
    }
}
