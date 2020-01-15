$(document).ready(function () {
    adminInfo();

    adminQueryAll(1, 10);

    turn();

    insert();
});

/**
 * 商品类别--分页
 */
function adminQueryAll(currentNo, pageSize) {
    var pageModel = {
        "currentNo": currentNo,
        "pageSize": pageSize,
    };

    $.ajax({
        type: "GET",
        url: "/emall/category/admin",
        data: pageModel,
        success: function (data) {
            var categoryList = data.obj.list;

            if (categoryList.length !== 0) {
                var tbody = $("#tableBody");
                tbody.empty();
                for (var i = 0; i < categoryList.length; i++) {
                    var element = "<tr>" +
                        "<td>" + categoryList[i].categoryId + "</td>" +
                        "<td>" + categoryList[i].categoryName + "</td>" +
                        "<td>" +
                        '<a class="opera" onclick="update(' + "'" + categoryList[i].categoryId + "'" + ')">修改名称</a>' +
                        '<a class="opera" onclick="del(' + "'" + categoryList[i].categoryId + "'" + ')">删除品类</a>' +
                        "</td>" +
                        "</tr>";
                    tbody.append(element);
                }

                var totalPages = data.obj.totalPages;
                $("#currentNo").val(currentNo);
                $("#totalPages").html(totalPages);

                var nextPage = currentNo + 1;
                var lastPage = currentNo - 1;
                if (currentNo === 1) {
                    $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link'></a>");
                    $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link' onclick='adminQueryAll(" + nextPage + ", 10)'></a>");
                } else if (currentNo === totalPages) {
                    $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link'></a>");
                    $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link' onclick='adminQueryAll(" + lastPage + ", 10)'></a>");
                } else {
                    $("#lastPage").replaceWith("<a id='lastPage' class='rc-pagination-item-link' onclick='adminQueryAll(" + lastPage + ", 10)'></a>");
                    $("#nextPage").replaceWith("<a id='nextPage' class='rc-pagination-item-link' onclick='adminQueryAll(" + nextPage + ", 10)'></a>");
                }
                $("#pageControl").css("display", "block");
            } else {
                $("#pageControl").css("display", "none");
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
        var currentNo = parseInt($("#currentNo").val());
        isNaN(currentNo) ? layer.msg("页码不能为空", {time : 1000}) : adminQueryAll(currentNo, 10);
    });
}

/**
 * 添加商品类别
 */
function insert() {
    $("#insert").click(function () {
        layer.open({
            id: 1,
            type: 1,
            title: '添加品类',
            skin: 'layui-layer-rim',
            area: ['400px', '160px', 'center'],
            btnAlign: 'c',
            content: '<div class="row" style="width: 320px; margin-left:35px; margin-top:15px;">'
                + '<div class="col-sm-12">'
                + '<div class="input-group">'
                + '<span class="input-group-addon">商品类别:</span>'
                + '<input id="add" type="text" class="form-control" placeholder="请输入名称">'
                + '</div>'
                + '</div>'
                + '</div>',
            btn: ['确定', '取消'],
            btn1: function (index) {
                var categoryName = $("#add").val();
                if (categoryName === null || categoryName === "") {
                    layer.msg("品类名称不能为空", {time : 1000});
                } else {
                    $.ajax({
                        type: "PUT",
                        url: "/emall/category/admin",
                        data: {"categoryName": categoryName},
                        success: function (data) {
                            if (data.status === true) {
                                layer.msg(data.msg, {time: 800}, function () {
                                    layer.close(index);
                                    window.location.reload();
                                });
                            } else {
                                layer.msg(data.msg, {time : 1000});
                            }
                        }
                    });
                }
            },
            btn2: function (index) {
                layer.close(index);
            }
        });
    });
}

/**
 * 修改品类名称
 */
function update(categoryId) {
    layer.open({
        id: 1,
        type: 1,
        title: '修改名称',
        skin: 'layui-layer-rim',
        area: ['400px', '160px', 'center'],
        btnAlign: 'c',
        content: '<div class="row" style="width: 320px; margin-left:35px; margin-top:15px;">'
            + '<div class="col-sm-12">'
            + '<div class="input-group">'
            + '<span class="input-group-addon">商品类别:</span>'
            + '<input id="add" type="text" class="form-control" placeholder="请输入名称">'
            + '</div>'
            + '</div>'
            + '</div>',
        btn: ['确定', '取消'],
        btn1: function (index) {
            var categoryName = $("#add").val();
            if (categoryName === undefined || categoryName.trim() === "") {
                layer.msg("品类名称不能为空", {time : 1000});
            } else {
                var category = {"categoryId": categoryId, "categoryName": categoryName};
                $.ajax({
                    type: "POST",
                    url: "/emall/category/admin",
                    data: JSON.stringify(category),
                    contentType: 'application/json;charset=UTF-8',
                    success: function (data) {
                        if (data.status === true) {
                            layer.msg(data.msg, {time: 800}, function () {
                                layer.close(index);
                                window.location.reload();
                            });
                        } else {
                            layer.msg(data.msg, {time : 1000});
                        }
                    }
                });
            }
        },
        btn2: function (index) {
            layer.close(index);
        }
    });
}

/**
 * 删除品类
 */
function del(categoryId) {
    layer.confirm(
        "您确定要删除该商品类别？",
        {btn: ["确定", "取消"]},
        function (index) {

            $.ajax({
                type: "DELETE",
                url: "/emall/category/admin",
                data: categoryId,
                contentType: 'application/json;charset=UTF-8',
                success: function (data) {
                    if (data.status === true) {
                        layer.msg(data.msg, {time: 800}, function () {
                            layer.close(index);
                            window.location.reload();
                        });
                    } else {
                        layer.msg(data.msg, {time : 1000});
                    }
                }
            });
        },
        function (index) {
            layer.close(index);
        }
    );
}