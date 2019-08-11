//登录验证
$(document).ready(function () {
    $("#register").click(function () {
        $(window).attr("location", "/user/register.html");
    });

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            login();
        }
    });
});

function login() {
    var userName = $("#username").val();
    var userPassword = $("#password").val();

    if (userName.trim() === "" || userPassword.trim() === "") {
        $(".err-msg").html("用户名或密码不能为空！");
        $(".error-item").css("display", "block")
    } else {
        var login = {"userName": userName, "userPassword": userPassword};
        $.ajax({
            type: "POST",
            url: "/user/loginValidate",
            data: JSON.stringify(login),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                if (data.status === true) {
                    layer.msg(data.msg, {time: 800}, function () {
                        if (data.obj.userRole === 0) {
                            $(window).attr("location", "/index.html");
                        } else {
                            $(window).attr("location", "/authenticated/admin/index.html");
                        }
                    });
                } else {
                    $(".err-msg").html("用户名或密码错误！");
                    $(".error-item").css("display", "block")
                }
            }
        });
    }
}