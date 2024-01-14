package study.wild.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.wild.domain.entity.Post;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.deleted = :isDeleted")
    Optional<Post> findPostByIdAndIsDeleted(@Param("id") Long id, @Param("isDeleted") boolean isDeleted);

    @Query("SELECT p FROM Post p WHERE p.deleted = :isDeleted")
    List<Post> findAllByAndDeleted(@Param("isDeleted") boolean isDeleted);

    @Query("SELECT p FROM Post p WHERE p.category.id = :categoryId AND p.deleted = :isDeleted")
    List<Post> findPostByCategoryIdAndDeleted(@Param("categoryId") Long categoryId, @Param("isDeleted") boolean isDeleted);
}
