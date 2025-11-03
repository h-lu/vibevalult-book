package com.vibevault.app;

import com.vibevault.repository.PlaylistRepository;
import com.vibevault.repository.FilePlaylistRepository;
import com.vibevault.service.PlaylistService;
import com.vibevault.service.PlaylistServiceImpl;
import com.vibevault.ui.PlaylistController;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class VibeVaultApp {
    public static void main(String[] args) {
        // 设置控制台输出为 UTF-8 编码，解决 Windows 中文乱码问题
        try {
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        // 依赖注入 (Dependency Injection)
        // 1. 创建最底层的Repository
        PlaylistRepository repository = new FilePlaylistRepository();
        // 2. 创建Service，并把Repository“注入”进去
        PlaylistService service = new PlaylistServiceImpl(repository);
        // 3. 创建Controller，并把Service“注入”进去
        PlaylistController controller = new PlaylistController(service);
        
        // 4. 启动应用
        controller.start();
    }
}