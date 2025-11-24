package com.vibevault.repository;

import com.vibevault.model.Playlist;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    // JpaRepository<Playlist, Long> 泛型参数说明:
    //  - Playlist: 这个Repository操作的实体类型
    //  - Long: 这个实体主键的类型

    // 我们不再需要手动声明 save 和 load(findById) 方法。
    // JpaRepository已经为我们提供了：
    // - save(Playlist entity)
    // - findById(Long id) : returns Optional<Playlist>
    // - findAll()
    // - deleteById(Long id)
    // - ...等等

    Optional<Playlist> findByName(String name);
}
