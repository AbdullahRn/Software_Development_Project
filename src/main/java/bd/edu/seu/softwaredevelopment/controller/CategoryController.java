package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dtos.CategoryDto;
import bd.edu.seu.softwaredevelopment.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService; // ✅ Real MongoDB Service

    // ✅ GET: Show category page with list and optional edit mode
    @GetMapping
    public String categoryPage(
            @RequestParam(required = false) String editId,
            Model model
    ) {
        model.addAttribute("title", "Category");
        model.addAttribute("content", "pages/category :: content");

        // Fetch all categories from MongoDB
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        if (editId != null) {
            // Find the category to edit by its MongoDB ID
            CategoryDto category = categoryService.getCategoryById(editId);
            if (category != null) {
                model.addAttribute("isEditing", true);
                model.addAttribute("editingCategoryId", editId);
                model.addAttribute("categoryName", category.getName());
            }
        } else {
            model.addAttribute("isEditing", false);
            model.addAttribute("categoryName", "");
        }

        return "layout"; // ✅ Thymeleaf layout pattern
    }

    // ✅ POST: Add Category to MongoDB
    @PostMapping("/add")
    public String addCategory(@RequestParam String name, RedirectAttributes ra) {

        if (name == null || name.trim().isEmpty()) {
            ra.addFlashAttribute("error", "Category name cannot be empty");
            return "redirect:/category";
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        try {
            categoryService.saveCategory(categoryDto); // ✅ Matches your Service implementation
            ra.addFlashAttribute("message", "Category successfully added ✅");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error adding category: " + e.getMessage());
        }

        return "redirect:/category";
    }

    // ✅ POST: Update Existing Category in MongoDB
    @PostMapping("/update")
    public String updateCategory(@RequestParam String id, @RequestParam String name, RedirectAttributes ra) {

        if (name == null || name.trim().isEmpty()) {
            ra.addFlashAttribute("error", "Category name cannot be empty");
            return "redirect:/category";
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);

        try {
            categoryService.updateCategory(id, categoryDto); // ✅ Uses Service update logic
            ra.addFlashAttribute("message", "Category successfully updated ✅");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to update: " + e.getMessage());
        }

        return "redirect:/category";
    }

    // ✅ POST: Delete Category from MongoDB
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam String id, RedirectAttributes ra) {

        try {
            categoryService.deleteCategory(id); // ✅ Real delete operation
            ra.addFlashAttribute("message", "Category successfully deleted ✅");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Could not delete category: " + e.getMessage());
        }

        return "redirect:/category";
    }
}