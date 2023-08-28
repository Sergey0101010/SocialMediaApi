package com.sergey.socialmediaapi.controllers;

import com.sergey.socialmediaapi.business.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/friend-request/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void friendRequest(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.friendRequest(id, userDetails);
    }

    @DeleteMapping("/delete-friend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteFriend(id, userDetails);
    }

    @PostMapping("/accept-friend/{id}")
    public void acceptFriend(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.acceptFriend(id, userDetails);
    }

    @PostMapping("/message/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void messageFriend(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.messageFriend(id, userDetails);
    }
}
