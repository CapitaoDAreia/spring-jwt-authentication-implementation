package com.example.demo.controllers;

import com.example.demo.domain.dto.AuthDTO;
import com.example.demo.domain.dto.JwtTokenDTO;
import com.example.demo.domain.entities.User;
import com.example.demo.services.JwtServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/auth")
public class AuthControllers {

    @Autowired
    AuthenticationManager manager;

    @Autowired
    JwtServices jwtServices;

    @PostMapping
    public ResponseEntity<?> auth(@RequestBody AuthDTO dto){
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var authentication = manager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) authentication.getPrincipal();

        var token = jwtServices.generateJwtToken(user);

        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(token);

        return ResponseEntity.ok().body(jwtTokenDTO);
    }
}
