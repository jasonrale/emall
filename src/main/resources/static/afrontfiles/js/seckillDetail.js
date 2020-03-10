$(document).ready(function () {
    navInfo();

    var seckillGoodsId = getUrlParam("seckillGoodsId");

    seckillDetail(seckillGoodsId);
});


/**
 * 秒杀商品详情信息
 */
function seckillDetail(seckillGoodsId) {
    $.ajax({
        type: "GET",
        async: false,
        url: "/emall/seckillGoods/" + seckillGoodsId + "/seckillGoodsId",
        success: function (data) {
            if (data.status === false) {
                $(".page-wrap").html('<p class="err-tip">此商品太淘气，找不到了</p>');
            } else {
                var seckillGoods = data.obj.seckillGoods;
                var remainSeconds = parseInt(data.obj.remainSeconds);
                var goingSeconds = parseInt(data.obj.goingSeconds);
                var status = seckillGoods.seckillGoodsStatus;
                var stock = seckillGoods.seckillGoodsStock

                $("#goodsName").html(seckillGoods.seckillGoodsName);
                $("#goodsDescribe").html(seckillGoods.seckillGoodsDescribe);
                $("#goodsPrice").html(seckillGoods.seckillGoodsPrice + "元");
                $("#startTime").html(new Date(seckillGoods.seckillGoodsStartTime).format("yyyy-MM-dd hh:mm:ss"));
                $("#endTime").html(new Date(seckillGoods.seckillGoodsEndTime).format("yyyy-MM-dd hh:mm:ss"));
                $("#remainSeconds").val(remainSeconds);
                if (stock > 0) {
                    $("#goodsStock").html(stock + "件");
                    if (status === 1) {
                        $("#goodsStatus").html("准备中");
                        $("#countDown").html(remainSeconds);
                    } else if (status === 2) {
                        $("#goodsStatus").html("进行中");
                        $("#countDown").html(remainSeconds).css("display", "none");
                        $("#captchaImg").attr("src", "/emall/seckill/" + seckillGoodsId + "/captcha");
                        $("#captcha").css("display", "block");
                    } else {
                        $("#goodsStatus").html("已结束");
                        $("#countDown").html(remainSeconds).css("display", "none");
                    }

                    $("#seckill").replaceWith('<input type="button" id="seckill" class="seckill" onclick="captchaPath(' + "'" + seckillGoodsId + "'" + ')" value="立即秒杀">');
                } else {
                    $("#goodsStock").html(stock + "件");
                    if (status === 3) {
                        $("#goodsStatus").html("已结束");
                        $("#countDown").html(remainSeconds).css("display", "none");
                    } else {
                        $("#goodsStatus").html("库存不足");
                        $("#countDown").html(remainSeconds).css("display", "none");
                    }
                }

                $("#goodsImage").attr("src", seckillGoods.seckillGoodsImage);
                $("#goodsDetails").attr("src", seckillGoods.seckillGoodsDetails);
                countDown(false, stock, goingSeconds, seckillGoodsId);
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
function countDown(timeFlag, stock, goingSeconds, seckillGoodsId) {
    var remainSeconds = parseInt($("#remainSeconds").val());
    var timeout;

    if (remainSeconds > 0) {//秒杀准备中，倒计时
        timeFlag = true;
        $("#goodsStatus").html("准备中");
        $("#seckill").attr("disabled", true);
        $("#countDown").html(remainSeconds - 1);
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, stock, goingSeconds, seckillGoodsId);
        }, 1000);
    } else if (remainSeconds === 0) {//秒杀进行中
        timeFlag = true;
        $("#countDownDiv").css("display", "none");
        $("#captchaImg").attr("src", "/emall/seckill/" + seckillGoodsId + "/captcha");
        $("#captcha").css("display", "block");
        $("#goodsStatus").html("进行中");
        $("#seckill").attr("disabled", false);
        $("#countDown").html(remainSeconds - 1);
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, stock, goingSeconds, seckillGoodsId);
        }, 1000);
    } else if (remainSeconds > goingSeconds) {//秒杀进行中
        timeFlag = true;
        $("#countDownDiv").css("display", "none");
        if (stock < 1) {
            $("#goodsStock").html(stock + "件");
            $("#goodsStatus").html("库存不足");
            $("#captcha").css("display", "none");
            $("#seckill").replaceWith('<input type="button" id="seckill" class="seckill" value="库存不足">');
            $("#seckill").attr("disabled", true);
            $("#countDown").html(remainSeconds - 1).css("display", "none");
        } else {
            $("#captcha").css("display", "block");
            $("#goodsStatus").html("进行中");
            $("#seckill").attr("disabled", false);
            $("#countDown").html(remainSeconds - 1);
        }
        $("#remainSeconds").val(remainSeconds - 1);
        timeout = setTimeout(function () {
            countDown(timeFlag, stock, goingSeconds, seckillGoodsId);
        }, 1000);
    } else {//秒杀已结束
        $("#countDownDiv").css("display", "none");
        $("#captcha").css("display", "none");
        $("#seckill").replaceWith('<input type="button" id="seckill" class="seckill" value="已结束">');
        $("#seckill").attr("disabled", true);
        $("#goodsStatus").html("已结束");
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
        layer.msg("请输入验证码", {time: 1000});
        return false;
    } else if (isNaN(captchaResult)) {
        layer.msg("请输入正确格式的计算结果", {time: 1200});
        return false;
    }


    showLoading();
    $.ajax({
        type: "GET",
        url: "/emall/seckill/" + seckillGoodsId + "/" + captchaResult + "/captcha/path",
        success: function (data) {
            if (data.status === true) {
                layer.closeAll();
                var path = data.obj;
                trySeckill(path, seckillGoodsId);
            } else {
                layer.msg(data.msg, {time: 1000}, function () {
                    $("#captchaImg").attr("src", "/emall/seckill/" + seckillGoodsId + "/captcha?v=" + Math.floor(Math.random() * 100));
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
        url: "/emall/seckill/" + path + "/trySeckill",
        type: "POST",
        data: {
            seckillGoodsId: seckillGoodsId
        },
        success: function (data) {
            if (data.status === true) {
                seckillResult(path, seckillGoodsId);
            } else {
                layer.msg(data.msg, {time: 1000}, function () {
                    $(window).attr("location", "../index.html");
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
        url: "/emall/seckill/" + seckillGoodsId + "/result",
        type: "GET",
        success: function (data) {
            if (data.status === true) {
                var result = data.obj;
                if (result === "fail") {
                    layer.msg("很抱歉，秒杀失败");
                } else if (result === "queuing") {//继续轮询
                    setTimeout(function () {
                        seckillResult(path, seckillGoodsId);
                    }, 500);
                } else {
                    layer.msg("恭喜您，秒杀成功！", {time: 1000}, function () {
                        window.location.href = "/emall/authenticated/user/orderConfirm.html?seckillGoodsId=" + result + "&path=" + path;
                    });
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