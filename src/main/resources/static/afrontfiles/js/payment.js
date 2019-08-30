$(document).ready(function () {
    navInfo();

    var orderId = getUrlParam("orderId");

    payment(orderId);
});

/**
 * 订单支付信息
 */
function payment(orderId) {
    $.ajax({
        type: "GET",
        url: "/order/valid/" + orderId + "/orderId",
        success: function (data) {
            if (data.status === false) {
                layer.msg(data.msg, {time: 1000}, function f() {
                    $(window).attr("location", "../../index.html");
                });
            } else {
                $("#orderId").html(orderId)
            }
        }
    });
}
