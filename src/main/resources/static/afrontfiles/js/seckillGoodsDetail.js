$(document).ready(function () {
    adminInfo();

    var seckillGoodsId = (getUrlParam("seckillGoodsId"));

    seckillGoodsDetail(seckillGoodsId);
});

function seckillGoodsDetail(seckillGoodsId) {
    $.ajax({
        type: "GET",
        url: "/seckillGoods/" + seckillGoodsId + "/seckillGoodsId",
        success: function (data) {
            var seckillGoods = data.obj;
            var status = seckillGoods.seckillGoodsStatus;
            $("#goodsName").html(seckillGoods.seckillGoodsName);
            $("#goodsDescribe").html(seckillGoods.seckillGoodsDescribe);
            $("#goodsStatus").html(status === 0 ? "未上架" : status === 1 ? "准备中" : status === 2 ? "进行中" : "已结束");
            $("#goodsPrice").html(seckillGoods.seckillGoodsPrice + "元");
            $("#goodsStock").html(seckillGoods.seckillGoodsStock + "件");
            $.ajax({
                type: "GET",
                url: "/category/" + seckillGoods.categoryId + "/categoryId",
                success: function (data) {
                    $("#category").html(data.obj.categoryName);
                }
            });
            $("#goodsImage").attr("src", seckillGoods.seckillGoodsImage).css("display", "block");
            $("#goodsDetails").attr("src", seckillGoods.seckillGoodsDetails).css("display", "block");
        }
    });
}