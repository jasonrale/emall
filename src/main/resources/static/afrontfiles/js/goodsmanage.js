$(window).ready(function () {
    adminInfo();

    queryGoods();

    addGoods();


});

/**
 * 商品管理--查询
 */
function queryGoods() {
    $.ajax({
        type : "GET",
        url : "/goods/allGoods",
        data : "currentNo=1&pageSize=10",
        dataType: "json",
        success : function (data) {
            var goodslist = data.obj.list;

            for (var i = 0; i < goodslist.size(); i++) {
                var tbody = $("#tableBody").find("tr");
                var element = "<tr>\n" +
                    "    <td>" + goodslist[i].gId + "</td>\n" +
                    "    <td>" + goodslist[i].gDescribe + "</td>\n" +
                    "    <td>" + goodslist[i].gPrice + "</td>\n" +
                    "    <td>" + goodslist[i].gStock + "</td>\n" +
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
