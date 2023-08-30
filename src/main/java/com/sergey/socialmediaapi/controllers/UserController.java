package com.sergey.socialmediaapi.controllers;

import com.sergey.socialmediaapi.business.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
@SecurityRequirement(name = "socialMediaAuth")
@Tag(name = "User", description = "User interaction API allowing them to make communications")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Send friendship request to some user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Friendship request successfully send"),
            @ApiResponse(responseCode = "400", description = "User with such email already exist"),
            @ApiResponse(responseCode = "404", description = "Id of requested user not found"),
            @ApiResponse(responseCode = "409", description = "You already have relation with this user")
    })
    @PostMapping("/friend-request/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void friendRequest(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.friendRequest(id, userDetails);
    }

    @Operation(summary = "Delete friend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully delete friend"),
            @ApiResponse(responseCode = "400", description = "This user is not your friend with you"),
            @ApiResponse(responseCode = "404", description = "Id of requested user not found"),
    })
    @DeleteMapping("/delete-friend/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteFriend(id, userDetails);
    }

    @Operation(summary = "Accept friendship request from some user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully accepted friend request"),
            @ApiResponse(responseCode = "400", description = "This user doesn't requested friendship with you"),
            @ApiResponse(responseCode = "404", description = "Id of requested user not found"),
    })
    @PostMapping("/accept-friend/{id}")
    public void acceptFriend(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.acceptFriend(id, userDetails);
    }

    @Operation(summary = "Send messaging request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully requested messaging with friend"),
            @ApiResponse(responseCode = "400", description = "This user is not your friend with you"),
            @ApiResponse(responseCode = "404", description = "Id of requested user not found"),
    })
    @PostMapping("/message/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void messageFriend(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        userService.messageFriend(id, userDetails);
    }
}
