$(document).ready(function () {
    adminInfo();

    adminQuery(1, 10, "all", "none");

    querySeckillGoods(1, 10);

    queryType();

    turn();
});

/**
 * 商品管理--根据查询类型传参
 */
function queryType() {
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            var listType = $("#listType").val();
            var param = $("#param").val();
            adminQuery(1, 10, listType, param);
        }
    });

    $("#queryType").click(function () {
        var listType = $("#listType").val();
        var param = $("#param").val();
        adminQuery(1, 10, listType, param);
    });
}

/**
 * 商品类别管理--秒杀商品查询
 */
function querySeckillGoods(currentNo, pageSize) {
    $("#seckillGoods").click(function () {
        adminQuery(currentNo, pageSize, "seckill", "none")
    });
}

/**
 * 商品管理--分页查询
 */
function adminQuery(currentNo, pageSize, listType, param) {
    var pageModel = {
        "currentNo": currentNo,
        "pageSize": pageSize
    };

    $.ajax({
        type: "GET",
        url: "/goods/admin/" + listType + "/" + param,
        data: pageModel,
        success: function (data) {
            var tbody = $(".goodsTable");
            tbody.empty();

            if (listType === "goodsId") {
                var goods = data.obj;

                if (goods !== null) {
                    var goodsId = goods.goodsId;
                    var goodsEle = "<tr>" +
                        "<td>" + goodsId + "</td>" +
                        "<td><p>" + goods.goodsName + "</p><p>" + goods.goodsDescribe + "</p></td>" +
                        "<td>" + goods.goodsPrice + "</td>" +
                        "<td>" + goods.goodsStock + "</td>" +
                        "<td>" + (goods.goodsStatus === 1 ?
                            '<a id="' + goodsId + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'" + goodsId + "'" + ')">下架</a>' :
                            '<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + goodsId + "'" + ')">上架</a>') +
                        "</td>" +
                        "<td>" +
                        '<a class="opear" href="goodsDetail.html?goodsId=' + goodsId + '">查看</a>' +
                        '<a class="opear" href="goodsEdit.html?goodsId=' + goodsId + '">编辑</a>' +
                        "</td>" +
                        "</tr>";
                    tbody.append(goodsEle);
                }

                $("#pageControl").css("display", "none");
                return false;
            } else if (listType === "seckill") {
                var seckillGoodsList = data.obj.list;

                if (seckillGoodsList.length !== 0) {
                    for (var i = 0; i < seckillGoodsList.length; i++) {
                        var seckillGoodsId = seckillGoodsList[i].seckillGoodsId;
                        var status = seckillGoodsList[i].seckillGoodsStatus
                        var ele = "<tr>" +
                            "<td>" + seckillGoodsId + "</td>" +
                            "<td><p>" + seckillGoodsList[i].seckillGoodsName + "</p><p>" + seckillGoodsList[i].seckillGoodsDescribe + "</p></td>" +
                            "<td>" + seckillGoodsList[i].seckillGoodsPrice + "元" + "</td>" +
                            "<td>" + seckillGoodsList[i].seckillGoodsStock + "件" + "</td>" +
                            "<td>" + (status === 1 || status === 3 ? '<a id="' + seckillGoodsId + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'" + seckillGoodsId + "', " + "'" + listType + "'" + ')">下架</a>' :
                                status === 0 ? '<a id="' + seckillGoodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + seckillGoodsId + "', " + "'" + listType + "'" + ')">上架</a>' : "秒杀进行中") +
                            "</td>" +
                            "<td>" +
                            '<a class="opear" href="seckillGoodsDetail.html?seckillGoodsId=' + seckillGoodsId + '">查看</a>' +
                            '<a class="opear" onclick="del(' + "'" + seckillGoodsId + "'" + ')">删除</a>' +
                            "</td>" +
                            "</tr>";
                        tbody.append(ele);
                    }

                    $("#pageControl").css("display", "block");
                } else {
                    $("#pageControl").css("display", "none");
                    return false;
                }
            } else {
                var goodsList = data.obj.list;

                if (goodsList.length !== 0) {
                    for (var j = 0; j < goodsList.length; j++) {
                        var id = goodsList[j].goodsId;
                        var element = "<tr>" +
                            "<td>" + id + "</td>" +
                            "<td><p>" + goodsList[j].goodsName + "</p><p>" + goodsList[j].goodsDescribe + "</p></td>" +
                            "<td>" + goodsList[j].goodsPrice + "元" + "</td>" +
                            "<td>" + goodsList[j].goodsStock + "件" +"</td>" +
                            "<td>" + (goodsList[j].goodsStatus === 1 ?
                                '<a id="' + id + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'" + id + "'" + ')">下架</a>' :
                                '<a id="' + id + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + id + "'" + ')">上架</a>') +
                            "</td>" +
                            "<td>" +
                            '<a class="opear" href="goodsDetail.html?goodsId=' + id + '">查看</a>' +
                            '<a class="opear" href="goodsEdit.html?goodsId=' + id + '">编辑</a>' +
                            "</td>" +
                            "</tr>";
                        tbody.append(element);
                    }

                    $("#pageControl").css("display", "block");
                } else {
                    $("#pageControl").css("display", "none");
                    return false;
                }
            }

            var totalPages = data.obj.totalPages;
            $("#currentNo").val(currentNo);
            $("#totalPages").html(totalPages);

            var nextPage = currentNo + 1;
            var lastPage = currentNo - 1;

            $("#type").val(listType);
            $("#word").val(param);

            listType = "'" + listType + "'";
            param = "'" + param + "'";
            if (currentNo === 1 && currentNo === totalPages) {
                $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link'></a>");
                $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link'></a>");
            } else if (currentNo === 1) {
                $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link'></a>");
                $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link' " +
                    'onclick="adminQuery(' + nextPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
            } else if (currentNo === totalPages) {
                $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link'></a>");
                $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link' " +
                    'onclick="adminQuery(' + lastPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
            } else {
                $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link' " +
                    'onclick="adminQuery(' + lastPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
                $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link' " +
                    'onclick="adminQuery(' + nextPage + ", " + pageSize + ", " + listType + ", " + param + ')"></a>');
            }
        }
    });
}

