/*操作结果判断*/
$(document).ready(function () {
    if ($.getUrlParam("resultType") === "register") {
        $(".register-success").css("display", "block");
    }
});
