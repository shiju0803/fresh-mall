#### 简介
- fresh-mall 针对中小商户、企业和个人学习者开发。使用Java编码，采用SpringBoot、Mybatis-Plus等易用框架，适合个人学习研究。同时支持单机部署、集群部署，用户与店铺范围动态定位，中小商户企业可根据业务动态扩容。fresh-mall使用uniapp前端框架，可同时编译到 微信小程序、H5、Android App、iOS App等几个平台，可为中小商户企业节约大量维护成本。也可支撑中小商户企业前期平台横扩需求。

#### 社区

- QQ讨论群：838613833 (进群前，请在网页右上角点star)
- 数据库初始化sql文件，请进入讨论交流群，群文件自行下载，欢迎讨论与交流

#### fresh-mall项目结构:

- Java 后端服务
    - fresh-mall-admin: 启动器（打包打这个就行）
    - fresh-mall-admin-api: 提供管理员管理系统的WebApi
    - fresh-mall-app-api: 提供APP、小程序、H5用户请求的WebApi
    - fresh-mall-rider-api: 提供骑手APP、小程序、H5用户请求的WebApi
    - fresh-mall-framework: 提供通用业务代码
    - fresh-mall-system: 提供数据模型以及数据访问层封装
    - fresh-mall-common: 提供注解、工具类等
    - fresh-mall-generator: 代码生成器
    
- Vue 前端页面
    - fresh-mall-admin-ui: 基于element-ui的后台管理页面
    - fresh-mall-app-ui: 基于uniapp的小程序、H5、APP前端代码
    - fresh-mall-rider-ui: 基于uniapp的小程序、H5、APP骑手代码
  
- 常见问题汇总（持续更新中）
  - 常见问题总体解决方案 [点我进入](https://gitee.com/zhengkaixing/fresh-mall/wikis/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98/%E5%89%8D%E7%AB%AFbuild%E6%89%93%E5%8C%85%E4%B8%80%E7%9B%B4%E5%8D%A1%E4%BD%8F)&nbsp;&nbsp;
  
- sql: 数据库初始化SQL脚本

#### 数据库初始化sql文件，请进入讨论交流群，群文件自行下载，欢迎讨论与交流
---
#### 优先更新地址

- [01-kxmall源码地址](https://gitee.com/zhengkaixing/kxmall.git) https://gitee.com/zhengkaixing/kxmall.git

---
#### 客户端系统演示

- 下面是微信小程序真机模式调试的界面，可Android安装Apk,也可同时支持苹果。
在这基础上，还增加了H5。可内置到微信公众号上，变成公众号商城！尽情体验！


---
- H5客户端（可打包成小程序、APP）
  - app(apk下载地址): [kxmall-apk](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall.apk)
  - 演示地址: [https://h5.kxmall.vip](https://h5.kxmall.vip)
  - 登录名:13333333333 验证码:666666 （访问请打开浏览器F12开发模式,使用手机模式进行操作）
  - 客户端由于调用地图需要https，所以程序目前固定id为11仓库
- 微信小程序-体验（可打包成小程序、APP）
  - 已跳过支付模块，可正常体验操作流程（注意：需要自己手动获取一下定位，方可正常使用。）
  - ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/weixin-mini2.jpg)
- Pages

| 河禾生鲜 | 河禾生鲜 | 河禾生鲜 |
| :----: | :----: | :----: |
| ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-1.jpeg)  | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-2.jpeg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-3.jpeg) |
| ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-4.jpeg)  | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-5.jpeg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-6.jpeg) |
| ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-7.jpeg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-8.jpeg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-app-9.jpeg) |


| 商家订单推送 | 骑手订单推送 |
| :----: | :----: |
| ![订单推送](https://kxmalls.oss-cn-hangzhou.aliyuncs.com/kxmalls10.jpg) |  ![订单推送](https://kxmalls.oss-cn-hangzhou.aliyuncs.com/kxmalls9.jpg)

#### 后台端系统演示

- 使用免费开源框架vue-element-admin，基于element-ui的后台管理页面！尽情体验！


---
- Admin后台
  - 演示地址: [https://www.kxmall.vip](https://www.kxmall.vip)
  - 登录名:guest 密码:123456 (guest仅有只读权限，无读配置权限)
  - 登录名(超级管理员):admin (密码已设置默认)
- Pages
 
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-7.png)  
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-6.png)  
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-5.png)   
![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-admin-4.png)   


#### 骑手端系统演示

---
- h5骑手后台（可打包成小程序、APP）
  - 演示地址: [https://rider.kxmall.vip](https://rider.kxmall.vip)
  - 登录名:16666666666 验证码:123456 （访问请打开浏览器F12开发模式,使用手机模式进行操作）
- 微信小程序-体验（可打包成小程序、APP）
  - 微信一键登录（注意：需要进入管理后台进行审核，方可正常使用。）
  - ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/weixin-mini3.jpg)
- Pages

| 河禾生鲜 | 河禾生鲜 | 河禾生鲜 |
| :----: | :----: | :----: |
| ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-rider-1.jpg)  | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-rider-2.jpg) | ![河禾生鲜](https://nontax.oss-cn-beijing.aliyuncs.com/kxmall/kxmall-rider-3.jpg) |


#### 项目部署方式

>项目部署
##### ⓪ 服务器推荐
服务器可根据自身业务来选购，单机环境推荐2C4G

##### ① 基础运行环境

| 运行环境 | 版本号 |
|:--------|:--------|
|  MySQL   |  5.7（推荐）   |
|  JDK   |  1.8（推荐）   |
|  Redis   |  4.0.1（其他也可以）   |
|  Nginx  |  只要Web容器就可以了  |

Redis安装可直接使用yum安装 
	
	yum install redis

安装完成后使用 redis-cli 命令，若能进入，则表示redis安装完成

1.服务器安装必备软件[JDK | mysql | Redis | Nginx]

本地部署文档

[kxmall本地项目启动文档](doc/kxmall本地项目启动文档.doc)

## 免责声明

本软件框架禁止任何单位和个人用于任何违法、侵害他人合法利益等恶意的行为，禁止用于任何违反我国法律法规的一切平台研发，任何单位和个人使用本软件框架用于产品研发而产生的任何意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、附带或衍生的损失等)，本团队不承担任何法律责任。本软件框架只能用于公司和个人内部的法律所允许的合法合规的软件产品研发。
