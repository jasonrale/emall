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
$(document).ready(function () {
    $("#login").click(function () {
        $(window).attr('location', '/user/login.html');
    });

    $("#exit").click(function () {
        $(window).attr('location','/user/exit');
    });

    $(".logo").click(function () {
        $(window).attr('location', '/index');
    });
});
