package com.teclead.ventures.javatask.controller;


import com.teclead.ventures.javatask.dto.RequestUserDto;
import com.teclead.ventures.javatask.dto.ResponseUserDto;
import com.teclead.ventures.javatask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    @Operation(summary = "add new user", tags = "user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserDto addUser(@Valid @RequestBody RequestUserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/all/")
    @Operation(summary = "get all users", tags = "user")
    public List<ResponseUserDto> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{vorname}/all/")
    @Operation(summary = "get all users by vorname", tags = "user")
    public List<ResponseUserDto> getAllUsersByVorname(@Valid @PathVariable String vorname) {
        return userService.getAllUsersByVorname(vorname);
    }

    @GetMapping("/{id}/")
    @Operation(summary = "get one user by user id", tags = "user")
    public ResponseUserDto getUserById(@Valid @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/")
    @Operation(summary = "update user", tags = "user")
    public void updateUser(@Valid @PathVariable Long id, @RequestBody RequestUserDto userDto) {
        userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}/")
    @Operation(summary = "delete user", tags = "user")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@Valid @PathVariable Long id) {
        userService.deleteUser(id);
    }

}
