$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('users.js loaded');

        const token = localStorage.getItem("token");
        console.log("token:",token);
        $.ajax({
            url: "/api/users",
            dataType : "text",
            contentType: "application/json; charset=utf-8",
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            statusCode: {
                200: function(response) {
                    console.log("HTTP 200 OK response:", response);
                    var users = JSON.parse(response);
                    var data = [];
                    for (var i=0; i<users.length; i++) {
                        const user = users[i];
                        data.push([
                            user.id,
                            user.name,
                            user.email,
                            user.roles
                        ]);
                    }
                    let table = $("#usersTable").DataTable({
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
                        ]
                    });

                    table.on('click', 'tbody tr', function (e) {
                        let data = table.row(this).data();
                        console.log('You clicked on ' + data[0] + "'s row");

                        $("#tableSection").hide();
                        $("#formSection").show();
                    });

                    $("#userFormButton").on( "click", function(e) {
                        $("#formSection").hide();
                        $("#tableSection").show();
                        e.preventDefault();
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