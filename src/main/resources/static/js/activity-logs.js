$(function () {
    "use strict";
    $(document).ready(function() {
        console.log('activity-logs.js loaded');
        refreshToken();
        const token = localStorage.getItem("token");
        let table;
        $.ajax({
            url: "/api/listUserActivity",
            dataType : "text",
            contentType: "application/json; charset=utf-8",
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            statusCode: {
                200: function(response) {
                    let activityLogs = JSON.parse(response);
                    console.log("HTTP 200 OK activityLogs:", activityLogs);
                    let data = [];
                    for (let i= 0; i < activityLogs.length; i++) {
                        const activityLog = activityLogs[i];
                        let timeStamp = new Date(activityLog.timeStamp);
                        data.push([
                            activityLog.eventId,
                            activityLog.clientId,
                            activityLog.appId,
                            activityLog.category,
                            activityLog.textParams,
                            activityLog.activityCode,
                            timeStamp.toLocaleDateString() + ' ' + timeStamp.toLocaleTimeString()
                        ]);
                    }
                    table = $("#activityLogsTable").DataTable({
                        fixedHeader: true,
                        scrollX: true,
                        autoWidth: true,
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
                            { title: 'eventId' },
                            { title: 'clientId' },
                            { title: 'appId' },
                            { title: 'category' },
                            { title: 'textParams' },
                            { title: 'activityCode' },
                            { title: 'timeStamp' }
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