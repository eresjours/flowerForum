package flower.community.service;

import flower.community.Datatransfermodel.PaginationDTO;
import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.exception.CustomizeErrorCode;
import flower.community.exception.CustomizeException;
import flower.community.mapper.QuestionMapper;
import flower.community.mapper.UserMapper;
import flower.community.model.Question;
import flower.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserMapper userMapper;

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


    /*
        根据标签搜索相关问题
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

    public void updateLikeCount(Long id) {
        questionMapper.updateLikecount(id);
    }

    public List<Question> getTopList() {
        List<Question> topQuestionList = questionMapper.getTopList();
        return topQuestionList;
    }
}
