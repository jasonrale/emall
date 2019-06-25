// $(document).ready(function () {
//     $("#submit").click(function () {
//         var userName = $("#username").val();
//         var password = $("#password").val();
//
//         if (userName === "" || password === "") {
//             $(".err-msg").html("用户名或密码不能为空！");
//             $(".error-item").css("display", "block")
//         } else {
//             var login = {"uName" : userName, "uPassword" : password};
//             $.ajax({
//                 type : "post",
//                 url : "/emall/login/validate",
//                 data : login,
//                 success : function (msg) {
//                     console.log(msg);
//                     if (msg === 0) {
//                         $(".err-msg").html("用户名或密码错误！");
//                         $(".error-item").css("display", "block")
//                     } else if (msg === 1) {
//                         $(window).attr("location","/emall/views/back/index.jsp");
//                     } else if (msg === -1) {
//                         $(window).attr("location","/emall/views/admin/admin.jsp");
//                     }
//                 }
//             });
//         }
//     });
// });

//登录验证
$(document).ready(function () {
    $("#submit").click(function () {
        $("#loginForm").validate({
            submitHandler:function(form){
                form.submit();
            }
        });
    });
});