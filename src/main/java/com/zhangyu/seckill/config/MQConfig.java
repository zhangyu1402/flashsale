package com.zhangyu.seckill.config;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zhangyu.seckill.bean.MQConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Configuration
public class MQConfig {
    private static final Logger logger = LoggerFactory.getLogger(MQConfig.class);

    /**
     * RabbitMQ cluster configuration
     */
    @Value("${rabbitmq.address-list}")
    private String addressList;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    @Value("${rabbitmq.virtual-host}")
    private String virtualHost;

    @Value("${rabbitmq.queue}")
    private String queue;

    @Bean
    public MQConfigBean mqConfigBean() {
//        MQConfigBean mqConfigBean = new MQConfigBean();

        if (StringUtils.isEmpty(addressList)) {
            throw new InvalidPropertyException(MQConfigBean.class, "addressList", "rabbitmq.address-list is Empty");
        }

        String[] addressStrArr = addressList.split(",");
        List<Address> addressList = new LinkedList<Address>();
        for (String addressStr : addressStrArr) {
            String[] strArr = addressStr.split(":");

            addressList.add(new Address(strArr[0], Integer.valueOf(strArr[1])));
        }
        MQConfigBean mqConfigBean = new MQConfigBean.Builder().addressList(addressList)
                .username(username)
                .password(password)
                .publisherConfirms(publisherConfirms)
                .queue(queue)
                .virtualHost(virtualHost).build();
        return mqConfigBean;
    }

    @Bean("mqConnectionSeckill")
    public Connection mqConnectionSeckill(@Autowired MQConfigBean mqConfigBean) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUsername(username);

        factory.setPassword(password);

        factory.setVirtualHost(virtualHost);

        return factory.newConnection(mqConfigBean.getAddressList());
    }
    @Bean("mqConnectionReceive")
    public Connection mqConnectionReceive(@Autowired MQConfigBean mqConfigBean) throws IOException, TimeoutException {
        return mqConnectionSeckill(mqConfigBean);
    }
}
