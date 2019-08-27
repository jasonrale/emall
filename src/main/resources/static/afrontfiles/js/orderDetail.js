$(document).ready(function () {
    userInfo();

    var orderId = getUrlParam("orderId");

    orderDetail(orderId);
});

/**
 * 订单详情信息
 * @param orderId
 */
function orderDetail(orderId) {
    $.ajax({
        type: "GET",
        url: "/order/" + orderId + "/orderId",
        success: function (data) {
            if (data.status === true) {
                var orderVo = data.obj;
                var order = orderVo.order;
                var orderItemList = orderVo.orderItemList;
                var shipping = orderVo.shipping;
                var orderStatus = parseInt(order.orderStatus);

                $("#orderId").html(order.orderId);
                $("#orderCreateTime").html(order.orderCreateTime);
                $("#shippingName").html(shipping.shippingName);
                if (orderStatus === -1) {
                    $("#orderStatus").html("已取消");
                } else if (orderStatus === 0) {
                    $("#orderStatus").html("未支付");
                    $(".order-info").append(
                        '<div class="text-line">' +
                        '<a class="btn" href="payment.html?orderId=' + order.orderId + '">去支付</a>' +
                        '<a class="btn order-cancel" id="cancel">取消订单</a>' +
                        "</div>"
                    );
                } else if (orderStatus === 1) {
                    $("#orderStatus").html("待发货");
                } else if (orderStatus === 2) {
                    $("#orderStatus").html("待收货");
                } else {
                    $("#orderStatus").html("已完成");
                }

                var total = 0;
                for (var i = 0; i < orderItemList.length; i++) {
                    var element = "<tr>" +
                        '<td class="cell cell-img">' +
                        '<a href="./detail.html?productId=26" target="_blank"><img class="p-img"src="" alt=""></a>' +
                        "</td>" +
                        '<td class="cell cell-info">' +
                        '<a class="link" href="../../goods/detail.html?seckillGoodsId=' + orderItemList[i].goodsId + '" target="_blank">' +
                        orderItemList[i].goodsName +
                        "</a>" +
                        "</td>" +
                        '<td class="cell cell-price">￥' + orderItemList[i].goodsPrice + "</td>" +
                        '<td class="cell cell-count">' + orderItemList[i].goodsCount + "</td>" +
                        '<td class="cell cell-total">￥' + orderItemList[i].orderItemSubtotal + "</td>" +
                        "</tr>";
                    total += orderItemList[i].orderItemSubtotal;
                    $("#tbody").append(element);
                }

                $(".panel-body").append('<p class="total"><span>订单总价：</span><span class="total-price">￥' + total + "</span></p>");
            } else {
                layer.msg(data.msg);
            }
        }
    });
}
