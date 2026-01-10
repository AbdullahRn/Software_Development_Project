package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.models.User;
import bd.edu.seu.softwaredevelopment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profilePage(Model model) {
        User user = userService.getCurrentLoggedInUser(); //

        if (user == null) return "redirect:/login";

        model.addAttribute("title", "Profile");
        model.addAttribute("content", "pages/profile :: content");
        model.addAttribute("user", user);

        return "layout";
    }
}