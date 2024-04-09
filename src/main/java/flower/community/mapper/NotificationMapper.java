package flower.community.mapper;

import flower.community.model.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Mapper
public interface NotificationMapper {


    @Insert("INSERT INTO NOTIFICATION (NOTIFIER, RECEIVER, OUTERID, TYPE, GMT_CREATE, STATUS, NOTIFIER_NAME, OUTER_TITLE) VALUES (#{notifier}, #{receiver}, #{outerId}, #{type}, #{gmtCreate}, #{status}, #{notifierName}, #{outerTitle})")
    void insertNotified(Notification notification);

    @Select("SELECT COUNT(1) FROM NOTIFICATION WHERE RECEIVER = #{receiver}")
    Integer countByReceiver(Long receiver);

    @Select("SELECT * FROM NOTIFICATION WHERE RECEIVER = #{id} ORDER BY GMT_CREATE DESC LIMIT #{offset}, #{size}")
    List<Notification> listByReceiver(@Param(value = "id") Long id, @Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Select("SELECT COUNT(1) FROM NOTIFICATION WHERE RECEIVER = #{id} AND STATUS = 0")
    Long countUnreadByReceiver(Long id);

    // 根据评论ID查找对应的评论
    @Select("SELECT * FROM NOTIFICATION WHERE ID = #{id}")
    Notification selectByPrimaryKey(Long id);


    @Update("UPDATE NOTIFICATION SET STATUS = #{status} WHERE ID = #{id}")
    void updateStatus(Notification notification);
}
