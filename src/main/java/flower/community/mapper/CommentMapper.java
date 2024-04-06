package flower.community.mapper;

import flower.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author WsW
 * @version 1.0
 */
@Mapper
public interface CommentMapper {


    @Insert("INSERT INTO COMMENT (PARENT_ID, TYPE, COMMENTATOR, GMT_CREATE, GMT_MODIFIED, CONTENT, LIKE_COUNT) VALUES (#{parentId}, #{type}, #{commentator}, #{gmtCreate}, #{gmtModified}, #{content}, #{likeCount})")
    void insert(Comment comment);

    @Select("SELECT * FROM COMMENT WHERE ID=#{parentId}")
    Comment selectByPrimaryKey(@Param("parentId") Long parentId);
}
