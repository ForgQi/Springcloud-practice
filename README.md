# 使用了spring cloud的后端程序
基于Java 11
这个项目是一个比较简单的项目，包含了
- spring cloud gateway
- feign
- consul
- redis
- elasticsearch
- spring actuator

项目是使用docker部署的，实践了gitlab-ci的方式自动构建部署。身份验证是基于oauth2，spring cloud gateway作为网关中介请求，
同时支持cookie与token两种方式登录。

consul作为注册中心与配置中心，支持配置动态更改。目前项目分为两个服务，一个oauth2验证服务，
一个提供其他所有服务，用spring boot admin进行可视化的服务监控，管理各个服务。

用elasticsearch作全文搜索，redis存储临时数据与缓存，日志通过socket的方式异步发送到Logstash，然后由Logstash转发至elasticsearch，
最后由kibana可视化分析。

主要是实践了一些主要功能的实现方式都不太深入，比如ci/cd，微服务，日志处理，数据库审计，全文搜索。
还实践了Java11的一些新特性用Java11的HTTP Client替换了feign的 client。

spring方面学习了一些基本的用法，各种bean怎么构造，条件注入。用了spring的一些很方便的注解，拦截器，线程池，重试。也简单了解一些设计模式。
因为是第一个spring boot项目，所以学习的东西都比较浅，但是覆盖的比较广，主要还是考虑怎么实现更好多一些，用户量不太大，大约1万左右，服务器性能
相比较是过剩的，所以没有碰到一些比较深入的场景，由于项目比较小很多设计还是多余的，比如微服务。
