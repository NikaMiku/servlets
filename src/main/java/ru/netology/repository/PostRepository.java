package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {
  private final List<Post> posts = new ArrayList<>();
  private final AtomicLong idCounter = new AtomicLong(1);

  public synchronized List<Post> all() {
    return new ArrayList<>(posts);
  }

  public synchronized Optional<Post> getById(long id) {
    return posts.stream().filter(post -> post.getId() == id).findFirst();
  }

  public synchronized Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(idCounter.getAndIncrement());
      posts.add(post);
    } else {
      var existingPost = getById(post.getId()).orElseThrow(() -> new NotFoundException("Post not found"));
      existingPost.setContent(post.getContent());
    }
    return post;
  }

  public synchronized void removeById(long id) {
    var post = getById(id).orElseThrow(() -> new NotFoundException("Post not found"));
    posts.remove(post);
  }
}