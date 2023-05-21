$(function () {
    "use strict";
    $(document).ready(function(){

        console.log('register.js loaded');

        var password = document.getElementById("registrationFormPassword")
            , confirm_password = document.getElementById("registrationFormRepeatPassword");

        function validatePassword(){
            if(password.value != confirm_password.value) {
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

            $.ajax({
                type: "POST",
                dataType : "text",
                contentType: "application/json; charset=utf-8",
                url: "/api/registerUser",
                data: JSON.stringify(user),
                statusCode: {
                    200: function(response) {
                        console.log("200 OK response:", response);
                    },
                    400: function() {
                        alert("User Already Exists");
                    },
                    401: function() {
                        alert("UnAuthenticated");
                    }
                },
                success: function (response) {
                    consloe.log("Success response:", response);
                },
                error: function(response) {
                    alert("Error response:" + response);
                }
            });
            e.preventDefault();
        });

    });
});
