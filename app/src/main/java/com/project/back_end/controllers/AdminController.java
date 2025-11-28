package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.ServiceMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "admin")
public class AdminController {

    @Autowired
    private ServiceMain serviceMain;

    // Admin Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        return serviceMain.validateAdmin(admin);
    }
}
