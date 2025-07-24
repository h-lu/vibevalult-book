package com.vibevault.ui;

import com.vibevault.service.PlaylistService;
import java.util.Scanner;
import java.util.InputMismatchException;
import com.vibevault.model.Song;

public class PlaylistController {
    private final PlaylistService playlistService;
    private final Scanner scanner;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> addSong();
                case 2 -> System.out.println(playlistService.listSongs());
                case 3 -> removeSong();
                case 0 -> running = false;
                default -> System.out.println("无效选择，请输入菜单中的数字。");
            }
        }
        playlistService.saveData(); // 退出时保存数据
        System.out.println("感谢使用 VibeVault！");
    }

    private void showMenu() {
        System.out.println("\n--- VibeVault 音乐播放列表 ---");
        System.out.println("1. 添加歌曲");
        System.out.println("2. 查看播放列表");
        System.out.println("3. 删除歌曲");
        System.out.println("0. 退出");
        System.out.print("请输入你的选择: ");
    }

    private int getUserChoice() {

        try {
            int choice = Integer.parseInt(scanner.nextLine()); 
            return choice;
        } catch (NumberFormatException e) {
            System.out.println("无效输入，请输入一个整数。");
            return -1; // 返回一个无效选项
        }
    }

    private void addSong() {
        System.out.print("请输入歌曲标题: ");
        String title = scanner.nextLine();
        System.out.print("请输入艺术家: ");
        String artist = scanner.nextLine();
        System.out.print("请输入时长（秒）: ");
        int duration = getUserChoice(); // 复用choice逻辑

        playlistService.addSong(new Song(title, artist, duration));
        System.out.println("歌曲添加成功！");
    }

    private void removeSong() {
        System.out.print("请输入要删除的歌曲编号: ");
        int index = getUserChoice();
        playlistService.removeSong(index);
        System.out.println("歌曲删除成功！");
    }
}
