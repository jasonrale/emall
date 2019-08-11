$(document).ready(function () {
    adminInfo();

    adminQuery(1, 10, "all", "none");

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
                            '<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + goodsId + "', " + goods.goodsActivity + ')">上架</a>') +
                        "</td>" +
                        "<td>" + (goods.goodsActivity === 0 ? "无" : "秒杀") + "</td>" +
                        "<td>" +
                        '<a class="opear" href="goodsDetail.html?goodsId=' + goodsId + '">查看</a>' +
                        '<a class="opear" href="goodsEdit.html?goodsId=' + goodsId + '">编辑</a>' +
                        "</td>" +
                        "</tr>";
                    tbody.append(goodsEle);
                }

                $("#pageControl").css("display", "none");
            } else {
                var goodsList = data.obj.list;

                if (goodsList.length !== 0) {
                    for (var i = 0; i < goodsList.length; i++) {
                        var goodsId = goodsList[i].goodsId;
                        var element = "<tr>" +
                            "<td>" + goodsId + "</td>" +
                            "<td><p>" + goodsList[i].goodsName + "</p><p>" + goodsList[i].goodsDescribe + "</p></td>" +
                            "<td>" + goodsList[i].goodsPrice + "</td>" +
                            "<td>" + goodsList[i].goodsStock + "</td>" +
                            "<td>" + (goodsList[i].goodsStatus === 1 ?
                                '<a id="' + goodsId + '" class="btn btn-xs btn-warning opear" onclick="' + "pull(" + "'" + goodsId + "'" + ')">下架</a>' :
                                '<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="' + "put(" + "'" + goodsId + "', " + goodsList[i].goodsActivity + ')">上架</a>') +
                            "</td>" +
                            "<td>" + (goodsList[i].goodsActivity === 0 ? "无" : "秒杀") + "</td>" +
                            "<td>" +
                            '<a class="opear" href="goodsDetail.html?goodsId=' + goodsId + '">查看</a>' +
                            '<a class="opear" href="goodsEdit.html?goodsId=' + goodsId + '">编辑</a>' +
                            "</td>" +
                            "</tr>";
                        tbody.append(element);
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

                    $("#pageControl").css("display", "block");
                } else {
                    $("#pageControl").css("display", "none");
                }
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
        isNaN(currentNo) ? layer.msg("页码不能为空") : adminQuery(currentNo, 10, listType, param);
    });
}

function pull(goodsId) {
    layer.confirm(
        "您确定要下架该商品？",
        {btn: ["确定", "取消"]},
        function (index) {
            $.ajax({
                type: "POST",
                url: "/goods/admin/pull",
                data: goodsId,
                contentType: 'application/json;charset=UTF-8',
                success: function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg, {time: 800}, function () {
                            layer.close(index);
                            $("#" + goodsId).replaceWith('<a id="' + goodsId + '" style="background-color: #55933b" class="btn btn-xs btn-warning opear" onclick="put(' + "'" + goodsId + "'" + ')">上架</a>');
                        });
                    } else {
                        layer.msg(data.msg);
                    }
                }
            });
        },
        function (index) {
            layer.close(index);
        }
    );
}

function put(goodsId, goodsActivity) {
    if (goodsActivity === 0) {
        layer.confirm(
            "您确定要上架该商品？",
            {btn: ["确定", "取消"]},
            function (index) {
                $.ajax({
                    type: "POST",
                    url: "/goods/admin/put",
                    data: goodsId,
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800}, function () {
                                layer.close(index);
                                $("#" + goodsId).replaceWith('<a id="' + goodsId + '" class="btn btn-xs btn-warning opear" onclick="pull(' + "'" + goodsId + "'" + ')">下架</a>');
                            });
                        } else {
                            layer.msg(data.msg);
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

            },
            btn2: function (index) {
                layer.close(index);
            }
        });

        laydate.render({
            elem: '#startTime',
            type: 'datetime'
        });

        laydate.render({
            elem: '#endTime',
            type: 'datetime'
        });
    }

}
