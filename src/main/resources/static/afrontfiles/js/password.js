/*密码修改验证*/
$(document).ready(function () {
    userPwdInit();

    //验证密码
    $("#submit").click(function () {
        var userId = $("#userId").val();
        var salt = $("#salt").val();
        var passwordReal = $("#passwordReal").val();
        var passwordOld = $("#passwordOld").val();
        var passwordNew = $("#passwordNew").val();
        var pwdConfirm = $("#pwdConfirm").val();

        var passwordVo = {"uId" : userId,
                          "salt" : salt,
                          "passwordReal" : passwordReal,
                          "passwordOld" : passwordOld,
                          "passwordNew" : passwordNew,
                          "pwdConfirm" : pwdConfirm
                         };

        showLoading();
        $.ajax({
            type : "POST",
            url : "/user/password",
            dataType : "json",
            data : passwordVo,
            success : function (data) {
                if (data.status === true) {
                    layer.msg(data.msg);
                    $(window).attr("location","/user/logout");
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
function userPwdInit() {
    $.ajax({
        type: "GET",
        url: "/user/userInfo",
        success: function (data) {
            if (data.status === true) {

                $("#login").css("display", "none");
                $("#welcome").css("display", "inline");
                $("#loginName").css("display", "inline").html(data.obj.uname);
                $("#logout").css("display", "inline");

                $("#userId").val(data.obj.uid);
                $("#salt").val(data.obj.usalt);
                $("#passwordReal").val(data.obj.upassword);
            }
        }
    });
}