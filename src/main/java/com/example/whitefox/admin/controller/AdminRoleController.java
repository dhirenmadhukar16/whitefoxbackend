package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.AdminRole;
import com.example.whitefox.admin.repository.AdminRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/roles")
@CrossOrigin("*")
public class AdminRoleController {

    @Autowired
    private AdminRoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<AdminRole>> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<AdminRole> createRole(@RequestBody AdminRole role) {
        return ResponseEntity.ok(roleRepository.save(role));
    }
}
