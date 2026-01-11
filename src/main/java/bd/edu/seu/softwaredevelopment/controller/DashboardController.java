package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.interfaces.PredictionServiceInterface;
import bd.edu.seu.softwaredevelopment.models.User;
import bd.edu.seu.softwaredevelopment.services.UserService;
import bd.edu.seu.softwaredevelopment.services.AiPredictionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserService userService;
    private final PredictionServiceInterface predictionService;
    private final AiPredictionService aiPredictionService;

    public DashboardController(UserService userService,
                               PredictionServiceInterface predictionService,
                               AiPredictionService aiPredictionService) {
        this.userService = userService;
        this.predictionService = predictionService;
        this.aiPredictionService = aiPredictionService;
    }

    @GetMapping("/dashboard-seller")
    public String sellerDashboard(Model model) {
        User user = userService.getCurrentLoggedInUser();
        if (user == null || user.getRole() != User.Role.SELLER) return "redirect:/login";

        model.addAttribute("title", "Seller Dashboard");
        model.addAttribute("content", "pages/dashboard-seller :: content");
        model.addAttribute("suggestions", predictionService.getProactiveSuggestions(user.getId()));

        var data = predictionService.getFormattedGraphData(user.getId(), false);
        model.addAttribute("transactionData", data);

        return "layout";
    }

    @GetMapping("/dashboard-supplier")
    public String supplierDashboard(Model model) {
        User user = userService.getCurrentLoggedInUser();
        if (user == null || user.getRole() != User.Role.SUPPLIER) return "redirect:/login";

        model.addAttribute("title", "Supplier Dashboard");
        model.addAttribute("content", "pages/dashboard-supplier :: content");

        var data = predictionService.getFormattedGraphData(user.getId(), true);
        model.addAttribute("transactionData", data);

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
