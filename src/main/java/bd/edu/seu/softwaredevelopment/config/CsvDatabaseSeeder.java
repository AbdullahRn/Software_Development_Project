package bd.edu.seu.softwaredevelopment.config;

import bd.edu.seu.softwaredevelopment.models.*;
import bd.edu.seu.softwaredevelopment.repositories.*;
import com.opencsv.CSVReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class CsvDatabaseSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;

    public CsvDatabaseSeeder(CategoryRepository categoryRepository,
                             SupplierRepository supplierRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository,
                             TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\n==============================================");
        System.out.println("✅ CSV Seeder Started...");
        System.out.println("==============================================");

        // ==========================================================
        // ✅ DATABASE CLEANUP (SAFE RESET) - COMMENT OUT LATER IF NEEDED
        // ----------------------------------------------------------
        // This will REMOVE ALL previous data from MongoDB before loading CSVs.
        // ✅ Prevents collision with old data
        // ✅ Ensures fresh dataset every startup
        //
        // To disable cleanup later:
        //   - Comment out the next line: cleanupDatabase();
        // ==========================================================
        cleanupDatabase();
        // ==========================================================


        // ✅ Load CSV files in correct order
        loadCategories();
        loadSuppliers();
        loadUsers();
        loadProducts();
        loadTransactions();

        System.out.println("\n==============================================");
        System.out.println("✅ CSV Seeder Completed Successfully.");
        System.out.println("==============================================\n");
    }

    // ==========================================================
    // ✅ DATABASE CLEANUP METHOD (SAFE RESET)
    // ----------------------------------------------------------
    // Removes all previous records from ALL collections.
    // This guarantees that no old data will remain.
    //
    // 🔥 COMMENT OUT THIS METHOD CALL (not method itself)
    // if you want to preserve previous DB data later.
    // ==========================================================
    private void cleanupDatabase() {

        System.out.println("\n-------------------- DB CLEANUP START --------------------");

        System.out.println("Existing Records Before Cleanup:");
        System.out.println("Categories: " + categoryRepository.count());
        System.out.println("Suppliers: " + supplierRepository.count());
        System.out.println("Users: " + userRepository.count());
        System.out.println("Products: " + productRepository.count());
        System.out.println("Transactions: " + transactionRepository.count());

        // ✅ Delete in reverse dependency order
        transactionRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
        supplierRepository.deleteAll();
        categoryRepository.deleteAll();

        System.out.println("\nRecords After Cleanup:");
        System.out.println("Categories: " + categoryRepository.count());
        System.out.println("Suppliers: " + supplierRepository.count());
        System.out.println("Users: " + userRepository.count());
        System.out.println("Products: " + productRepository.count());
        System.out.println("Transactions: " + transactionRepository.count());

        System.out.println("--------------------- DB CLEANUP END ---------------------\n");
    }
    // ==========================================================


    private void loadCategories() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/categories.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // header

            for (String[] row : rows) {
                Category c = new Category();
                c.setId(row[0]);
                c.setName(row[1]);
                categoryRepository.save(c);
            }
        }

        System.out.println("✅ Categories Loaded: " + categoryRepository.count());
    }

    private void loadSuppliers() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/suppliers.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // header

            for (String[] row : rows) {
                Supplier s = new Supplier();
                s.setId(row[0]);
                s.setName(row[1]);
                s.setContactInfo(row[2]);
                s.setAddress(row[3]);
                supplierRepository.save(s);
            }
        }

        System.out.println("✅ Suppliers Loaded: " + supplierRepository.count());
    }

    private void loadUsers() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/users.csv");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // header

            for (String[] row : rows) {
                User u = new User();
                u.setId(row[0]);
                u.setName(row[1]);
                u.setEmail(row[2]);

                // ✅ Encrypt password using BCrypt
                String rawPassword = row[3];
                u.setPassword(encoder.encode(rawPassword));

                u.setPhoneNumber(row[4]);
                u.setAddress(row[5]);

                // ✅ Role enum must match exactly SUPPLIER or SELLER
                u.setRole(User.Role.valueOf(row[6].trim().toUpperCase()));

                // ✅ createdAt
                try {
                    u.setCreatedAt(LocalDateTime.parse(row[7]));
                } catch (Exception e) {
                    u.setCreatedAt(LocalDateTime.now());
                }

                userRepository.save(u);
            }
        }

        System.out.println("✅ Users Loaded: " + userRepository.count());
        System.out.println("✅ All passwords encrypted with BCrypt.");
    }

    private void loadProducts() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/products.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // header

            for (String[] row : rows) {
                Product p = new Product();
                p.setId(row[0]);
                p.setName(row[1]);
                p.setCategoryId(row[2]);
                p.setBrand(row[3]);
                p.setPrice(new BigDecimal(row[4]));
                p.setStockQuantity(Integer.parseInt(row[5]));

                // ✅ supplierId references SUPPLIER USER ID (SUP1..SUPn)
                p.setSupplierId(row[6]);

                p.setSku(row[7]);
                p.setDescription(row[8]);
                p.setReorderLevel(Integer.parseInt(row[9]));

                // ✅ createdAt (requires setter in Product.java)
                try {
                    p.setCreatedAt(LocalDateTime.parse(row[10]));
                } catch (Exception e) {
                    p.setCreatedAt(LocalDateTime.now());
                }

                productRepository.save(p);
            }
        }

        System.out.println("✅ Products Loaded: " + productRepository.count());
    }

    private void loadTransactions() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/transactions.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> rows = reader.readAll();
            rows.remove(0); // header

            for (String[] row : rows) {
                Transaction t = new Transaction();
                t.setId(row[0]);
                t.setProductId(row[1]);
                t.setUserId(row[2]);

                // ✅ supplierId blank for SALE, filled for PURCHASE
                t.setSupplierId(row[3].isBlank() ? null : row[3]);

                t.setTransactionType(row[4]);
                t.setTotalProducts(Integer.parseInt(row[5]));
                t.setTotalPrice(new BigDecimal(row[6]));
                t.setDiscount(row[7].isBlank() ? 0.0 : Double.parseDouble(row[7]));
                t.setPromotion(row[8].equalsIgnoreCase("true"));
                t.setSaleDate(LocalDate.parse(row[9]));
                t.setStatus(row[10]);

                // ✅ createdAt
                try {
                    t.setCreatedAt(LocalDateTime.parse(row[11]));
                } catch (Exception e) {
                    t.setCreatedAt(LocalDateTime.now());
                }

                transactionRepository.save(t);
            }
        }

        System.out.println("✅ Transactions Loaded: " + transactionRepository.count());
    }
}
