$(document).ready(function () {
    userInfo();

    var seckillGoodsId = getUrlParam("seckillGoodsId");
    var path = getUrlParam("path");

    init(seckillGoodsId, path);

    seckillSubmit(seckillGoodsId, path);
});

/**
 * 验证path
 * @param seckillGoodsId
 * @param path
 * @returns {boolean}
 */
function pathValid(seckillGoodsId, path) {
    var result = false;

    $.ajax({
        type: "GET",
        async: false,
        url: "/seckill/" + seckillGoodsId + "/" + path + "/checkPath",
        success: function (data) {
            result = data.status;

        }
    });

    return result;
}

/**
 * 数据初始化
 * @param seckillGoodsId
 * @param path
 */
function init(seckillGoodsId, path) {
    if (!pathValid(seckillGoodsId, path) || path === null || path === "") {
        layer.msg("请求非法", {time: 1000});
        $("#submit").attr("disabled", true);
        $(window).attr("location", "../../index.html");
    } else {
        seckillOrder(seckillGoodsId);
        queryAllShipping();
    }
}

/**
 * 秒杀订单确认信息
 */
function seckillOrder(seckillGoodsId) {
    $.ajax({
        type: "GET",
        async: false,
        url: "/seckillGoods/" + seckillGoodsId + "/seckillGoodsId",
        success: function (data) {
            if (data.status === true) {
                var seckillGoods = data.obj.seckillGoods;
                var element = "<tr>" +
                    '<td class="cell-img">' +
                    '<a href="../../seckillGoods/detail.html?seckillGoodsId=' + seckillGoods.seckillGoodsId + '" target="_blank">' +
                    '<img class="p-img" src="' + seckillGoods.seckillGoodsImage + '" alt="' + seckillGoods.seckillGoodsName + '"/>' +
                    "</a>" +
                    "</td>" +
                    '<td class="cell-info">' +
                    '<a class="link" href="../../seckillGoods/detail.html?seckillGoodsId=' + seckillGoods.seckillGoodsId + '" target="_blank">' + seckillGoods.seckillGoodsName + "</a>" +
                    "</td>" +
                    '<td class="cell-price">' + "￥" + seckillGoods.seckillGoodsPrice + "</td>" +
                    '<td class="cell-count">1</td>' +
                    '<td class="cell-total">' + "￥" + seckillGoods.seckillGoodsPrice + "</td>" +
                    "</tr>";
                $(".product-table").append(element);
                $(".submit-total").html("￥" + seckillGoods.seckillGoodsPrice);
            }
        }
    });
}

/**
 * 查询所有收货信息
 */
function queryAllShipping() {
    $.ajax({
        type: "GET",
        url: "/shipping/all",
        success: function (data) {
            if (data.status === true) {
                var shippingList = data.obj;
                $(".panel-body.address-con").empty();
                if (shippingList.length === 0) {
                    var ele = '<div class="address active">' +
                        '<div class="address-add">' +
                        '<div class="address-new">' +
                        '<div class="text" onclick="newShipping()">使用新地址</div>' +
                        "</div>" +
                        "</div>" +
                        "</div>";

                    $(".panel-body.address-con").append(ele);
                } else {
                    for (var i = 0; i < shippingList.length; i++) {
                        var shippingId = shippingList[i].shippingId;
                        var elementHead;
                        if (i === 0) {
                            elementHead = '<div class="address active" id="' + shippingId + '">';
                        } else {
                            elementHead = '<div class="address" id="' + shippingId + '">';
                        }
                        var element = elementHead +
                            '<div class="address-item" id="item:' + shippingId + '" onclick="select(this)">' +
                            '<div class="address-title" id="name:' + shippingId + '">' + shippingList[i].shippingName + "</div>" +
                            '<div class="address-detail" id="detail:' + shippingId + '">' + shippingList[i].shippingAddress + "</div>" +
                            '<div class="mobile-number" id="mobile:' + shippingId + '">' + shippingList[i].shippingMobileNumber + "</div>" +
                            '<div class="address-opera">' +
                            '<span class="link address-update" onclick="updateShipping(' + "'" + shippingId + "'" + ')">编辑</span>' +
                            '<span class="link address-delete">删除</span>' +
                            "</div>" +
                            "</div>" +
                            '<div class="address-add" onclick="newShipping()">' +
                            '<div class="address-new">' +
                            '<div class="text">使用新地址</div>' +
                            "</div>" +
                            "</div>" +
                            "</div>";

                        $(".panel-body.address-con").append(element);
                    }
                }
            }
        }
    });
}

