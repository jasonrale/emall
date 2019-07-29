//登录验证
$(document).ready(function () {
    $("#register").click(function () {
        $(window).attr("location", "/user/register.html");
    });

    $("#submit").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();

        if (username === "" || password === "") {
            $(".err-msg").html("用户名或密码不能为空！");
            $(".error-item").css("display", "block")
        } else {
            var login = {"uName" : username, "uPassword" : password};
            $.ajax({
                type : "POST",
                url : "/user/authenticate",
                data : login,
                success : function (data) {
                    if (data.status === true) {
                        showLoading();
                        layer.msg("登录成功");
                        $(window).attr("location", data.msg);
                    } else{
                        $(".err-msg").html("用户名或密码错误！");
                        $(".error-item").css("display", "block")
                    }
                }
            });
        }
    });
});