$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('jwt-tokens.js loaded');
        refreshToken();
        const token = localStorage.getItem("token");
        let table;
        let rowId;
        $.ajax({
            url: "/api/jwt-tokens",
            dataType : "text",
            contentType: "application/json; charset=utf-8",
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            statusCode: {
                200: function(response) {
                    console.log("HTTP 200 OK response:", response);
                    var tokens = JSON.parse(response);
                    var data = [];
                    for (var i=0; i<tokens.length; i++) {
                        const tokens = tokens[i];
                        data.push([
                            user.id,
                            user.name,
                            user.email,
                            user.roles,
                            user.status
                        ]);
                    }
                    table = $("#tokensTable").DataTable({
                        fixedHeader: true,
                        data: data,
                        columnDefs: [
                            {
                                target: 0,
                                visible: false,
                                searchable: false
                            },
                        ],
                        columns: [
                            { title: 'id' },
                            { title: 'name' },
                            { title: 'email' },
                            { title: 'roles' },
                            { title: 'status' }
                        ]
                    });

                },
                401: function() {
                    $("#errorMessage").append("HTTP 401 UnAuthenticated");
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
});