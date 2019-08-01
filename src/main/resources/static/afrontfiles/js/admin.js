$(window).ready(function () {
    adminInfo();

    //商品管理跳转
    $("#goods").click(function () {
        $("#goods").attr("class", "active");
        $("#category").attr("class", "");
        $("#order").attr("class", "");
        $("#page-wrapper").empty();
        $.ajax({
            type : "post",
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
    });

    //商品管理-上一页
    $("#lastPage").click(function () {
        var pageNow = $("#pageNow").val();
        var type = $("#type").val();
        var key = $("#key").val();
        if (pageNow > 1) {
            $("#page-wrapper").empty();
            $.ajax({
                type : "post",
                url : "/emall/goods/lastPage",
                data : "pageNow=" + pageNow + "&type=" + type + "&key=" + key,
                dataType: "html",
                success : function (data) {
                    $("#page-wrapper").html(data);
                }
            });
        }
    });

    //商品管理-下一页
    $("#nextPage").click(function () {
        var pageNow = $("#pageNow").val();
        var pageCount = $("#pageCount").val();
        var type = $("#type").val();
        var key = $("#key").val();
        if (pageNow < pageCount) {
            $("#page-wrapper").empty();
            $.ajax({
                type : "post",
                url : "/emall/goods/nextPage",
                data : "pageNow=" + pageNow + "&pageCount=" + pageCount + "&type=" + type + "&key=" + key,
                dataType: "html",
                success : function (data) {
                    $("#page-wrapper").html(data);
                }
            });
        }
    });

    //商品管理-跳转
    $("#get").click(function () {
        var pageNow = $("#pageNow").val();
        var type = $("#type").val();
        var key = $("#key").val();
        $("#page-wrapper").empty();
        $.ajax({
            type : "post",
            url : "/emall/goods/skip",
            data : "pageSkip=" + pageNow + "&type=" + type + "&key=" + key,
            dataType: "html",
            success : function (data) {
                $("#page-wrapper").html(data);
            }
        });
    });

    //商品管理-查询
    $("#sbtn").click(function () {
        var select = $("#select").val();
        var keyWord = $("#keyWord").val();
        if (keyWord !== "" && /^[0-9]+$/.test(keyWord)) {
            if (select === "goodId") {
                $.ajax({
                    type : "post",
                    url : "/emall/goods/skip",
                    data : "pageSkip=1&type=goodId&key=" + keyWord,
                    dataType: "html",
                    success : function (data) {
                        $("#page-wrapper").html(data);
                    }
                });
            } else if (select === "goodName") {
                $.ajax({
                    type : "post",
                    url : "/emall/goods/skip",
                    data : "pageSkip=1&type=goodName&key=" + keyWord,
                    dataType: "html",
                    success : function (data) {
                        $("#page-wrapper").html(data);
                    }
                });
            }
        }
    });

    //品类管理跳转
    $("#category").click(function () {
        $("#category").attr("class", "active");
        $("#goods").attr("class", "");
        $("#order").attr("class", "");
        $("#page-wrapper").load("categorymanage.jsp");
        return false;
    });

    //订单管理跳转
    $("#order").click(function () {
        $("#order").attr("class", "active");
        $("#category").attr("class", "");
        $("#goods").attr("class", "");
        $("#page-wrapper").load("ordermanage.jsp");
        return false;
    });

});

/**
 * 获取管理员信息
 */
function adminInfo() {
    $.ajax({
        type: "GET",
        url: "/admin/adminInfo",
        success: function (data) {
            if (data.status === true) {
                $("#adminName").html(data.obj.uname);
            }
        }
    });
}