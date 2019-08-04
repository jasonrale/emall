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
 * 商品管理--添加上传
 */
function addSubmit() {
    var goods = {
        "gName": $("name").val(),
        "gDescribe": $("describe").val(),
        "cId": $("category").val(),
        "gStock": $("stock").val(),
        "gPrice": $("price").val(),
        "gStatus": 1
    };

    var fileObj = $("#uploadImage").file[0]; // js 获取文件对象
    var formData = new FormData();
    formData.append("file", fileObj); //加入文件对象

    var data = {
        "goods": goods,
        "fileData": formData
    };

    $.ajax({
        type: "PUT",
        url: "",
        data: data,
        contentType: 'application/json;charset=UTF-8',
        success: function () {
            console.log("success");
        },
        error: function () {
            console.log("err");
        }
    });
}
