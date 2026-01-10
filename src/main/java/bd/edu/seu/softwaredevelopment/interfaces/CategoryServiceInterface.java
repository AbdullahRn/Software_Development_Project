package bd.edu.seu.softwaredevelopment.interfaces;

import bd.edu.seu.softwaredevelopment.dtos.CategoryDto;

import java.util.List;

public interface CategoryServiceInterface {
    CategoryDto saveCategory(CategoryDto categoryDto);

    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getAllCategories();
    CategoryDto getCategoryById(String id);
    CategoryDto updateCategory(String id, CategoryDto categoryDto);
    void deleteCategory(String id);
}


