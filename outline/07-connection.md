# 第七章：对抗“孤立”：打开通往世界的大门

> "没有人是一座孤岛。" - 约翰·多恩

*   **本章危机**: 我们的应用已经坚实、可信、且秩序井然，但它是一个“孤岛”。它所有的美好，都只存在于它自己的命令行世界里。如何让其他人、其他程序也能享用它的能力？
*   **本章顿悟**: 我们需要打破“进程”的壁垒，通过“网络”这个媒介，建立一种标准化的“对话协议”，向全世界广播我们应用的能力。这个协议，就是“Web API”。

---
## 本章结构

*   **`01-why-api.qmd`: 为何需要API？应用程序的“外交协议”**
    *   **内容描述**: 本节将从第一性原理探讨，为何身处不同“进程”甚至不同“计算机”上的程序需要一种标准化的方式来对话。我们将揭示API（应用程序编程接口）的本质——一个明确定义的**通信契约（Contract）**。我们将聚焦于在Web上占据统治地位的REST API风格，理解它如何优雅地利用HTTP协议（`GET`, `POST`, `PUT`, `DELETE`）来表达对“资源”的操作。
    *   **Vibe Check (思考与练习)**:
        1.  **思考**: 你每天使用的手机App（例如，天气App、社交App），它们是如何获取最新信息的？它们背后是否都在通过网络调用某个API？你能想象一下“获取当前天气”的API契约可能长什么样吗？（例如，请求地址、返回的数据格式）。
        2.  **AI协同**: "请解释REST API与SOAP API在设计哲学上的主要区别。为什么REST最终在开放Web世界中胜出了？"
        3.  **设计**: 为我们的VibeVault应用设计一套核心的REST API契约。例如：
            *   获取所有播放列表：`GET /api/playlists`
            *   创建一个新播放列表：`POST /api/playlists`
            *   获取单个播放列表的详情：`GET /api/playlists/{id}`
            *   向某个播放列表添加歌曲：`POST /api/playlists/{id}/songs`

*   **`02-why-spring-boot.qmd`: 为何选择Spring Boot？站在巨人的肩膀上**
    *   **内容描述**: 构建一个能处理HTTP请求、解析URL、转换JSON、管理线程池的Web服务，需要处理大量繁琐的底层细节。本节将论证，我们为何不应该“重复发明轮子”。我们将理解Spring Boot的本质——**“约定优于配置”**和**“自动配置”**。它是一个强大的“生产力引擎”，通过整合一个内嵌的Web服务器（Tomcat）和大量的“Starter”依赖，让我们能用极少的代码，快速启动一个功能完备、生产级的Web服务，从而专注于API的“契约”本身。
    *   **Vibe Check (思考与练习)**:
        1.  **核心练习**: 将我们的Gradle项目，通过添加`spring-boot-starter-web`依赖，正式改造为一个Spring Boot应用。创建一个新的主类，标记`@SpringBootApplication`注解，并成功地启动它。观察控制台，看看Spring Boot自动为我们配置了什么。
        2.  **探索**: Spring Boot应用默认在哪个端口号上启动？请尝试通过修改`src/main/resources/application.properties`文件，将端口号改为`8888`。
        3.  **AI协同**: "请向我解释Spring Boot的核心思想之一‘依赖注入’（Dependency Injection）或‘控制反转’（Inversion of Control）。它和我们之前手动`new`一个对象相比，解决了什么根本问题？"

*   **`03-first-api.qmd`: 构建你的第一个API端点**
    *   **内容描述**: 在本节，我们将把之前设计的三层架构，与Spring Boot的Web层无缝集成。我们将学习`@RestController`, `@Service`, `@Repository`这些注解如何让Spring自动发现并管理我们的组件。我们将创建一个`PlaylistController`，并使用`@GetMapping`注解，暴露我们的第一个API端点`GET /api/playlists`，让它能通过浏览器或curl等工具被真实地访问到。
    *   **Vibe Check (思考与练习)**:
        1.  **核心练习**: 创建`PlaylistController`，注入`PlaylistService`，并编写一个方法来处理`GET /api/playlists`请求。这个方法应该调用服务层的方法，获取所有播放列表，并将其返回。启动应用，并使用浏览器访问`http://localhost:8080/api/playlists`，看看你能否看到（可能是空的）JSON数据。
        2.  **编码练习**: 实现`POST /api/playlists`端点。你需要使用`@PostMapping`和`@RequestBody`注解。使用Postman或类似的API工具，来测试你是否能成功地创建一个新的播放列表。
        3.  **DTO的重要性**: 直接从Controller返回我们的`Playlist`或`Song`领域对象，可能会暴露过多的内部细节。创建一个`PlaylistDTO`，并重构你的API，使其返回DTO对象。这个练习将让你深刻体会到API作为“公共契约”的稳定性是多么重要。
        4.  **思考**: `@RestController`注解和传统的`@Controller`注解有什么区别？（提示：与`@ResponseBody`有关）。