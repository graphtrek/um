$(function () {
    "use strict";
    $(document).ready(function() {
        console.log('list-beans.js loaded');
        refreshToken();
        const token = localStorage.getItem("token");
        let table;
        $.ajax({
            url: "/api/list-beans",
            dataType : "text",
            contentType: "application/json; charset=utf-8",
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token
            },
            statusCode: {
                200: function(response) {
                    let beans = JSON.parse(response);
                    console.log("HTTP 200 OK activityLogs:", beans);
                    let data = [];
                    for (let i= 0; i < beans.length; i++) {
                        const bean = beans[i];
                        let start = new Date(bean.start);
                        let end = new Date(bean.end);

                        data.push([
                            bean.elapsed,
                            start.toLocaleDateString() + ' ' + start.toLocaleTimeString([], {
                                hourCycle: 'h23',
                                hour: '2-digit',
                                minute: '2-digit',
                                second: "2-digit"
                            }),
                            end.toLocaleDateString() + ' ' + end.toLocaleTimeString([], {
                                hourCycle: 'h23',
                                hour: '2-digit',
                                minute: '2-digit',
                                second: "2-digit"
                            }),
                            bean.beanName
                        ]);
                    }
                    table = $("#beansTable").DataTable({
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
                            { title: 'Initialization time (ms)' },
                            { title: 'Start' },
                            { title: 'End' },
                            { title: 'BeanName' }
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