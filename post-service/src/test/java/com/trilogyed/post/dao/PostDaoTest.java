package com.trilogyed.post.dao;


import com.trilogyed.post.model.Post;
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
public class PostDaoTest {

    @Autowired
    PostDao postDao;

    @Before
    public void setUp(){
        List<Post> postList = postDao.findAll();
        for (Post post:
             postList) {
            postDao.deleteById(post.getPostID());
        }
    }

    @Test
    public void createGetGetAllDeletePostTest(){
        Post post = new Post();
        post.setPosterName("post name");
        post.setPostDate(LocalDate.of(10,10,10));
        post.setPost("this is a post");

        postDao.save(post);

        assertEquals(post, postDao.getOne(post.getPostID()));

        postDao.deleteById(post.getPostID());

        assertEquals(0,postDao.findAll().size());
    }

    @Test
    public void updatePostTest(){
        Post post = new Post();
        post.setPosterName("post name");
        post.setPostDate(LocalDate.of(10,10,10));
        post.setPost("this is a post");

        postDao.save(post);

        post.setPost("this is the new post");

        postDao.save(post);

        assertEquals("this is the new post", postDao.getOne(post.getPostID()).getPost());
    }

    @Test
    public void getPostByPostName(){
        Post post = new Post();
        post.setPosterName("post name");
        post.setPostDate(LocalDate.of(10,10,10));
        post.setPost("this is a post");

        postDao.save(post);

        List<Post> postList = new ArrayList<>();
        postList.add(post);

        assertEquals(postList,postDao.getPostByPosterName("post name"));
    }
}
