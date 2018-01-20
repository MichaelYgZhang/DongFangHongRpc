# DongFangHong-RPC 
###### 东方红象征着一切伟大开始的第一步,信息交流核心控制权,小巧灵活,就像RPC在互联网中的位置一样,所以命名为DongFangHong,希望这将是一个一切好的开端
simple rpc use neety,zookeeper,spring

- git本地上次项目出现的问题以及解决的办法

```js
1. cd /DongFangHongRpc 
2. git init
3. .gitingore
4. git add .
5. git commit -m "init"
6. github create new repository
7. cd DongFangHongRpc   $ git remote add origin git@github.com:MichaelYgZhang/DongFangHongRpc.git
8. git pull origin master --allow-unrelated-histories
9. git merge conflict
10. git push origin master
```

```js
计划
1. zookeeper register mode  finished
2. neety server client  say hello  mode
3. spring mode integrate
4. 1th demo
5. request, response, 
6. Serializable, decode, encode
7. load balance, filter, log .... others
8. TODO
```

```js
1. 创建RpcService注解类
@Target({ElementType.TYPE})	//类，接口，枚举
@Retention(RetentionPolicy.RUNTIME)	//运行时有效
@Component //spring 容器扫描  spring-context
public @interface RpcService {
    Class<?> value();
    String version() default "";
}

2. 扫描带有RpcService的注解并初始化handlerMap，获取接口名称以及版本号，如下:
重写ApplicationContextAware.setApplicationContext(ApplicationContext ctx)
Map<String, Object> serviceBeansMap = ctx.getBeansWithAnnotation(RpcService.class);
@RpcService(value = HelloService.class, version = "sample.hello2")
public class HelloServiceImpl2 implements HelloService {//略}

3.分为以下几个步骤处理
 a. 重写InitializingBean.afterPropertiesSet();
 b. NIO(不推荐)/Netty(推荐) 做接收数据->解码->处理->编码->返回数据处理
 c. 注册服务到zookeeper 

4. ✅
```

##### 总结
- 历时3天模仿完成miniRPC-DongFangHongRPC，现在作出如下总结
- 已完成功能点:Zookeeper提供服务注册与发现；Netty，SocketChannel完成通信；序列化基于Protostuff实现；Proxy.newProxyInstance(...); 动态代理模式调用Server；还有比如负载均衡采用随机，采用Builder模式构造请求对象等.目前阶段属于第一步阶段,能够完成基本的Client调用Server功能，调用依赖关系如下:

-  ![dubbo](http://dubbo.io/books/dubbo-dev-book/sources/images/dubbo-relation.jpg)

- TODO 
	- 增加文件配置,如果调用Zookeeper失败，则调用本地配置文件,比如超时配置,版本,IP,Port....
	- 服务发现增加Cache,增加ZookeeperWatch机制
	- 负载均衡增加其他比如Hash,轮询机制等
	- Filter拦截过滤功能
	- Socket传输安全问题
	- 线程池子，序列化扩展，监控功能....

- 模仿参见[huangyong](https://gitee.com/huangyong/rpc), [dubbo](https://github.com/alibaba/dubbo)
