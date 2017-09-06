# 单机运行应用
(pom.xml 含内置 tomcat 插件)
mvn clean package

# Tomcat 集群部署应用
1、mvn clean package -P prod
2、将打包的应用war包，复制至 tomcat1、tomcat2 中
3、修改 tomcat 中的server.xml 文件
4、运行 tomcat1、tomcat2
5、浏览器根据不同端口号分别访问处于不同 tomcat 中的应用页面

# Ngnix 负载均衡
1、安装 nginx 
2、启动 tomcat1、tomcat2
3、配置 nginx.conf ，导流请求至两个 tomcat 中处理

# Ngnix 静态加载
1、将静态资源 html、css 等存放于 nginx 服务器上

# Session 共享
基于 redis 的session共享，原理在于将 session 存储于redis中，所有tomcat服务器都接收请求时都从redis中读取相应session信息。
（这就是 session 服务器）

此处由于使用 tomcat9 ，现有 redis-session-manager 尚未支持，故无法实现
启动过程会出现部分类不存在，这是由于tomcat9中相应类已不存在
https://github.com/jcoleman/tomcat-redis-session-manager
从此处可知，其完整支持 tomcat6，7

实现方式:
1、tomcat6,7服务器
2、cluster_conf/tomcat/conf/context.xml 中的以下代码迁移至 tomcat/conf/context.xml 中
	<Manager className="com.orangefunction.tomcat.redissessions.RedisSessionManager"
         host="localhost" 
         port="6379" 
         database="0" 
		 password="hundsun@1"
		 maxInactiveInterval="60"/>
    配置好 redis。
3、cluster_conf/tomcat/lib 下 jar 包迁移至 tomcat/lib 中

