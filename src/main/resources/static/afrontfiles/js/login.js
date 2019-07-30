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
            showLoading();
            $.ajax({
                type : "POST",
                url : "/user/loginValidate",
                data : login,
                success : function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg);
                        if (data.obj.urole === 0) {
                            $(window).attr("location", "/index.html");
                        } else {
                            $(window).attr("location","/authenticated/admin/index.html");
                        }
                    } else{
                        $(".err-msg").html("用户名或密码错误！");
                        $(".error-item").css("display", "block")
                    }
                }
            });
        }
    });
});