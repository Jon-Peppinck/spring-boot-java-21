package com.example.blog.service;

import com.example.blog.entity.Post;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

  @Autowired
  private PostRepository postRepository;

  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  public Post getPostById(Long id) {
    return postRepository.findById(id).orElse(null);
  }

  public Post createPost(Post post) {
    post.setDateAdded(LocalDateTime.now());
    post.setDateModified(LocalDateTime.now());
    return postRepository.save(post);
  }

  public Post updatePost(Long id, Post updatedPost) {
    Post existingPost = getPostById(id);
    if (existingPost != null) {
      existingPost.setText(updatedPost.getText());
      existingPost.setDateModified(LocalDateTime.now());
      return postRepository.save(existingPost);
    }
    return null;
  }

  public void deletePost(Long id) {
    postRepository.deleteById(id);
  }
}
