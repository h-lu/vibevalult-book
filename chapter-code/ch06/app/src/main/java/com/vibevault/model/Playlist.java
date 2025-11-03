package com.vibevault.model;

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
     * 从播放列表中删除指定索引的歌曲。
     * @param songIndex 要删除的歌曲的索引，从0开始。
     */
    public void removeSong(int songIndex) {
        if (songIndex >= 0 && songIndex < this.songs.size()) {
            this.songs.remove(songIndex);
        }
    }

    // 这是为了让 System.out.println(playlistObject) 时能有更友好的输出
    @Override
    public String toString() {
        return "Playlist{name='" + name + "', song_count=" + songs.size() + "}";
    }

    public int getSongCount() {
        return this.songs.size();
    }

    public List<Song> getSongs() {
        // Return a copy to prevent external modification of the internal list
        return new ArrayList<>(this.songs);
    }

    public String getName() {
        return this.name;
    }
}
