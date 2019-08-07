$(document).ready(function () {
    adminInfo();

    queryAll();

    addGoods();
});

/**
 * 商品管理--分页
 */
function queryAll() {
    $.ajax({
        type : "GET",
        url : "/goods/allGoods",
        data : "currentNo=1&pageSize=10",
        dataType: "json",
        success : function (data) {
            var goodslist = data.obj.list;

            for (var i = 0; i < goodslist.length; i++) {
                var tbody = $("#tableBody").find("tr");
                var element = "<tr>\n" +
                    "    <td>" + goodslist[i].goodsId + "</td>\n" +
                    "    <td>" + goodslist[i].goodsDescribe + "</td>\n" +
                    "    <td>" + goodslist[i].goodsPrice + "</td>\n" +
                    "    <td>" + goodslist[i].goodsStock + "</td>\n" +
                    '    <td><a class="btn btn-xs btn-warning opear">下架</a></td>\n' +
                    "    <td>\n" +
                    '        <a class="opear" href="">编辑</a>\n' +
                    "    </td>\n" +
                    "</tr>";
                tbody.after(element);
            }
        }
    });
}

/**
 * 商品管理--添加
 */
function addGoods() {
    $("#addGoods").click(function () {
        $("#query").css("display", "none");
        $("#add").css("display", "block");
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
    var imageFile = $("#uploadImage")[0].file[0];
    var detailFile = $("#uploadDetail")[0].file[0];
    var formData = new FormData();

    formData.append("goodsName", $("name").val());
    formData.append("goodsDescribe", $("describe").val());
    formData.append("categoryId", $("category").val());
    formData.append("goodsStock", $("stock").val());
    formData.append("goodsPrice", $("price").val());
    formData.append("imageFile", imageFile);
    formData.append("detailFile", detailFile);
    formData.append("goodsStatus", "1");

    $.ajax({
        type: "PUT",
        url: "",
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            console.log("success");
        },
        error: function () {
            console.log("err");
        }
    });
}
