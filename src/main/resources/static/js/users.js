$(function () {
    "use strict";
    $(document).ready(function(){
        console.log('users.js loaded');
        refreshToken();
        const token = localStorage.getItem("token");
        let table;
        let rowId;
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
                    const users = JSON.parse(response);
                    console.log("HTTP 200 OK users:", users);
                    let data = [];
                    for (let i=0; i<users.length; i++) {
                        let user = users[i];
                        data.push([
                            user.id,
                            user.name,
                            user.email,
                            user.status,
                            user.mfaType,
                            user.phone
                            //user.roles
                        ]);
                    }
                    table = $("#usersTable").DataTable({
                        fixedHeader: false,
                        scrollY: '50vh',
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
                            { title: 'status' },
                            { title: 'mfa' },
                            { title: 'phone' }
                            //{ title: 'roles' }
                        ]
                    });

                    table.on('click', 'tbody tr', function (e) {

                        let row = table.row( this );
                        rowId = row.index();
                        let data = table.row(this).data();
                        console.log('You clicked on ' + data[0] + "'s row");

                        let rowData = {
                            id:  data[0],
                            name:  data[1],
                            email:  data[2],
                            status: data[3],
                            mfaType: data[4],
                            phone: data[5]
                            // roles: data[4]
                        }


                        $("#tableSection").hide();
                        $("#formSection").show();
                        $("#submitButton").attr("disabled", false);
                        $("#submitButtonLoading").attr("hidden","hidden");
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

        $("#userForm").on("submit", function (e) {
            const user = convertFormToJSON(this);
            $("#submitButton").attr("disabled", true);
            $("#submitButtonLoading").removeAttr("hidden");
            console.log("User:", user);

            let newData = [user.id ,user.name, user.email, user.status] //Array, data here must match structure of table data
            table.row(rowId).data( newData ).draw();

            e.preventDefault();
            $("#formSection").hide();
            $("#tableSection").show();

        });


    });
});