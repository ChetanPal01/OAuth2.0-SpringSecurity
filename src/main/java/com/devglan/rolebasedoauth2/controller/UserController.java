package com.devglan.rolebasedoauth2.controller;

import com.devglan.rolebasedoauth2.dto.ApiResponse;
import com.devglan.rolebasedoauth2.dto.UserDto;
import com.devglan.rolebasedoauth2.service.AuthenticationFacadeService;
import com.devglan.rolebasedoauth2.service.UserService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.devglan.rolebasedoauth2.constant.AuthConstants.*;

@RestController
@RequestMapping("/users")
@Log
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationFacadeService authenticationFacadeService;

    @Secured({ROLE_ADMIN})
    @GetMapping
    public ApiResponse listUser(){
        log.info(String.format("received request to list user {}", authenticationFacadeService.getAuthentication().getPrincipal()));
        return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findAll());
    }

    @Secured({ROLE_ADMIN})
    @PostMapping
    public ApiResponse create(@RequestBody UserDto user){
        log.info(String.format("received request to create user {}", authenticationFacadeService.getAuthentication().getPrincipal()));
        return new ApiResponse(HttpStatus.OK, SUCCESS, userService.save(user));
    }

    @Secured({ROLE_ADMIN, ROLE_USER})
    @GetMapping(value = "/{id}")
    public ApiResponse getUser(@PathVariable long id){
        log.info(String.format("received request to update user {}", authenticationFacadeService.getAuthentication().getPrincipal()));
        return new ApiResponse(HttpStatus.OK, SUCCESS, userService.findOne(id));
    }

    @Secured({ROLE_ADMIN})
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") Long id){
        log.info(String.format("received request to delete user {}", authenticationFacadeService.getAuthentication().getPrincipal()));
        userService.delete(id);
    }



}
