/*修改信息验证*/
$(document).ready(function () {
    navInfo();

    $("#return").click(function () {
        $(window).attr("location","../../authenticated/user/userCenter.html");
    });

    $("#submit").click(function () {
        userUpdate();
    });

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            userUpdate();
        }
    });

    userUpdateInit();
});

/**
 * 用户个人信息初始化
 */
function userUpdateInit() {
    $.ajax({
        type: "GET",
        url: "/emall/user",
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

function userUpdate() {
    var userId = $("#userId").val();
    var userName = $("#userName").val();
    var userSex = $("input[name='sex']:checked").val();
    var userMobileNumber = $("#mobileNumber").val();

    if (userName === undefined || userName.trim() === "") {
        layer.msg("用户名称不能为空", {time : 1000});
        return false;
    } else if (!mobileValid(userMobileNumber)) {
        layer.msg("手机号码格式错误", {time : 1000});
        return false;
    }
    var update = {"userId": userId, "userName": userName, "userSex": userSex, "userMobileNumber": userMobileNumber};

    showLoading();
    $.ajax({
        type: "POST",
        url: "/emall/user",
        dataType: "json",
        data: JSON.stringify(update),
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            //用户已存在
            if (data.status === false) {
                layer.msg(data.msg, {time : 1000});
            } else {
                layer.msg(data.msg, {time: 800}, function () {
                    $(window).attr("location", "../../authenticated/user/userCenter.html");
                });
            }
        }
    });
}

/**
 * 验证手机号码
 */
function mobileValid(mobileNumber) {
    var result = true;

    var mobileValidate = /^(1[3-9])\d{9}$/;

    if (!(mobileValidate.test(mobileNumber))) {
        layer.msg("手机号码格式不正确！", {time : 1000});
        result = false;
    }
    return result;
}

