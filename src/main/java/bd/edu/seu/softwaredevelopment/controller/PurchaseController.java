package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dtos.TransactionRequest;
import bd.edu.seu.softwaredevelopment.dtos.UserDto;
import bd.edu.seu.softwaredevelopment.interfaces.ProductServiceInterface;
import bd.edu.seu.softwaredevelopment.interfaces.TransactionServiceInterface;
import bd.edu.seu.softwaredevelopment.interfaces.UserServiceInterface;
import bd.edu.seu.softwaredevelopment.models.User;
import bd.edu.seu.softwaredevelopment.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private TransactionServiceInterface transactionService;

    @Autowired
    private ProductServiceInterface productService;

    @Autowired
    private UserServiceInterface userService;
    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String purchasePage(Model model) {
        model.addAttribute("title", "Purchase Management");
        model.addAttribute("content", "pages/purchase :: content");

        // Fetch products and suppliers to populate dropdowns
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());

        return "layout";
    }

    @PostMapping
    public String doPurchase(@ModelAttribute TransactionRequest req, RedirectAttributes ra) {
        try {
            User currentUser = userService.getCurrentLoggedInUser();

            if (currentUser == null) {
                ra.addFlashAttribute("error", "You must be logged in to perform this action.");
                return "redirect:/login";
            }

            req.setUserId(currentUser.getId());
            transactionService.purchase(req);

            ra.addFlashAttribute("message", "Stock updated successfully and purchase logged! ✅");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Failed to process purchase: " + e.getMessage());
        }
        return "redirect:/purchase";
    }
}