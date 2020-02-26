$(document).ready(function () {
    navInfo();

    showLoading();

    var keyWord = encodeURI(encodeURI(getUrlParam("keyWord")));
    var categoryId = encodeURI(getUrlParam("categoryId"));

    if (keyWord !== "null") {
        listByKey(keyWord, "none", 1, 20);
    } else {
        listByCategory(categoryId, "none", 1, 20);
    }
});

/**
 * 关键字分页查询商品列表显示
 */
function listByKey(keyWord, sort, currentNo, pageSize) {
    var pageModel = {
        "currentNo": currentNo,
        "pageSize": pageSize
    };

    $.ajax({
        type: "GET",
        url: "/emall/goods/" + keyWord + "/keyWord/" + sort + "/sort",
        data: pageModel,
        success: function (data) {
            if (data.status === true) {
                if (sort === "none") {
                    $("#default").replaceWith('<li class="sort-item active" id="default" onclick="listByKey(' + "'" + keyWord + "', " + "'none', " + "1, " + "20" + ')">默认排序</li>');
                    $("#price").replaceWith('<li class="sort-item" id="price" onclick="listByKey(' + "'" + keyWord + "', " + "'asc', " + "1, " + "20" + ')">价格排序</li>');
                } else if (sort === "desc") {
                    $("#default").attr("class", "sort-item");
                    $("#price").replaceWith('<li class="sort-item active" id="price" onclick="listByKey(' + "'" + keyWord + "', " + "'asc', " + "1, " + "20" + ')">价格排序</li>');
                } else {
                    $("#default").attr("class", "sort-item");
                    $("#price").replaceWith('<li class="sort-item active" id="price" onclick="listByKey(' + "'" + keyWord + "', " + "'desc', " + "1, " + "20" + ')">价格排序</li>');
                }

                var goodsList = data.obj.list;
                if (goodsList.length === 0) {
                    $("#none").css("display", "block");
                    layer.closeAll();
                    return false;
                } else {
                    var goodsUl = $("#goodsUl");
                    goodsUl.empty();

                    for (var i = 0; i < goodsList.length; i++) {
                        var element = '<li class="p-item">' +
                            '<div class="p-img-con">' +
                            '<a class="link" href="detail.html?goodsId=' + goodsList[i].goodsId + '">' +
                            '<img class="p-img" src="' + goodsList[i].goodsImage + '" alt="' + goodsList[i].goodsName + '">' +
                            "</a>" +
                            "</div>" +
                            '<div class="p-price-con">' +
                            '<span class="p-price">' + "￥" + goodsList[i].goodsPrice + "</span>" +
                            "</div>" +
                            '<div class="p-name-con">' +
                            '<a class="p-name" href="detail.html?goodsId=' + goodsList[i].goodsId + '">' + goodsList[i].goodsName + "</a>" +
                            "</div>" +
                            "</li>";
                        goodsUl.append(element);
                    }

                    //分页
                    var nextPage = currentNo + 1;
                    var lastPage = currentNo - 1;

                    var totalPages = data.obj.totalPages;
                    keyWord = "'" + keyWord + "'";
                    sort = "'" + sort + "'";
                    $("#totalPages").html(currentNo + "/" + totalPages);
                    if (currentNo === 1 && currentNo === totalPages) {
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item disabled">' + "上一页" + "</span>");
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item disabled">' + "下一页" + "</span>");
                    } else if (currentNo === 1) {
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item disabled">' + "上一页" + "</span>");
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item" ' +
                            'onclick="listByKey(' + keyWord + ", " + sort + ", " + nextPage + ", " + pageSize + ')">下一页</span>');
                    } else if (currentNo === totalPages) {
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item disabled">' + "下一页" + "</span>");
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item" ' +
                            'onclick="listByKey(' + keyWord + ", " + sort + ", " + lastPage + ", " + pageSize + ')">上一页</span>');
                    } else {
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item" ' +
                            'onclick="listByKey(' + keyWord + ", " + sort + ", " + lastPage + ", " + pageSize + ')">上一页</span>');
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item" ' +
                            'onclick="listByKey(' + keyWord + ", " + sort + ", " + nextPage + ", " + pageSize + ')">下一页</span>');
                    }

                    $("#pageControl").css("display", "block");
                }

                layer.closeAll();
            }
        }
    });
}

