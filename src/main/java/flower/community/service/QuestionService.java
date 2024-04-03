package flower.community.service;

import flower.community.Datatransfermodel.PaginationDTO;
import flower.community.Datatransfermodel.QuestionDTO;
import flower.community.mapper.QuestionMapper;
import flower.community.mapper.UserMapper;
import flower.community.model.Question;
import flower.community.model.User;
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

    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();    //通过SQL语句获取question总数
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
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);          //questionDTO就相当于一个组合对象 question+user
            questionDTOList.add(questionDTO);
        }

        // 将获取的questionList放入paginationDTO, 前端和分页使用
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
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
        paginationDTO.setQuestions(questionDTOList);

        return paginationDTO;
    }
}
