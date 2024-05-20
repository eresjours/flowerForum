package flower.community.mapper;

import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.model.Comment;
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
    @Select("SELECT * FROM QUESTION ORDER BY GMT_CREATE DESC LIMIT #{offset}, #{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(1) FROM QUESTION")
    Integer count();

    @Select("SELECT * FROM QUESTION WHERE CREATOR = #{userId} ORDER BY GMT_CREATE DESC LIMIT #{offset}, #{size}")
    List<Question> listByUserId(@Param(value = "userId") Long userId, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(1) FROM QUESTION WHERE CREATOR = #{userId}")
    Integer countByUserId(@Param(value = "userId") Long userId);


    @Select("SELECT * FROM QUESTION WHERE ID = #{id} ORDER BY GMT_CREATE DESC")
    Question getById(@Param("id") Long id);

    @Update("UPDATE QUESTION SET TITLE = #{title}, DESCRIPTION = #{description}, GMT_MODIFIED = #{gmtModified}, TAG = #{tag} WHERE ID = #{id}")
    int update(Question question);

    @Update("UPDATE QUESTION SET VIEW_COUNT = VIEW_COUNT + 1 WHERE ID = #{id};")
    void updateViewCount(Long id);

    @Select("SELECT * FROM QUESTION WHERE ID = #{parentId}")
    Question selectByPrimaryKey(@Param(value = "parentId") Long parentId);

    @Update("UPDATE QUESTION SET COMMENT_COUNT = COMMENT_COUNT + 1 WHERE ID = #{id};")
    void updateCommentCount(Long id);

    @Select("SELECT * FROM COMMENT WHERE PARENT_ID = #{id} AND TYPE = 1 ORDER BY GMT_CREATE DESC")
    List<Comment> selectByQuestionId(Long id);

    @Select("SELECT * FROM COMMENT WHERE PARENT_ID = #{id} AND TYPE = 2 ORDER BY GMT_CREATE DESC")
    List<Comment> selectByCommentId(Long id);

    @Select("SELECT * FROM QUESTION WHERE ID != #{id} AND TAG REGEXP #{tag}")
    List<QuestionDTO> selectRelated(QuestionDTO questionDTO);

    @Select("SELECT * FROM QUESTION WHERE TITLE LIKE CONCAT('%', #{search}, '%') ORDER BY GMT_CREATE DESC LIMIT #{offset}, #{size}")
    List<Question> selectBySearch(@Param(value = "search") String search, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(*) FROM QUESTION WHERE TITLE LIKE CONCAT('%', #{search}, '%')")
    Integer countBySearch(String search);

    @Update("UPDATE QUESTION SET LIKE_COUNT = LIKE_COUNT + 1 WHERE ID = #{id};")
    void updateLikecount(Long id);

    @Select("SELECT * FROM QUESTION WHERE LIKE_COUNT > 0 ORDER BY LIKE_COUNT DESC LIMIT 5")
    List<Question> getTopList();

    @Delete("DELETE FROM QUESTION WHERE ID = #{id}")
    void deleteById(Long id);
}
