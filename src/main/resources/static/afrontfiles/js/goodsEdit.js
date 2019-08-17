$(document).ready(function () {
    adminInfo();

    var goodsId = (getUrlParam("goodsId"));

    goodsInit(goodsId);

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            updateSubmit(goodsId);
        }
    });
});

/**
 * 商品信息初始化
 * @param goodsId
 */
function goodsInit(goodsId) {
    $.ajax({
        type: "GET",
        url: "/goods/" + goodsId + "/goodsId",
        success: function (data) {
            var goods = data.obj;
            $("#name").val(goods.goodsName);
            $("#describe").val(goods.goodsDescribe);
            $("#price").val(goods.goodsPrice);
            $("#stock").val(goods.goodsStock);
            $.ajax({
                type: "get",
                url: "/category",
                dataType: "json",
                success: function (data) {
                    var categoryList = data.obj;
                    for (var i = 0; i < categoryList.length; i++) {
                        if (categoryList[i].categoryId !== goods.categoryId) {
                            $("#category").append("<option value='" + categoryList[i].categoryId + "'>" + categoryList[i].categoryName + "</option>");
                        } else {
                            $("#category").append("<option value='" + categoryList[i].categoryId + "' selected>" + categoryList[i].categoryName + "</option>");
                        }
                    }
                }
            });
            $("#submit").replaceWith('<input type="button" class="btn btn-xl btn-primary" id="submit" value="提交" onclick="updateSubmit(' + goodsId + ')">');
            $("#viewImage").attr("src", goods.goodsImage).css("display", "block");
            $("#viewDetail").attr("src", goods.goodsDetails).css("display", "block");
            $("#status").val(goods.goodsStatus);
        }
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
        layer.msg("请上传正确格式的图片", {time : 1000});
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
        layer.msg("请上传正确格式的图片", {time : 1000});
        detail.val("");
        return false;
    }

    var fileObj = detail[0].files[0];
    $("#viewDetail").attr("src", URL.createObjectURL(fileObj)).css("display", "block");
    $("#detailNotice").attr("class", "hiddenNotice")
}

/**
 * 商品管理--修改上传
 */
function updateSubmit(goodsId) {
    var goodsName = $("#name").val();
    var goodsDescribe = $("#describe").val();
    var categoryId = $("#category").val();
    var goodsStock = $("#stock").val();
    var goodsPrice = $("#price").val();
    var goodsStatus = $("#status").val();
    var image = $("#uploadImage");
    var detail = $("#uploadDetail");

    if (goodsName === undefined || goodsName.trim() === "") {
        layer.msg("商品名称不能为空", {time : 1000});
        return false;
    } else if (goodsDescribe === undefined || goodsDescribe.trim() === "") {
        layer.msg("商品描述不能为空", {time : 1000});
        return false;
    } else if (categoryId === "none") {
        layer.msg("商品类别不能为空", {time : 1000});
        return false;
    } else if (goodsStock === undefined) {
        layer.msg("商品库存不能为空", {time : 1000});
        return false;
    } else if (goodsPrice === undefined) {
        layer.msg("商品价格不能为空", {time : 1000});
        return false;
    }

    var goods = {
        "goodsId": goodsId,
        "goodsName": goodsName,
        "goodsDescribe": goodsDescribe,
        "categoryId": categoryId,
        "goodsStock": goodsStock,
        "goodsPrice": goodsPrice,
        "goodsStatus": goodsStatus
    };

    var formData = new FormData();
    if (image.val() !== "") {
        formData.append("imageFile", image[0].files[0]);
    } else if (detail.val() !== "") {
        formData.append("detailFile", detail[0].files[0]);
    }
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
                layer.msg(data.msg, {time: 800}, function () {
                    $(window).attr("location", "goodsmanage.html")
                });
            } else {
                layer.msg(data.msg);
            }
        },
        error: function () {
            layer.msg("商品修改失败", {time: 1000});
        }
    });
}

