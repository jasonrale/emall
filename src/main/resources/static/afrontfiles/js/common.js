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
function isLogin() {
    $.ajax({
        type: "GET",
        url: "/user/isLogin",
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

                $("#userName").html(data.obj.uname);
                $("#sex").html(data.obj.usex === 1 ? "男" : "女");
                $("#mobileNumber").html(data.obj.umobileNumber);
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
