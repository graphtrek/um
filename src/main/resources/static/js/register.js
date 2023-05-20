$(function () {

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

    $("#registerForm").on("submit", function (e) {

        var dataString = $(this).serialize();

        $.ajax({
            type: "POST",
            url: "/register",
            data: dataString,
            success: function () {
               alert("Success");
            },
            error: function() {
                alert("Error");
            }
        });
        e.preventDefault();
    });

});
