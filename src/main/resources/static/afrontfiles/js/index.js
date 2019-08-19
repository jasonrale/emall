$(document).ready(function () {
    userInfo();

    carousel();
});

/**
 * 首页轮播
 */
function carousel() {
    layui.use(['carousel', 'form'], function () {
        var carousel = layui.carousel, form = layui.form;
        var ins = carousel.render({
            elem: '#seckillList',
            width: '100%',
            height: '100%',
            anim: 'anim',
            arrow: 'hover',
            indicator: 'none',
            interval: 3000
        });

        querySeckillGoods(ins);
    });
}

function querySeckillGoods(ins) {
    $.ajax({
        type: "GET",
        url: "/seckillGoods",
        success: function (data) {
            if (data.status === true) {
                var seckillGoodsList = data.obj;
                var size = seckillGoodsList.length;

                for (var i = 0; i < size; i = i + 3) {
                    var element =
                        "<div>" +
                        '<ul style="width: 100%; position: relative; left: 0; height: 370px;">' +
                        '<li style="width: 33.33%;">' +
                        '<img alt="" class="banner-img" src="' + seckillGoodsList[i].seckillGoodsImage + '"/>' +
                        '<span class="seckill-price">' +
                        '<h3 style="background-color: #cdcdcd;">' + "￥" + seckillGoodsList[i].seckillGoodsPrice + "</h3>" +
                        "</span>" +
                        '<div class="seckill"><input type="button" class="seckillbtn" value="前往秒杀" ' +
                        'onclick="seckillDetail(' + "'" + seckillGoodsList[i].seckillGoodsId + "'" + ')"></div>' +
                        "</li>";
                    if (i + 1 < size) {
                        element +=
                            '<li style="width: 33.33%;">' +
                            '<img alt="" class="banner-img" src="' + seckillGoodsList[i + 1].seckillGoodsImage + '"/>' +
                            '<span class="seckill-price">' +
                            '<h3 style="background-color: #cdcdcd;">' + "￥" + seckillGoodsList[i + 1].seckillGoodsPrice + "</h3>" +
                            "</span>" +
                            '<div class="seckill"><input type="button" class="seckillbtn" value="前往秒杀" ' +
                            'onclick="seckillDetail(' + "'" + seckillGoodsList[i + 1].seckillGoodsId + "'" + ')"></div>' +
                            "</li>";
                    }

                    if (i + 2 < size) {
                        element +=
                            '<li style="width: 33.33%;">' +
                            '<img alt="" class="banner-img" src="' + seckillGoodsList[i + 2].seckillGoodsImage + '"/>' +
                            '<span class="seckill-price">' +
                            '<h3 style="background-color: #cdcdcd;">' + "￥" + seckillGoodsList[i + 2].seckillGoodsPrice + "</h3>" +
                            "</span>" +
                            '<div class="seckill"><input type="button" class="seckillbtn" value="前往秒杀" ' +
                            'onclick="seckillDetail(' + "'" + seckillGoodsList[i + 2].seckillGoodsId + "'" + ')"></div>' +
                            "</li>";
                    }

                    element += "</ul></div>";

                    $("#pageList").append(element);

                    ins.reload({elem: '#seckillList'});
                }
            }
        }
    });
}

/**
 * 跳转秒杀商品详情页面
 */
function seckillDetail(seckillGoodsId) {
    window.open("seckillGoods/seckillDetail.html?seckillGoodsId=" + seckillGoodsId);
}