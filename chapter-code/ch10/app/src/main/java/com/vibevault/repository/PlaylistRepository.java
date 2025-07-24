package com.vibevault.repository;

import com.vibevault.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
    // JpaRepository<Playlist, String> 泛型参数说明:
    //  - Playlist: 这个Repository操作的实体类型
    //  - String: 这个实体主键的类型

    // 我们不再需要手动声明 save 和 load(findById) 方法。
    // JpaRepository已经为我们提供了：
    // - save(Playlist entity)
    // - findById(String id) : returns Optional<Playlist>
    // - findAll()
    // - deleteById(String id)
    // - ...等等
}
