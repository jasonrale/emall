$(document).ready(function () {
    navInfo();

    queryAll();
});

/**
 * 查询个人所有购物车明细
 */
function queryAll() {
    $.ajax({
        type: "GET",
        url: "/cartItem",
        success: function (data) {
            if (data.status === true) {
                var cartItemList = data.obj;
                $(".page-wrap.w").append('<div class="cart-header">' +
                    '<table class="cart-table">' +
                    "<tr>" +
                    '<th class="cart-cell cell-check">' +
                    '<label class="cart-label">' +
                    '<span class="cart-cell cell-box">选择</span>' +
                    "</label>" +
                    "</th>" +
                    '<th class="cart-cell cell-info">商品信息</th>' +
                    '<th class="cart-cell cell-price">单价</th>' +
                    '<th class="cart-cell cell-count">数量</th>' +
                    '<th class="cart-cell cell-total">合计</th>' +
                    '<th class="cart-cell cell-opera">操作</th>' +
                    "</tr>" +
                    "</table>" +
                    "</div>" +
                    '<div class="cart-list">' +
                    "</div>"
                );

                for (var i = 0; i < cartItemList.length; i++) {
                    var cartItem = cartItemList[i];
                    var goodsId = cartItem.goodsId;

                    var element = '<table class="cart-table" data-goodsId="' + goodsId + '">' +
                        "<tr>" +
                        '<td class="cart-cell cell-check">' +
                        '<label class="cart-label">' +
                        '&nbsp;&nbsp;<input type="checkbox" class="cart-select" name="checkbox-item" value="' + cartItem.cartItemId + "," + cartItem.cartItemSubtotal + '"/>' +
                        "</label>" +
                        "</td>" +
                        '<td class="cart-cell cell-img">' +
                        '<a class="link" href="../../goods/detail.html?goodsId=' + goodsId + '">' +
                        '<img class="p-img" src="' + cartItem.goodsImage + '" alt="' + cartItem.goodsName + '"/>' +
                        "</a>" +
                        "</td>" +
                        '<td class="cart-cell cell-info">' +
                        '<a class=link href="../../goods/detail.html?goodsId=' + goodsId + '">' + cartItem.goodsName + "</a>" +
                        "</td>" +
                        '<td class="cart-cell cell-price">￥' + cartItem.goodsPrice + "</td>" +
                        '<td class="cart-cell cell-count">' +
                        '<span>' + cartItem.goodsCount + "<span>" +
                        "</td>" +
                        '<td class="cart-cell cell-total">￥' + cartItem.cartItemSubtotal + "</td>" +
                        '<td class="cart-cell cell-opera">' +
                        '<span class="link cart-delete" onclick="del(' + "'" + cartItem.cartItemId + "'" + ')">删除</span>' +
                        "</td>" +
                        "</tr>" +
                        "</table>";

                    $(".cart-list").append(element);
                }

                $(".page-wrap.w").append('<div class="cart-footer">' +
                    '<div class="select-con">' +
                    "<label>" +
                    '&nbsp;&nbsp;<input type="checkbox" class="checkAll"/>' +
                    "<span>全选</span>" +
                    "</label></div>" +
                    '<div class="delete-con">' +
                    '<span class="link delete-selected">' +
                    '<span onclick="delSelect()">删除选中</span>' +
                    "</span>" +
                    "</div>" +
                    '<div class="submit-con">' +
                    "<span>总价：</span>" +
                    '<span class="submit-total">￥0</span>' +
                    '<span class="btn btn-submit" onclick="settleAccounts()">去结算</span>' +
                    "</div>" +
                    "</div>"
                );

                checkBox();
                totalPrice();
            } else {
                $(".page-wrap.w").append('<p class="err-tip"><span>您的购物车空空如也，</span> <a href="../../index.html">立即去购物</a></p>');
            }
        }
    });
}

/**
 * checkbox全选与取消
 */
function checkBox() {
    var checkList = $(".cart-list");

    var checkBoxItemNum = checkList.find("input[name='checkbox-item']").length;

    $(".checkAll").change(function () {
        if ($(this).prop("checked") === true) {
            checkList.find("input[name='checkbox-item']").prop("checked", true);
        } else {
            checkList.find("input[name='checkbox-item']").prop("checked", false);
        }
    });

    checkList.find("input[name='checkbox-item']").change(function () {
        if (checkList.find("input[name='checkbox-item']:checked").length === checkBoxItemNum) {
            $(".checkAll").prop("checked", true);
        } else {
            $(".checkAll").prop("checked", false);
        }
    });
}

/**
 * 总价变化
 */
function totalPrice() {
    $(".checkAll").change(function () {
        var totalPrice = 0;
        $(".cart-list").find("input[name='checkbox-item']:checked").each(function () {
            var value = $(this).val();
            totalPrice += parseInt(value.split(",")[1]);
        });

        $(".submit-total").html("￥" + totalPrice);
    });

    $(".cart-list").find("input[name='checkbox-item']").change(function () {
        var totalPrice = 0;
        $(".cart-list").find("input[name='checkbox-item']:checked").each(function () {
            var value = $(this).val();
            totalPrice += parseInt(value.split(",")[1]);
        });

        $(".submit-total").html("￥" + totalPrice);
    });
}

/**
 * 删除选中购物车明细
 */
function delSelect() {
    var cartItemIdList;
    cartItemIdList = [];
    $(".cart-list").find("input[name='checkbox-item']:checked").each(function () {
        var value = $(this).val();
        cartItemIdList.push(value.split(",")[0]);
    });

    if (cartItemIdList.length === 0) {
        layer.msg("没有选择商品", {time: 1000});
        return false;
    }

    layer.confirm(
        "您确定要删除所选中的商品吗？",
        {btn: ["确定", "取消"]},
        function (index) {
            $.ajax({
                type: "DELETE",
                url: "/cartItem/select",
                data: JSON.stringify(cartItemIdList),
                contentType: 'application/json;charset=UTF-8',
                success: function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg, {time: 1000}, function () {
                            window.location.reload();
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
 * 删除单个购物车明细
 * @param cartItemId
 */
function del(cartItemId) {
    layer.confirm(
        "您确定要删除该商品吗？",
        {btn: ["确定", "取消"]},
        function (index) {
            $.ajax({
                type: "DELETE",
                url: "/cartItem",
                data: cartItemId,
                contentType: 'application/json;charset=UTF-8',
                success: function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg, {time: 1000}, function () {
                            window.location.reload();
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
 * 结算生成订单
 */
function settleAccounts() {
    var cartItemIdList;
    cartItemIdList = [];
    $(".cart-list").find("input[name='checkbox-item']:checked").each(function () {
        var value = $(this).val();
        cartItemIdList.push(value.split(",")[0]);
    });

    var totalPrice = $(".submit-total").html().split("￥")[1];

    sessionStorage.setItem("cartItemIdList", JSON.stringify(cartItemIdList));
    sessionStorage.setItem("totalPrice", JSON.stringify(totalPrice));

    $(window).attr("location", "orderConfirm.html?from=cart");
}