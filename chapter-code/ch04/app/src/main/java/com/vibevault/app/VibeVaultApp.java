package com.vibevault.app;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.vibevault.model.Playlist;
import com.vibevault.model.Song;

public class VibeVaultApp {
    private static final String DATA_FILE = "data/playlist.csv";
    
    public static void main(String[] args) {
        // 1. 初始化：在循环外准备好世界所需的一切
        Scanner scanner = new Scanner(System.in);
        Playlist playlist = new Playlist("我的收藏");
        playlist.loadFromFile(DATA_FILE); // 启动时加载保存的播放列表
        boolean running = true;

        // 2. 生命的开始：进入主事件循环
        while (running) {
            // 3. 输出：向用户展示当前世界状态和可选操作
            System.out.println("\n--- VibeVault 音乐播放列表 ---");
            System.out.println("1. 添加歌曲");
            System.out.println("2. 查看播放列表");
            System.out.println("0. 退出");
            System.out.print("请输入你的选择: ");

            // 4. 输入：等待并接收用户的指令（事件）
            try {
                int choice = scanner.nextInt(); 
                scanner.nextLine(); // 消费掉nextInt()留下的换行符

                switch (choice) {
                    case 1:
                        System.out.print("请输入歌曲标题: ");
                        String title = scanner.nextLine();
                        System.out.print("请输入艺术家: ");
                        String artist = scanner.nextLine();
                        System.out.print("请输入时长（秒）: ");
                        int duration = scanner.nextInt();
                        scanner.nextLine(); // 黄金法则：再次消费掉为duration输入的换行符
                
                        playlist.addSong(new Song(title, artist, duration));
                        System.out.println("歌曲添加成功！");
                        break;
                    case 2:
                        playlist.listSongs();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("无效选择，请输入菜单中的数字。");
                        break;
                }

            } catch (InputMismatchException e) {
                // 5. 异常处理：当用户的输入不符合我们的预期时
                System.out.println("无效输入！请输入一个数字。");
                scanner.next(); // 关键一步：清除缓冲区中的无效输入
            }
        }

        // 6. 结束：循环终止后的收尾工作
        System.out.println("感谢使用 VibeVault！");
        playlist.saveToFile(DATA_FILE); // 退出前保存播放列表到文件
        scanner.close();
    }
}