$(document).ready(function () {
    adminInfo();

    categoryList();

    $(document).keyup(function (event) {
        if (event.keyCode === 13) {
            addSubmit();
        }
    });
});

/**
 * 获取商品类别列表
 */
function categoryList() {
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
    var goodsActivity = $("#activity").val();
    var image = $("#uploadImage");
    var detail = $("#uploadDetail");

    if (goodsName === undefined || goodsName.trim() === "") {
        layer.msg("商品名称不能为空");
        return false;
    } else if (goodsDescribe === undefined || goodsDescribe.trim() === "") {
        layer.msg("商品描述不能为空");
        return false;
    } else if (categoryId === "none") {
        layer.msg("商品类别不能为空");
        return false;
    } else if (goodsStock === undefined) {
        layer.msg("商品库存不能为空");
        return false;
    } else if (goodsPrice === undefined) {
        layer.msg("商品价格不能为空");
        return false;
    } else if (image.val() === "") {
        layer.msg("商品图片不能为空");
        return false;
    } else if (detail.val() === "") {
        layer.msg("商品详情图片不能为空");
        return false;
    }

    var goods = {
        "goodsName": goodsName,
        "goodsDescribe": goodsDescribe,
        "categoryId": categoryId,
        "goodsStock": goodsStock,
        "goodsPrice": goodsPrice,
        "goodsActivity": goodsActivity,
        "goodsStatus": 0
    };

    var formData = new FormData();
    formData.append("imageFile", image[0].files[0]);
    formData.append("detailFile", detail[0].files[0]);
    formData.append("goods", JSON.stringify(goods));

    showLoading();
    $.ajax({
        type: "PUT",
        url: "/goods",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            if (data.status === true) {
                layer.msg("商品添加成功", {time: 800}, function () {
                    $(window).attr("location", "goodsManage.html");
                });

            } else {
                layer.msg(data.msg);
            }
        },
        error: function () {
            layer.msg("商品添加失败");
        }
    });
}