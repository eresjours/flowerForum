package flower.community.mapper;

import flower.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO QUESTION (TITLE, DESCRIPTION, GMT_CREATE, GMT_MODIFIED, CREATOR, TAG) VALUES (#{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void create(Question question);

    // 跳过offset条数据, 查询size条数据
    @Select("SELECT * FROM QUESTION LIMIT #{offset}, #{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(1) FROM QUESTION")
    Integer count();

    @Select("SELECT * FROM QUESTION WHERE CREATOR = #{userId} LIMIT #{offset}, #{size}")
    List<Question> listByUserId(@Param(value = "userId") Long userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(1) FROM QUESTION WHERE CREATOR = #{userId}")
    Integer countByUserId(@Param(value = "userId") Long userId);


    @Select("SELECT * FROM QUESTION WHERE ID = #{id}")
    Question getById(@Param("id") Long id);

    @Update("UPDATE QUESTION SET TITLE = #{title}, DESCRIPTION = #{description}, GMT_MODIFIED = #{gmtModified}, TAG = #{tag} WHERE ID = #{id}")
    int update(Question question);

    @Update("UPDATE QUESTION SET VIEW_COUNT = VIEW_COUNT + 1 WHERE ID = #{id};")
    void updateViewCount(Long id);

    @Select("SELECT * FROM QUESTION WHERE ID = #{parentId}")
    Question selectByPrimaryKey(@Param(value = "parentId") Long parentId);

    @Update("UPDATE QUESTION SET COMMENT_COUNT = COMMENT_COUNT + 1 WHERE ID = #{id};")
    void updateCommentCount(Long id);
}
