package com.vibevault.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

    // 1. 成员变量 (Member Variables / Fields)
    //    它们定义了Playlist的“属性”或“状态”
    //    使用private，将实现细节“锁”在黑盒子里
    private final String name;
    private final List<Song> songs;

    // 2. 构造函数 (Constructor)
    //    它的名字必须与类名完全相同，且没有返回类型
    //    负责在对象创建时，进行初始化工作
    public Playlist(String name) {
        // 3. 'this' 关键字
        //    它指向“当前正在被创建的这个对象实例”
        //    用来明确区分“成员变量name”和“参数name”
        this.name = name;
        this.songs = new ArrayList<>(); // 初始化为空的歌曲列表
    }

    /**
     * 向播放列表末尾添加一首歌。
     * @param song 要添加的歌曲，不能为null。
     */
    public void addSong(Song song) {
        if (song != null) {
            this.songs.add(song);
        }
    }

    /**
     * 列出播放列表中的所有歌曲到控制台。
     */
    public void listSongs() {
        System.out.println("--- Playlist: " + this.name + " ---");
        if (this.songs.isEmpty()) {
            System.out.println("This playlist is empty.");
        } else {
            for (int i = 0; i < this.songs.size(); i++) {
                Song currentSong = this.songs.get(i);
                System.out.println((i + 1) + ". " + currentSong.title() + " - " + currentSong.artist());
            }
        }
        System.out.println("---------------------------------");
    }

    // 这是为了让 System.out.println(playlistObject) 时能有更友好的输出
    @Override
    public String toString() {
        return "Playlist{name='" + name + "', song_count=" + songs.size() + "}";
    }

    /**
     * 将整个播放列表序列化为CSV字符串列表。
     * @return 包含每首歌CSV格式的字符串列表
     */
    public List<String> saveToStrings() {
        List<String> csvLines = new ArrayList<>();
        for (Song song : songs) {
            csvLines.add(song.toCsvString());
        }
        return csvLines;
    }

    /**
     * 从CSV字符串列表中加载数据，重建播放列表。
     * 注意：这将清空当前播放列表。
     * @param csvLines 包含每首歌CSV格式的字符串列表
     */
    public void loadFromStrings(List<String> csvLines) {
        this.songs.clear(); // 清空旧数据
        for (String line : csvLines) {
            this.songs.add(Song.fromCsvString(line));
        }
    }

    /**
     * 将当前播放列表保存到磁盘文件。
     * @param filePath 文件的路径，例如 "playlist.csv"
     */
    public void saveToFile(String filePath) {
        List<String> csvLines = this.saveToStrings();
        Path path = Paths.get(filePath);
        try {
            Files.write(path, csvLines);
            System.out.println("播放列表已成功保存到 " + filePath);
        } catch (IOException e) {
            // 提供更友好的用户反馈
            System.err.println("错误：无法保存播放列表。请检查文件权限或磁盘空间。");
            // 打印详细的异常信息，方便开发者调试
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘文件加载播放列表。
     * @param filePath 文件的路径，例如 "playlist.csv"
     */
    public void loadFromFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            List<String> csvLines = Files.readAllLines(path); // 魔法发生的地方！
            this.loadFromStrings(csvLines); // 调用我们之前写的反序列化方法
        } catch (IOException e) {
            // 在下一节，我们将详细讨论如何处理这个异常
            System.out.println("信息：未找到播放列表文件或读取失败，将创建一个新的播放列表。");
        }
    }
    public int getSongCount() {
        return this.songs.size();
    }

    public List<Song> getSongs() {
        return new ArrayList<>(this.songs);
    }
}
