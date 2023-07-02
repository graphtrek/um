$(function () {
    "use strict";
    $(document).ready(function(){

        console.log('register.js loaded');

        var password = document.getElementById("registrationFormPassword")
            , confirm_password = document.getElementById("registrationFormRepeatPassword");

        function validatePassword(){
            if(password.value !== confirm_password.value) {
                confirm_password.setCustomValidity("Passwords Don't Match");
            } else {
                confirm_password.setCustomValidity('');
            }
        }

        password.onchange = validatePassword;
        confirm_password.onkeyup = validatePassword;

        // $("#registerUserForm").on("submit", function (e) {
        //
        //     var dataString = $(this).serialize();
        //
        //     $.ajax({
        //         type: "POST",
        //         url: "/api/registerUserForm",
        //         data: dataString,
        //         success: function () {
        //            alert("Success");
        //         },
        //         error: function() {
        //             alert("Error");
        //         }
        //     });
        //     e.preventDefault();
        // });

        function convertFormToJSON(form) {
            const array = $(form).serializeArray(); // Encodes the set of form elements as an array of names and values.
            const json = {};
            $.each(array, function () {
                if(this.name !== "_csrf")
                    json[this.name] = this.value || "";
            });
            return json;
        }

        $("#registerUserForm").on("submit", function (e) {

            const user = convertFormToJSON(this);
            $("#submitButton").attr("disabled", true);
            $.ajax({
                type: "POST",
                dataType : "text",
                contentType: "application/json; charset=utf-8",
                url: "/api/registerUser",
                data: JSON.stringify(user),
                statusCode: {
                    200: function(response) {
                        console.log("200 OK response:", response);

                        $("#successMessage").append("Check your emails. " +
                            '<a href="/api/resendToken?token='+response+'" class="link-danger">Resend email</a></p>');
                        //location.href = "/login";
                    },
                    400: function() {
                        $("#errorMessage").append("HTTP 400 User Already Exists");
                    },
                    401: function() {
                        $("#errorMessage").append("HTTP 400 UnAuthenticated");
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
                    $("#successMessage").empty();
                    $("#successMessage").show();
                },
                error: function(response) {
                    console.log("Error response:", response);
                    $("#submitButton").attr("disabled", false);
                    $("#errorMessage").empty();
                    $("#errorMessage").show();
                    $("#successMessage").empty();
                    $("#successMessage").hide();
                }
            });
            e.preventDefault();
        });

    });
});
