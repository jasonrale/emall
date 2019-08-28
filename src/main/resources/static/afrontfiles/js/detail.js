$(document).ready(function () {
    userInfo();

    var goodsId = getUrlParam("goodsId");

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
            if (goods === null) {
                seckillGoodsDetail(goodsId);
            } else {
                $("#goodsName").html(goods.goodsName);
                $("#goodsDescribe").html(goods.goodsDescribe);
                $("#goodsPrice").html(goods.goodsPrice + "元");
                $("#goodsStock").html(goods.goodsStock + "件");
                $("#goodsImage").attr("src", goods.goodsImage);
                $("#goodsDetails").attr("src", goods.goodsDetails);

                countValid(goods.goodsStock);
            }

            buy(goodsId);
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

/**
 * 秒杀商品详情信息
 */
function seckillGoodsDetail(seckillGoodsId) {
    $.ajax({
        type: "GET",
        url: "/seckillGoods/fromDB/" + seckillGoodsId + "/seckillGoodsId",
        success: function (data) {
            var seckillGoods = data.obj;
            var status = seckillGoods.seckillGoodsStatus;
            $("#goodsName").html(seckillGoods.seckillGoodsName);
            $("#goodsDescribe").html(seckillGoods.seckillGoodsDescribe);
            $("#goodsStatus").html(status === 0 ? "未上架" : status === 1 ? "准备中" : status === 2 ? "进行中" : "已结束");
            $("#status").css("display", "block");
            $("#goodsPrice").html(seckillGoods.seckillGoodsPrice + "元");
            $("#goodsStock").html(seckillGoods.seckillGoodsStock + "件");
            $("#startTime").html(new Date(seckillGoods.seckillGoodsStartTime).format("yyyy-MM-dd hh:mm:ss"));
            $("#start").css("display", "block");
            $("#endTime").html(new Date(seckillGoods.seckillGoodsEndTime).format("yyyy-MM-dd hh:mm:ss"));
            $("#end").css("display", "block");
            $("#goodsImage").attr("src", seckillGoods.seckillGoodsImage);
            $("#goodsDetails").attr("src", seckillGoods.seckillGoodsDetails);
            $("#countDiv").css("display", "none");
            $("#btn").css("display", "none");
            if (status === 2) {
                $("#goSeckill").append('<a class="skipSeckill" onclick="goSeckill(' + "'" + seckillGoods.seckillGoodsId + "'" + ')">前往秒杀</a>').css("display", "block");
            }
        }
    });
}

/**
 * 前往秒杀
 */
function goSeckill(seckillGoodsId) {
    $(window).attr("location", "../../seckillGoods/seckillDetail.html?seckillGoodsId=" + seckillGoodsId);
}

/**
 * 立即购买
 */
function buy(goodsId) {
    $(".buy").click(function () {
        var count = $("#count").val();
        $(window).attr("location", "../authenticated/user/orderConfirm.html?goodsId=" + goodsId + "&count=" + count);
    });
}