function select(ele) {
    var id = $(ele).attr("id");
    var shippingId = id.split(":")[1];
    $(".address.active").attr("class", "address");
    document.getElementById("" + shippingId).className = "address active";
}

/**
 * 新建收货信息
 */
function newShipping() {
    $("#update").css("display", "none");
    var str = "'new'";
    $("#shippingSave").replaceWith('<a class="btn address-btn" id="shippingSave" onclick="shippingSave(' + str + ')">保存</a>');
    $("#modal").css("display", "block");
}

/**
 * 修改收货信息
 */
function updateShipping(shippingId) {
    $("#add").css("display", "none");
    $("#shippingId").val(shippingId);
    $("#shippingName").val(document.getElementById("name:" + shippingId).innerHTML);
    $("#shippingAddress").val(document.getElementById("detail:" + shippingId).innerHTML);
    $("#shippingMobileNumber").val(document.getElementById("mobile:" + shippingId).innerHTML);

    var str = "'update'";
    $("#shippingSave").replaceWith('<a class="btn address-btn" id="shippingSave" onclick="shippingSave(' + str + ')">保存</a>');
    $("#modal").css("display", "block");
}

/**
 * 保存收货信息
 */
function shippingSave(type) {
    var shippingName = $("#shippingName").val();
    var shippingAddress = $("#shippingAddress").val();
    var shippingMobileNumber = $("#shippingMobileNumber").val();
    var shippingId = $("#shippingId").val();

    if (shippingName === undefined || shippingName.trim() === "") {
        layer.msg("收货人姓名不能为空");
        return false;
    } else if (shippingAddress === undefined || shippingAddress.trim() === "") {
        layer.msg("收货人地址不能为空");
        return false;
    } else if (!mobileValid(shippingMobileNumber)) {
        layer.msg("手机号码格式不正确！");
        return false;
    }

    if (type === "new") {
        var shipping = {
            "shippingName": shippingName,
            "shippingAddress": shippingAddress,
            "shippingMobileNumber": shippingMobileNumber
        };
        $.ajax({
            type: "PUT",
            url: "/shipping",
            data: JSON.stringify(shipping),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                if (data.status === true) {
                    layer.msg(data.msg);
                    shippingCancel();
                    queryAllShipping();
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    } else {
        var shipping = {
            "shippingId": shippingId,
            "shippingName": shippingName,
            "shippingAddress": shippingAddress,
            "shippingMobileNumber": shippingMobileNumber
        };
        $.ajax({
            type: "POST",
            url: "/shipping",
            data: JSON.stringify(shipping),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                if (data.status === true) {
                    layer.msg(data.msg);
                    shippingCancel();
                    queryAllShipping();
                } else {
                    layer.msg(data.msg);
                }
            }
        });
    }
}

/**
 * 取消已填写收货信息
 */
function shippingCancel() {
    $("#modal").css("display", "none");
    $("#shippingName").val("");
    $("#shippingAddress").val("");
    $("#shippingMobileNumber").val("");
}

/**
 * 验证手机号码
 */
function mobileValid(mobileNumber) {
    var result = true;
    var mobileValidate = /^(1[3-9])\d{9}$/;

    if (!(mobileValidate.test(mobileNumber))) {
        result = false;
    }

    return result;
}

/**
 * 秒杀订单提交
 * @param seckillGoodsId
 * @param path
 */
function seckillSubmit(seckillGoodsId, path) {
    var json = {
        seckillGoodsId: seckillGoodsId,
        shippingId: $(".address.active").attr("id"),
        path: path
    };

    $("#submit").click(function () {
        $.ajax({
            type: "PUT",
            url: "/seckillOrder",
            data: JSON.stringify(json),
            contentType: 'application/json;charset=UTF-8',
            success: function (data) {
                if (data.status === true) {
                    layer.msg(data.msg, {time: 1500}, function () {
                        $(window).attr("location", "/orderDetail.html?orderId=" + data.obj)
                    });
                } else {
                    layer.msg(data.msg, {time: 1000});
                    $(window).attr("location", "../../index.html");
                }
            }
        });
    });
}
