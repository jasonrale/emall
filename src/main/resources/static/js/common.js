(function ($) {
    $.getUrlParam = function (name) {
        var sValue = location.search.match(new RegExp("[\?\&]" + name + "=([^\&]*)(\&?)", "i"));
        return sValue ? sValue[1] : sValue;
    }
})(jQuery);
$(document).ready(function () {
    $("#exit").click(function () {
        $(window).attr('location','/emall/user/exit');
    })
});
