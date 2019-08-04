/*注册验证*/
$(document).ready(function () {
    var getUserName = $("#username");
    var getPassword = $("#password");
    var getConfirm = $("#passwordConfirm");
    var getSex = $(".sex:checked");
    var getMobile = $("#mobile");

    //验证用户名
    function uNameValid() {
        var result = true;
        var userName = getUserName.val();

        if (userName === "") {
            $(".err-msg").html("用户名不能为空！");
            $(".error-item").css("display", "block");
            result = false;
        } else if (userName.length < 3){
            $(".err-msg").html("用户名长度不小于3个字符！");
            $(".error-item").css("display", "block");
            result = false;
        } else {
            $(".error-item").css("display", "none");
        }
        return result;
    }

    //验证密码
    function pwdValid() {
        var result = true;
        var password = getPassword.val();

        if (password.length < 6) {
            $(".err-msg").html("密码长度不能少于6位！");
            $(".error-item").css("display", "block");
            result = false;
        } else {
            $(".error-item").css("display", "none");
        }
        return result;
    }

    //验证确认密码
    function pwdConfirmValid() {
        var result = true;
        var password = getPassword.val();
        var passwordConfirm = getConfirm.val();

        if (password !== passwordConfirm) {
            $(".err-msg").html("两次密码输入不一致！");
            $(".error-item").css("display", "block");
            result = false;
        } else {
            $(".error-item").css("display", "none");
        }
        return result;
    }

    //验证手机号码
    function mobileValid() {
        var result = true;
        var mobileNumber = getMobile.val();
        var mobileValidate = /^(1[3-9])\d{9}$/;

        if (!(mobileValidate.test(mobileNumber))) {
            $(".err-msg").html("手机号码格式不正确！");
            $(".error-item").css("display", "block");
            result = false;
        } else {
            $(".error-item").css("display", "none");
        }
        return result;
    }

    //验证提交
    function submitValid() {
        if (uNameValid() === true && pwdValid() === true && pwdConfirmValid() === true && mobileValid() === true) {
            $(".error-item").css("display", "none");

            var userName = getUserName.val();
            var password = getPassword.val();
            var sex = getSex.val();
            var mobileNumber = getMobile.val();

            var submit = {
                "userName": userName,
                "userPassword": password,
                "userSex": sex,
                "userMobileNumber": mobileNumber
            };

            showLoading();
            $.ajax({
                type : "PUT",
                url: "/user",
                dataType : "json",
                data: JSON.stringify(submit),
                contentType: 'application/json;charset=UTF-8',
                success : function (data) {
                    if (data.status === true) {
                        layer.msg("注册成功", {time: 800}, function () {
                            $(window).attr("location", "/result/result.html?resultType=register");
                        });
                    } else {
                        layer.msg(data.msg)
                    }
                }
            });
        }
    }

    // getUserName.blur(uNameValid);
    //
    // getPassword.blur(pwdValid);
    //
    // getConfirm.blur(pwdConfirmValid);
    //
    // getMobile.blur(mobileValid);

    $("#goLogin").click(function () {
        $(window).attr("location", "/user/login.html");
    });
    $("#submit").click(submitValid);
});
