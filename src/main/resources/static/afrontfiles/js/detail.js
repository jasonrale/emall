$(document).ready(function () {
    userInfo();

    var goodsId = (getUrlParam("goodsId"));

    detail(goodsId);
});

/**
 * 商品详情信息
 */
function detail(goodsId) {
    $.ajax({
        type: "GET",
        url: "/goods/" + goodsId + "/goodsId",
        success: function (data) {
            var goods = data.obj;
            $("#goodsName").html(goods.goodsName);
            $("#goodsDescribe").html(goods.goodsDescribe);
            $("#goodsPrice").html(goods.goodsPrice + "元");
            $("#goodsStock").html(goods.goodsStock + "件");
            $("#goodsImage").attr("src", goods.goodsImage);
            $("#goodsDetails").attr("src", goods.goodsDetails);

            countValid(goods.goodsStock);
        }
    });
}

function countValid(goodsStock) {
    var element = document.getElementById("count");
    element.onkeyup = function () {
        this.value = this.value.replace(/^(0+)|[^\d]+/g, '');
        if (element.value > goodsStock) {
            element.value = goodsStock;
        } else if (element.value <= 0) {
            element.value = 1;
        }
    }
}