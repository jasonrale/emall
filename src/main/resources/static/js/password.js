/*密码修改验证*/
$(document).ready(function () {
    //验证密码
    $("#submit").click(function () {
        var passwordReal = $("#passwordReal").val();
        var passwordOld = $("#passwordOld").val();
        var passwordNew = $("#passwordNew").val();
        var pwdConfirm = $("#pwdConfirm").val();

        if (passwordOld !== passwordReal) {
            $(".err-msg").html("原密码错误！");
            $(".password-error").css("display", "block");
        } else if (pwdConfirm.length < 6 || passwordNew.length < 6) {
            $(".err-msg").html("密码长度不能少于6位！");
            $(".password-error").css("display", "block");
        } else if (passwordNew !== pwdConfirm) {
            $(".err-msg").html("两次密码输入不一致！");
            $(".password-error").css("display", "block");
        } else {
            $(".password-error").css("display", "none");

            var password = {"uPassword" : pwdConfirm};

            $.ajax({
                type : "post",
                url : "/emall/user/password",
                dataType : "json",
                data : password,
                success : function (msg) {
                    if (msg === 1) {
                        alert("密码修改成功，请重新登录！");
                        $(window).attr("location","/emall/views/user/login.jsp");
                    }
                }
            });
        }
    });
});