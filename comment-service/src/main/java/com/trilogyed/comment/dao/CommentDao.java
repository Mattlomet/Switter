package com.trilogyed.comment.dao;

import com.trilogyed.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment,Integer> {
    List<Comment> getAllCommentsByPostId(int postId);
}
