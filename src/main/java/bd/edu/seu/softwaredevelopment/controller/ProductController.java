package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dtos.ProductDto;
import bd.edu.seu.softwaredevelopment.interfaces.CategoryServiceInterface;
import bd.edu.seu.softwaredevelopment.interfaces.ProductServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServiceInterface productService;

    @Autowired
    private CategoryServiceInterface categoryService;

    @GetMapping
    public String productPage(@RequestParam(defaultValue = "1") int page, Model model) {
        // Fix: Ensure we handle null from service to prevent .size() crash
        List<ProductDto> allProducts = productService.getAllProducts();
        if (allProducts == null) {
            allProducts = new ArrayList<>();
        }

        // Pagination logic
        int itemsPerPage = 10;
        int totalItems = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // Safety check for empty lists
        int start = Math.max(0, (page - 1) * itemsPerPage);
        int end = Math.min(start + itemsPerPage, totalItems);

        List<ProductDto> paginatedProducts = (start < totalItems)
                ? allProducts.subList(start, end)
                : new ArrayList<>();

        model.addAttribute("title", "Products");
        model.addAttribute("content", "pages/product :: content");
        model.addAttribute("products", paginatedProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", Math.max(1, totalPages));
        return "layout";
    }

    @GetMapping("/add")
    public String addProductPage(Model model) {
        model.addAttribute("title", "Add Product");
        model.addAttribute("content", "pages/add-edit-product :: content");
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isEditing", false);
        model.addAttribute("product", new ProductDto());
        return "layout";
    }

    // Fixed: Added missing Edit mapping
    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        ProductDto product = productService.getProductById(id);
        model.addAttribute("title", "Edit Product");
        model.addAttribute("content", "pages/add-edit-product :: content");
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("isEditing", true);
        model.addAttribute("product", product);
        return "layout";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute ProductDto dto,
                              @RequestParam(required = false) MultipartFile imageFile,
                              RedirectAttributes ra) {
        try {
            // Logic: Determine if we update or save new based on ID
            if (dto.getId() != null && !dto.getId().isEmpty()) {
                productService.updateProduct(dto, imageFile);
                ra.addFlashAttribute("message", "Product updated successfully! ✅");
            } else {
                productService.saveProduct(dto, imageFile);
                ra.addFlashAttribute("message", "Product saved successfully! ✅");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/product";
    }

    @PostMapping("/delete")
    public String deleteProduct(@RequestParam String id, RedirectAttributes ra) {
        try {
            productService.deleteProduct(id);
            ra.addFlashAttribute("message", "Product deleted successfully! ✅");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Could not delete product: " + e.getMessage());
        }
        return "redirect:/product";
    }
}