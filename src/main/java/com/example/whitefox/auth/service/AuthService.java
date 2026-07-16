package com.example.whitefox.auth.service;

import com.example.whitefox.auth.dto.AuthCheckDto;
import com.example.whitefox.auth.dto.LoginRequest;
import com.example.whitefox.auth.dto.LoginResponse;
import com.example.whitefox.auth.dto.RegisterRequest;
import com.example.whitefox.auth.entity.User;
import com.example.whitefox.auth.repository.UserRepository;
import com.example.whitefox.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.whitefox.customers.repository.CustomerRepository;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.trucklogistics.repository.TruckRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final TruckRepository truckRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthCheckDto.Response checkUser(AuthCheckDto.Request request) {
        String identifier = request.getIdentifier();
        Optional<User> userOpt = identifier.contains("@") 
                ? userRepository.findByEmail(identifier) 
                : userRepository.findByPhone(identifier);
                
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // If the user has a password, they should use PASSWORD auth.
            // (Assuming all users created via Seeder or new flow will have passwords).
            String authMethod = (user.getPassword() != null && !user.getPassword().isEmpty()) ? "PASSWORD" : "OTP";
            return AuthCheckDto.Response.builder()
                    .exists(true)
                    .authMethod(authMethod)
                    .role(user.getRole())
                    .active(user.getActive() != null ? user.getActive() : true)
                    .build();
        }
        
        return AuthCheckDto.Response.builder()
                .exists(false)
                .authMethod("OTP")
                .active(false)
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        String identifier = request.getIdentifier();
        User user = identifier.contains("@") 
                ? userRepository.findByEmail(identifier).orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"))
                : userRepository.findByPhone(identifier).orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (user.getPassword() == null || user.getPassword().isEmpty() || !passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid credentials");
        }
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        UUID resolvedUserId = user.getId();
        if ("CUSTOMER".equals(user.getRole())) {
            var customerOpt = customerRepository.findByEmail(user.getEmail());
            if (customerOpt.isPresent()) {
                resolvedUserId = customerOpt.get().getId();
            }
        } else if ("TRUCK_DRIVER".equals(user.getRole())) {
            var truckOpt = truckRepository.findAll().stream()
                .filter(t -> user.getEmail().equals(t.getEmail()))
                .findFirst();
            if (truckOpt.isPresent()) {
                resolvedUserId = truckOpt.get().getId();
            }
        }

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .storeId(user.getStoreId())
                .userId(resolvedUserId)
                .build();
    }

    public LoginResponse register(
            RegisterRequest request
    ) {

        if (userRepository.findByEmail(
                request.getEmail()
        ).isPresent()) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role("CUSTOMER")
                .active(true)
                .build();

        userRepository.save(user);

        Customer customer = Customer.builder()
                .customerCode("CUST-" + System.currentTimeMillis())
                .name(request.getFirstName() + " " + request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .build();
        customerRepository.save(customer);

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );


        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .userId(customer.getId())
                .build();
    }
}