    console.log('profile.js loaded');
    function checkToken(){
        const JWT = localStorage.getItem("token");
        const jwtPayload = JSON.parse(window.atob(JWT.split('.')[1]))
        const datetime= new Date(jwtPayload.exp*1000);
        //console.log("expiry time (epoch):",jwtPayload.exp * 1000);
        //console.log("expiry time (ISO):", datetime.toISOString());
        console.log("expires in:", Math.round((datetime - Date.now())/1000) + " sec");
        const isExpired = Date.now() >= jwtPayload.exp * 1000;
        console.log("token is expired:",isExpired);
        return isExpired;
    }
    function initNavbar() {
        const isExpired = checkToken();
        console.log("JWT isExpired:", isExpired);
        if(isExpired) {
            const refreshToken = localStorage.getItem("refreshToken");
            $.ajax({
                url: "/api/refreshToken",
                method: "POST",
                dataType: "text",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify({
                    refreshToken: refreshToken
                }),
                statusCode: {
                    200: function (response) {
                        let responseData = JSON.parse(response);
                        console.log("response:", responseData);
                        if (responseData.accessToken && responseData.refreshToken) {
                            localStorage.setItem("token", responseData.accessToken);
                            localStorage.setItem("refreshToken", responseData.refreshToken);
                        } else {
                            console.log("call ok but no token:", responseData.qrCode);

                        }
                    },
                    400: function () {
                        $("#errorMessage").append("HTTP 400 refreshToken not correct. Login again.");
                        localStorage.removeItem("token");
                        localStorage.removeItem("refreshToken");
                    },
                    401: function () {
                        $("#errorMessage").append("HTTP 401 refreshToken not correct. Login again.");
                        localStorage.removeItem("token");
                        localStorage.removeItem("refreshToken");
                    }
                },
                success: function (response) {
                    console.log("Success response:", response);
                    $("#errorMessage").empty();
                    $("#errorMessage").hide();
                },
                error: function (response) {
                    console.log("Error response:", response);
                    $("#errorMessage").empty();
                    $("#errorMessage").show();
                }
            });
        }

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