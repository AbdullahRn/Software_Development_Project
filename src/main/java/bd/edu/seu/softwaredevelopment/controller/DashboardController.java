package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.interfaces.PredictionServiceInterface;
import bd.edu.seu.softwaredevelopment.models.User;
import bd.edu.seu.softwaredevelopment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController { // Removed class-level mapping

    @Autowired
    private UserService userService;
    @Autowired
    private PredictionServiceInterface predictionService;

    @GetMapping("/dashboard-seller")
    public String sellerDashboard(Model model) {
        User user = userService.getCurrentLoggedInUser();
        if (user == null || user.getRole() != User.Role.SELLER) return "redirect:/login";

        model.addAttribute("title", "Seller Dashboard");
        model.addAttribute("content", "pages/dashboard-seller :: content");
        model.addAttribute("suggestions", predictionService.getProactiveSuggestions(user.getId()));
        model.addAttribute("transactionData", predictionService.getFormattedGraphData(null));
        return "layout";
    }

    @GetMapping("/dashboard-supplier")
    public String supplierDashboard(Model model) {
        User user = userService.getCurrentLoggedInUser();
        if (user == null || user.getRole() != User.Role.SUPPLIER) return "redirect:/login";

        model.addAttribute("title", "Supplier Dashboard");
        model.addAttribute("content", "pages/dashboard-supplier :: content");
        model.addAttribute("transactionData", predictionService.getFormattedGraphData(null));
        return "layout";
    }

    @GetMapping("/dashboard")
    public String dashboardRedirect() {
        User user = userService.getCurrentLoggedInUser();
        if (user == null) return "redirect:/login";

        if (user.getRole() == User.Role.SELLER) return "redirect:/dashboard-seller";
        if (user.getRole() == User.Role.SUPPLIER) return "redirect:/dashboard-supplier";

        return "redirect:/login?error=unauthorized";
    }

}