$(document).ready(function () {
    navInfo();

    var orderId = getUrlParam("orderId");

    payment(orderId);

    pay();
});

/**
 * 订单支付信息
 */
function payment(orderId) {
    $.ajax({
        type: "GET",
        url: "/emall/order/valid/" + orderId + "/orderId",
        success: function (data) {
            if (data.status === false) {
                layer.msg(data.msg, {time: 1000}, function () {
                    $(window).attr("location", "../../index.html");
                });
            } else {
                $("#orderId").html(orderId);
            }
        }
    });
}

/**
 * 订单支付
 */
function pay() {
    $(".qr-code").click(function () {
        var orderId = $("#orderId").html();

        $.ajax({
            type: "POST",
            url: "/emall/order/pay/" + orderId + "/orderId",
            success: function (data) {
                layer.msg(data.msg, {time: 1000}, function () {
                    $(window).attr("location", "orderDetail.html?orderId=" + orderId);
                });
            }
        });
    });
}
