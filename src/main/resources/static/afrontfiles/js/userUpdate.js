/*修改信息验证*/
$(document).ready(function () {
    userInfoInit();

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
                if (data.status === false) {
                    layer.msg(data.msg);
                } else {
                    alert(data.msg);
                    $(window).attr("location","../../authenticated/user/userCenter.html");
                }
            }
        });
    });


    //手机号码验证
    jQuery.validator.addMethod("isMobile", function(value, element) {
        var length = value.length;
        var mobile = /^(1[3-9])\d{9}$/;
        return this.optional(element) || (length === 11 && mobile.test(value));
    }, "手机号码格式不正确");

    $("#updateUserForm").validate({
        rules: {
            userName: {
                required: true,
                minlength: 3
            },
            mobileNumber: {
                required: true,
                isMobile: true
            }
        },
        messages: {
            userName: {
                required: "用户名不能为空",
                minlength: "用户名长度不小于3个字符"
            },
            mobileNumber: {
                required: "手机号码不能为空",
                isMobile: "手机号码格式不正确"
            }
        }
    });



});

/**
 * 用户个人信息初始化
 */
function userInfoInit() {
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

//验证用户名
// function uNameVali() {
//     var result = true;
//     var userName = $("#userName").val();
//
//     if (userName === "") {
//         $(".uName-msg").html("用户名不能为空！");
//         $(".uName-error").css("display", "block");
//         result = false;
//     } else if (userName.length < 3) {
//         $(".uName-msg").html("用户名长度不小于3个字符！");
//         $(".uName-error").css("display", "block");
//         result = false;
//     } else {
//         $.ajax({
//             type : "GET",
//             url : "/user/userNameExist",
//             data : userName,
//             async : false,
//             success : function (data) {
//                 if (data.status === false) {
//                     $(".uName-msg").html("用户名已存在！");
//                     $(".uName-error").css("display", "block");
//                     result = false;
//                 } else {
//                     $(".uName-error").css("display", "none");
//                 }
//             }
//         });
//         return result;
//     }
// }

//验证手机号码
// function phoneVali() {
//     var result = true;
//     var mobileNumber = $("#mobileNumber").val();
//     var mobileValidate = /^(1[3-9])\d{9}$/;
//
//     if (!(mobileValidate.test(mobileNumber))) {
//         $(".phone-msg").html("手机号码格式不正确！");
//         $(".phone-error").css("display", "block");
//         result = false;
//     } else {
//         $(".phone-error").css("display", "none");
//     }
//
//     return result;
// }