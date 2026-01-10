package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dtos.TransactionRequest;
import bd.edu.seu.softwaredevelopment.interfaces.ProductServiceInterface;
import bd.edu.seu.softwaredevelopment.interfaces.TransactionServiceInterface;
import bd.edu.seu.softwaredevelopment.interfaces.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sell")
public class SellController {

    @Autowired
    private TransactionServiceInterface transactionService;

    @Autowired
    private ProductServiceInterface productService;

    @Autowired
    private UserServiceInterface userService;

    @GetMapping
    public String sellPage(Model model) {
        model.addAttribute("title", "Sell");
        model.addAttribute("content", "pages/sell :: content");
        model.addAttribute("products", productService.getAllProducts());
        return "layout";
    }

    @PostMapping
    public String doSell(@ModelAttribute TransactionRequest req, RedirectAttributes ra) {
        try {
            req.setUserId(userService.getCurrentLoggedInUser().getId());
            transactionService.sell(req); // Logic handles stock decrease and ML logging
            ra.addFlashAttribute("message", "Product sold successfully! ✅");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sell";
    }
}