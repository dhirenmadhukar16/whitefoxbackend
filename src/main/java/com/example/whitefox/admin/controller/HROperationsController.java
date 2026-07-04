package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.LeaveRequest;
import com.example.whitefox.admin.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/hroperations")
@CrossOrigin("*")
public class HROperationsController {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @GetMapping("/leaves")
    public ResponseEntity<List<LeaveRequest>> getAllLeaves() {
        return ResponseEntity.ok(leaveRequestRepository.findAll());
    }
}
