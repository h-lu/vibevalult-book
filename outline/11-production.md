# 第十一章：迈向生产：容器化与云端部署

> "在我的电脑上可以运行。" - 每一个开发者都曾说过的谎言

*   **本章危机**: 你的代码在本地IDE里跑得很欢，但如果我让你把它部署到一台全新的Linux服务器上，你需要装多少东西？JDK？PostgreSQL？配置环境变量？
*   **本章顿悟**: 我们将把第八章的代码和环境打包进一个“盒子”里。

---
## 本章结构

*   **`11.1-production-crisis.md`: 生产危机：“在我电脑上能跑”**
    *   **核心思想**: 为什么需要Docker。

*   **`11.2-dockerfile.md`: 打包应用：编写Dockerfile**
    *   **步骤**:
        1.  **创建文件**: 在 `chapter-code/ch08` 根目录下创建一个名为 `Dockerfile` 的文件（无后缀）。
        2.  **编写内容**:
            *   使用 `FROM eclipse-temurin:21-jdk-alpine` 作为基础镜像。
            *   `COPY build/libs/*.jar app.jar`。
            *   `ENTRYPOINT ["java", "-jar", "/app.jar"]`。
        3.  **构建**: 运行 `./gradlew bootJar` 生成jar包，然后运行 `docker build -t vibevault .`。

*   **`11.3-docker-compose.md`: 容器交响乐：docker-compose**
    *   **步骤**:
        1.  **创建文件**: 在项目根目录创建 `docker-compose.yml`。
        2.  **定义服务**:
            *   `app`: 使用刚才构建的镜像。
            *   `db`: 使用 `postgres:15` 镜像。
        3.  **连接**: 配置 `app` 的环境变量 `SPRING_DATASOURCE_URL` 指向 `jdbc:postgresql://db:5432/vibevault`。
        4.  **持久化**: 务必为 `db` 服务配置 `volumes`，映射到宿主机的目录，确保**第八章辛苦存下的数据**不会丢失。
        5.  **启动**: `docker-compose up -d`。

*   **`11.4-cloud-deployment.md`: 部署到云端**
    *   **步骤**:
        1.  **准备服务器**: 购买一台便宜的云服务器。
        2.  **传输**: 将你的代码（或镜像）传输到服务器。
        3.  **运行**: 在服务器上安装Docker，运行 `docker-compose up`。
        4.  **验证**: 使用公网IP访问你的API。

*   **`11.5-epilogue.md`: 旅程的终点，亦是起点**
    *   **核心思想**: 恭喜你！你已经从第一章的 `record Song` 开始，亲手打造了一个完整的、安全的、云端运行的分布式系统。
