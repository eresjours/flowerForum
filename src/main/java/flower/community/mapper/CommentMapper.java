package flower.community.mapper;

import flower.community.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Mapper
public interface CommentMapper {


    @Insert("INSERT INTO COMMENT (PARENT_ID, TYPE, COMMENTATOR, GMT_CREATE, GMT_MODIFIED, CONTENT, LIKE_COUNT) VALUES (#{parentId}, #{type}, #{commentator}, #{gmtCreate}, #{gmtModified}, #{content}, #{likeCount})")
    void insert(Comment comment);

    @Select("SELECT * FROM COMMENT WHERE PARENT_ID=#{parentId} AND TYPE=1")
    List<Comment> selectByQuestionID(@Param("parentId") Long parentId);

//    @Select("SELECT * FROM COMMENT WHERE PARENT_ID=#{parentId} AND TYPE=2")
//    Comment selectByCommentID(@Param("parentId") Long parentId);

    @Select("SELECT * FROM COMMENT WHERE ID=#{parentId}")
    Comment selectByCommentID(@Param("parentId") Long parentId);

    @Update("UPDATE COMMENT SET COMMENT_COUNT = COMMENT_COUNT + 1 WHERE ID = #{id};")
    void updateCommentCount(@Param("id") Long id);

    @Select("SELECT PARENT_ID FROM COMMENT WHERE ID=#{outerid}")
    Long getQuestionIdByParentId(@Param("outerid") Long outerid);
}
