$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('profile.js loaded');

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

    });
});