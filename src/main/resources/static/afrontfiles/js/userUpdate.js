/*修改信息验证*/
$(document).ready(function () {
    $("#return").click(function () {
        $(window).attr("location","../../authenticated/user/userCenter.html");
    });

    $("#submit").click(function () {
        var userId = $("#userId").val();
        var userName = $("#userName").val();
        var userSex = $("input[name='sex']:checked").val();
        var userMobileNumber = $("#mobileNumber").val();

        var update = {"userId": userId, "userName": userName, "userSex": userSex, "userMobileNumber": userMobileNumber};

        showLoading();
        $.ajax({
            type : "POST",
            url: "/user",
            dataType : "json",
            data: JSON.stringify(update),
            contentType: 'application/json;charset=UTF-8',
            success : function (data) {
                //用户已存在
                if (data.status === false) {
                    layer.msg(data.msg);
                } else {
                    layer.msg(data.msg, {time: 800}, function () {
                        $(window).attr("location", "../../authenticated/user/userCenter.html");
                    });
                }
            }
        });
    });

    userUpdateInit();

});

/**
 * 用户个人信息初始化
 */
function userUpdateInit() {
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
                $("#userName").val(data.obj.userName);
                $("#mobileNumber").val(data.obj.userMobileNumber);
                if (data.obj.userSex === 1) {
                    $("#male").attr("checked", "checked")
                    $("#female").attr("checked", false)
                } else {
                    $("#male").attr("checked", false)
                    $("#female").attr("checked", "checked")
                }
            }
        }
    });
}