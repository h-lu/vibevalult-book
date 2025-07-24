# 第四章：对抗“遗忘”：让记忆穿越关机的黑暗

> "记忆是唯一不会被夺走的财富。"

*   **本章危机**: 我们在程序中精心创造的一切，随着程序窗口的关闭而灰飞烟灭。我们遭遇了第一次、也是最深刻的一次存在主义危机：“遗忘”。我们的创造物，原来只是一个短暂的幻影。
*   **本章顿悟**: 内存是思想的沙盒，但磁盘是历史的石碑。我们需要将内存中转瞬即逝的对象，“序列化”为一种可记录的格式，并将其刻写在“非易失”的磁盘上，从而赋予我们的创造物穿越时间的能力。

---
## 本章结构

*   **`01-why-persistence.qmd`: 为何需要持久化？RAM vs. Disk**
    *   **内容描述**: 本节将深入探讨计算机存储的物理现实。我们将从第一性原理出发，理解为什么计算机需要同时拥有RAM（内存）和Disk（磁盘）这两种存储介质。我们将通过对比它们在**速度、成本、易失性**这三个维度上的巨大差异，来揭示“持久化”的本质——它是在这些物理限制下，为了让数据“幸存”下来而必须进行的一次“跨介质迁移”。
    *   **Vibe Check (思考与练习)**:
        1.  **思考**: 你的电脑上有哪些信息是“易失”的（例如，剪贴板里复制的内容），哪些是“持久”的（例如，你保存在桌面上的一个Word文档）？这个区别对你日常使用电脑的方式有何影响？
        2.  **AI协同**: "请解释‘序列化’（Serialization）和‘反序列化’（Deserialization）的通用概念。除了我们即将使用的CSV文本格式，还有哪些常见的序列化格式（如JSON, XML, Protocol Buffers），请AI画一个表格对比它们各自的优缺点，特别是在‘可读性’和‘效率’方面。"
        3.  **设计思考**: 我们选择将歌曲保存为CSV（逗号分隔值）格式。请在一张纸或一个文本文件中，设计出这个CSV的具体格式。例如，每行代表一首歌，歌的每个属性（歌名、作者、时长）应该按什么顺序排列？用什么符号分隔？这个设计过程，就是最原始的“数据建模”。

*   **`02-serialization.qmd`: 序列化：将对象翻译成文字**
    *   **内容描述**: 在本节，我们将亲手实现“序列化”和“反序列化”的逻辑。我们会为`Song`对象编写`toCsvString()`方法，将其内部状态转换为一行CSV文本。反过来，我们会编写一个静态方法`fromCsvString(String csv)`，它能解析一行CSV文本，并“重建”出一个`Song`对象。我们还将为`Playlist`类编写`saveToStrings()`和`loadFromStrings()`方法，来处理整个歌曲列表的转换。
    *   **Vibe Check (思考与练习)**:
        1.  **核心练习**: 在`Song` record中，实现`toCsvString()`方法和静态的`fromCsvString(String csv)`方法。确保`song.toCsvString()`之后再`Song.fromCsvString()`能得到一个完全相同的`Song`对象。
        2.  **编码练习**: 在`Playlist`类中，实现`saveToStrings()`方法，它返回一个`List<String>`，列表中的每个字符串都是一首歌的CSV表示。同时，实现`loadFromStrings(List<String> csvLines)`方法，它能清空当前播放列表，并用CSV行重建它。
        3.  **破坏性实验**: 手动创建一个包含错误格式的CSV字符串（例如，属性数量不对，时长不是数字），然后调用你的`Song.fromCsvString()`方法。程序会抛出什么异常（例如`ArrayIndexOutOfBoundsException`, `NumberFormatException`）？你应该在`fromCsvString`方法内部处理这些异常，还是让调用者（`Playlist`类）去处理？为什么？
        4.  **思考**: 如果我们的`Song`对象的某个属性（比如歌名）本身就包含了逗号，我们目前的CSV格式会崩溃。如何改进我们的序列化/反序列化逻辑来解决这个问题？（提示：可以给包含逗号的字段加上英文双引号，但这会让解析变得多复杂？）。

*   **`03-inscription.qmd`: 刻写石碑：现代文件I/O**
    *   **内容描述**: 我们已经能将对象和文字互换了，现在是时候将这些文字“刻”到磁盘上了。本节将学习Java现代I/O库（NIO.2）中极其简洁和强大的`Files`类。我们将使用`Files.write`和`Files.readAllLines`这两个静态方法，轻松地完成整个播放列表的写入和读取操作。
    *   **Vibe Check (思考与练习)**:
        1.  **核心练习**: 在`Playlist`类中，创建两个新的公共方法：`saveToFile(String filePath)` 和 `loadFromFile(String filePath)`。这两个方法将分别调用前一节中实现的`saveToStrings`和`loadFromStrings`，并使用`Files.write`和`Files.readAllLines`来完成实际的文件操作。
        2.  **编码练习**: 修改你的`main`方法。在程序启动时，自动调用`playlist.loadFromFile("playlist.csv")`。在用户选择“退出”后，调用`playlist.saveToFile("playlist.csv")`。现在，你的应用终于拥有了“记忆”！
        3.  **探索**: `Files`类中还有很多有用的方法。请尝试使用`Files.exists(path)`来检查文件是否存在，如果`playlist.csv`不存在，则跳过加载。再尝试使用`Files.size(path)`来获取文件的大小。

*   **`04-resilience.qmd`: 应对意外：处理I/O异常**
    *   **内容描述**: 与外部世界（如文件系统）的交互总是充满风险：文件可能不存在，磁盘可能已满，程序可能没有读写权限。本节将深入探讨Java的“受检异常”（Checked Exception）设计哲学，理解它如何强制我们成为更负责任的程序员。我们将学习使用`try-catch`块来优雅地处理`IOException`，并了解`try-with-resources`语法糖如何让资源管理变得简单而安全。
    *   **Vibe Check (思考与练习)**:
        1.  **核心练习**: 使用`try-catch`块将你的`loadFromFile`和`saveToFile`方法中的文件操作代码包裹起来，捕获`IOException`。
        2.  **破坏性实验**: 尝试将文件保存到一个你没有写入权限的目录（例如系统的根目录 `/`）。观察并捕获抛出的异常（可能是`AccessDeniedException`）。
        3.  **编码练习**: 在`loadFromFile`的`catch`块中，不要只是打印错误，而是向用户显示一条友好的消息，例如“提示：未找到旧的播放列表文件，将为您创建一个新的。”
        4.  **AI协同与深入思考**: `try-with-resources`语法糖是如何确保资源（如文件流）一定被关闭的？请AI解释其背后的`AutoCloseable`接口和它生成的字节码与传统`try-catch-finally`有何不同。