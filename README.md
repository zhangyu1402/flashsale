# Flashsale

![licence](<https://img.shields.io/badge/license-mit-brightgreen>) ![spring version](<https://img.shields.io/badge/SpringBoot-2.1.2.RELEASE-brightgreen>) ![version](<https://img.shields.io/badge/version-1.0.0-brightgreen>)

Flashsale is a microservice to handle high concurrency scenario. This project idea comes to my mind when I want to buy Travis scott x AJ1 on nike's website on a flashsale activity. But I failed because the website crashed. I know it hard to handle such a high volume of request in a short period of time. So I want to learn learn the technique about this and try by myself.

## Installation

First your have to clone my project from my [github](https://www.github.com/zhangyu1402/flashsale). Before you run my program, you have to make sure you have all the software dependencies.

1. Redis

   pull docker redis image. (of cause you must have docker on your machine)

   ```bash
   docker pull redis
   ```

   Then we need a config file. we can copy a sample on <http://download.redis.io/redis-stable/redis.conf> 

   ```bash
   cd /
   mkdir yzh/redis/config
   cd yzh/redis/config
   touch redis.conf
   vim redis.conf
   ```

   we have to change some config in that sample:  _bind_, _protected-mode_, _requirepass_. Then we can start that image:

   ```bash
   docker run -d \
   -p 6379:6379 \
   -v /yzh/redis/config/redis.conf:/etc/redis/redis.conf \
   --privileged=true \
   --name redis \
   redis \
   redis-server /etc/redis/redis.conf
   ```

2. RabbitMQ

   pull docker RabbitMQ image just like before.

   ```bash
   docker pull rabbitmq:management
   ```

   This time we don't need extra config just run it.

   ```bash
   docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:management
   ```

3. MySQL

   ```bash
   docker pull mysql:5.6
   docker run --name mysql -e MYSQL_ROOT_PASSWORD=yourpassword -d -i -p 3306:3306 --restart=always  mysql:5.6
   ```

Until now, we have all the dependencies. Then you have to change the file _application-dev.properties_ and run my program. 

# Usage

Currently my project only support placing an order. 

API: POST  yourURL/seckill/order/{product_id}/{phone}/{md5}

Later I will add  service to confirm that order and pay for it. 

# Design

**Project depolyment Structure**

<img src="https://github.com/zhangyu1402/flashsale/blob/master/img/seckill_img1.png?raw=true" width="70%"/>



**Seckill Work Flow**

<img src="https://github.com/zhangyu1402/flashsale/blob/master/img/seckill_img2.png?raw=true" width="85%"/>



#Contributing

 Feel free to dive in! [Open an issue ](https://github.com/zhangyu1402/flashsale/issues/new) or submit PRs.



#License

 [MIT](https://opensource.org/licenses/MIT) © Yu Zhang


