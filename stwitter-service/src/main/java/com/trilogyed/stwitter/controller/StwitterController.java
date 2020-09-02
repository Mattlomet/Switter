package com.trilogyed.stwitter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trilogyed.stwitter.service.ServiceLayer;
import com.trilogyed.stwitter.viewmodel.CommentViewModel;
import com.trilogyed.stwitter.viewmodel.PostViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
public class StwitterController {

    @Autowired
    ServiceLayer service;

    public StwitterController(ServiceLayer service) {
        this.service = service;
    }

    // Post service routes

    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public PostViewModel createPost(@RequestBody @Valid PostViewModel post) throws JsonProcessingException {
         return service.createPost(post);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public PostViewModel getPost(@PathVariable int id) throws JsonProcessingException {
        return service.getPost(id);
    }

    @RequestMapping(value = "/post/user/{poster_name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<PostViewModel> getPostsByPosterName(@PathVariable String poster_name) throws JsonProcessingException {
        return service.getPostsByPosterName(poster_name);
    }


    // Comment Service Routes


    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@RequestBody @Valid CommentViewModel comment) {
        System.out.println(comment);
        service.createComment(comment);
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CommentViewModel getComment(@PathVariable int id) throws JsonProcessingException {
        return service.getComment(id);
    }

    @RequestMapping(value = "/comments/post/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<CommentViewModel> getCommentByPostId(@PathVariable int id) throws JsonProcessingException {
        return service.getCommentsByPostId(id);
    }


    @RequestMapping(value = "/comments/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@RequestBody @Valid CommentViewModel comment) {
        service.updateComment(comment);
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment( int id) {
        service.deleteComment(id);
    }
}
