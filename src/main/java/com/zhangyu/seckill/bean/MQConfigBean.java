package com.zhangyu.seckill.bean;

import com.rabbitmq.client.Address;

import java.util.List;


public class MQConfigBean {

    /**
     *     RabbitMQ cluster config bean
     */
    private List<Address> addressList;
    private String username;
    private String password;
    private boolean publisherConfirms;
    private String virtualHost;
    private String queue;

    private MQConfigBean(Builder builder) {
        setAddressList(builder.addressList);
        setUsername(builder.username);
        setPassword(builder.password);
        setPublisherConfirms(builder.publisherConfirms);
        setVirtualHost(builder.virtualHost);
        setQueue(builder.queue);
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPublisherConfirms() {
        return publisherConfirms;
    }

    public void setPublisherConfirms(boolean publisherConfirms) {
        this.publisherConfirms = publisherConfirms;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }


    public static final class Builder {
        private List<Address> addressList;
        private String username;
        private String password;
        private boolean publisherConfirms;
        private String virtualHost;
        private String queue;

        public Builder() {
        }

        public Builder addressList(List<Address> val) {
            addressList = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder publisherConfirms(boolean val) {
            publisherConfirms = val;
            return this;
        }

        public Builder virtualHost(String val) {
            virtualHost = val;
            return this;
        }

        public Builder queue(String val) {
            queue = val;
            return this;
        }

        public MQConfigBean build() {
            return new MQConfigBean(this);
        }
    }
}
