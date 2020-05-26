Spring下使用，基于redis的分布式锁-注解方式
===
## 使用方法

### 1、拉取源码后使用maven命令打包

        mvn clean package
        
### 2、引入到自己的maven工程中

        <dependency>
            <groupId>com.github.anzlee</groupId>
            <artifactId>distributed-lock</artifactId>
            <version>1.0</version>
        </dependency>

### 3、在需要使用分布式锁的方法上加上注解`@Lock`即可

        @RestController
        @RequestMapping("test")
        public class TestController {
        
            /**
             * 接口
             * @return
             */
            @RequestMapping(value = "", method = RequestMethod.GET)
            @Lock
            public Response<Object> test(){
                return new Response<>();
            }
        
        }

####该工程只适用于spring工程，如果不是spring工程，可以参考`com.github.anzlee.lock.redisson.DistributedRedisLock`类使用lock和unlock方法对代码块加解锁

## redisson实现原理

---
* 加锁

        1、判断有没有${key}

        2、如果没有，则在其下设置一个字段为“hash:Thread-1”，值为“1”的键值对 ，并设置它的过期时间

        3、如果存在，则进一步判断“hash:Thread-1”是否存在，若存在，则其值加1，并重新设置过期时间

        4、返回${key}的生存时间（毫秒）

* 解锁

        1、判断是否存在${key}

        2、如果不存在，向Channel中广播一条消息，广播的内容是0，并返回1

        3、如果存在，进一步判断字段hash:Thread-1是否存在

        4、若字段不存在，返回空，若字段存在，则字段值减1

        5、若减完以后，字段值仍大于0，则返回0

        6、减完后，若字段值小于或等于0，则广播一条消息，广播内容是0，并返回1；
        
* 等待
        
        1、当无法立即获取到锁的时候，订阅Channel，当资源可用时可以及时知道，并抢占，防止无效的轮询而浪费资源
        
        2、当资源可用用的时候，循环去尝试获取锁，由于多个线程同时去竞争资源，所以这里用了信号量，对于同一个资源只允许一个线程获得锁，其它的线程阻塞
          
---