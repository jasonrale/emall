$(document).ready(function () {
    userSkip();

    adminSkip();

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            query();
        }
    });
});

//获取url后的参数
function getUrlParam(name) {
    var sValue = location.search.match(new RegExp("[\?\&]" + name + "=([^\&]*)(\&?)", "i"));
    return sValue ? sValue[1] : sValue;
}


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

/**
 * 后台跳转功能
 */
function adminSkip() {
    $("#goodsSkip").click(function () {
        $(window).attr("location", "goodsManage.html");
    });

    $("#categorySkip").click(function () {
        $(window).attr("location", "categoryManage.html");
    });

    $("#orderSkip").click(function () {
        $(window).attr("location", "orderManage.html");
    });
}

/**
 * 用户端跳转功能
 */
function userSkip() {
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
}

/**
 * 通用关键字搜索框
 */
function query() {
    var keyWord = $("#keyWord").val();
    if (keyWord === undefined) {
        return false;
    } else if (keyWord.trim() === "") {
        return false;
    }

    $(window).attr('location', '/goods/goodsList.html?keyWord=' + encodeURI(keyWord));
}


