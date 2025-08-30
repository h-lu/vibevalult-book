# 第十章：对抗“匿名”：认证，回答“你是谁”

> "认识你自己。" - 德尔斐神谕

*   **本章危机**: 我们的VibeVault是一个完全开放和匿名的世界。任何人都可以访问所有API，可以随意查看、创建、修改甚至删除任何播放列表。我的VibeVault不是“我的”。**我们面临“匿名危机”**。
*   **本章顿悟**: 我们需要引入“身份”的概念，来可靠地回答第一个根本问题：“你是谁？” 这就是**认证 (Authentication)**。

---
## 本章结构

*   **`10.1-why-authentication.md`: 为何需要认证？**
    *   **核心思想**: 从第一性原理出发，深入探讨认证与授权的本质区别。明确本章的核心任务是解决“你是谁”的问题。
    *   **Vibe Check**:
        1.  **思考**: 区分现实世界场景中的认证与授权（如图书馆门禁 vs. 借阅权限）。
        2.  **AI协同**: 了解常见的认证方式及其优缺点。

*   **`10.2-secure-by-default.md`: 第一道防线：默认安全**
    *   **核心思想**: 引入Spring Security依赖，让读者亲身体验所有API突然被“锁住”的“默认安全”原则。顿悟：安全系统宁可“错杀”，绝不“放过”，这是一种保护机制，而非bug。
    *   **Vibe Check**:
        1.  **核心练习**: 添加`spring-boot-starter-security`依赖，重启应用，并使用Postman或浏览器验证API返回401/403错误。
        2.  **编码练习**: 创建`SecurityConfig`，使用`.requestMatchers("/**").permitAll()`暂时放行所有请求，证明我们已获得配置系统的能力。

*   **`10.3-user-and-password.md`: 数字身份的基石：用户与密码**
    *   **核心思想**: 创建`User`实体。直面“如何存储密码”的危机，从第一性原理顿悟“**绝不存储明文密码**”的铁律，并引入`PasswordEncoder` (BCrypt) 作为解决方案。
    *   **Vibe Check**:
        1.  **编码练习**: 创建`User`实体、`UserRepository`，并在`SecurityConfig`中定义`PasswordEncoder` Bean。
        2.  **AI协同**: "请解释BCrypt这类现代哈希算法与MD5这类旧算法相比，在安全性上有什么根本优势（例如‘加盐’和‘慢哈希’）？"

*   **`10.4-the-front-gate.md`: 敞开大门：构建认证API**
    *   **核心思想**: 遵循三层架构，创建`AuthenticationService`来编排注册和登录的业务逻辑。构建`AuthenticationController`作为API入口。在`SecurityConfig`中，精确地只放行`/api/auth/**`，其他所有API (`anyRequest()`) 依然是`authenticated()`。
    *   **Vibe Check**:
        1.  **核心练习**: 实现`signup`和`signin`的API。
        2.  **集成测试**: **[关键]** 编写第一个集成测试，用代码验证注册成功，以及使用错误密码登录会失败。建立起测试这张“安全网”。

*   **章末危机**: 用户可以注册和登录了。但他们会立刻发现一个新问题：**登录成功后，服务器转身就忘了我是谁！** 这自然地创造了下一章的“失忆危机”。