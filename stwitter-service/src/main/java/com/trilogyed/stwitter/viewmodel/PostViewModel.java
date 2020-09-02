package com.trilogyed.stwitter.viewmodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostViewModel {

    private int postID;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate postDate;
    @Size(min = 1,max = 50, message = "Post name can only be between 1-50 characters")
    private String posterName;
    @Size(min = 1,max = 255, message = "Post can only be between 1-255 characters")
    private String post;
    private List<CommentViewModel> commentViewModelList;

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<CommentViewModel> getCommentViewModelList() {
        return commentViewModelList;
    }

    public void setCommentViewModelList(List<CommentViewModel> commentViewModelList) {
        this.commentViewModelList = commentViewModelList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostViewModel that = (PostViewModel) o;
        return postID == that.postID &&
                postDate.equals(that.postDate) &&
                posterName.equals(that.posterName) &&
                post.equals(that.post) &&
                commentViewModelList.equals(that.commentViewModelList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postID, postDate, posterName, post, commentViewModelList);
    }

    @Override
    public String toString() {
        return "PostViewModel{" +
                "postID=" + postID +
                ", postDate=" + postDate +
                ", posterName='" + posterName + '\'' +
                ", post='" + post + '\'' +
                ", commentViewModelList=" + commentViewModelList +
                '}';
    }
}
