$(document).ready(function () {
    userInfo();

    var seckillGoodsId = (getUrlParam("seckillGoodsId"));

    seckillDetail(seckillGoodsId);
});

/**
 * 商品详情信息
 */
function seckillDetail(seckillGoodsId) {
    $.ajax({
        type: "GET",
        async: false,
        url: "/seckillGoods/" + seckillGoodsId + "/seckillGoodsId",
        success: function (data) {
            if (data.status === false) {
                $(".page-wrap").html('<p class="err-tip">此商品太淘气，找不到了</p>');
            } else {
                var seckillGoods = data.obj.seckillGoods;
                var remainSeconds = parseInt(data.obj.remainSeconds);
                var goingSeconds = parseInt(data.obj.goingSeconds);
                var status = seckillGoods.seckillGoodsStatus;

                $("#goodsName").html(seckillGoods.seckillGoodsName);
                $("#goodsDescribe").html(seckillGoods.seckillGoodsDescribe);
                $("#goodsPrice").html(seckillGoods.seckillGoodsPrice + "元");
                $("#goodsStock").html(seckillGoods.seckillGoodsStock + "件");
                $("#goodsStatus").html(status === 1 ? "准备中" : status === 2 ? "进行中" : "已结束");
                $("#startTime").html(new Date(seckillGoods.seckillGoodsStartTime).format("yyyy-MM-dd hh:mm:ss"));
                $("#endTime").html(new Date(seckillGoods.seckillGoodsEndTime).format("yyyy-MM-dd hh:mm:ss"));
                $("#remainSeconds").val(remainSeconds);
                if (remainSeconds > 0) {
                    $("#countDown").html(remainSeconds);
                } else {
                    $("#countDown").html(remainSeconds).css("display", "none");
                    $("#captchaImg").attr("src", "/seckillGoods/captcha/" + seckillGoods.seckillGoodsId + "/seckillGoodsId");
                    $("#captcha").css("display", "block");
                }
                $("#goodsImage").attr("src", seckillGoods.seckillGoodsImage);
                $("#goodsDetails").attr("src", seckillGoods.seckillGoodsDetails);
                $("#seckill").replaceWith('<input type="button" id="seckill" class="seckill" onclick="captcha(' + "'" + seckillGoods.seckillGoodsId + "'" + ')" value="立即秒杀">');
                countDown(false, goingSeconds);
            }
        },
        error: function () {
            $(".page-wrap").html('<p class="err-tip">此商品太淘气，找不到了</p>');
        }
    });
}

/**
 * 倒计时
 */
function countDown(timeFlag, goingSeconds) {
    var remainSeconds = parseInt($("#remainSeconds").val());
    var timeout;

    if (remainSeconds > 0) {//秒杀准备中，倒计时
        timeFlag = true;
        $("#goodsStatus").html("准备中");
        $("#seckill").attr("disabled", true);
        $("#countDown").html(remainSeconds - 1);
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, goingSeconds);
        }, 1000);
    } else if (remainSeconds === goingSeconds) {//秒杀进行中
        timeFlag = true;
        $("#countDownDiv").css("display", "none");
        $("#captchaImg").attr("src", "/seckillGoods/captcha/" + seckillGoods.seckillGoodsId + "/seckillGoodsId");
        $("#captcha").css("display", "block");
        $("#goodsStatus").html("进行中");
        $("#seckill").attr("disabled", false);
        $("#countDown").html(remainSeconds - 1);
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, goingSeconds);
        }, 1000);
    } else if (remainSeconds > goingSeconds) {//秒杀进行中
        timeFlag = true;
        $("#countDownDiv").css("display", "none");
        $("#captcha").css("display", "block");
        $("#goodsStatus").html("进行中");
        $("#seckill").attr("disabled", false);
        $("#countDown").html(remainSeconds - 1);
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, goingSeconds);
        }, 1000);
    } else {//秒杀已结束
        $("#countDownDiv").css("display", "none");
        $("#seckill").attr("disabled", true);
        $("#goodsStatus").html("已结束");
        if (timeFlag) {
            clearTimeout(timeout);
        }
    }
}

/**
 * 秒杀验证码
 * @param seckillGoodsId
 */
function captcha(seckillGoodsId) {

}