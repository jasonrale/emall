$(document).ready(function () {
    navInfo();

    userInfoInit();
});

/**
 * 用户个人信息初始化
 */
function userInfoInit() {
    $.ajax({
        type: "GET",
        url: "/emall/user",
        success: function (data) {
            if (data.status === true) {
                $("#login").css("display", "none");
                $("#welcome").css("display", "inline");
                $("#loginName").css("display", "inline").html(data.obj.userName);
                $("#logout").css("display", "inline");

                $("#userName").html(data.obj.userName);
                $("#sex").html(data.obj.userSex === 1 ? "男" : "女");
                $("#mobileNumber").html(data.obj.userMobileNumber);
            }
        }
    });
}