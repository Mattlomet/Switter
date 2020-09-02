package com.trilogyed.comment.dao;


import com.trilogyed.comment.model.Comment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class CommentDaoTest {

    @Autowired
    CommentDao commentDao;

    @Before
    public void setUp(){
        List<Comment> commentList = commentDao.findAll();
        for (Comment comment:
             commentList) {
            commentDao.deleteById(comment.getCommentId());
        }
    }

    @Test
    public void createGetGetAllDeleteCommentTest(){
        Comment comment = new Comment();
        comment.setCommenterName("comment name");
        comment.setComment("comment");
        comment.setCreateDate(LocalDate.of(10,10,10));
        comment.setPostId(1);

        commentDao.save(comment);

        assertEquals(comment, commentDao.getOne(comment.getCommentId()));

        commentDao.deleteById(comment.getCommentId());

        assertEquals(0, commentDao.findAll().size());
    }

    @Test
    public void updateCommentTest(){
        Comment comment = new Comment();
        comment.setCommenterName("comment name");
        comment.setComment("comment");
        comment.setCreateDate(LocalDate.of(10,10,10));
        comment.setPostId(1);

        commentDao.save(comment);

        comment.setComment("this is the new comment");

        commentDao.save(comment);

        assertEquals("this is the new comment",commentDao.getOne(comment.getCommentId()).getComment());
    }

    @Test
    public void getCommentByPostIdTest(){
        Comment comment = new Comment();
        comment.setCommenterName("comment name");
        comment.setComment("comment");
        comment.setCreateDate(LocalDate.of(10,10,10));
        comment.setPostId(1);

        commentDao.save(comment);

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);


        assertEquals(commentList,commentDao.getAllCommentsByPostId(1));
    }
}
