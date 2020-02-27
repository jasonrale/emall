$(document).ready(function () {
    queryAllByType(1, 10, "all", "none");

    queryByType();

    turn();
});

/**
 * 订单管理--根据查询类型传参
 */
function queryByType() {
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            var listType = $("#listType").val();
            var param = $("#param").val();
            queryAllByType(1, 10, listType, param);
        }
    });

    $("#query").click(function () {
        var listType = $("#listType").val();
        var param = $("#param").val();
        queryAllByType(1, 10, listType, param);
    });
}

/**
 * 根据查询类型类型查询订单
 */
function queryAllByType(currentNo, pageSize, listType, param) {
    if ((listType === "orderId" || listType === "userId") && param.trim() === "") {
        return false;
    }

    var pageModel = {
        "currentNo": currentNo,
        "pageSize": pageSize
    };

    $.ajax({
        type: "GET",
        url: "/emall/order/" + listType + "/" + param,
        data: pageModel,
        success: function (data) {
            $(".orderTable").empty();

            if (listType === "orderId") {
                var orderManageVo = data.obj;

                if (orderManageVo !== null) {
                    var orderId = orderManageVo.orderId;

                    var status = orderManageVo.orderStatus === 4 ? "已取消" : orderManageVo.orderStatus === 1 ? "未支付" :
                        orderManageVo.orderStatus === 0 ? "待发货" : orderManageVo.orderStatus === 2 ? "待收货" : "已完成";
                    var shipping = orderManageVo.shipping;

                    var element = "<tr>" +
                        '<td><a class="opear" href="orderManageDetail.html?orderId=' + orderId + '">' + orderId + "</a></td>" +
                        "<td>" + orderManageVo.userId + "</td>" +
                        "<td>" + shipping.shippingName + "</td>" +
                        "<td>" + shipping.shippingMobileNumber + "</td>" +
                        "<td>" + shipping.shippingAddress + "</td>" +
                        '<td id="' + orderId + '">' + status + "</td>" +
                        "<td>" + orderManageVo.orderPayment + "</td>" +
                        "<td>" + new Date(orderManageVo.orderCreateTime).format("yyyy-MM-dd hh:mm:ss") + "</td>" +
                        '<td><a class="opear" href="orderManageDetail.html?orderId=' + orderId + '">查看</a>';

                    if (orderManageVo.orderStatus === 0) {
                        element += '<a class="opear" id="send' + orderId + '" onclick="send(' + "'" + orderId + "'" + ')">发货</a></td></tr>';
                    } else {
                        element += "</tr>"
                    }

                    $(".orderTable").append(element);
                }
                $("#pageControl").css("display", "none");
                return false;
            } else {
                var orderManageVoList = data.obj.list;

                var element = "";

                if (orderManageVoList.length !== 0) {

                    for (var i = 0; i < orderManageVoList.length; i++) {
                        var orderManageVo = orderManageVoList[i];

                        var orderId = orderManageVo.orderId;

                        var status = orderManageVo.orderStatus === 4 ? "已取消" : orderManageVo.orderStatus === 1 ? "未支付" :
                            orderManageVo.orderStatus === 0 ? "待发货" : orderManageVo.orderStatus === 2 ? "待收货" : "已完成";
                        var shipping = orderManageVo.shipping;

                        element += "<tr>" +
                            '<td><a class="opear" href="orderManageDetail.html?orderId=' + orderId + '">' + orderId + "</a></td>" +
                            "<td>" + orderManageVo.userId + "</td>" +
                            "<td>" + shipping.shippingName + "</td>" +
                            "<td>" + shipping.shippingMobileNumber + "</td>" +
                            "<td>" + shipping.shippingAddress + "</td>" +
                            '<td id="' + orderId + '">' + status + "</td>" +
                            "<td>" + orderManageVo.orderPayment + "</td>" +
                            "<td>" + new Date(orderManageVo.orderCreateTime).format("yyyy-MM-dd hh:mm:ss") + "</td>" +
                            '<td><a class="opear" href="orderManageDetail.html?orderId=' + orderId + '">查看</a>';

                        if (orderManageVo.orderStatus === 0) {
                            element += '<a class="opear" id="send' + orderId + '" onclick="send(' + "'" + orderId + "'" + ')">发货</a></td></tr>';
                        } else {
                            element += "</tr>"
                        }

                    }

                    $(".orderTable").append(element);

                    var totalPages = data.obj.totalPages;
                    if (totalPages === 1) {
                        $("#pageControl").css("display", "none");
                    } else {
                        //分页
                        $("#currentNo").val(currentNo);
                        $("#totalPages").html(totalPages);

                        var nextPage = currentNo + 1;
                        var lastPage = currentNo - 1;

                        $("#type").val(listType);
                        $("#word").val(param);

                        listType = "'" + listType + "'";
                        param = "'" + param + "'";

                        if (currentNo === 1) {
                            $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link'></a>");
                            $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link' " + '' +
                                'onclick="queryAllByType(' + nextPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
                        } else if (currentNo === totalPages) {
                            $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link'></a>");
                            $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link' " + '' +
                                'onclick="queryAllByType(' + lastPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
                        } else {
                            $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link' " + '' +
                                'onclick="queryAllByType(' + lastPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
                            $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link' " + '' +
                                'onclick="queryAllByType(' + nextPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
                        }
                        $("#pageControl").css("display", "block");
                    }
                } else {
                    $("#pageControl").css("display", "none");
                    return false;
                }
            }
        }
    });
}

/**
 * 订单管理--分页跳转
 */
function turn() {
    var element = document.getElementById("currentNo");
    element.onkeyup = function () {
        this.value = this.value.replace(/^(0+)|[^\d]+/g, '');
        var totalPages = parseInt($("#totalPages").html());
        if (element.value > totalPages) {
            element.value = totalPages;
        }
    };

    $("#turn").click(function () {
        var listType = $("#type").val();
        var param = $("#word").val();

        var currentNo = parseInt($("#currentNo").val());
        isNaN(currentNo) ? layer.msg("页码不能为空", {time: 1000}) : queryAllByType(currentNo, 10, listType, param);
    });
}

function send(orderId) {
    layer.confirm(
        "您确定要发货吗？",
        {btn: ["确定", "取消"]},
        function (index) {
            $.ajax({
                type: "POST",
                url: "/emall/order/send",
                data: orderId,
                contentType: 'application/json;charset=UTF-8',
                success: function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg, {time: 1000}, function () {
                            layer.close(index);
                            $("#send" + orderId).css("display", "none");
                            $("#" + orderId).replaceWith('<td id="' + orderId + '">' + "待收货" + "</td>");
                        });
                    } else {
                        layer.msg(data.msg, {time: 1000});
                    }
                }
            });
        },
        function (index) {
            layer.close(index);
        }
    );
}