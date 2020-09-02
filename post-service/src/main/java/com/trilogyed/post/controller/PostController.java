package com.trilogyed.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.post.dao.PostDao;
import com.trilogyed.post.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.DataInput;
import java.io.IOException;
import java.util.List;

@RestController
@CacheConfig(cacheNames = {"posts"})
public class PostController {

    @Autowired
    PostDao postDao;

    ObjectMapper mapper = new ObjectMapper();

    @CachePut(key = "#result.getPostID()")
    @RequestMapping(value = "/posts", method = RequestMethod.POST)
    public Post createPost(@RequestBody Post post) throws IOException {
        return postDao.save(post);
    }


    @Cacheable
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
    public Post getPost(@PathVariable int id) {
        return postDao.getOne(id);
    }

    @Cacheable
    @RequestMapping(value = "/posts/user/{posterName}", method = RequestMethod.GET)
    public List<Post> getPostByPostName(@PathVariable String posterName) {
        return postDao.getPostByPosterName(posterName);
    }


    @CacheEvict(key = "#result.getPostID()")
    @RequestMapping(value = "/posts", method = RequestMethod.PUT)
    public void updatePost(@RequestBody Post post) {
        postDao.save(post);
    }

    @CacheEvict
    @RequestMapping(value = "/posts/{id}", method = RequestMethod.DELETE)
    public void deletePost(@PathVariable int id) {
        postDao.deleteById(id);
    }
}
