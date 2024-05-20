package flower.community.service;

import flower.community.Datatransfermodel.PaginationDTO;
import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import flower.community.mapper.CommentMapper;
import flower.community.mapper.QuestionMapper;
import flower.community.mapper.UserMapper;
import flower.community.model.Comment;
import flower.community.model.Question;
import flower.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WsW
 * @version 1.0
 *
 * 查询question同时查询user对象，将user对象赋给questionService，来使用
 */
@Service //spring 会自动管理
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取问题列表,支持搜索和分页功能
     *
     * @param search 搜索关键词,为空则返回全部问题
     * @param page 页码
     * @param size 每页数据量
     * @return 包含问题列表和分页信息的 PaginationDTO 对象
     */
    public PaginationDTO list(String search, Integer page, Integer size) {

        Integer totalCount;
        if (StringUtils.isNotBlank(search)) {
            // 如果search不为空，通过like进行模糊查询
            // 获得search总数
            totalCount = questionMapper.countBySearch(search);
        } else {
            totalCount = questionMapper.count();    //通过SQL语句获取question总数
        }


        PaginationDTO paginationDTO = new PaginationDTO();
//        Integer totalCount = questionMapper.count();    //通过SQL语句获取question总数
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
            获取所有question并添加user对象
         */
        List<Question> questions;
        if (StringUtils.isNotBlank(search)) {
            // 如果search不为空，通过like进行模糊查询
            questions = questionMapper.selectBySearch(search, offset, size);
        } else {
            questions = questionMapper.list(offset, size);
        }

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);          //questionDTO就相当于一个组合对象 question+user
            questionDTOList.add(questionDTO);
        }

        // 将获取的questionList放入paginationDTO, 前端和分页使用
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    /**
     * 获取指定用户的问题列表并分页
     *
     * @param userId 用户 ID
     * @param page 页码
     * @param size 每页数据量
     * @return 包含问题列表和分页信息的 PaginationDTO 对象
     */
    /* 根据用户ID获取所有问题并分页 */
    public PaginationDTO<QuestionDTO> list(Long userId, Integer page, Integer size) {
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        Integer totalCount = questionMapper.countByUserId(userId);    //通过SQL语句获取question总数
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
            获取所有question并添加user对象
         */
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);          //questionDTO就相当于一个组合对象 question+user
            questionDTOList.add(questionDTO);
        }

        // 将获取的questionList放入paginationDTO, 前端和分页使用
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    /**
     * 根据问题 ID 获取问题详情
     *
     * @param id 问题 ID
     * @return 包含问题详情和创建者信息的 QuestionDTO 对象
     * @throws CustomizeException 如果查询不到对应的问题,抛出该异常
     */
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.getById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        // copyProperties() 方法将从 question 对象所有属性复制到 questionDTO 对象
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findByID(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 创建或更新问题
     *
     * @param question 要创建或更新的问题对象
     * @throws CustomizeException 如果更新问题失败,抛出该异常
     */
    public void createOrUpdate(Question question) {

        if (question.getId() == null) { // 如果 id 为空，说明是第一次创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        } else {    // 如果存在，说明是对question进行修改
            question.setGmtModified(System.currentTimeMillis());    //对修改时间进行更新
            int updated = questionMapper.update(question);  //返回被修改的行数
            if (updated != 1) { // 说明修改失败
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
            }
        }
    }

    /*
        增加阅读数操作
     */
    public void incView(Long id) {
        questionMapper.updateViewCount(id);
    }


    /**
     * 根据问题标签查询相关问题
     *
     * @param questionDTO 包含问题标签的 QuestionDTO 对象
     * @return 查询到的相关问题列表
     */
    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        // 如果标签是空的
        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }

        // 将标签中的 , 全部用 | 替换，方便数据库正则表达式匹配
        String relatedTag = StringUtils.replace(questionDTO.getTag(), ",", "|");
        // 开一个新的question防止干扰
        QuestionDTO question = new QuestionDTO();
        question.setId(questionDTO.getId());
        question.setTag(relatedTag);
        List<QuestionDTO> questionDTOList = questionMapper.selectRelated(question);

        return questionDTOList;
    }

    /**
     * 更新指定问题的点赞数
     *
     * @param id 问题ID
     */
    public void updateLikeCount(Long id) {
        questionMapper.updateLikecount(id);
    }

    /**
     * 获取点赞数最高的问题列表
     *
     * @return 点赞数最高的问题列表
     */
    public List<Question> getTopList() {
        List<Question> topQuestionList = questionMapper.getTopList();
        return topQuestionList;
    }

    @Transactional  //添加事务功能，当对数据库的操作有一条错误，全部进行回滚
    public void deleteById(Long id) {
        // 删除需要删除的问题
        questionMapper.deleteById(id);
//        /*
//            同时将问题下的回复全部删除
//         */
//        // 1.先获取所有回复的ID
//        List<Comment> comments = commentMapper.selectByQuestionID(id);
//        // 2.遍历每条回复，删除他们的二级回复
//        for (Comment comment : comments) {
//            // 获取所有二级回复
//            commentMapper.delete
//        }
//        // 还需要将响应回复的通知也全部删除

    }
}
