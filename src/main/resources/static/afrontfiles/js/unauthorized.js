//登录验证
$(document).ready(function () {
    layer.msg("您的登录已过期，请重新登录");

    $("#register").click(function () {
        $(window).attr("location", "/user/register.html");
    });

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            authenticate();
        }
    });
});

/**
 * 认证
 */
function authenticate() {
    var username = $("#username").val();
    var password = $("#password").val();

    if (username === "" || password === "") {
        $(".err-msg").html("用户名或密码不能为空！");
        $(".error-item").css("display", "block")
    } else {
        var login = {"userName": username, "userPassword": password};
        $.ajax({
            type: "POST",
            url: "/user/unauthorized",
            data: JSON.stringify(login),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                if (data.status === true) {
                    showLoading();
                    layer.msg("登录成功", {time: 800}, function () {
                        $(window).attr("location", data.msg);
                    });
                } else {
                    $(".err-msg").html("用户名或密码错误！");
                    $(".error-item").css("display", "block")
                }
            }
        });
    }
}