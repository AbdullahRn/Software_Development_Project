package bd.edu.seu.softwaredevelopment.controller;

import bd.edu.seu.softwaredevelopment.dto.TransactionDetailsDto;
import bd.edu.seu.softwaredevelopment.models.Transaction;
import bd.edu.seu.softwaredevelopment.services.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // ✅ List Page + search + pagination (REAL DB)
    @GetMapping
    public String transactionPage(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(required = false) String search,
                                  Model model) {

        int itemsPerPage = 10;

        List<Transaction> paginated = transactionService.getPaginatedTransactions(page, itemsPerPage, search);
        long totalCount = transactionService.getTransactionCount(search);

        int totalPages = (int) Math.ceil((double) totalCount / itemsPerPage);

        model.addAttribute("title", "Transactions");
        model.addAttribute("content", "pages/transaction :: content");

        model.addAttribute("transactions", paginated);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("search", search == null ? "" : search);

        return "layout";
    }

    // ✅ Details page (REAL DB)
    @GetMapping("/{id}")
    public String transactionDetails(@PathVariable String id, Model model) {

        TransactionDetailsDto transactionDetails = transactionService.getTransactionDetails(id);

        model.addAttribute("title", "Transaction Details");
        model.addAttribute("content", "pages/transaction-details :: content");
        model.addAttribute("transaction", transactionDetails.getTransaction());
        model.addAttribute("product", transactionDetails.getProduct());
        model.addAttribute("user", transactionDetails.getUser());
        model.addAttribute("supplier", transactionDetails.getSupplier());

        return "layout";
    }

    // ✅ Update Status (REAL DB)
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam String transactionId,
                               @RequestParam String status) {

        transactionService.updateStatus(transactionId, status);

        return "redirect:/transaction/" + transactionId + "?message=Status%20updated%20successfully";
    }
}
