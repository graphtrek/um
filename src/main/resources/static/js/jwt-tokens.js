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
                    let tokens = JSON.parse(response);
                    console.log("HTTP 200 OK tokens:", tokens);
                    let data = [];
                    for (let i=0; i<tokens.length; i++) {
                        const token = tokens[i];
                        let issuedAtUtcTime = new Date(token.issuedAtUtcTime);
                        let expiresAtUtcTime = new Date(token.expiresAtUtcTime);
                        data.push([
                            token.subject,
                            token.userName,
                            token.scope,
                            issuedAtUtcTime.toLocaleDateString() + ' ' + issuedAtUtcTime.toLocaleTimeString(),
                            expiresAtUtcTime.toLocaleDateString() + ' ' + expiresAtUtcTime.toLocaleTimeString(),
                            token.ip
                        ]);
                    }
                    table = $("#tokensTable").DataTable({
                        fixedHeader: false,
                        scrollY: '50vh',
                        data: data,
                        columnDefs: [
                            {
                                target: 0,
                                visible: true,
                                searchable: true
                            },
                        ],
                        columns: [
                            { title: 'Subject' },
                            { title: 'User' },
                            { title: 'Scope' },
                            { title: 'Issued' },
                            { title: 'Expires' },
                            { title: 'IP' }
                        ]
                    });

                },
                401: function() {
                    $("#errorMessage").append("HTTP 401 UnAuthenticated");
                }
            },
            success: function (response) {
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