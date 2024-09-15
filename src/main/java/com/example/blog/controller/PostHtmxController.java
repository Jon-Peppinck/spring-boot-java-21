package com.example.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blog.entity.Post;
import com.example.blog.service.PostService;

@Controller
@RequestMapping("/htmx/posts")
public class PostHtmxController {

  @Autowired
  private PostService postService;

  @GetMapping
  public String getAllPosts(Model model) {
    List<Post> posts = postService.getAllPosts();

    model.addAttribute("title", "Blog Page");
    model.addAttribute("content", "all-posts");
    model.addAttribute("posts", posts);
    return "index";
  }

  @PostMapping
  public String createPost(@RequestParam String text, Model model) {
    Post post = new Post();
    post.setText(text);
    postService.createPost(post);
    List<Post> posts = postService.getAllPosts();
    model.addAttribute("posts", posts);
    return "blog/all-posts :: #posts-list";
  }

  @GetMapping("/{id}/edit")
  public String getPostForEdit(@PathVariable Long id, Model model) {
    Post post = postService.getPostById(id);
    model.addAttribute("post", post);
    return "blog/edit-post-form :: #edit-form-container";
  }

  @PatchMapping("/{id}")
  public String updatePost(@PathVariable Long id, @RequestParam String text, Model model) {
    Post post = postService.getPostById(id);
    post.setText(text);
    postService.updatePost(id, post);
    List<Post> posts = postService.getAllPosts();
    model.addAttribute("posts", posts);
    return "blog/all-posts :: #posts-list";
  }

  @DeleteMapping("/{id}")
  public String deletePost(@PathVariable Long id, Model model) {
    postService.deletePost(id);
    List<Post> posts = postService.getAllPosts();
    model.addAttribute("posts", posts);
    return "blog/all-posts :: #posts-list";
  }
}
