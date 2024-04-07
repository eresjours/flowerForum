function post() {
    // 从前端获取评论问题id 和 评论内容
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

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
            if (response.code == 200) {
                $("#comment_section").hidden();
            } else {
                alert(response.message);
            }

            console.log(response)
        },
        dataType: "json"
    });
}