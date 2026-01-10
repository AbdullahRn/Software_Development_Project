package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dtos.RegisterRequest;
import bd.edu.seu.softwaredevelopment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserService userService;

    // ✅ NEW: Logic to redirect user to the correct dashboard after login
    @GetMapping("/")
    public String index(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Check authorities (roles) assigned in CustomAuthenticationProvider
        boolean isSeller = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"));
        boolean isSupplier = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUPPLIER"));

        System.out.println("ISSupplier: " + isSupplier + "ISSeller: " + isSeller);


        if (isSeller) {
            return "redirect:/dashboard-seller";
        } else if (isSupplier) {
            return "redirect:/dashboard-supplier";
        }


        // Fallback if no role matches
        return "redirect:/login?error=unauthorized";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password!");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        model.addAttribute("title", "Login");
        model.addAttribute("content", "pages/login :: content");
        return "auth-layout";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("title", "Register");
        model.addAttribute("content", "pages/register :: content");
        return "auth-layout";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registerRequest") RegisterRequest dto,
                               RedirectAttributes ra) {
        try {
            userService.registerUser(dto);
            ra.addFlashAttribute("message", "Registration successful! Please login. ✅");
            return "redirect:/login";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}