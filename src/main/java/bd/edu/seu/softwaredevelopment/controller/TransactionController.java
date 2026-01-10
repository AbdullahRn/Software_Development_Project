package bd.edu.seu.softwaredevelopment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    // ✅ Phase-1 dummy transactions
    private final List<Map<String, Object>> transactions = new ArrayList<>();

    public TransactionController() {
        // Create 20 dummy detailed transactions
        for (int i = 1; i <= 20; i++) {
            transactions.add(createDetailedTransaction(i));
        }
    }

    private Map<String, Object> createDetailedTransaction(int id) {

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("id", id);
        transaction.put("transactionType", (id % 2 == 0) ? "PURCHASE" : "SELL");
        transaction.put("status", (id % 3 == 0) ? "PENDING" : "COMPLETED");
        transaction.put("description", "This is transaction description " + id);
        transaction.put("note", "Note for transaction " + id);
        transaction.put("totalProducts", 2 + (id % 5));
        transaction.put("totalPrice", 1000 + id * 250);
        transaction.put("createdAt", new Date(System.currentTimeMillis() - (long) id * 86400000));
        transaction.put("updatedAt", new Date());

        // ✅ Product map
        Map<String, Object> product = new HashMap<>();
        product.put("name", "Product " + id);
        product.put("sku", "SKU" + id);
        product.put("price", 100 + id * 10);
        product.put("stockQuantity", 10 + id);
        product.put("description", "Product description " + id);
        product.put("imageUrl", "/images/default-product.png");

        // ✅ User map
        Map<String, Object> user = new HashMap<>();
        user.put("name", "User " + id);
        user.put("email", "user" + id + "@gmail.com");
        user.put("phoneNumber", "017000000" + id);
        user.put("role", (id % 2 == 0) ? "ADMIN" : "USER");

        // ✅ Supplier map (optional for PURCHASE)
        Map<String, Object> supplier = null;
        if ((id % 2) == 0) {
            supplier = new HashMap<>();
            supplier.put("name", "Supplier " + id);
            supplier.put("contactInfo", "019000000" + id);
            supplier.put("address", "Dhaka, Bangladesh");
        }

        transaction.put("product", product);
        transaction.put("user", user);
        transaction.put("supplier", supplier);

        return transaction;
    }

    // ✅ List Page + search + pagination (simple)
    @GetMapping
    public String transactionPage(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(required = false) String search,
                                  Model model) {

        int itemsPerPage = 10;

        List<Map<String, Object>> filtered = transactions;

        if (search != null && !search.trim().isEmpty()) {
            String s = search.toLowerCase();
            filtered = transactions.stream()
                    .filter(t ->
                            t.get("transactionType").toString().toLowerCase().contains(s) ||
                                    t.get("status").toString().toLowerCase().contains(s) ||
                                    t.get("totalPrice").toString().toLowerCase().contains(s)
                    )
                    .toList();
        }

        int totalPages = (int) Math.ceil((double) filtered.size() / itemsPerPage);

        int start = (page - 1) * itemsPerPage;
        int end = Math.min(start + itemsPerPage, filtered.size());

        List<Map<String, Object>> paginated = filtered.subList(start, end);

        model.addAttribute("title", "Transactions");
        model.addAttribute("content", "pages/transaction :: content");

        model.addAttribute("transactions", paginated);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("search", search == null ? "" : search);

        return "layout";
    }

    // ✅ Details page
    @GetMapping("/{id}")
    public String transactionDetails(@PathVariable Integer id, Model model) {

        Map<String, Object> transaction = transactions.stream()
                .filter(t -> t.get("id").equals(id))
                .findFirst()
                .orElse(null);

        model.addAttribute("title", "Transaction Details");
        model.addAttribute("content", "pages/transaction-details :: content");
        model.addAttribute("transaction", transaction);

        return "layout";
    }

    // ✅ Update Status (Phase-1 dummy)
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Integer transactionId,
                               @RequestParam String status) {

        transactions.stream()
                .filter(t -> t.get("id").equals(transactionId))
                .findFirst()
                .ifPresent(t -> {
                    t.put("status", status);
                    t.put("updatedAt", new Date());
                });

        return "redirect:/transaction/" + transactionId + "?message=Status%20updated%20successfully";
    }
}
