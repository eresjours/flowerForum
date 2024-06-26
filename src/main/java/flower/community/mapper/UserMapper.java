package flower.community.mapper;

import flower.community.model.Comment;
import flower.community.model.Question;
import flower.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USER (NAME, ACCOUNT_ID, TOKEN, GMT_CREATE, GMT_MODIFIED, AVATAR_URL) VALUES (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    void insert(User user);

    @Select("SELECT * FROM USER WHERE TOKEN = #{token}")
    User findByToken(@Param("token") String token);

    @Select("SELECT * FROM USER WHERE ID = #{id}")
    User findByID(@Param("id") Long creator);

    @Select("SELECT * FROM USER WHERE ACCOUNT_ID = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("UPDATE USER SET NAME=#{name}, TOKEN=#{token}, GMT_MODIFIED=#{gmtModified}, AVATAR_URL=#{avatarUrl} WHERE ACCOUNT_ID=#{accountId}")
    void update(User User);

}