/**
 * 商品类别管理--分页跳转
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
        isNaN(currentNo) ? layer.msg("页码不能为空", {time : 1000}) : adminQuery(currentNo, 10, listType, param);
    });
}

function pull(goodsId, listType) {
    if (listType !== "seckill") {
        layer.confirm(
            "您确定要下架该商品？",
            {btn: ["确定", "取消"]},
            function (index) {
                $.ajax({
                    type: "POST",
                    url: "/goods/pull",
                    data: goodsId,
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800}, function () {
                                layer.close(index);
                                $("#" + goodsId).replaceWith('<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="put(' + "'" + goodsId + "'" + ')">上架</a>');
                            });
                        } else {
                            layer.msg(data.msg, {time : 1000});
                        }
                    }
                });
            },
            function (index) {
                layer.close(index);
            }
        );
    } else {
        layer.confirm(
            "您确定要下架该商品？",
            {btn: ["确定", "取消"]},
            function (index) {
                $.ajax({
                    type: "POST",
                    url: "/seckillGoods/pull",
                    data: goodsId,
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800}, function () {
                                layer.close(index);
                                $("#" + goodsId).replaceWith('<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="put(' + "'" + goodsId + "', " + "'seckill'" +')">上架</a>');
                            });
                        } else {
                            layer.msg(data.msg, {time : 1000});
                        }
                    }
                });
            },
            function (index) {
                layer.close(index);
            }
        );
    }
}

function put(goodsId, listType) {
    if (listType !== "seckill") {
        layer.confirm(
            "您确定要上架该商品？",
            {btn: ["确定", "取消"]},
            function (index) {
                $.ajax({
                    type: "POST",
                    url: "/goods/put",
                    data: goodsId,
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800}, function () {
                                layer.close(index);
                                $("#" + goodsId).replaceWith('<a id="' + goodsId + '" class="btn btn-xs btn-warning opear" onclick="pull(' + "'" + goodsId + "'" + ')">下架</a>');
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
    } else {
        layer.open({
            id: 1,
            type: 1,
            title: '上架秒杀商品',
            skin: 'layui-layer-rim',
            area: ['400px', '210px', 'center'],
            btnAlign: 'c',
            content: '<div class="row" style="width: 320px; margin-left:55px; margin-top:15px;">'
                + '<div class="col-sm-12">'
                + '<div class="input-group">'
                + '<span class="input-group-addon">开始时间:</span>'
                + '<input id="startTime" type="text" class="form-control" style="width: 160px">'
                + '</div>'
                + '<div class="input-group" style="margin-top: 10px">'
                + '<span class="input-group-addon">结束时间:</span>'
                + '<input id="endTime" type="text" class="form-control" style="width: 160px">'
                + '</div>'
                + '</div>'
                + '</div>',
            btn: ['确定', '取消'],
            btn1: function (index) {
                var start = $("#startTime").val();
                var end = $("#endTime").val();
                if (start === undefined || start.trim() === "") {
                    layer.msg("开始时间不能为空", {time: 1000});
                    return false;
                } else if (end === undefined || end.trim() === "") {
                    layer.msg("结束时间不能为空", {time: 1000});
                    return false;
                }
                var startTime = new Date(Date.parse(start));
                var endTime = new Date(Date.parse(end));
                if (startTime > endTime) {
                    layer.msg("开始时间不能大于结束时间", {time: 1200});
                    return false;
                } else if (startTime < new Date(new Date().getTime() + 1800 * 1000)) {
                    layer.msg("开始时间至少在当前时间一小时以后", {time: 1200});
                    return false;
                }

                var timeInfo = {"seckillGoodsId": goodsId, "startTime": startTime, "endTime": endTime};
                $.ajax({
                    type: "POST",
                    url: "/seckillGoods",
                    data: timeInfo,
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800});
                            layer.close(index);
                            $("#" + goodsId).replaceWith('<a id="' + goodsId + '" class="btn btn-xs btn-warning opear" onclick="pull(' + "'" + goodsId + "'" + ')">下架</a>');
                        } else {
                            layer.msg(data.msg, {time: 1000});
                        }
                    }
                });
            },
            btn2: function (index) {
                layer.close(index);
            }
        });
        laydate.render({
            elem: '#startTime',
            format:'yyyy-MM-dd HH:mm:ss',
            type: 'datetime',
            trigger: 'click'
        });

        laydate.render({
            elem: '#endTime',
            format:'yyyy-MM-dd HH:mm:ss',
            type: 'datetime',
            trigger: 'click'
        });
    }
}

function del(seckillGoodsId) {
    layer.confirm(
        "您确定要删除该商品？",
        {btn: ["确定", "取消"]},
        function (index) {
            $.ajax({
                type: "DELETE",
                url: "/seckillGoods/" + seckillGoodsId + "/seckillGoodsId",
                data: seckillGoodsId,
                success: function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg, {time: 800}, function () {
                            layer.close(index);
                            querySeckillGoods(1, 10);
                        });
                    } else {
                        layer.msg(data.msg, {time : 1000});
                    }
                }
            });
        },
        function (index) {
            layer.close(index);
        }
    );
}
