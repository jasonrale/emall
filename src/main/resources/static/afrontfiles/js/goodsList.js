$(document).ready(function () {
    $("#price").click(function () {
        $(window).attr("location","/emall/goods/sort?sort=price");
        $("#price").attr("class", "sort-item active");
        $("#default").attr("class", "sort-item");
    });

    $("#default").click(function () {
        $(window).attr("location","/emall/goods/sort?sort=default");
        $("#price").attr("class", "sort-item");
        $("#default").attr("class", "sort-item active");
    });
});