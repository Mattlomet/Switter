package com.trilogyed.stwitter.servicelayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.stwitter.feign.CommentServiceFeign;
import com.trilogyed.stwitter.feign.PostServiceFeign;
import com.trilogyed.stwitter.model.Comment;
import com.trilogyed.stwitter.model.Post;
import com.trilogyed.stwitter.service.ServiceLayer;
import com.trilogyed.stwitter.viewmodel.CommentViewModel;
import com.trilogyed.stwitter.viewmodel.PostViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ServiceLayerTest {

    ServiceLayer serviceLayer;

    @Mock
    PostServiceFeign postServiceFeign;

    @Mock
    CommentServiceFeign commentServiceFeign;

    @Mock
    RabbitTemplate rabbitTemplate;

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws JsonProcessingException {
        setUpPostServiceFeignMock();
        setUpCommentServiceFeignMocks();
        serviceLayer = new ServiceLayer(commentServiceFeign, postServiceFeign, rabbitTemplate);
    }

    @Test
    public void createGetPostTest() throws JsonProcessingException {
        PostViewModel post = new PostViewModel();
        post.setPosterName("post name");
        post.setPostDate(LocalDate.of(10,10,10));
        post.setPost("this is a post");

        post = serviceLayer.createPost(post);

        assertEquals(post,serviceLayer.getPost(post.getPostID()));
    }

    @Test
    public void getPostByPosterName() throws JsonProcessingException {
        PostViewModel post1 = new PostViewModel();
        post1.setPostID(1);
        post1.setPosterName("post name");
        post1.setPostDate(LocalDate.of(10,10,10));
        post1.setPost("this is a post");

        CommentViewModel comment1 = new CommentViewModel();
        comment1.setCommentId(1);
        comment1.setCommenterName("comment name");
        comment1.setComment("comment");
        comment1.setCreateDate(LocalDate.of(10,10,10));
        comment1.setPostId(1);

        List<CommentViewModel> commentViewModelList = new ArrayList<>();
        commentViewModelList.add(comment1);

        post1.setCommentViewModelList(commentViewModelList);

        List<PostViewModel> postViewModelList = new ArrayList<>();
        postViewModelList.add(post1);

        assertEquals(postViewModelList,serviceLayer.getPostsByPosterName(post1.getPosterName()));
    }

    @Test
    public void getCommentById() throws JsonProcessingException {
        CommentViewModel comment1 = new CommentViewModel();
        comment1.setCommentId(1);
        comment1.setCommenterName("comment name");
        comment1.setComment("comment");
        comment1.setCreateDate(LocalDate.of(10,10,10));
        comment1.setPostId(1);

        assertEquals(comment1,serviceLayer.getComment(comment1.getCommentId()));
    }

    @Test
    public void getCommentsByPostId() throws JsonProcessingException {
        CommentViewModel comment1 = new CommentViewModel();
        comment1.setCommentId(1);
        comment1.setCommenterName("comment name");
        comment1.setComment("comment");
        comment1.setCreateDate(LocalDate.of(10,10,10));
        comment1.setPostId(1);

        List<CommentViewModel> commentViewModelList = new ArrayList<>();
        commentViewModelList.add(comment1);

        assertEquals(commentViewModelList,serviceLayer.getCommentsByPostId(comment1.getCommentId()));
    }



    public void setUpPostServiceFeignMock() throws JsonProcessingException {
        postServiceFeign = mock(PostServiceFeign.class);

        Post post = new Post();
        post.setPosterName("post name");
        post.setPostDate(LocalDate.of(10,10,10));
        post.setPost("this is a post");

        Post post1 = new Post();
        post1.setPostID(1);
        post1.setPosterName("post name");
        post1.setPostDate(LocalDate.of(10,10,10));
        post1.setPost("this is a post");

        String postString = mapper.writeValueAsString(post1);

        List<Post> postList = new ArrayList<>();
        postList.add(post1);

        String postStringList = mapper.writeValueAsString(postList);

        doReturn(post1).when(postServiceFeign).createPost(post);
        doReturn(postString).when(postServiceFeign).getPostById(1);
        doReturn(postStringList).when(postServiceFeign).getPostByPosterName("post name");
    }

        public void setUpCommentServiceFeignMocks() throws JsonProcessingException {
        commentServiceFeign = mock(CommentServiceFeign.class);

        CommentViewModel comment1 = new CommentViewModel();
        comment1.setCommentId(1);
        comment1.setCommenterName("comment name");
        comment1.setComment("comment");
        comment1.setCreateDate(LocalDate.of(10,10,10));
        comment1.setPostId(1);

        Comment comment = new Comment();
        comment.setCommentId(1);
        comment.setCommenterName("comment name");
        comment.setComment("comment");
        comment.setCreateDate(LocalDate.of(10,10,10));
        comment.setPostId(1);

        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);

        String commentString = mapper.writeValueAsString(comment);
        String commentStringList = mapper.writeValueAsString(commentList);


        doReturn(commentString).when(commentServiceFeign).getCommentById(1);
        doReturn(commentStringList).when(commentServiceFeign).getCommentsByPostId(1);

    }
}
