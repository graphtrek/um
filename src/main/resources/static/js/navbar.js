console.log('profile.js loaded');

function initNavbar() {

    const token = localStorage.getItem("token");
    $("#logout").on("click", function (event) {
        $.ajax({
            url: "/api/logout",
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token
            },
            statusCode: {
                200: function (response) {
                    console.log("200 OK response:", response);
                    localStorage.removeItem("token");
                    location.href = "/";
                },
                400: function () {
                    $("#errorMessage").append("HTTP 400 User Already Exists");
                },
                401: function () {
                    $("#errorMessage").append("HTTP 401 UnAuthenticated");
                    localStorage.removeItem("token");
                },
                500: function () {
                    $("#errorMessage").append("HTTP 500 application error");
                }

            },
            success: function (response) {
                console.log("Success response:", response);
                $("#errorMessage").empty();
                $("#errorMessage").hide();
                $("#successMessage").show();
            },
            error: function (response) {
                console.log("Error response:", response);
                $("#submitButton").attr("disabled", false);
                $("#errorMessage").empty();
                $("#errorMessage").show();
                $("#successMessage").hide();
            }
        });
        event.preventDefault();
    });

}