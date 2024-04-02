package flower.community.Datatransfermodel;

import lombok.Data;

/**
 * @author WsW
 * @version 1.0
 */
@Data
public class GithubUser {

    private String name;        //github姓名
    private Long id;            //github id
    private String bio;         //github简介
    private String avatar_url;  //github 头像

}
