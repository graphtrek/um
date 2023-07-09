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
                            user.roles,
                            user.status
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
                            { title: 'status' }
                        ]
                    });

                    table.on('click', 'tbody tr', function (e) {
                        let data = table.row(this).data();
                        console.log('You clicked on ' + data[0] + "'s row");

                        var rowData = {
                            id:  data[0],
                            name:  data[1],
                            email:  data[2],
                            roles: data[3]
                        }

                        let entries = Object.entries(rowData)
                        entries.map( ([key, val] = entry) => {
                            console.log("key:", key, "value:", val);
                            let elementKey = "#" + key;
                            if ( $( elementKey ).length ) {
                                let type = $(elementKey).prop('nodeName');
                                console.log("type:", type);
                                switch (type) {
                                    default:
                                        $(elementKey).val(val);
                                        break;

                                    case 'radio':
                                    case 'checkbox': {
                                    }
                                        break;

                                    case 'select-multiple': {
                                    }
                                        break;

                                    case 'select':
                                    case 'select-one':
                                        break;

                                    case 'date':
                                        break;
                                }

                            }

                        });

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

        function convertFormToJSON(form) {
            const array = $(form).serializeArray(); // Encodes the set of form elements as an array of names and values.
            const json = {};
            $.each(array, function () {
                if(this.name !== "_csrf")
                    json[this.name] = this.value || "";
            });
            return json;
        }

        $("#userForm").on("submit", function (e) {
            const user = convertFormToJSON(this);
            $("#submitButton").attr("disabled", true);
            $("#submitButtonLoading").removeAttr("hidden");
            console.log("User:", user);


            e.preventDefault();
            $("#formSection").hide();
            $("#tableSection").show();

        });


    });
});