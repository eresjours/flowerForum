package flower.community.mapper;

import flower.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author WsW
 * @version 1.0
 */
@Mapper
public interface QuestionMapper {

    @Insert("INSERT INTO QUESTION (TITLE, DESCRIPTION, GMT_CREATE, GMT_MODIFIED, CREATOR, TAG) VALUES (#{TITLE}, #{DESCRIPTION}, #{GMT_CREATE}, #{GMT_MODIFIED}, #{CREATOR}, #{TAG})")
    void create(Question question);
}
