$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('login.js loaded');

        $( "#loginFormButton" ).on( "click", function() {
            const username = $("#loginFormEmail").val();
            const password = $("#loginFormPassword").val();
            const code = $("#loginFormCode").val();
            $.ajax({
                url: "/api/token",
                method: "POST",
                dataType : "text",
                contentType: "application/json; charset=utf-8",
                headers: {
                    "Authorization": "Basic " + btoa(username+":"+password)
                },
                data: JSON.stringify({
                    code: code
                }),
                statusCode: {
                    200: function(response) {
                        let responseData = JSON.parse(response);
                        console.log("response:", responseData);
                        if(responseData.accessToken) {
                            console.log("The username or password were correct granted token:", responseData.accessToken);
                            localStorage.setItem("token", responseData.accessToken);
                            location.href = "/profile";
                        } else {
                            console.log("The username or password were correct qrCode:", responseData.qrCode);
                            $("#loginFormCode").parent().show();
                            $("#loginImage").attr("src",responseData.qrCode);
                        }
                    },
                    400: function() {
                        $("#errorMessage").append("HTTP 400 Verification code not Correct. Try again.");
                        localStorage.removeItem("token");
                    },
                    401: function() {
                        $("#errorMessage").append("HTTP 401 The username or password were not correct. Try again.");
                        localStorage.removeItem("token");
                    }
                },
                success: function (response) {
                    console.log("Success response:", response);
                    $("#errorMessage").empty();
                    $("#errorMessage").hide();
                },
                error: function(response) {
                    console.log("Error response:", response);
                    $("#errorMessage").empty();
                    $("#errorMessage").show();
                }
            });
        });

    });
});