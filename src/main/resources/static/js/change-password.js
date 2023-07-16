$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('change-password.js loaded');

        var password = document.getElementById("changePasswordFormPassword")
            , confirm_password = document.getElementById("changePasswordFormRepeatPassword");

        function validatePassword(){
            if(password.value !== confirm_password.value) {
                confirm_password.setCustomValidity("Passwords Don't Match");
            } else {
                confirm_password.setCustomValidity('');
            }
        }

        password.onchange = validatePassword;
        confirm_password.onkeyup = validatePassword;

        $("#changePasswordForm").on("submit", function (e) {
            const passwordResetRequest = convertFormToJSON(this);
            $("#submitButton").attr("disabled", true);
            $("#submitButtonLoading").removeAttr("hidden");
            $.ajax({
                type: "POST",
                dataType : "text",
                contentType: "application/json; charset=utf-8",
                url: "/api/resetPassword",
                data: JSON.stringify(passwordResetRequest),
                statusCode: {
                    200: function(response) {
                        console.log("200 OK response:", response);
                        localStorage.removeItem("token");
                        //location.href = "/login";
                    },
                    400: function() {
                        $("#errorMessage").append("Invalid Password Change Token <b><a href='/forgot-password'>Try Again</a></b>");
                    },
                    401: function() {
                        $("#errorMessage").append("HTTP 401 UnAuthenticated");
                        localStorage.removeItem("token");
                    },
                    500: function() {
                        $("#errorMessage").append("HTTP 500 application error");
                        localStorage.removeItem("token");
                    }

                },
                success: function (response) {
                    console.log("Success response:", response);
                    $("#errorMessage").empty();
                    $("#errorMessage").hide();
                    $("#successMessage").show();
                    localStorage.removeItem("token");
                    $("#submitButtonLoading").attr("hidden","hidden");
                },
                error: function(response) {
                    console.log("Error response:", response);
                    $("#submitButton").attr("disabled", false);
                    $("#errorMessage").empty();
                    $("#errorMessage").show();
                    $("#successMessage").hide();
                    localStorage.removeItem("token");
                    $("#submitButtonLoading").attr("hidden","hidden");
                }
            });
            e.preventDefault();
        });

    });
});