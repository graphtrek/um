$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('profile.js loaded');

        function convertFormToJSON(form) {
            const array = $(form).serializeArray(); // Encodes the set of form elements as an array of names and values.
            const json = {};
            $.each(array, function () {
                if(this.name !== "_csrf")
                    json[this.name] = this.value || "";
            });
            return json;
        }

        const token = localStorage.getItem("token");

        $.ajax({
            url: "/api/profile",
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            statusCode: {
                200: function(profileData) {
                    console.log("200 OK response:", profileData);
                    $("#id").val(profileData.id);
                    $("#email").val(profileData.email);
                    $("#name").val(profileData.name);
                    $("#roles").val(profileData.roles);
                },
                400: function() {
                    $("#errorMessage").append("HTTP 400 User Already Exists");
                },
                401: function() {
                    $("#errorMessage").append("HTTP 401 UnAuthenticated");
                },
                500: function() {
                    $("#errorMessage").append("HTTP 500 application error");
                }

            },
            success: function (response) {
                console.log("Success response:", response);
                $("#errorMessage").empty();
                $("#errorMessage").hide();
                $("#successMessage").show();
            },
            error: function(response) {
                console.log("Error response:", response);
                $("#submitButton").attr("disabled", false);
                $("#errorMessage").empty();
                $("#errorMessage").show();
                $("#successMessage").hide();
            }
        });

        $("#userForm").on("submit", function (e) {
            const user = convertFormToJSON(this);
            $("#submitButton").attr("disabled", true);
            $("#submitButtonLoading").removeAttr("hidden");
            $.ajax({
                url: "/api/saveProfile",
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + token
                },
                dataType : "text",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(user),
                statusCode: {
                    200: function(user) {
                        console.log("200 OK response:", user);

                        //$("#resendEmail").attr("href","/api/resendToken?token=" + response);
                        //location.href = "/login";
                    },
                    400: function() {
                        $("#errorMessage").append("HTTP 400 User Already Exists");
                    },
                    401: function() {
                        $("#errorMessage").append("HTTP 401 UnAuthenticated");
                    },
                    500: function() {
                        $("#errorMessage").append("HTTP 500 application error");
                    }
                },
                success: function (response) {
                    console.log("Success response:", response);
                    $("#submitButton").attr("disabled", false);
                    $("#errorMessage").empty();
                    $("#errorMessage").hide();
                    $("#successMessage").show();
                    $("#submitButtonLoading").attr("hidden","hidden");
                },
                error: function(response) {
                    console.log("Error response:", response);
                    $("#submitButton").attr("disabled", false);
                    $("#errorMessage").empty();
                    $("#errorMessage").show();
                    $("#successMessage").hide();
                    $("#submitButtonLoading").attr("hidden","hidden");
                }
            });
            e.preventDefault();
        });

    });
});