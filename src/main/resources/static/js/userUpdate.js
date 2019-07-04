/*修改信息验证*/
$(document).ready(function () {
    var state;
    var uNameOld = $("#username").val();

    //验证用户名
    function uNameVali() {
        var userName = $("#username").val();

        if (userName === "") {
            $(".uName-msg").html("用户名不能为空！");
            $(".uName-error").css("display", "block");
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
                    if (msg === false && userName !== uNameOld) {
                        $(".uName-msg").html("用户名已存在！");
                        $(".uName-error").css("display", "block");
                        state = false;
                    } else if (msg === true){
                        state = true;
                        $(".uName-error").css("display", "none");
                    } else {
                        state = true;
                        $(".uName-error").css("display", "none");
                    }
                }
            });
            return state;
        }
    }

    //验证手机号码
    function phoneVali() {
        var phoneNumber = $("#phone").val();
        var phoneValidate = /^(1[3-9])\d{9}$/;

        if (!(phoneValidate.test(phoneNumber))) {
            $(".phone-msg").html("手机号码格式不正确！");
            $(".phone-error").css("display", "block");
            state = false;
        } else {
            $(".phone-error").css("display", "none");
            state = true;
        }

        return state;
    }

    $("#submit").click(function () {
        if (phoneVali() === true & uNameVali() === true) {
            var userName = $("#username").val();
            var sex = $(".sex:checked").val();
            var phoneNumber = $("#phone").val();

            var update = {"uName" : userName, "uSex" : sex, "uPhoneNumber" : phoneNumber};

            $.ajax({
                type : "post",
                url : "/emall/user/update",
                dataType : "json",
                data : update,
                success : function (msg) {
                    if (msg === 1) {
                        $(window).attr("location","/emall/views/back/user/userCenter.jsp");
                    }
                }
            });
        }
    });
});