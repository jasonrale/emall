$(document).ready(function () {
    $.ajax({
        type: "get",
        url: "/user/getCurrentUser",
        success: function (data) {
            if (data.status === true) {
                $("#login").css("display", "none");
                $("#welcome").css("display", "inline");
                $("#username").css("display", "inline").html(data.obj.uname);
                $("#logout").css("display", "inline");
            }
        }
    });
});
