package com.sergey.socialmediaapi.business;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserService {
    void friendRequest(UUID id, UserDetails userDetails);

    void deleteFriend(UUID id, UserDetails userDetails);

    void acceptFriend(UUID id, UserDetails userDetails);

    void messageFriend(UUID id, UserDetails userDetails);
}
