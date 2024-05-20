package flower.community.service;

import flower.community.Datatransfermodel.NotificationDTO;
import flower.community.Datatransfermodel.PaginationDTO;
import flower.community.enums.NotificationStatusEnum;
import flower.community.enums.NotificationTypeEnum;
import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import flower.community.mapper.NotificationMapper;
import flower.community.model.Notification;
import flower.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 获取指定用户的通知列表,并进行分页处理
     *
     * @param userId 用户 ID
     * @param page   页码
     * @param size   每页数据量
     * @return 包含通知列表和分页信息的 PaginationDTO 对象
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        Integer totalCount = notificationMapper.countByReceiver(userId);    //通过SQL语句获取question总数
        paginationDTO.computeTotalPage(totalCount, size);
        /*
            容错处理
         */
        if (page < 1)
            page = 1;
        if (page > paginationDTO.getTotalPage())
            page = paginationDTO.getTotalPage();

        // 将page和size放入paginationDTO进行处理
        // totalCount之前已经放进去了，不再放入
        paginationDTO.setPagination(page);

        // 通过当前页数(page)和数据数量(size), 计算出偏移量
        // 在page页跳过offset条数据
        Integer offset = size * (page - 1);

        /*
            获取所有发给receiver的评论
            获取所有notification并添加user对象
         */
        List<Notification> notifications = notificationMapper.listByReceiver(userId, offset, size);
        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        // 将获取的questionList放入paginationDTO, 前端和分页使用
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    /**
     * 获取指定用户的未读通知数量
     *
     * @param id 用户 ID
     * @return 未读通知数量
     */
    public Long unreadCount(Long id) {
        return notificationMapper.countUnreadByReceiver(id);
    }

    /**
     * 将指定的通知标记为已读,并返回该通知的详细信息
     *
     * @param id   通知 ID
     * @param user 当前登录用户
     * @return 通知详细信息的 NotificationDTO 对象
     * @throws CustomizeException 如果通知不存在或用户验证失败
     */
    public NotificationDTO read(Long id, User user) {   //评论的id 和 当前登录的用户
        Notification notification = notificationMapper.selectByPrimaryKey(id);

        // 验证怕评论是否存在
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }

        // 验证用户是否正确
        if (user.getId() != notification.getReceiver()) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        // 更新为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateStatus(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
