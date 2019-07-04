/*注册验证*/
$(document).ready(function () {
    //验证用户名
    function uNameValid() {
        var result = true;
        var userName = $("#username").val();

        if (userName === "" || userName === null) {
            $(".err-msg").html("用户名不能为空！");
            $(".error-item").css("display", "block");
            result = false;
        } else if (userName.length < 3){
            $(".err-msg").html("用户名长度不小于3个字符！");
            $(".error-item").css("display", "block");
            result = false;
        }

        return result;
    }

    //验证密码
    function pwdValid() {
        var result = true;
        var password = $("#password").val();
        var pwdConfirm = $("#pwdConfirm").val();

        if (pwdConfirm.length < 6 || password.length < 6) {
            $(".err-msg").html("密码长度不能少于6位！");
            $(".error-item").css("display", "block");
            result = false;
        } else if (password !== pwdConfirm) {
            $(".err-msg").html("两次密码输入不一致！");
            $(".error-item").css("display", "block");
            result = false;
        }

        return result;
    }

    //验证手机号码
    function phoneValid() {
        var result = true;
        var phoneNumber = $("#mobile").val();
        var phoneValidate = /^(1[3-9])\d{9}$/;

        if (!(phoneValidate.test(phoneNumber))) {
            $(".err-msg").html("手机号码格式不正确！");
            $(".error-item").css("display", "block");
            result = false;
        }

        return result;
    }

    //验证提交
    function submitValid() {
        alert("验证");
        if (uNameValid() === true && pwdValid() === true && phoneValid() === true) {
            showLoading();
            $(".error-item").css("display", "none");

            var userName = $("#username").val();
            var password = $("#password").val();
            var sex = $(".sex:checked").val();
            var mobileNumber = $("#phone").val();

            var submit = {"uName" : userName, "uPassword" : password, "uSex" : sex, "uMobileNumber" : mobileNumber};

            $.ajax({
                type : "post",
                url : "/user/registerValidate",
                dataType : "json",
                data : submit,
                success : function (data) {
                    if (data.status === true) {
                        layer.msg("注册成功")
                        $(window).attr("location","/result/register");
                    } else {
                        layer.msg(data.msg)
                    }
                },
            });
        }
    }

   $("#username").blur(uNameValid);

   $("#pwdConfirm").blur(pwdValid);

   $("#mobile").blur(phoneValid);

   $("#submit").click(submitValid);
});
