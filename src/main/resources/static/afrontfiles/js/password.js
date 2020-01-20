/*密码修改验证*/
$(document).ready(function () {
    navInfo();

    userPasswordInit();

    $("#submit").click(function () {
        password();
    });

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            password();
        }
    });
});

/**
 * 用户信息初始化
 */
function userPasswordInit() {
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
                $("#userSalt").val(data.obj.userSalt);
                $("#passwordReal").val(data.obj.userPassword);
            }
        }
    });
}

/**
 * 密码验证
 * @returns {boolean}
 */
function password() {
    var passwordOld = $("#passwordOld").val();
    var passwordNew = $("#passwordNew").val();
    var passwordConfirm = $("#passwordConfirm").val();

    if (passwordOld === undefined || passwordOld.trim() === "") {
        layer.msg("原密码不能为空", {time : 1000});
        return false;
    } else if (passwordNew === undefined || passwordNew.trim() === "") {
        layer.msg("新密码不能为空", {time : 1000});
        return false;
    } else if (passwordConfirm === undefined || passwordConfirm.trim() === "" || passwordConfirm !== passwordNew) {
        layer.msg("两次输入密码不一致", {time : 1000});
        return false;
    } else if (passwordNew.length < 6 || passwordOld.length < 6) {
        layer.msg("密码长度不能少于6位", {time: 1000});
        return false;
    }

    var passwordVo = {
        "passwordOld": passwordOld,
        "passwordNew": passwordNew,
        "passwordConfirm": passwordConfirm
    };

    showLoading();
    $.ajax({
        type: "POST",
        url: "/emall/user/password",
        dataType: "json",
        data: JSON.stringify(passwordVo),
        contentType: 'application/json;charset=UTF-8',
        success: function (data) {
            if (data.status === true) {
                layer.msg(data.msg, {time: 800}, function () {
                    $(window).attr("location", "/emall/user/logout");
                });

            } else {
                layer.msg(data.msg, {time : 1000});
            }
        }
    });
}