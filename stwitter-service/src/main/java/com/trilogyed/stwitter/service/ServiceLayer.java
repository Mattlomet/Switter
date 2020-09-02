package com.trilogyed.stwitter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trilogyed.stwitter.feign.CommentServiceFeign;
import com.trilogyed.stwitter.feign.PostServiceFeign;
import com.trilogyed.stwitter.model.Comment;
import com.trilogyed.stwitter.model.Post;
import com.trilogyed.stwitter.viewmodel.CommentViewModel;
import com.trilogyed.stwitter.viewmodel.PostViewModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class ServiceLayer {

    @Autowired
    private final CommentServiceFeign commentServiceFeign;

    @Autowired
    private final PostServiceFeign postServiceFeign;

    ObjectMapper mapper = new ObjectMapper();

    public static final String EXCHANGE = "comment-exchange";
    public static final String ROUTING_KEY_CREATE = "comment.queue.create";
    public static final String ROUTING_KEY_UPDATE = "comment.queue.update";
    public static final String ROUTING_KEY_DELETE = "comment.queue.delete";

    @Autowired
    private final RabbitTemplate rabbitTemplate;


    public ServiceLayer(CommentServiceFeign commentServiceFeign, PostServiceFeign postServiceFeign, RabbitTemplate rabbitTemplate) {
        this.commentServiceFeign = commentServiceFeign;
        this.postServiceFeign = postServiceFeign;
        this.rabbitTemplate = rabbitTemplate;
    }

    //Comment API
    public void createComment(CommentViewModel commentViewModel){
        Comment comment = new Comment();
        comment.setComment(commentViewModel.getComment());
        comment.setCommenterName(commentViewModel.getCommenterName());
        comment.setCreateDate(commentViewModel.getCreateDate());
        comment.setPostId(commentViewModel.getPostId());

        rabbitTemplate.convertAndSend(EXCHANGE,ROUTING_KEY_CREATE,comment);
    }

    public CommentViewModel getComment(int id) throws JsonProcessingException {
        Comment comment = mapper.readValue(commentServiceFeign.getCommentById(id),Comment.class);
        return buildCommentViewModel(comment);
    }

    public List<CommentViewModel> getCommentsByPostId(int id) throws JsonProcessingException {

        List<Comment> commentList = mapper.readValue(commentServiceFeign.getCommentsByPostId(id),new TypeReference<List<Comment>>(){});

        List<CommentViewModel> commentViewModelList = new ArrayList<>();

        for (Comment comment:
             commentList) {
            commentViewModelList.add(buildCommentViewModel(comment));
        }

        return commentViewModelList;
    }

    public void updateComment(CommentViewModel commentViewModel){
        Comment comment = new Comment();
        comment.setCommentId(commentViewModel.getCommentId());
        comment.setComment(commentViewModel.getComment());
        comment.setCommenterName(commentViewModel.getCommenterName());
        comment.setCreateDate(commentViewModel.getCreateDate());
        comment.setPostId(commentViewModel.getPostId());

        rabbitTemplate.convertAndSend(EXCHANGE,ROUTING_KEY_UPDATE,comment);
    }

    public void deleteComment(int id){
        rabbitTemplate.convertAndSend(EXCHANGE,ROUTING_KEY_DELETE,id);
    }

    // Post API

    public PostViewModel createPost(PostViewModel pvm) throws JsonProcessingException {
        Post post = new Post();
        post.setPost(pvm.getPost());
        post.setPostDate(pvm.getPostDate());
        post.setPosterName(pvm.getPosterName());


        post = postServiceFeign.createPost(post);
        post.setPostID(post.getPostID());

        return buildPostViewModel(post);
    }

    public PostViewModel getPost(int id) throws JsonProcessingException {
        Post post = mapper.readValue(postServiceFeign.getPostById(id),Post.class);
        return buildPostViewModel(post);
    }

    public List<PostViewModel> getPostsByPosterName(String posterName) throws JsonProcessingException {

        List<Post> postList = mapper.readValue(postServiceFeign.getPostByPosterName(posterName), new TypeReference<List<Post>>(){});

        List<PostViewModel> postViewModelList = new ArrayList<>();
        for (Post post:
             postList) {
            postViewModelList.add(buildPostViewModel(post));
        }

        return postViewModelList;
    }

    public void updatePost(PostViewModel pvm) throws JsonProcessingException {
        Post post = new Post();
        post.setPost(pvm.getPost());
        post.setPostDate(pvm.getPostDate());
        post.setPosterName(pvm.getPosterName());
        post.setPostID(post.getPostID());


        postServiceFeign.updatePost(post);
    }

    public void deletePost(int id){
        postServiceFeign.deletePost(id);
    }


    // helper methods

    public CommentViewModel buildCommentViewModel(Comment comment){
        CommentViewModel cvm = new CommentViewModel();
        cvm.setCommentId(comment.getCommentId());
        cvm.setComment(comment.getComment());
        cvm.setCommenterName(comment.getCommenterName());
        cvm.setCreateDate(comment.getCreateDate());
        cvm.setPostId(comment.getPostId());
        return cvm;
    }

    public PostViewModel buildPostViewModel(Post post) throws JsonProcessingException {
        PostViewModel pvm = new PostViewModel();
        pvm.setPostID(post.getPostID());
        pvm.setPost(post.getPost());
        pvm.setPosterName(post.getPosterName());
        pvm.setPostDate(post.getPostDate());
        pvm.setCommentViewModelList(getCommentsByPostId(post.getPostID()));
        return pvm;
    }

}
