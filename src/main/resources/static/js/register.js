/*注册验证*/
$(document).ready(function () {
    var state;
    //验证用户名
    function uNameVali() {
        var userName = $("#username").val();

        if (userName === "") {
            $(".err-msg").html("用户名不能为空！");
            $(".error-item").css("display", "block");
            state = false;
        } else {
            var register = {"uName" : userName};
            $.ajax({
                type : "post",
                url : "/emall/register/validate",
                dataType : "json",
                data : register,
                async : false,
                success : function (msg) {
                    if (msg === false) {
                        $(".err-msg").html("用户名已存在！");
                        $(".error-item").css("display", "block");
                        state = false;
                    } else if (msg === true){
                        state = true;
                    }
                }
            });
            return state;
        }
    }

    //验证密码
    function pwdVali() {
        var password = $("#password").val();
        var pwdConfirm = $("#pwdConfirm").val();

        if (pwdConfirm.length < 6 || password.length < 6) {
            $(".err-msg").html("密码长度不能少于6位！");
            $(".error-item").css("display", "block");
            state = false;
        } else if (password !== pwdConfirm) {
            $(".err-msg").html("两次密码输入不一致！");
            $(".error-item").css("display", "block");
            state = false;
        } else {
            state = true;
        }

        return state;
    }

    //验证手机号码
    function phoneVali() {
        var phoneNumber = $("#phone").val();
        var phoneValidate = /^1[3|4|5|7|8]\d{9}$/;

        if (!(phoneValidate.test(phoneNumber))) {
            $(".err-msg").html("手机号码格式不正确！");
            $(".error-item").css("display", "block");
            state = false;
        } else {
            state = true;
        }

        return state;
    }

    //验证提交
    function submitVali() {
        if (uNameVali() === true && pwdVali() === true && phoneVali() === true) {
            $(".error-item").css("display", "none");

            var userName = $("#username").val();
            var password = $("#password").val();
            var sex = $(".sex:checked").val();
            var phoneNumber = $("#phone").val();

            var submit = {"uName" : userName, "uPassword" : password, "uSex" : sex, "uPhoneNumber" : phoneNumber};

            $.ajax({
                type : "post",
                url : "/emall/register/submit",
                dataType : "json",
                data : submit,
                success : function (msg) {
                    if (msg === 1) {
                        $(window).attr("location","/emall/views/result/result.jsp?result=1");
                    }
                }
            });
        }
    }

   $("#username").blur(uNameVali);

   $("#pwdConfirm").blur(pwdVali);

   $("#phone").blur(phoneVali);

   $("#submit").click(submitVali);
});
