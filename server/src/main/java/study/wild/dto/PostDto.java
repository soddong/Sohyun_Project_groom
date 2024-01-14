package study.wild.dto;

import study.wild.domain.entity.Category;
import study.wild.domain.entity.Post;

public record PostDto(
        Long id,
        Long categoryId,
        String title,
        String content,
        long view
) {
    public static PostDto from(Post post) {
        return new PostDto(post.getId(), post.getCategory().getId(),
                post.getTitle(), post.getContent(), post.getView());
    }

    public Post toEntity(Category category) {
        return Post.builder()
                .id(this.id)
                .category(category)
                .title(this.title)
                .content(this.content)
                .view(this.view)
                .build();
    }
}

