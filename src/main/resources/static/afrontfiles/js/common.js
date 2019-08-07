//获取url后的参数
(function ($) {
    $.getUrlParam = function (name) {
        var sValue = location.search.match(new RegExp("[\?\&]" + name + "=([^\&]*)(\&?)", "i"));
        return sValue ? sValue[1] : sValue;
    }
})(jQuery);

//展示loading
function showLoading(){
    return layer.msg('处理中...', {icon: 16, shade: [0.5, '#f5f5f5'], scrollbar: false, offset: '0px', time: 5000});
}

/**
 * 判断是否登录
 */
function userInfo() {
    $.ajax({
        type: "GET",
        url: "/user",
        success: function (data) {
            if (data.status === true) {
                $("#login").css("display", "none");
                $("#welcome").css("display", "inline");
                $("#loginName").css("display", "inline").html(data.obj.userName);
                $("#logout").css("display", "inline");
            }
        }
    });
}

/**
 * 获取管理员信息
 */
function adminInfo() {
    $.ajax({
        type: "GET",
        url: "/user/admin",
        success: function (data) {
            if (data.status === true) {
                $("#adminName").html(data.obj.userName);
            }
        }
    });
}

$(document).ready(function () {
    $("#login").click(function () {
        $(window).attr('location', '/user/login.html');
    });

    $("#logout").click(function () {
        $(window).attr("location", "/user/logout")
    });

    $(".logo").click(function () {
        $(window).attr('location', '/index.html');
    });

    $("#emall").click(function () {
        $(window).attr('location', '/index.html');
    });

    //后台管理
    $("#goodsSkip").click(function () {
        $(window).attr("location", "goodsmanage.html");
    });

    $("#categorySkip").click(function () {
        $(window).attr("location", "categorymanage.html");
    });

    $("#orderSkip").click(function () {
        $(window).attr("location", "ordermanage.html");
    });
});
