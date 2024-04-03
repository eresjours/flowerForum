package flower.community.Datatransfermodel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class PaginationDTO {

    private List<QuestionDTO> questions;

    private boolean showPrevious;       //是否有向前按钮
    private boolean showFirstPage;      //是否有第一页按钮
    private boolean showNext;           //是否有下一页按钮
    private boolean showEndPage;        //是否有最后一页按钮
    private Integer page;               //当前页
    private List<Integer> pages = new ArrayList<>();        //展示页数
    private Integer totalPage;                              //代表question总数总共可以分为几页

    /*
        根据总页数 当前页数 采集数量 完成分页操作
     */
    public void setPagination(Integer page) {

        /*
            将要在页面展示的页数添加到pages中
         */
        this.page = page;
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            // 加入当前页之前的页数
            if (page - i > 0) {
                pages.add(0, page - i); //头部插入
            }

            // 当前页之后的页
            if (page + i <= totalPage) {
                pages.add(page + i);        //尾部插入
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pages.contains(1)) {    //包含第一页时不展示
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 是否展示最后一页
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }

    /*
        根据question总数和每页展示数据量计算总共分的页数
     */
    public void computeTotalPage(Integer totalCount, Integer size) {
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
    }
}
