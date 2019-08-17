$(document).ready(function () {
    adminInfo();

    var goodsId = (getUrlParam("goodsId"));

    goodsDetail(goodsId);
});

/**
 * 商品详情信息
 */
function goodsDetail(goodsId) {
    $.ajax({
        type: "GET",
        url: "/goods/" + goodsId + "/goodsId",
        success: function (data) {
            var goods = data.obj;
            $("#goodsName").html(goods.goodsName);
            $("#goodsDescribe").html(goods.goodsDescribe);
            $("#goodsStatus").html(goods.goodsStatus === 1 ? "在售" : "下架");
            $("#goodsPrice").html(goods.goodsPrice + "元");
            $("#goodsStock").html(goods.goodsStock + "件");
            $.ajax({
                type: "GET",
                url: "/category/" + goods.categoryId + "/categoryId",
                success: function (data) {
                    $("#category").html(data.obj.categoryName);
                }
            });
            $("#goodsImage").attr("src", goods.goodsImage).css("display", "block");
            $("#goodsDetails").attr("src", goods.goodsDetails).css("display", "block");
        }
    });
}