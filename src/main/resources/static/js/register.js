/*注册验证*/
$(document).ready(function () {
    var getUserName = $("#username");
    var getPassword = $("#password");
    var getConfirm = $("#pwdConfirm");
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
        var pwdConfirm = getConfirm.val();

        if (password !== pwdConfirm) {
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

            var submit = {"uName" : userName, "uPassword" : password, "uSex" : sex, "uMobileNumber" : mobileNumber};

            showLoading();
            $.ajax({
                type : "post",
                url : "/user/registerValidate",
                dataType : "json",
                data : submit,
                success : function (data) {
                    if (data.status === true) {
                        layer.msg("注册成功");
                        $(window).attr("location", "/result");
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

   $("#submit").click(submitValid);
});
