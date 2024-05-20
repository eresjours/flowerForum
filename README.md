# flowerForum

## README
README.md 为本项目的说明书，请仔细阅读

## pom
pom.xml 是 Maven 管理项目构建过程的关键配置文件,定义了项目的坐标、依赖、构建流程、插件配置等基本元素,是 Maven 项目不可或缺的组成部分  
通过 pom.xml 可以很好地统一项目的构建和依赖管理


## 项目结构

### cmcc
cmcc 为One net官方提供的Java-HTTP-SDK

### flower
flower 下存放所有项目后端代码
#### advice
自定义异常捕获
#### controller
负责请求转发，接受页面传递过来的参数，根据参数的不同，是调用不同的Service层方法进行操作，操作完成后将返回结果传递给页面。
#### dto
数据传输对象，主要将不同表进行组合使用
#### enums
定义了评论和通知使用到的枚举类
#### exception
自定义异常处理机制
1.Java枚举类CustomizeErrorCode,它实现了ICustomizeErrorCode接口。枚举类中定义了一系列错误代码及其对应的错误消息,这些常量可以在程序中统一使用和管理。  
2.Java自定义异常类CustomizeException,它继承自RuntimeException。该异常类的构造函数接收一个实现了ICustomizeErrorCode接口的对象作为参数,并将该对象的错误代码和错误消息存储到异常对象的code和message属性中。这样,在抛出自定义异常时,可以携带统一定义的错误代码和消息,方便错误处理和跟踪。  
3.Java接口ICustomizeErrorCode,它定义了两个方法getMessage()和getCode()。该接口的目的是为了规范自定义错误代码的结构,即错误代码必须包含一个错误消息和一个错误代码。  
#### interceptor
对所有URL路径设置拦截器检查用户登陆状态
#### mapper
它负责将领域对象和数据库表之间进行映射，实现数据的读取和写入, 包括查询、插入、更新和删除等操作。
#### model
模型，存储实体类的模型
#### provide
负责GitHub的Access Token的获取和用户信息的获取
#### service
Service层则承担着业务逻辑的处理和管理
#### CommunityApplication
项目运行入口


### static
static文件夹主要用于存放静态资源文件,如JavaScript、CSS和图像等  
当客户端请求这些静态资源时,Spring会直接从static文件夹中读取并响应,不需要再经过Spring的控制器处理  
这种做法可以提高响应静态资源的效率
#### css
css 中包含各种样式文件
如：bootstrap.css 用于引入流行的前端框架Bootstrap提供的样式  
***注意:*** community 中为自定义的css样式
#### figure
css 中包含虚拟形象样式  
js 中 l2d.js 为Live2D框架的实现,包含了模型管理、动画播放、交互处理等各方面的功能；pio.js 提供了多种个性化定制选项,如模型切换、触摸反应、植物环境获取、浇水等  
***live2d-widget-models 为虚拟形象的模型文件，可在这里添加自己喜欢的模型***
#### fonds
fonds 为 editormd 的插件的依赖文件
#### images
存储静态图像资源文件
#### js
主要用于实现各种交互效果、数据处理、页面动态渲染等功能
***注意：community.js 用于回复页面动态渲染和提交请求***

### templates
templates文件夹用于存放模板文件,使用了Thymeleaf作为模板引擎  
当用户请求一个页面时,对应的控制器方法会处理相关的业务逻辑,并把模型数据传递给模板引擎,由模板引擎结合.html模板和模型数据渲染出最终的HTML页面
#### index
index 为登录首页
#### admin
admin 为管理员界面  
adminLogin 为管理员登陆界面
#### error
error 错误界面
#### figure
虚拟形象显示界面  
***添加新模型后这里需要修改***
#### footer
页脚
#### navigation
导航栏
#### profile
展示个人问题和最新回复
#### publish
问题发布页面
#### question
问题展示界面

### application.properties
用于存储应用程序的各种配置属性  
总之,application.properties 是 Spring Boot 应用的核心配置文件,用于集中管理和自定义各种配置属性,对应用的顺利启动和正常运行起到重要作用。


## 依赖

### SpringBoot
Spring Boot 是一个基于 Spring 的框架，旨在简化 Spring 应用的配置和开发过程，通过自动配置和约定大于配置的原则，使开发者能够快速搭建独立、生产级别的应用程序。
### BootStrap3
Bootstrap 是一个用于快速开发 Web 应用程序和网站的前端框架。
自 Bootstrap 3 起，框架包含了贯穿于整个库的移动设备优先的样式。浏览器支持：所有的主流浏览器都支持 Bootstrap。
响应式设计：Bootstrap 的响应式 CSS 能够自适应于台式机、平板电脑和手机。
### mybatis
MyBatis是一个半自动的ORM（对象关系映射）持久层框架，它允许开发者直接编写SQL语句，同时提供了输入映射和输出映射的功能，使得数据库操作更加灵活和方便。
在Spring Boot项目中，通过添加`org.mybatis.spring.boot`依赖，可以轻松地将MyBatis框架集成进来。
这样做的好处是可以利用Spring Boot的自动配置特性，快速搭建起与数据库交互的持久层。这个依赖包含了必要的MyBatis和Spring Boot之间的配置，使得它们能够无缝工作。
### Maven
Maven是Java开发者的强大助手，它简化了项目构建和管理过程，使开发者能够专注于编码和业务逻辑的实现。它的自动化和标准化特性，使得项目的构建和维护变得更加高效和一致
### live2D
Live2D是一种先进的绘图渲染技术，它能够让二维图像在保持原有风格和质感的同时，展现出动态和近似三维的效果。
Live2D是一项将2D图像“赋予生命”的技术，它极大地丰富了数字内容的表现力和互动性。
更多模型请访问[Live2D GitHub仓库](https://github.com/imuncle/live2d)
### H2database
H2 Database 是一个方便、灵活且易于使用的数据库，适用于开发、测试和嵌入式场景。
### lombok
Lombok是一个Java库，能自动插入编辑器并构建工具，简化Java开发
这里使用 lombok 的 @Data 注解自动生成 getter 和 setter 方法
### Editor.md
Editor.md 是一款开源的、可嵌入的 Markdown 在线编辑器（组件），基于 CodeMirror、jQuery 和 Marked 构建。
### okhttp3
OKhttp是一个功能强大且易于使用的网络请求库，它通过各种优化手段提供了快速和稳定的网络通信能力。