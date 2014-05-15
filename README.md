mongodbSimplePerformanceTesting
===============================


一个mongodb的非常简单的支持调整读写线程的web小项目。

项目运行需要先开启mongodb，默认28017端口，配置文件在resource目录下，关于mongodb的连接配置也可以修改这个文件。

控制页面在index.jsp 
以tomcat为例，访问页面：
http://localhost:8080/MongodbPerformanceTestingTool/

读写线程的控制分离，可以同时作读写测试，两者之间无干扰。

读写线程数量不要设置过多，线程没有Thread.sleep()所以线程多了绝对死机。

页面想用折线图的方式显示，以后有时间再搞。//TODO

语言是scala开发的，不过不熟，基本上整个项目都是java的结构，scala以后熟练做代码重构。//TODO

我本机thinkpadx230单独读写1W+/sec ，一起读写 读和写 5000/s平均，不知道正常不正常。

如果您发现问题，欢迎您随时联系：weiyue9@gmail.com
