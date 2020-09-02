package com.trilogyed.comment.controller;

import com.trilogyed.comment.CommentServiceApplication;
import com.trilogyed.comment.dao.CommentDao;
import com.trilogyed.comment.model.Comment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CacheConfig(cacheNames = {"comments"})
@Service
public class CommentController {

    @Autowired
    CommentDao commentDao;

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @RabbitListener(queues = CommentServiceApplication.QUEUE_NAME_CREATE)
    public void createComment(Comment comment) {
        commentDao.save(comment);
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.GET)
    public Comment getComment(@PathVariable int id) {
        return commentDao.getOne(id);
    }

    @RequestMapping(value = "/comments/post/{id}", method = RequestMethod.GET)
    public List<Comment> getCommentByPostId(@PathVariable int id) {
        return commentDao.getAllCommentsByPostId(id);
    }


    @RequestMapping(value = "/comments/{id}", method = RequestMethod.PUT)
    @RabbitListener(queues = CommentServiceApplication.QUEUE_NAME_UPDATE)
    public void updateComment(Comment comment, @PathVariable int id) {
        commentDao.save(comment);
    }

    @RequestMapping(value = "/comments/{id}", method = RequestMethod.DELETE)
    @RabbitListener(queues = CommentServiceApplication.QUEUE_NAME_DELETE)
    public void deleteComment( int id) {
        commentDao.deleteById(id);
    }
}



