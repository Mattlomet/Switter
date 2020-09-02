package com.trilogyed.stwitter.feign;

import com.trilogyed.stwitter.model.Post;
import com.trilogyed.stwitter.viewmodel.PostViewModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "post-service")
public interface PostServiceFeign {

    @GetMapping("/posts/{id}")
    String getPostById(@PathVariable int id);

    @GetMapping("/posts/user/{poster_name}")
    String getPostByPosterName(@PathVariable String poster_name);

    @PostMapping("/posts")
    Post createPost(@RequestBody Post post);

    @PutMapping("/posts")
    void updatePost(@RequestBody Post post);

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable int id);

}
