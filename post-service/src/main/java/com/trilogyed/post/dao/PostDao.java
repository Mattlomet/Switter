package com.trilogyed.post.dao;

import com.trilogyed.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao extends JpaRepository<Post,Integer> {
    List<Post> getPostByPosterName(String posterName);
}
