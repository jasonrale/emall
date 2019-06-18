/*操作结果判断*/
$(document).ready(function () {
    if ($.getUrlParam("result") === "1") {
        $(".register-success").css("display", "block");
    }
});
