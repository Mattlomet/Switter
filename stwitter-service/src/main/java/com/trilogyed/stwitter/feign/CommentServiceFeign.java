package com.trilogyed.stwitter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "comment-service")
public interface CommentServiceFeign {

    @GetMapping("/comments/{id}")
    String getCommentById(@PathVariable int id);

    @GetMapping("/comments/post/{id}")
    String getCommentsByPostId(@PathVariable int id);

}
