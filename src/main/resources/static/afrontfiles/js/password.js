/*密码修改验证*/
$(document).ready(function () {
    userPasswordInit();

    //验证密码
    $("#submit").click(function () {
        var userId = $("#userId").val();
        var userSalt = $("#userSalt").val();
        var passwordReal = $("#passwordReal").val();
        var passwordOld = $("#passwordOld").val();
        var passwordNew = $("#passwordNew").val();
        var passwordConfirm = $("#passwordConfirm").val();

        var passwordVo = {
            "userId": userId,
            "userSalt": userSalt,
                          "passwordReal" : passwordReal,
                          "passwordOld" : passwordOld,
                          "passwordNew" : passwordNew,
            "passwordConfirm": passwordConfirm
                         };

        showLoading();
        $.ajax({
            type : "POST",
            url : "/user/password",
            dataType : "json",
            data: JSON.stringify(passwordVo),
            contentType: 'application/json;charset=UTF-8',
            success : function (data) {
                if (data.status === true) {
                    layer.msg(data.msg, {time: 800}, function () {
                        $(window).attr("location", "/user/logout");
                    });

                } else {
                    layer.msg(data.msg);
                }
            }
        });
    });
});

/**
 * 用户信息初始化
 */
function userPasswordInit() {
    $.ajax({
        type: "GET",
        url: "/user",
        success: function (data) {
            if (data.status === true) {

                $("#login").css("display", "none");
                $("#welcome").css("display", "inline");
                $("#loginName").css("display", "inline").html(data.obj.userName);
                $("#logout").css("display", "inline");

                $("#userId").val(data.obj.userId);
                $("#userSalt").val(data.obj.userSalt);
                $("#passwordReal").val(data.obj.userPassword);
            }
        }
    });
}