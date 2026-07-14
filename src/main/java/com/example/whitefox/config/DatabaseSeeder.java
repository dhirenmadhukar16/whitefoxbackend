package com.example.whitefox.config;

import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.garments.entity.ServiceCatalog;
import com.example.whitefox.garments.repository.ServiceCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;

    @Override
    public void run(String... args) throws Exception {

        // ─────────────────────────────────────────────────────────
        // SERVICE CATALOG  (needed for orders to work)
        // ─────────────────────────────────────────────────────────
        if (serviceCatalogRepository.count() == 0) {
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Wash & Iron").itemName("Shirt").price(50.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Wash & Iron").itemName("T-Shirt").price(40.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Wash & Iron").itemName("Trousers").price(60.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Wash & Iron").itemName("Jeans").price(70.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Dry Clean").itemName("Blazer").price(200.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Dry Clean").itemName("Jacket").price(250.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Dry Clean").itemName("Coat").price(300.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Ironing").itemName("Shirt").price(20.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Ironing").itemName("Trousers").price(25.0).build());
            serviceCatalogRepository.save(ServiceCatalog.builder().serviceType("Ironing").itemName("T-Shirt").price(15.0).build());
        }

        // ─────────────────────────────────────────────────────────
        // 1. SUPER ADMIN
        //    Email   : admin@whitefox.com
        //    Password: Admin@123
        // ─────────────────────────────────────────────────────────
        var adminOpt = userRepository.findByEmail("admin@whitefox.com");
        if (adminOpt.isEmpty()) {
            userRepository.save(User.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .email("admin@whitefox.com")
                    .phone("1231231234")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role("ADMIN")
                    .active(true)
                    .build());
        } else {
            // Ensure role is correct even if user already existed
            User admin = adminOpt.get();
            if (!"ADMIN".equals(admin.getRole())) {
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }
        }

        // ─────────────────────────────────────────────────────────
        // 2. HQ ADMIN
        //    Email   : hq@whitefox.com
        //    Password: Hq@123456
        // ─────────────────────────────────────────────────────────
        var hqOpt = userRepository.findByEmail("hq@whitefox.com");
        if (hqOpt.isEmpty()) {
            userRepository.save(User.builder()
                    .firstName("HQ")
                    .lastName("Admin")
                    .email("hq@whitefox.com")
                    .phone("9999999999")
                    .password(passwordEncoder.encode("Hq@123456"))
                    .role("HQ_ADMIN")
                    .active(true)
                    .build());
        } else {
            User hq = hqOpt.get();
            if (!"HQ_ADMIN".equals(hq.getRole())) {
                hq.setRole("HQ_ADMIN");
                userRepository.save(hq);
            }
        }

        // ─────────────────────────────────────────────────────────
        // 3. SAMPLE CUSTOMER  (so dev can test customer app immediately)
        //    Email   : customer@gmail.com
        //    Password: Customer@123
        // ─────────────────────────────────────────────────────────
        var custUserOpt = userRepository.findByEmail("customer@gmail.com");
        if (custUserOpt.isEmpty()) {
            userRepository.save(User.builder()
                    .firstName("Test")
                    .lastName("Customer")
                    .email("customer@gmail.com")
                    .phone("9998887776")
                    .password(passwordEncoder.encode("Customer@123"))
                    .role("CUSTOMER")
                    .active(true)
                    .build());
        }

        // Ensure the Customer profile record exists (needed for customer app login)
        var custProfileOpt = customerRepository.findByEmail("customer@gmail.com");
        if (custProfileOpt.isEmpty()) {
            customerRepository.save(Customer.builder()
                    .customerCode("CUST-001")
                    .name("Test Customer")
                    .phone("9998887776")
                    .email("customer@gmail.com")
                    .address("123 Sample Street")
                    .city("Mumbai")
                    .build());
        }

        // ─────────────────────────────────────────────────────────
        // NOTE: Stores, Store Managers, Riders, and Truck Drivers
        // are NOT seeded here — they must be created via the Admin
        // portal (admin@whitefox.com). This keeps the database clean
        // for every developer who pulls the project.
        // ─────────────────────────────────────────────────────────

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║           WhiteFox Database Seeded ✅                ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  ADMIN   → admin@whitefox.com   / Admin@123          ║");
        System.out.println("║  HQ      → hq@whitefox.com      / Hq@123456          ║");
        System.out.println("║  CUSTOMER→ customer@gmail.com   / Customer@123       ║");
        System.out.println("║                                                       ║");
        System.out.println("║  ⚠ Stores, Riders, Truck Drivers → create via Admin  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}
