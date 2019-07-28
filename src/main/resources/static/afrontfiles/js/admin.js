$(window).ready(function () {
    //商品管理跳转
    $("#goods").click(function () {
        $("#goods").attr("class", "active");
        $("#category").attr("class", "");
        $("#order").attr("class", "");
        $("#page-wrapper").empty();
        $.ajax({
            type : "post",
            url : "/emall/goods/skip",
            data : "pageSkip=1&type=goodsManage&key=0",
            dataType: "html",
            success : function (data) {
                $("#page-wrapper").html(data);
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