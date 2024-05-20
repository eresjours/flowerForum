package flower.community.service;

import flower.community.mapper.UserMapper;
import flower.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WsW
 * @version 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 创建或更新用户信息
     *
     * @param user 用户对象
     */
    public void createOrUpdate(User user) {
        //根据传入的accountId在数据库中查找是否存在对应用户
        User dbUser = userMapper.findByAccountId(user.getAccountId());
        // 如果该用户不存在，就执行插入操作
        if (dbUser == null) {
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            // 存在就更新
            // 将修改时间 名字 头像 token 进行一个更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setName(user.getName());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
