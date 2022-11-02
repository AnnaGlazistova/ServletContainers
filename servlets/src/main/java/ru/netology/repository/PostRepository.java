package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
@Repository
public class PostRepository {

    private final ConcurrentHashMap<Long, Post> mapOfPosts = new ConcurrentHashMap<>();

    private final AtomicLong counter = new AtomicLong(0);

    public List<Post> all() {
        return new ArrayList<>(mapOfPosts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(mapOfPosts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(counter.getAndIncrement());
            mapOfPosts.put(post.getId(), post);
        } else if (mapOfPosts.containsKey(post.getId())) {
            mapOfPosts.replace(post.getId(), post);
        } else {
            throw new NotFoundException("Post does not exist");
        }
        return post;
    }

    public void removeById(long id) {
        if (mapOfPosts.containsKey(id)) {
            mapOfPosts.remove(id);
        } else {
            throw new NotFoundException("Post does not exist");
        }
    }
}
