package com.example.whitefox.config;

import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.repository.LaundryOrderRepository;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.riders.repository.RiderRepository;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final RiderRepository riderRepository;
    private final LaundryOrderRepository laundryOrderRepository;
    private final com.example.whitefox.garments.repository.ServiceCatalogRepository serviceCatalogRepository;

    @Override
    public void run(String... args) throws Exception {
        if (serviceCatalogRepository.count() == 0) {
            serviceCatalogRepository.save(com.example.whitefox.garments.entity.ServiceCatalog.builder().serviceType("Wash & Iron").itemName("Shirt").price(50.0).build());
            serviceCatalogRepository.save(com.example.whitefox.garments.entity.ServiceCatalog.builder().serviceType("Wash & Iron").itemName("T-Shirt").price(40.0).build());
            serviceCatalogRepository.save(com.example.whitefox.garments.entity.ServiceCatalog.builder().serviceType("Wash & Iron").itemName("Trousers").price(60.0).build());
            serviceCatalogRepository.save(com.example.whitefox.garments.entity.ServiceCatalog.builder().serviceType("Dry Clean").itemName("Blazer").price(200.0).build());
            serviceCatalogRepository.save(com.example.whitefox.garments.entity.ServiceCatalog.builder().serviceType("Dry Clean").itemName("Jacket").price(250.0).build());
            serviceCatalogRepository.save(com.example.whitefox.garments.entity.ServiceCatalog.builder().serviceType("Ironing").itemName("Shirt").price(20.0).build());
            serviceCatalogRepository.save(com.example.whitefox.garments.entity.ServiceCatalog.builder().serviceType("Ironing").itemName("Trousers").price(25.0).build());
        }
        var adminOpt = userRepository.findByEmail("admin@gmail.com");
        if (adminOpt.isEmpty()) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@gmail.com")
                    .phone("1231231234")
                    .password(passwordEncoder.encode("password"))
                    .role("ADMIN")
                    .active(true)
                    .build();
            userRepository.save(admin);
        } else {
            User admin = adminOpt.get();
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }

        if (storeRepository.count() == 0) {
            Store store = Store.builder()
                    .storeCode("STR-001")
                    .name("Downtown Laundry Hub")
                    .phone("1234567890")
                    .email("downtown@whitefox.com")
                    .address("123 Main St")
                    .city("Metropolis")
                    .build();
            storeRepository.save(store);

            Customer customer = Customer.builder()
                    .customerCode("CUST-001")
                    .name("John Doe")
                    .phone("9876543210")
                    .email("john@example.com")
                    .address("456 Elm St")
                    .city("Metropolis")
                    .build();
            customerRepository.save(customer);

            Rider rider = Rider.builder()
                    .riderCode("RID-001")
                    .name("Speedy Gonzalez")
                    .phone("5551234567")
                    .email("speedy@whitefox.com")
                    .vehicleNumber("XYZ-999")
                    .build();
            riderRepository.save(rider);

            LaundryOrder order = LaundryOrder.builder()
                    .orderNumber("ORD-1001")
                    .customer(customer)
                    .store(store)
                    .deliveryRider(rider)
                    .subtotal(50.0)
                    .gst(2.5)
                    .totalAmount(52.5)
                    .pickupDate(LocalDate.now())
                    .deliveryDate(LocalDate.now().plusDays(2))
                    .build();
            laundryOrderRepository.save(order);

            System.out.println("✅ DATABASE SEEDED WITH MOCK DATA!");
        }
    }
}
