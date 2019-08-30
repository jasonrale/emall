$(document).ready(function () {
    navInfo();

    var seckillGoodsId = getUrlParam("seckillGoodsId");

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
                if (status === 1) {
                    $("#countDown").html(remainSeconds);
                } else if (status === 2) {
                    $("#countDown").html(remainSeconds).css("display", "none");
                    $("#captchaImg").attr("src", "/seckill/" + seckillGoodsId + "/captcha");
                    $("#captcha").css("display", "block");
                } else {
                    $("#countDown").html(remainSeconds).css("display", "none");
                    $("#captcha").css("display", "none");
                }
                $("#goodsImage").attr("src", seckillGoods.seckillGoodsImage);
                $("#goodsDetails").attr("src", seckillGoods.seckillGoodsDetails);
                $("#seckill").replaceWith('<input type="button" id="seckill" class="seckill" onclick="captchaPath(' + "'" + seckillGoodsId + "'" + ')" value="立即秒杀">');
                countDown(false, goingSeconds, seckillGoodsId);
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
function countDown(timeFlag, goingSeconds, seckillGoodsId) {
    var remainSeconds = parseInt($("#remainSeconds").val());
    var timeout;

    if (remainSeconds > 0) {//秒杀准备中，倒计时
        timeFlag = true;
        $("#goodsStatus").html("准备中");
        $("#seckill").attr("disabled", true);
        $("#countDown").html(remainSeconds - 1);
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, goingSeconds, seckillGoodsId);
        }, 1000);
    } else if (remainSeconds === 0) {//秒杀进行中
        timeFlag = true;
        $("#countDownDiv").css("display", "none");
        $("#captchaImg").attr("src", "/seckill/" + seckillGoodsId + "/captcha");
        $("#captcha").css("display", "block");
        $("#goodsStatus").html("进行中");
        $("#seckill").attr("disabled", false);
        $("#countDown").html(remainSeconds - 1);
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, goingSeconds, seckillGoodsId);
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
            countDown(timeFlag, goingSeconds, seckillGoodsId);
        }, 1000);
    } else {//秒杀已结束
        $("#countDownDiv").css("display", "none");
        $("#captcha").css("display", "none");
        $("#seckill").attr("disabled", true);
        $("#goodsStatus").html("已结束");
        $("#seckill").replaceWith('<input type="button" id="seckill" class="seckill" value="已结束">');
        if (timeFlag) {
            clearTimeout(timeout);
        }
    }
}

/**
 * 获取秒杀路径
 * @param seckillGoodsId
 */
function captchaPath(seckillGoodsId) {
    var captchaResult = $("#captchaResult").val();
    if (captchaResult.trim() === "") {
        layer.msg("请输入验证码");
        return false;
    }

    showLoading();
    $.ajax({
        type: "GET",
        url: "/seckill/" + seckillGoodsId + "/" + captchaResult + "/captcha/path",
        success: function (data) {
            if (data.status === true) {
                layer.closeAll();
                var path = data.obj;
                trySeckill(path, seckillGoodsId);
            } else {
                layer.msg(data.msg, {time: 1000}, function () {
                    $("#captchaImg").attr("src", "/seckill/" + seckillGoodsId + "/captcha");
                });
            }
        }
    });
}

/**
 * 尝试秒杀
 * @param path
 * @param seckillGoodsId
 */
function trySeckill(path, seckillGoodsId) {
    $.ajax({
        url: "/seckill/" + path + "/trySeckill",
        type: "POST",
        data: {
            seckillGoodsId: seckillGoodsId
        },
        success: function (data) {
            if (data.status === true) {
                seckillResult(path, seckillGoodsId);
            } else {
                layer.msg(data.msg, {time: 1000}, function () {
                    $(window).attr("location", "../../index.html");
                });
            }
        },
        error: function () {
            layer.msg("客户端请求有误");
        }
    });
}

/**
 * 获取秒杀结果
 * @param path
 * @param seckillGoodsId
 */
function seckillResult(path, seckillGoodsId) {
    showLoading();
    $.ajax({
        url: "/seckill/" + seckillGoodsId + "/result",
        type: "GET",
        success: function (data) {
            if (data.status === true) {
                var result = data.obj;
                if (result === "fail") {
                    layer.msg("很抱歉，秒杀失败");
                } else if (result === "queuing") {//继续轮询
                    setTimeout(function () {
                        seckillResult(seckillGoodsId);
                    }, 500);
                } else {
                    layer.confirm("恭喜您，秒杀成功！请确认订单？",
                        {btn: ["确定", "取消"]},
                        function () {
                            window.location.href = "/authenticated/user/orderConfirm.html?seckillGoodsId=" + result + "&path=" + path;
                        },
                        function () {
                            layer.closeAll();
                        }
                    );
                }
            } else {
                layer.msg(data.msg);
            }
        },
        error: function () {
            layer.msg("客户端请求有误");
        }
    });
}