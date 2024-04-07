function post() {
    // 从前端获取评论问题id 和 评论内容
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

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
            "parentId": questionId,
            "content": content,
            "type": 1
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