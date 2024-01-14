package study.wild.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.wild.common.exception.CategoryNotFoundException;
import study.wild.common.exception.NonEmptyCategoryException;
import study.wild.dto.CategoryDto;
import study.wild.dto.PostDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private PostCategoryService postCategoryService;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void 카테고리_등록_테스트() {
        // given
        CategoryDto categoryDto = createCategoryDto("기록");

        // when
        CategoryDto saveCategory = categoryService.createCategory(categoryDto);

        // then
        assertThat(saveCategory.name()).isEqualTo(categoryDto.name());

    }

    @Test
    public void 게시글_수정_테스트() {
        // given
        CategoryDto savedCategory = categoryService.createCategory(createCategoryDto("기록"));

        // when
        CategoryDto updatedCategory = categoryService.updateCategory(
                savedCategory.id(),
                createCategoryDto("공부")
        );

        // then
        assertThat(updatedCategory.name()).isEqualTo("공부");
    }

    @Test
    public void 전체_게시글_조회_테스트() {
        // given
        createAndSaveCategoryDto("공부");
        createAndSaveCategoryDto("기록");
        createAndSaveCategoryDto("여행");

        // when
        List<CategoryDto> categories = categoryService.getCategoryAll();

        // then
        assertThat(categories).hasSize(4) // default 포함
                .extracting(CategoryDto::name)
                .containsExactly("", "공부", "기록", "여행");
    }

    @Test
    public void 카테고리_삭제_테스트() {
        // given
        CategoryDto savedCategory = categoryService.createCategory(createCategoryDto("공부"));

        // when
        categoryService.deleteCategory(savedCategory.id());

        // then
        assertThat(categoryService.getCategoryAll()).hasSize(1); // default 카테고리
    }

    @Test
    public void 게시글이_포함된_카테고리_삭제시_예외발생() {
        // given
        CategoryDto savedCategory = categoryService.createCategory(createCategoryDto("공부"));
        PostDto postDto = createPostDto("제목", savedCategory.id(), "내용");
        postCategoryService.createPostWithCategory(postDto);

        // when & then
        assertThatThrownBy(() -> postCategoryService.deleteCategoryWithValidation(savedCategory.id()))
                .isInstanceOf(NonEmptyCategoryException.class);
    }

    @Test
    public void 조회하고자_하는_게시물이_없으면_예외발생() {
        assertThatThrownBy(() -> categoryService.getCategory(2L))
                .isInstanceOf(CategoryNotFoundException.class);

    }

    private CategoryDto createCategoryDto(String name) {
        return new CategoryDto(null, name);
    }

    private Long createAndSaveCategoryDto(String name) {
        CategoryDto categoryDto = createCategoryDto(name);
        return categoryService.createCategory(categoryDto).id();
    }

    private PostDto createPostDto(String title, Long categoryId, String content) {
        return new PostDto(null, categoryId, title, content, 0);
    }
}