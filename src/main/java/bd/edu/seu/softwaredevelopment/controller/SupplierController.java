package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dtos.SupplierDto;
import bd.edu.seu.softwaredevelopment.interfaces.SupplierServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierServiceInterface supplierService;

    @GetMapping
    public String supplierPage(Model model) {
        model.addAttribute("title", "Suppliers");
        model.addAttribute("content", "pages/supplier-form :: content");
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "layout";
    }

    @GetMapping("/add")
    public String addSupplierPage(Model model) {
        model.addAttribute("title", "Add Supplier");
        model.addAttribute("content", "pages/add-edit-supplier :: content");
        model.addAttribute("isEditing", false);
        model.addAttribute("supplier", new SupplierDto());
        return "layout";
    }

    @GetMapping("/edit/{id}")
    public String editSupplierPage(@PathVariable String id, Model model) {
        SupplierDto supplier = supplierService.getSupplierById(id);
        model.addAttribute("title", "Edit Supplier");
        model.addAttribute("content", "pages/add-edit-supplier :: content");
        model.addAttribute("isEditing", true);
        model.addAttribute("supplier", supplier);
        return "layout";
    }

    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute SupplierDto dto, RedirectAttributes ra) {
        try {
            if (dto.getId() != null && !dto.getId().isEmpty()) {
                supplierService.updateSupplier(dto.getId(), dto);
                ra.addFlashAttribute("message", "Supplier updated successfully! ✅");
            } else {
                supplierService.saveSupplier(dto);
                ra.addFlashAttribute("message", "Supplier added successfully! ✅");
            }
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/supplier";
    }

    @PostMapping("/delete")
    public String deleteSupplier(@RequestParam String id, RedirectAttributes ra) {
        try {
            supplierService.deleteSupplier(id);
            ra.addFlashAttribute("message", "Supplier deleted successfully! ✅");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/supplier";
    }
}