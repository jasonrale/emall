/*修改信息验证*/
$(document).ready(function () {
    $("#return").click(function () {
        $(window).attr("location","../../authenticated/user/userCenter.html");
    });

    $("#submit").click(function () {
        var userId = $("#userId").val();
        var userName = $("#userName").val();
        var sex = $("input[name='sex']:checked").val();
        var mobileNumber = $("#mobileNumber").val();

        var update = {"uId" : userId, "uName" : userName, "uSex" : sex, "uMobileNumber" : mobileNumber};

        showLoading();
        $.ajax({
            type : "POST",
            url : "/user/userUpdate",
            dataType : "json",
            data : update,
            success : function (data) {
                //用户已存在
                if (data.status === false) {
                    layer.msg(data.msg);
                } else {
                    layer.msg(data.msg);
                    $(window).attr("location","../../authenticated/user/userCenter.html");
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
        url: "/user/isLogin",
        success: function (data) {
            if (data.status === true) {
                $("#login").css("display", "none");
                $("#welcome").css("display", "inline");
                $("#loginName").css("display", "inline").html(data.obj.uname);
                $("#logout").css("display", "inline");

                $("#userId").val(data.obj.uid);
                $("#userName").val(data.obj.uname);
                $("#mobileNumber").val(data.obj.umobileNumber);
                if (data.obj.usex === 1) {
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