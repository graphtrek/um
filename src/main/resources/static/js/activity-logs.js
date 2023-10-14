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
                            timeStamp.toLocaleDateString() + ' ' + timeStamp.toLocaleTimeString([], {
                                hourCycle: 'h23',
                                hour: '2-digit',
                                minute: '2-digit',
                                second: "2-digit"
                            }),
                            activityLog.clientId,
                            activityLog.text,
                            activityLog.appId,
                            activityLog.activityCode,
                            activityLog.category,
                            activityLog.elapsed,
                            activityLog.resultCode,
                            activityLog.textParams,
                            activityLog.eventId

                        ]);
                    }
                    table = $("#activityLogsTable").DataTable({
                        fixedHeader: true,
                        order: [[0, 'desc']],
                        lengthMenu: [10, 25, 50, 100, 500, 1000, 5000],
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
                            { title: 'timeStamp' },
                            { title: 'clientId' },
                            { title: 'text'},
                            { title: 'appId' },
                            { title: 'activityCode' },
                            { title: 'category' },
                            { title: 'elapsed' },
                            { title: 'resultCode'},
                            { title: 'textParams' },
                            { title: 'eventId' }
                        ],
                        dom: 'lifrtpB',
                        buttons: [
                            {
                                extend: 'collection',
                                className: 'custom-html-collection',
                                buttons: [
                                    '<h3>Export</h3>',
                                    'pdf',
                                    'csv',
                                    'excel',
                                    '<h3 class="not-top-heading">Column Visibility</h3>',
                                    'columnsToggle'
                                ]
                            }
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