console.log('login.js loaded');

$( "#loginFormButton" ).on( "click", function() {
    const username = $("#loginFormEmail").val();
    const password = $("#loginFormPassword").val();
    $.ajax({
        url: "/token",
        method: "POST",
        headers: {
            "Authorization": "Basic " + btoa(username+":"+password)
        },
        statusCode: {
            200: function(token) {
                console.log("The username or password were correct granted token:", token);
                localStorage.setItem("token", token);
                location.href = "/users";
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