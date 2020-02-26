$(document).ready(function () {
    adminQuery(1, 10, "all", "none");

    querySeckillGoods(1, 10);

    queryByType();

    turn();
});

/**
 * 商品管理--根据查询类型传参
 */
function queryByType() {
    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            var listType = $("#listType").val();
            var param = $("#param").val();
            adminQuery(1, 10, listType, param);
        }
    });

    $("#query").click(function () {
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
        adminQuery(currentNo, pageSize, "seckillAll", "none")
        $("#listType").find("option[value='goodsId']").val("seckillGoodsId").text("按秒杀商品id查询");
        $("#listType").find("option[value='goodsName']").val("seckillGoodsName").text("按秒杀商品名称查询");

    });
}

/**
 * 商品管理--分页查询
 */
function adminQuery(currentNo, pageSize, listType, param) {
    if ((listType === "goodsId" || listType === "goodsName" || listType === "seckillGoodsId" || listType === "seckillGoodsName") && param.trim() === "") {
        return false;
    }

    var pageModel = {
        "currentNo": currentNo,
        "pageSize": pageSize
    };

    $.ajax({
        type: "GET",
        url: "/emall/goods/admin/" + listType + "/" + param,
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
                            '<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + goodsId + "', " + "'" + listType + "'" + ')">上架</a>') +
                        "</td>" +
                        "<td>" +
                        '<a class="opear" href="goodsDetail.html?goodsId=' + goodsId + '">查看</a>' +
                        '<a class="opear" href="goodsEdit.html?goodsId=' + goodsId + '">编辑</a>' +
                        '<a class="opear" onclick="delGoods(' + "'" + goodsId + "'" + ')">删除</a>' +
                        "</td>" +
                        "</tr>";
                    tbody.append(goodsEle);
                }

                $("#pageControl").css("display", "none");
                return false;
            } else if (listType === "seckillGoodsId") {
                var seckillGoods = data.obj;

                if (seckillGoods !== null) {
                    var seckillGoodsId = seckillGoods.seckillGoodsId;
                    var status = seckillGoods.seckillGoodsStatus;
                    var seckillGoodsEle = "<tr>" +
                        "<td>" + seckillGoodsId + "</td>" +
                        "<td><p>" + seckillGoods.seckillGoodsName + "</p><p>" + seckillGoods.seckillGoodsDescribe + "</p></td>" +
                        "<td>" + seckillGoods.seckillGoodsPrice + "元" + "</td>" +
                        "<td>" + seckillGoods.seckillGoodsStock + "件" + "</td>" +
                        "<td>" + (status === 0 ? '<a id="' + seckillGoodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + seckillGoodsId + "', " + "'" + listType + "'" + ')">上架</a>' :
                            status === 1 ? '<a id="' + seckillGoodsId + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'" + seckillGoodsId + "', " + "'" + listType + "'" + ')">下架</a>' : status === 2 ? "进行中" : "已结束") +
                        "</td>" +
                        "<td>" +
                        '<a class="opear" href="seckillGoodsDetail.html?seckillGoodsId=' + seckillGoodsId + '">查看</a>';

                    if (status !== 2 && status !== 3) {
                        seckillGoodsEle += '<a class="opear" href="goodsEdit.html?seckillGoodsId=' + seckillGoodsId + '">编辑</a>' +
                            '<a class="opear" onclick="delSeckillGoods(' + "'" + seckillGoodsId + "'" + ')">删除</a></td></tr>';
                    }

                    tbody.append(seckillGoodsEle);
                }

                $("#pageControl").css("display", "none");
                return false;
            } else if (listType === "seckillAll" || listType === "seckillGoodsName") {
                var seckillGoodsList = data.obj.list;

                if (seckillGoodsList.length !== 0) {
                    for (var i = 0; i < seckillGoodsList.length; i++) {
                        var seckillGoodsId = seckillGoodsList[i].seckillGoodsId;
                        var status = seckillGoodsList[i].seckillGoodsStatus;
                        var ele = "<tr>" +
                            "<td>" + seckillGoodsId + "</td>" +
                            "<td><p>" + seckillGoodsList[i].seckillGoodsName + "</p><p>" + seckillGoodsList[i].seckillGoodsDescribe + "</p></td>" +
                            "<td>" + seckillGoodsList[i].seckillGoodsPrice + "元" + "</td>" +
                            "<td>" + seckillGoodsList[i].seckillGoodsStock + "件" + "</td>" +
                            "<td>" + (status === 0 ? '<a id="' + seckillGoodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + seckillGoodsId + "', " + "'" + listType + "'" + ')">上架</a>' :
                                status === 1 ? '<a id="' + seckillGoodsId + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'" + seckillGoodsId + "', " + "'" + listType + "'" + ')">下架</a>' : status === 2 ? "进行中" : "已结束") +
                            "</td>" +
                            "<td>" +
                            '<a class="opear" href="seckillGoodsDetail.html?seckillGoodsId=' + seckillGoodsId + '">查看</a>';

                        if (status !== 2 && status !== 3) {
                            ele += '<a class="opear" href="goodsEdit.html?seckillGoodsId=' + seckillGoodsId + '">编辑</a>' +
                                '<a class="opear" onclick="delSeckillGoods(' + "'" + seckillGoodsId + "'" + ')">删除</a></td></tr>';
                        }

                        tbody.append(ele);
                    }

                    if (data.obj.totalPages === 1) {
                        $("#pageControl").css("display", "none");
                    } else {
                        $("#pageControl").css("display", "block");
                    }
                } else {
                    $("#pageControl").css("display", "none");
                    return false;
                }
            } else {
                var goodsList = data.obj.list;

                if (goodsList.length !== 0) {
                    for (var j = 0; j < goodsList.length; j++) {
                        var goodsId = goodsList[j].goodsId;
                        var element = "<tr>" +
                            "<td>" + goodsId + "</td>" +
                            "<td><p>" + goodsList[j].goodsName + "</p><p>" + goodsList[j].goodsDescribe + "</p></td>" +
                            "<td>" + goodsList[j].goodsPrice + "元" + "</td>" +
                            "<td>" + goodsList[j].goodsStock + "件" +"</td>" +
                            "<td>" + (goodsList[j].goodsStatus === 1 ?
                                '<a id="' + goodsId + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'" + goodsId + "'" + ')">下架</a>' :
                                '<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + goodsId + "', " + "'" + listType + "'" + ')">上架</a>') +
                            "</td>" +
                            "<td>" +
                            '<a class="opear" href="goodsDetail.html?goodsId=' + goodsId + '">查看</a>' +
                            '<a class="opear" href="goodsEdit.html?goodsId=' + goodsId + '">编辑</a>' +
                            '<a class="opear" onclick="delGoods(' + "'" + goodsId + "'" + ')">删除</a>' +
                            "</td>" +
                            "</tr>";
                        tbody.append(element);
                    }

                    if (data.obj.totalPages === 1) {
                        $("#pageControl").css("display", "none");
                    } else {
                        $("#pageControl").css("display", "block");
                    }
                } else {
                    $("#pageControl").css("display", "none");
                    return false;
                }
            }

            //分页
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
 * 商品管理--分页跳转
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

/**
 * 下架商品
 * @param goodsId
 */
function pull(goodsId, listType) {
    if (listType === "all" || listType === "goodsId" || listType === "goodsName") {
        layer.confirm(
            "您确定要下架该商品？",
            {btn: ["确定", "取消"]},
            function (index) {
                $.ajax({
                    type: "POST",
                    url: "/emall/goods/pull",
                    data: goodsId,
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800}, function () {
                                layer.close(index);
                                $("#" + goodsId).replaceWith('<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="put(' + "'" + goodsId + "', " + "'" + listType + "'" + ')">上架</a>');
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
        layer.confirm(
            "您确定要下架该商品？",
            {btn: ["确定", "取消"]},
            function (index) {
                $.ajax({
                    type: "POST",
                    url: "/emall/seckillGoods/pull",
                    data: goodsId,
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800}, function () {
                                layer.close(index);
                                $("#" + goodsId).replaceWith('<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="put(' + "'" + goodsId + "', " + "'" + listType + "'" + ')">上架</a>');
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
    }

}

/**
 * 上架商品
 * @param goodsId
 * @param listType
 */
function put(goodsId, listType) {
    if (listType === "all" || listType === "goodsId" || listType === "goodsName") {
        layer.confirm(
            "您确定要上架该商品？",
            {btn: ["确定", "取消"]},
            function (index) {
                $.ajax({
                    type: "POST",
                    url: "/emall/goods/put",
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
                    layer.msg("开始时间不能大于结束时间", {time: 1000});
                    return false;
                } else if (startTime < new Date(new Date().getTime())) {
                    layer.msg("开始时间不能小于当前时间", {time: 1000});
                    return false;
                }

                var timeInfo = {"seckillGoodsId": goodsId, "startTime": startTime, "endTime": endTime};
                $.ajax({
                    type: "POST",
                    url: "/emall/seckillGoods/put",
                    data: timeInfo,
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800});
                            layer.close(index);
                            $("#" + goodsId).replaceWith('<a id="' + goodsId + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'"
                                + goodsId + "', " + "'" + listType + "'" + ')">下架</a>');
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

/**
 * 删除商品
 * @param goodsId
 */
function delGoods(goodsId) {
    layer.confirm(
        "您确定要删除该商品吗？",
        {btn: ["确定", "取消"]},
        function (index) {
            $.ajax({
                type: "DELETE",
                url: "/emall/goods",
                data: goodsId,
                contentType: 'application/json;charset=UTF-8',
                success: function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg, {time: 800}, function () {
                            layer.close(index);
                            var listType = $("#listType").val();
                            var param = $("#param").val();
                            var currentNo = $("#currentNo").val();
                            adminQuery(currentNo, 10, listType, param);
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
}

/**
 * 删除秒杀商品
 * @param seckillGoodsId
 */
function delSeckillGoods(seckillGoodsId) {
    layer.confirm(
        "您确定要删除该商品吗？",
        {btn: ["确定", "取消"]},
        function (index) {
            $.ajax({
                type: "DELETE",
                url: "/emall/seckillGoods",
                data: seckillGoodsId,
                contentType: 'application/json;charset=UTF-8',
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
