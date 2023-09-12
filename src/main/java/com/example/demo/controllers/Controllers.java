package com.example.demo.controllers;

import com.example.demo.services.InfoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class Controllers {

    @Autowired
    InfoServices infoServices;

    @GetMapping("/info")
    public ResponseEntity<String> getInfo(){
        String info = infoServices.generateStubInfo();
        return ResponseEntity.ok().body(info);
    }
}
