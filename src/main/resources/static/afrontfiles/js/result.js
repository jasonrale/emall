/*操作结果判断*/
$(document).ready(function () {
    var resultType = (getUrlParam("resultType"));
    if (resultType === "register") {
        $(".register-success").css("display", "block");
    } else if (resultType === "reset") {
        $(".pass-reset-success").css("display", "block");
    } else if (resultType === "cart") {
        $(".cart-add-success").css("display", "block");
    } else if (resultType === "payment") {
        $(".payment-success").css("display", "block");
    } else if (resultType === "403"){
        $(".permission-denied").css("display", "block");
    } else {
        $(".default-success").css("display", "block");
    }
});
