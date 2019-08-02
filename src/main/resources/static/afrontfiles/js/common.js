//获取url后的参数
(function ($) {
    $.getUrlParam = function (name) {
        var sValue = location.search.match(new RegExp("[\?\&]" + name + "=([^\&]*)(\&?)", "i"));
        return sValue ? sValue[1] : sValue;
    }
})(jQuery);

//展示loading
function showLoading(){
    var idx = layer.msg('处理中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: '0px', time:10000}) ;
    return idx;
}

/**
 * 判断是否登录
 */
function userInfo() {
    $.ajax({
        type: "GET",
        url: "/user/userInfo",
        success: function (data) {
            if (data.status === true) {
                $("#login").css("display", "none");
                $("#welcome").css("display", "inline");
                $("#loginName").css("display", "inline").html(data.obj.uname);
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
        url: "/admin/adminInfo",
        success: function (data) {
            if (data.status === true) {
                $("#adminName").html(data.obj.uname);
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
});
