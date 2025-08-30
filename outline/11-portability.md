# 第十一章：对抗“遗忘”：JWT，无状态的数字护照

> "记忆不是记录，而是对过去的重建。"

*   **本章危机**: HTTP协议是“健忘”的（无状态）。我刚刚才登录，为什么下一个请求服务器就不认识我了？传统的Session方案在现代分布式系统下又有何弊端？**我们面临“失忆危机”**。
*   **本章顿悟**: 我们需要一张“自包含的数字护照”，让用户可以随身携带自己的身份证明，而服务器无需记忆。这就是**JWT (JSON Web Token)**。

---
## 本章结构

*   **`11.1-why-jwt.md`: 为何选择JWT？无状态的数字护照**
    *   **核心思想**: 从第一性原理出发，通过对比Session的有状态和JWT的无状态，深刻理解为何JWT是现代可扩展架构的基石。剖析JWT的`Header.Payload.Signature`三段式结构。
    *   **Vibe Check**:
        1.  **思考**: 对比Session和JWT在解决多服务器认证问题上的根本不同。
        2.  **AI协同**: 探究JWT的`Header.Payload.Signature`三段式结构。

*   **`11.2-the-passport-office.md`: 数字护照办公室：签发JWT**
    *   **核心思想**: 创建`JwtService`，其**唯一职责**是根据用户信息和密钥**生成**一个Token。学习如何通过`application.properties`安全地配置密钥。
    *   **Vibe Check**:
        1.  **核心练习**: 引入`jjwt`库，创建`JwtService`，实现`generateToken`方法。
        2.  **编码练习**: 修改`AuthenticationService`的`signin`和`signup`方法，使其在成功后返回一个真实的JWT。
        3.  **验证**: 使用Postman或集成测试调用登录接口，确认能收到一长串JWT字符串。

*   **`11.3-the-gatekeeper.md`: 城门的哨兵：验证JWT**
    *   **核心思想**: 创建`JwtAuthenticationFilter`。顿悟：过滤器(Filter)是在请求到达Controller之前的“检查站”。这个“哨兵”的职责是拦截请求、检查JWT、验证签名，并将用户信息放入`SecurityContextHolder`。
    *   **Vibe Check**:
        1.  **核心练习**: 实现`JwtAuthenticationFilter`并将其添加到`SecurityConfig`的过滤器链中，同时将Session策略设为`STATELESS`。
        2.  **编码练习**: 创建一个简单的受保护端点（如`/api/test/user`），尝试在不带/带JWT的情况下访问它，观察结果。

*   **`11.4-the-diplomatic-channel.md`: 外交豁免：处理CORS**
    *   **核心思想**: 当我们尝试将第九章的前端与本章的后端连接时，会自然地撞上CORS这堵墙。从“同源策略”的第一性原理出发，理解CORS的必要性，并在`SecurityConfig`中建立“外交豁免通道”。
    *   **Vibe Check**:
        1.  **核心练习**: 在`SecurityConfig`中添加CORS配置。
        2.  **实验**: 尝试注释掉CORS配置，在浏览器中观察失败的请求；取消注释后观察成功。

*   **章末危机**: 系统现在能完美地识别“你是谁”了。但一个更危险的问题浮现了：**用户A登录后，可以随意通过API操作用户B的数据！** 这创造了最终章的“所有权危机”。