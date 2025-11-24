# 第十章：构建安全堡垒：从认证到授权的完整旅程

> "你的，才是你的。不是你的，你不能碰。"

*   **本章危机**: 打开你第八章的代码 (`chapter-code/ch08`)。目前的系统是完全开放的。任何人都可以通过API删除所有数据。
*   **本章顿悟**: 我们需要在现有的代码基础上，一层层加上安全防护：认证、JWT、授权。

---
## 本章结构

*   **`10.1-security-crisis.md`: 安全危机：匿名的世界**
    *   **步骤**:
        1.  **添加依赖**: 在 `build.gradle.kts` 中添加 `spring-boot-starter-security`。
        2.  **重启应用**: 再次访问 `http://localhost:8080/api/playlists`。观察发生了什么？（应该返回401 Unauthorized）。
        3.  **理解**: 这就是“默认安全”。

*   **`10.2-authentication.md`: 第一道防线：认证**
    *   **步骤**:
        1.  **创建User实体**: 在 `model` 包下新建 `User.java`。它应该包含 `id`, `username`, `password` (加密后的) 等字段。
        2.  **关联数据库**: 确保 `User` 实体是一个 `@Entity`，并能被Hibernate自动映射到你第八章配置好的PostgreSQL数据库中。
        3.  **实现注册API**: 编写 `AuthController`。在注册时，**必须**使用 `BCryptPasswordEncoder` 对密码进行加密，然后再保存到数据库。

*   **`10.3-jwt-passport.md`: 数字护照：JWT无状态认证**
    *   **步骤**:
        1.  **引入JWT库**: 添加 `jjwt` 依赖。
        2.  **编写JwtService**: 创建一个服务，专门负责生成Token和解析Token。
        3.  **编写Filter**: 创建 `JwtAuthenticationFilter`。把它加入到Spring Security的过滤器链中。
        4.  **测试**: 使用Postman调用登录接口，获取Token。然后把Token放到Header中 (`Authorization: Bearer <token>`)，再次访问API，验证是否能通。

*   **`10.4-authorization.md`: 第二道防线：授权**
    *   **步骤**:
        1.  **重构Playlist**: 打开 `Playlist.java`。如果第八章用的是 `String name` 作为主键，现在必须重构为 `Long id`。
        2.  **添加关联**: 在 `Playlist` 中添加 `@ManyToOne private User owner;`。
        3.  **数据迁移**: 重新运行应用，Hibernate会自动更新数据库表结构（注意：这可能会清空现有数据，或者需要手动处理SQL）。
        4.  **添加保护**: 在 `PlaylistController` 的删除方法上，添加逻辑：只有 `playlist.getOwner().equals(currentUser)` 才能删除。

*   **`10.5-security-testing.md`: 验证安全：集成测试**
    *   **步骤**:
        1.  **编写测试**: 在 `src/test/java` 下，编写一个新的集成测试。
        2.  **模拟场景**: 模拟用户A登录，获取Token，然后尝试删除用户B的歌单。断言结果是失败的。
