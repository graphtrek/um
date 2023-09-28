$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('profile.js loaded');
        refreshToken();
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
                    const jwtToken = parseJwt(token);
                    console.log("token:", jwtToken);
                    if(jwtToken.scope !== "ROLE_ADMIN") {
                        $("#authenticationTypeSelector").removeClass("hidden");
                        $('#userForm ul.dropdown-menu li[code]').removeClass('active');
                        const $active_li = $('#authenticationTypeDropDown').find('li[code="'+ profileData.mfaType +'"]');
                        const selText = $active_li.find('a:first').html();
                        const selCode = $active_li.attr("code");
                        $('#authenticationTypeButton').attr("code",selCode);
                        $('#authenticationTypeButton').find('span:first').html(selText);
                    }

                    $("#id").val(profileData.id);
                    $("#email").val(profileData.email);
                    $("#name").val(profileData.name);
                    $("#phone").val(profileData.phone);
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
                $("#successMessage").hide();
            },
            error: function(response) {
                console.log("Error response:", response);
                $("#submitButton").attr("disabled", false);
                $("#errorMessage").empty();
                $("#errorMessage").show();
                $("#successMessage").hide();
            }
        });

        $('#authenticationTypeDropDown li').click(function(event) {
            dropdownSelector(this);
        });

        $("#userForm").on("submit", function (e) {
            refreshToken();
            const token = localStorage.getItem("token");

            const user = convertFormToJSON(this);
            user.mfaType = $('#authenticationTypeButton').attr("code");
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
                        $('#successMessage').fadeOut(2000);
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