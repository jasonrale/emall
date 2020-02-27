$(document).ready(function () {
    navInfo();

    orderList(1, 10);
});

/**
 * 获取订单列表
 */
function orderList(currentNo, pageSize) {
    var pageModel = {
        "currentNo": currentNo,
        "pageSize": pageSize
    };

    $.ajax({
        type: "GET",
        url: "/emall/order/currentUser",
        data: pageModel,
        success: function (data) {
            var orderVoList = data.obj.list;

            var element = '<table class="order-list-table header">' +
                "<tbody>" +
                "<tr>" +
                '<th class="cell cell-img">&nbsp;&nbsp;商品图片</th>' +
                '<th class="cell cell-info">&nbsp;&nbsp;&nbsp;商品信息</th>' +
                '<th class="cell cell-price">&nbsp;&nbsp;单价</th>' +
                '<th class="cell cell-count">数量</th>' +
                '<th class="cell cell-total">小计</th>' +
                "</tr>" +
                "</tbody>" +
                "</table>";

            if (orderVoList.length !== 0) {
                for (var i = 0; i < orderVoList.length; i++) {
                    var orderVo = orderVoList[i];

                    var orderId = orderVo.orderId;
                    var status = orderVo.orderStatus === 4 ? "已取消" : orderVo.orderStatus === 1 ? "未支付" :
                        orderVo.orderStatus === 0 ? "待发货" : orderVo.orderStatus === 2 ? "待收货" : "已完成";

                    var shipping = orderVo.shipping;

                    element += '<table class="order-list-table order-item">' +
                        "<tbody>" +
                        "<tr>" +
                        '<td colspan="5" class="order-info">' +
                        '<span class="order-text">' +
                        "<span>订单号：</span>" +
                        '<a class="link order-num" href="orderDetail.html?orderId=' + orderId + '" target="_blank">' + orderId + "</a>" +
                        "</span>" +
                        '<span class="order-text">' + new Date(orderVo.orderCreateTime).format("yyyy-MM-dd hh:mm:ss") + "</span>" +
                        '<span>收件人：<span class="order-text">' + shipping.shippingName + "</span></span>\n" +
                        '<span>订单状态：<span class="order-text">' + status + "</span></span>" +
                        '<span>订单总价：<span class="order-total">￥' + orderVo.orderPayment + "</span></span>" +
                        '<a class="link order-detail" href="orderDetail.html?orderId=' + orderId + '" target="_blank">查看详情&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>' +
                        "</td>" +
                        "</tr>";

                    var orderItemList = orderVo.orderItemList;
                    for (var j = 0; j < orderItemList.length; j++) {
                        var orderItem = orderItemList[j];

                        element += "<tr>" +
                            '<td class="cell cell-img">' +
                            '<a href="../../goods/detail.html?goodsId=' + orderItem.goodsId + '" target="_blank">' +
                            '<img class="p-img" src="' + orderItem.goodsImage + '" alt="' + orderItem.goodsName + '" >' +
                            "</a>" +
                            "</td>" +
                            '<td class="cell cell-info">' +
                            '<a class="link" href="../../goods/detail.html?goodsId=' + orderItem.goodsId + '" target="_blank">' +
                            orderItem.goodsName +
                            "</a>" +
                            "</td>" +
                            '<td class="cell cell-price">￥' + orderItem.goodsPrice + "</td>" +
                            '<td class="cell cell-count">' + orderItem.goodsCount + "</td>" +
                            '<td class="cell cell-total">' + orderItem.orderItemSubtotal + "</td>" +
                            "</tr>";
                    }

                    element += "</tbody></table>";
                }

                $(".order-list-con").empty().append(element);
            } else {
                $(".pg-content").css("display", "none");
                return false;
            }

            //分页
            var nextPage = currentNo + 1;
            var lastPage = currentNo - 1;

            var totalPages = data.obj.totalPages;
            $("#totalPages").html(currentNo + "/" + totalPages);
            if (currentNo === 1 && currentNo === totalPages) {
                $("#lastPage").replaceWith('<span id="lastPage" class="pg-item disabled">' + "上一页" + "</span>");
                $("#nextPage").replaceWith('<span id="nextPage" class="pg-item disabled">' + "下一页" + "</span>");
            } else if (currentNo === 1) {
                $("#lastPage").replaceWith('<span id="lastPage" class="pg-item disabled">' + "上一页" + "</span>");
                $("#nextPage").replaceWith('<span id="nextPage" class="pg-item" ' +
                    'onclick="orderList(' + nextPage + ", " + pageSize + ')">下一页</span>');
            } else if (currentNo === totalPages) {
                $("#nextPage").replaceWith('<span id="nextPage" class="pg-item disabled">' + "下一页" + "</span>");
                $("#lastPage").replaceWith('<span id="lastPage" class="pg-item" ' +
                    'onclick="orderList(' + lastPage + ", " + pageSize + ')">上一页</span>');
            } else {
                $("#lastPage").replaceWith('<span id="lastPage" class="pg-item" ' +
                    'onclick="orderList(' + lastPage + ", " + pageSize + ')">上一页</span>');
                $("#nextPage").replaceWith('<span id="nextPage" class="pg-item" ' +
                    'onclick="orderList(' + nextPage + ", " + pageSize + ')">下一页</span>');
            }
        }
    });
}