/**
 * 商品类别分页查询商品列表显示
 */
function listByCategory(categoryId, sort, currentNo, pageSize) {
    var pageModel = {
        "currentNo": currentNo,
        "pageSize": pageSize
    };

    $.ajax({
        type: "GET",
        url: "/emall/goods/" + categoryId + "/categoryId/" + sort + "/sort",
        data: pageModel,
        success: function (data) {
            if (data.status === true) {
                if (sort === "none") {
                    $("#default").replaceWith('<li class="sort-item active" id="default" onclick="listByCategory(' + "'" + categoryId + "', " + "'none', " + "1, " + "20" + ')">默认排序</li>');
                    $("#price").replaceWith('<li class="sort-item" id="price" onclick="listByCategory(' + "'" + categoryId + "', " + "'asc', " + "1, " + "20" + ')">价格排序</li>');
                } else if (sort === "desc") {
                    $("#default").attr("class", "sort-item");
                    $("#price").replaceWith('<li class="sort-item active" id="price" onclick="listByCategory(' + "'" + categoryId + "', " + "'asc', " + "1, " + "20" + ')">价格排序</li>');
                } else {
                    $("#default").attr("class", "sort-item");
                    $("#price").replaceWith('<li class="sort-item active" id="price" onclick="listByCategory(' + "'" + categoryId + "', " + "'desc', " + "1, " + "20" + ')">价格排序</li>');
                }

                var goodsList = data.obj.list;
                if (goodsList.length === 0) {
                    $("#none").css("display", "block");
                    layer.closeAll();
                    return false;
                } else {
                    var goodsUl = $("#goodsUl");
                    goodsUl.empty();

                    for (var i = 0; i < goodsList.length; i++) {
                        var element = '<li class="p-item">' +
                            '<div class="p-img-con">' +
                            '<a class="link" href="detail.html?goodsId=' + goodsList[i].goodsId + '">' +
                            '<img class="p-img" src="' + goodsList[i].goodsImage + '" alt="' + goodsList[i].goodsName + '">' +
                            "</a>" +
                            "</div>" +
                            '<div class="p-price-con">' +
                            '<span class="p-price">' + "￥" + goodsList[i].goodsPrice + "</span>" +
                            "</div>" +
                            '<div class="p-name-con">' +
                            '<a class="p-name" href="detail.html?goodsId=' + goodsList[i].goodsId + '">' + goodsList[i].goodsName + "</a>" +
                            "</div>" +
                            "</li>";
                        goodsUl.append(element);
                    }

                    //分页
                    var nextPage = currentNo + 1;
                    var lastPage = currentNo - 1;

                    var totalPages = data.obj.totalPages;
                    categoryId = "'" + categoryId + "'";
                    sort = "'" + sort + "'";
                    $("#totalPages").html(currentNo + "/" + totalPages);
                    if (currentNo === 1 && currentNo === totalPages) {
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item disabled">' + "上一页" + "</span>");
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item disabled">' + "下一页" + "</span>");
                    } else if (currentNo === 1) {
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item disabled">' + "上一页" + "</span>");
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item" ' +
                            'onclick="listByCategory(' + categoryId + ", " + sort + ", " + nextPage + ", " + pageSize + ')">下一页</span>');
                    } else if (currentNo === totalPages) {
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item disabled">' + "下一页" + "</span>");
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item" ' +
                            'onclick="listByCategory(' + categoryId + ", " + sort + ", " + lastPage + ", " + pageSize + ')">上一页</span>');
                    } else {
                        $("#lastPage").replaceWith('<span id="lastPage" class="pg-item" ' +
                            'onclick="listByCategory(' + categoryId + ", " + sort + ", " + lastPage + ", " + pageSize + ')">上一页</span>');
                        $("#nextPage").replaceWith('<span id="nextPage" class="pg-item" ' +
                            'onclick="listByCategory(' + categoryId + ", " + sort + ", " + nextPage + ", " + pageSize + ')">下一页</span>');
                    }

                    $("#pageControl").css("display", "block");
                }

                layer.closeAll();
            }
        }
    });
}