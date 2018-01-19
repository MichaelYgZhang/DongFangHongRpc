# DongFangHong-RPC
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
2. neety server client  say hello  mode  TODO
3. spring mode integrate
4. 1th demo
5. request, response, 
6. Serializable, decode, encode
7. load balance, filter, log .... others
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

```

- 模仿参见[huangyong](https://gitee.com/huangyong/rpc), [dubbo](https://github.com/alibaba/dubbo)
