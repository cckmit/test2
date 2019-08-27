SETUP:

1. ECLIPSE MARKET PLACE: 搜索STS并安装
2. 安装MQTT包到本地maven仓库（注意替换文件路径）： mvn install:install-file -Dfile=D:\repository\robotserver\backend\libs\hemamqtt.jar -DgroupId=com.hema.mqtt -DartifactId=com.hema.mqtt -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true
