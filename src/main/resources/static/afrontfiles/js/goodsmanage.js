$(document).ready(function () {
    adminInfo();

    adminQuery(1, 10, "all", "none");

    queryType();

    turn();

    addGoods();
});

/**
 * 商品管理--根据查询类型传参
 */
function queryType() {
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
        type : "GET",
        url: "/goods/admin/queryByType?listType=" + listType + "&param=" + param,
        data: pageModel,
        success : function (data) {
            var tbody = $(".goodsTable");
            tbody.empty();

            if (listType === "goodsId") {
                var goods = data.obj;

                var goodsEle = "<tr>\n" +
                    "    <td>" + goods.goodsId + "</td>\n" +
                    "    <td><p>" + goods.goodsName + "</p><p>" + goods.goodsDescribe + "</p></td>\n" +
                    "    <td>" + goods.goodsPrice + "</td>\n" +
                    "    <td>" + goods.goodsStock + "</td>\n" +
                    '    <td><a class="btn btn-xs btn-warning opear">下架</a></td>\n' +
                    "    <td>\n" +
                    '        <a class="opear" href="">编辑</a>\n' +
                    "    </td>\n" +
                    "</tr>";
                tbody.append(goodsEle);

                $("#pageControl").css("display", "none")
            } else {
                var goodslist = data.obj.list;

                for (var i = 0; i < goodslist.length; i++) {
                    var element = "<tr>\n" +
                        "    <td>" + goodslist[i].goodsId + "</td>\n" +
                        "    <td><p>" + goodslist[i].goodsName + "</p><p>" + goodslist[i].goodsDescribe + "</p></td>\n" +
                        "    <td>" + goodslist[i].goodsPrice + "</td>\n" +
                        "    <td>" + goodslist[i].goodsStock + "</td>\n" +
                        '    <td><a class="btn btn-xs btn-warning opear">下架</a></td>\n' +
                        "    <td>\n" +
                        '        <a class="opear" href="">编辑</a>\n' +
                        "    </td>\n" +
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
                if (currentNo === 1) {
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

/**
 * 商品管理--添加
 */
function addGoods() {
    $("#addGoods").click(function () {
        $("#query").css("display", "none");
        $("#add").css("display", "block");

        $.ajax({
            type: "get",
            url: "/category",
            dataType: "json",
            success: function (data) {
                var categoryList = data.obj;
                for (var i = 0; i < categoryList.length; i++) {
                    $("#category").append("<option value='" + categoryList[i].categoryId + "'>" + categoryList[i].categoryName + "</option>");
                }
            }
        });
    });
}

/**
 * 商品图片预览
 */
function viewImage() {
    var image = $("#uploadImage");
    var path = image.val();
    var extStart = path.lastIndexOf("."), ext = path.substring(extStart, path.length).toUpperCase();
    if (ext !== ".PNG" && ext !== ".JPG" && ext !== ".JPEG" && ext !== ".GIF") {
        layer.msg("请上传正确格式的图片");
        image.val("");
        return false;
    }

    var fileObj = image[0].files[0];
    $("#viewImage").attr("src", URL.createObjectURL(fileObj)).css("display", "block");
    $("#imageNotice").attr("class", "hiddenNotice")
}

/**
 * 商品详情预览
 */
function viewDetail() {
    var detail = $("#uploadDetail");
    var path = detail.val();
    var extStart = path.lastIndexOf("."), ext = path.substring(extStart, path.length).toUpperCase();
    if (ext !== ".PNG" && ext !== ".JPG" && ext !== ".JPEG" && ext !== ".GIF") {
        layer.msg("请上传正确格式的图片");
        detail.val("");
        return false;
    }

    var fileObj = detail[0].files[0];
    $("#viewDetail").attr("src", URL.createObjectURL(fileObj)).css("display", "block");
    $("#detailNotice").attr("class", "hiddenNotice")
}

/**
 * 商品管理--添加上传
 */
function addSubmit() {
    var goodsName = $("#name").val();
    var goodsDescribe = $("#describe").val();
    var categoryId = $("#category").val();
    var goodsStock = $("#stock").val();
    var goodsPrice = $("#price").val();
    var image = $("#uploadImage");
    var detail = $("#uploadDetail");

    if (goodsName === undefined || goodsDescribe === undefined ||
        goodsStock === undefined || goodsPrice === undefined || image.val() === "" || detail.val() === "") {
        layer.msg("请填写表单所有内容！");
        return false;
    }

    var goods = {
        "goodsName": goodsName,
        "goodsDescribe": goodsDescribe,
        "categoryId": categoryId,
        "goodsStock": goodsStock,
        "goodsPrice": goodsPrice,
        "goodsStatus": "1"
    };

    var files = [];
    files[0] = image[0].files[0];
    files[1] = detail[0].files[0];

    var formData = new FormData();
    formData.append("imageFile", files[0]);
    formData.append("detailFile", files[1]);
    formData.append("goods", JSON.stringify(goods));

    showLoading();
    $.ajax({
        type: "POST",
        url: "/goods",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            if (data.status === true) {
                layer.msg("商品添加成功", {time: 800}, function () {
                    $(window).attr("location", "goodsmanage.html");
                });

            } else {
                layer.msg("商品添加失败");
            }
        },
        error: function () {
            layer.msg("商品添加失败");
        }
    });
}
