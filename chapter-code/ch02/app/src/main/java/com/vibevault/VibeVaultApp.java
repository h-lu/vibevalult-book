package com.vibevault;

public class VibeVaultApp {
    public static void main(String[] args) {
        // 1. 创建一些Song原子
        Song song1 = new Song("Bohemian Rhapsody", "Queen", 355);
        Song song2 = new Song("Stairway to Heaven", "Led Zeppelin", 482);
        Song song3 = new Song("Hotel California", "Eagles", 390);

        // 2. 创建一个Playlist分子
        Playlist rockClassics = new Playlist("Rock Classics");

        // 3. 为分子添加行为（调用方法）
        rockClassics.addSong(song1);
        rockClassics.addSong(song2);
        rockClassics.addSong(song3);

        // 4. 验证结果
        rockClassics.listSongs();
        
        System.out.println(rockClassics); // 验证我们重写的toString方法
    }
}
