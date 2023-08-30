package com.sergey.socialmediaapi.business;

import com.sergey.socialmediaapi.domain.dto.PostResponseDto;
import com.sergey.socialmediaapi.domain.maper.PostingMapper;
import com.sergey.socialmediaapi.domain.model.RelationState;
import com.sergey.socialmediaapi.domain.model.User;
import com.sergey.socialmediaapi.repositories.PostRepository;
import com.sergey.socialmediaapi.repositories.UserRelationRepository;
import com.sergey.socialmediaapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActivityFeedServiceImpl implements ActivityFeedService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Override
    public Page<PostResponseDto> getAllFeedPosts(boolean newToOld, int page, int size, UserDetails userDetails) {
        Sort postSort = newToOld ? Sort.by("creationDate").descending() : Sort.by("creationDate").ascending();
        Pageable postPage = PageRequest.of(page, size, postSort);
        User userByEmail = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        List<User> subscribedList = getUserList(userByEmail);

        return postRepository.findPostsByCreatorIn(subscribedList, postPage)
                .map(PostingMapper::postToPostResponseDto);
    }

    private static List<User> getUserList(User userByEmail) {
        List<User> friendsList = userByEmail
                .getUserRelations()
                .stream()
                .filter(userRelation -> userRelation.getRelationState().equals(RelationState.FRIENDS))
                .map(userRelation -> userRelation.getRelationId().getRelatingUser().equals(userByEmail) ?
                        userRelation.getRelationId().getRelatedUser() :
                        userRelation.getRelationId().getRelatingUser())
                .toList();
        List<User> subscribedListRelating =
                userByEmail
                .getUserRelations()
                .stream()
                .filter(userRelation -> (userRelation.getRelationState().equals(RelationState.PENDING_FIRST_SECOND) ||
                        userRelation.getRelationState().equals(RelationState.SUBSCRIBE_FIRST_SECOND))
                        && userRelation.getRelationId().getRelatingUser().equals(userByEmail))
                .map(userRelation -> userRelation.getRelationId().getRelatingUser())
                .toList();
        List<User> subscribedListRelated =
                userByEmail
                        .getUserRelations()
                        .stream()
                        .filter(userRelation -> (userRelation.getRelationState().equals(RelationState.PENDING_SECOND_FIRST) ||
                                userRelation.getRelationState().equals(RelationState.SUBSCRIBE_SECOND_FIRST))
                                && userRelation.getRelationId().getRelatedUser().equals(userByEmail))
                        .map(userRelation -> userRelation.getRelationId().getRelatedUser())
                        .toList();
        List<User> subscribedList = new ArrayList<>(Stream.concat(friendsList.stream(), subscribedListRelating.stream()).toList());
        subscribedList.addAll(subscribedListRelated);
        return subscribedList;
    }
}
