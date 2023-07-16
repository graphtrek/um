$(function () {
    "use strict";
    $(document).ready(function(){

        console.log('forgot-password.js loaded');

        $( "#forgotPasswordForm" ).on("submit", function (e) {
            const email = $("#email").val();
            $("#submitButton").attr("disabled", true);
            $("#submitButtonLoading").removeAttr("hidden");
            $.ajax({
                type: "POST",
                dataType : "text",
                contentType: "application/json; charset=utf-8",
                url: "/api/forgotPassword",
                data: email,
                statusCode: {
                    200: function(passwordResetToken) {
                        console.log("200 OK response:", passwordResetToken);
                        localStorage.setItem("passwordResetToken", passwordResetToken);
                    },
                    400: function() {
                        $("#errorMessage").append("HTTP 400 User Not Found");
                    },
                    401: function() {
                        $("#errorMessage").append("HTTP 401 UnAuthenticated");
                        localStorage.removeItem("passwordResetToken");
                        localStorage.removeItem("token");
                    },
                    500: function() {
                        $("#errorMessage").append("HTTP 500 application error");
                        localStorage.removeItem("passwordResetToken");
                    }

                },
                success: function (response) {
                    console.log("Success response:", response);
                    $("#errorMessage").empty();
                    $("#errorMessage").hide();
                    $("#successMessage").show();
                    localStorage.removeItem("passwordResetToken");
                    $("#submitButtonLoading").attr("hidden","hidden");
                },
                error: function(response) {
                    console.log("Error response:", response);
                    $("#submitButton").attr("disabled", false);
                    $("#errorMessage").empty();
                    $("#errorMessage").show();
                    $("#successMessage").hide();
                    localStorage.removeItem("passwordResetToken");
                    $("#submitButtonLoading").attr("hidden","hidden");
                }
            });
            e.preventDefault();
        });

        $("#resendEmail").on("click", function (e) {
            $("#submitButton").attr("disabled", true);
            $("#submitButtonLoading").removeAttr("hidden");
            $.ajax({
                type: "GET",
                dataType : "text",
                contentType: "application/json; charset=utf-8",
                url: "/api/resendPasswordResetToken",
                data: {
                    token: localStorage.getItem("passwordResetToken")
                },
                statusCode: {
                    200: function(passwordResetToken) {
                        console.log("200 OK response:", passwordResetToken);
                        localStorage.setItem("passwordResetToken", passwordResetToken);
                        //$("#resendEmail").attr("href","/api/resendToken?token=" + response);
                        //location.href = "/login";
                    },
                    400: function() {
                        $("#errorMessage").append("HTTP 400 User Not Found");
                    },
                    401: function() {
                        $("#errorMessage").append("HTTP 401 UnAuthenticated");
                        localStorage.removeItem("passwordResetToken");
                    },
                    500: function() {
                        $("#errorMessage").append("HTTP 500 application error");
                        localStorage.removeItem("passwordResetToken");
                    }

                },
                success: function (response) {
                    console.log("Success response:", response);
                    $("#errorMessage").empty();
                    $("#errorMessage").hide();
                    $("#successMessage").show();
                    localStorage.removeItem("passwordResetToken");
                    $("#submitButtonLoading").attr("hidden","hidden");
                },
                error: function(response) {
                    console.log("Error response:", response);
                    $("#submitButton").attr("disabled", false);
                    $("#errorMessage").empty();
                    $("#errorMessage").show();
                    $("#successMessage").hide();
                    localStorage.removeItem("passwordResetToken");
                    $("#submitButtonLoading").attr("hidden","hidden");
                }
            });
            e.preventDefault();
        });

    });
});