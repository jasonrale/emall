$(document).ready(function () {
    var orderId = getUrlParam("orderId");

    orderManageDetail(orderId);
});

/**
 * 订单详情信息
 * @param orderId
 */
function orderManageDetail(orderId) {
    $.ajax({
        type: "GET",
        url: "/emall/order/" + orderId + "/orderId",
        success: function (data) {
            if (data.status === true) {
                var orderVo = data.obj;
                var orderItemList = orderVo.orderItemList;
                var shipping = orderVo.shipping;
                var orderStatus = parseInt(orderVo.orderStatus);

                $("#orderId").html(orderVo.orderId);
                $("#orderCreateTime").html(new Date(orderVo.orderCreateTime).format("yyyy-MM-dd hh:mm:ss"));
                $("#shippingName").html(shipping.shippingName);
                $("#shippingMobileNumber").html(shipping.shippingMobileNumber);
                $("#shippingAddress").html(shipping.shippingAddress);
                if (orderStatus === 4) {
                    $("#orderStatus").html("已取消");
                } else if (orderStatus === 1) {
                    $("#orderStatus").html("未支付");
                } else if (orderStatus === 0) {
                    $("#orderStatus").html("待发货");
                } else if (orderStatus === 2) {
                    $("#orderStatus").html("待收货");
                } else {
                    $("#orderStatus").html("已完成");
                }
                $("#orderPayment").html(orderVo.orderPayment + "元");

                for (var i = 0; i < orderItemList.length; i++) {
                    var element = "<tr>" +
                        '<td class="cell cell-img">' +
                        '<a href="../../goods/detail.html?goodsId=' + orderItemList[i].goodsId + '" target="_blank">' +
                        '<img class="itemImg" src="' + orderItemList[i].goodsImage + '" alt="' + orderItemList[i].goodsName + '">' +
                        "</a>" +
                        "</td>" +
                        '<td class="cell cell-info">' +
                        '<a class="link" href="../../goods/detail.html?goodsId=' + orderItemList[i].goodsId + '" target="_blank">' +
                        orderItemList[i].goodsName +
                        "</a>" +
                        "</td>" +
                        '<td class="cell cell-price">￥' + orderItemList[i].goodsPrice + "</td>" +
                        '<td class="cell cell-count">' + orderItemList[i].goodsCount + "</td>" +
                        '<td class="cell cell-total">￥' + orderItemList[i].orderItemSubtotal + "</td>" +
                        "</tr>";
                    $(".itemTable").append(element);
                }

            } else {
                layer.msg(data.msg, {time: 1000}, function () {
                    $(window).attr("location", "orderManage.html");
                });
            }
        }
    });
}