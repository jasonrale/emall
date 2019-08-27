$(document).ready(function () {
    userInfo();

});

/**
 * 获取订单列表
 */
function orderList(currentNo, pageSize) {
    $.ajax({
        type: "GET",
        url: "/order/" + pageSize + "/" + currentNo + "/currentNo",
        success: function (data) {
            var orderList = data.obj;

        }
    });
}
