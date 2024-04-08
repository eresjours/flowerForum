/**
 * 提交type=1的回复
 */
function post() {
    // 从前端获取评论问题id 和 评论内容
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    comment2parent(questionId, 1, content);
}

/**
 * 提交type=2的回复
 */
function comment(e, content) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-"+commentId).val();
    comment2parent(commentId, 2, content);
}

/**
 * 提交回复
 */
function comment2parent(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }

    /* 通过post请求将数据返回后端进行处理 */
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) { //评论成功
                // 将页面进行刷新
                window.location.reload();
                $("#comment_section").hidden();
            } else {
                // 未登录状态跳转登录
                if (response.code == 2003) {
                    var isAccept = confirm(response.message);
                    if (isAccept) {
                        window.open("https://github.com/login/oauth/authorize?client_id=09300ad66c4cdb5affae&redirect_uri=http://localhost:8887/callback&scope=user&state=1&allow_signup=true")
                        /*因为在弹起新页面时，页面没有办法传递元素，所以存入浏览器中localStorage*/
                        window.localStorage.setItem("closeable", true);
                    }
                } else {    // 其它弹出提示框即可
                    alert(response.message);
                }
            }

            console.log(response)
        },
        dataType: "json"
    });
}

/**
 * 展开二级回复
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id"); //获取问题id
    var comments = $("#comment-"+id);   //拼接得到二级回复的id

    // 获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) { //如果存在就说明已经展开，需要折叠
        comments.removeClass("in");
        e.removeAttribute("data-collapse");  // 移除二级评论展开状态
        e.classList.remove("active");   // 删除展开后的颜色
    } else {

        var subCommentContainer = $("#comment-"+id);

        // 根据元素数量判断是否加载过，加载过就不用再次请求
        if (subCommentContainer.children().length != 1) {

            comments.addClass("in");    // 添加in class实现展开功能
            e.setAttribute("data-collapse", "in");  // 标记二级评论展开状态
            e.classList.add("active");  // 删除展开后的颜色

        } else {
            // 获得所有二级回复，并拼接显示
            $.getJSON("/comment/" + id, function (data) {

                // 遍历出所有子元素
                $.each(data.data.reverse(), function (index, comment) {

                    /*回复人和头像*/
                    var mediaLeftElement = $("<div/>", {
                        "class":"media-left"
                    }).append($("<img/>", {
                        "class":"media-object img-circle",
                        "src":comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class":"media-body"
                    }).append($("<h5/>", {
                        "class":"media-heading",
                        "html":comment.user.name
                    })).append($("<div/>", {
                        "html":comment.content
                    })).append($("<div/>", {
                        "class":"menu"
                    }).append($("<span/>", {
                        "class":"pull-right",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    var mediaElement = $("<div/>", {
                        "class":"media"
                    }).append(mediaLeftElement).append(mediaBodyElement);
                    /*回复人和头像*/

                    // 回复内容
                    var commentElement = $("<div/>", {
                        "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                        html: comment.content
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);  // 将拼接的界面每次追加到最前面
                });


                comments.addClass("in");    // 添加in class实现展开功能
                e.setAttribute("data-collapse", "in");  // 标记二级评论展开状态
                e.classList.add("active");  // 删除展开后的颜色
            });
        }

    }

}