#!/usr/bin/env bash
#1、关闭两个 tomcat
#2、删除两个 tomcat 中原有的应用
#3、打包应用，复制一份新命名的 war ，部署到 tomcat 后，运行时会自动解压，由于解压后应用 war 包名一致，此处是为了浏览器访问路径更改为 beauty_ssm_cluster,
#而不是 beauty_ssm_cluster_1.0-SNAPSHOT_20170825/
#4、部署 war 包至两个 tomcat 中
#5、删除复制的 war 包
#6、启动两个 tomcat（集群）
#补充：由于原有打包策略中带有日期，此处还有待进一步完善

./cluster_conf/tomcat/tomcat_9.0_1/bin/shutdown.sh
./cluster_conf/tomcat/tomcat_9.0_2/bin/shutdown.sh

# 获取当前目录的绝对路径 -- $(cd `dirname $0`; pwd)
# basepath 代表根目录：beauty_ssm_cluster/
cd ..
basepath=$(cd `dirname $0`; pwd)
echo $basepath

rm -rf cluster_conf/tomcat/tomcat_9.0_1/webapps/beauty_ssm_cluster.war
rm -rf cluster_conf/tomcat/tomcat_9.0_2/webapps/beauty_ssm_cluster.war

mvn clean package -P prod -DskipTests

cd target
cp beauty_ssm_cluster_1.0-SNAPSHOT_20170827.war beauty_ssm_cluster.war

cd $basepath
cp target/beauty_ssm_cluster.war cluster_conf/tomcat/tomcat_9.0_1/webapps
cp target/beauty_ssm_cluster.war cluster_conf/tomcat/tomcat_9.0_2/webapps

rm -rf target/beauty_ssm_cluster.war

./cluster_conf/tomcat/tomcat_9.0_1/bin/startup.sh
./cluster_conf/tomcat/tomcat_9.0_2/bin/startup.sh
