$(function () {
    "use strict";
    $(document).ready(function() {
        console.log('activity-log-report.js loaded');
        refreshToken();
        const token = localStorage.getItem("token");

        let pivotData;
        let now = new Date();

        let startOfMonth = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
        let endOfMonth   = moment().endOf('month').format('YYYY-MM-DD');

        let defaultStart = moment().subtract(2, 'months').startOf('month').format('YYYY-MM-DD');
        let defaultEnd   = moment().endOf('month').format('YYYY-MM-DD');

        let derivers = $.pivotUtilities.derivers;
        var renderers = {};
        $.extend(renderers, $.pivotUtilities.renderers, $.pivotUtilities.plotly_renderers);
//        let renderers = $.extend(
//            $.pivotUtilities.renderers,
//            $.pivotUtilities.plotly_renderers,
//            $.pivotUtilities.export_renderers);
        let tpl = $.pivotUtilities.aggregatorTemplates;

        let defaultConfig = {
	    	resolution:'daily',
            from: defaultStart,
            to: defaultEnd,
            type: 'timesheet'
        };

        let reportDefaultConfigTimesheet = {
            renderers: renderers,
            //cols: ['partnerCategory'],
            rows: ['activityCode','appId'],
            aggregators: {
                "Count":      function() { return tpl.count()() },
                "Sum of elapsed":      function() { return tpl.sum()(["elapsed"]) }
            },
            rendererName: 'Bar Chart',
            //rendererName: "TSV Export",
            aggregatorName: 'Count',
            hiddenFromDragDrop: ['id','timeStamp','eventId'],
            hiddenFromAggregators: ['id'],
            vals: ['eventId'],
            //aggregator: Sum(intFormat)(["hours"]),
            //attributeArray: "hours",
            rowOrder: "value_a_to_z", colOrder: "value_z_to_a"
        };

        defaultConfig.config = reportDefaultConfigTimesheet;
        let currentConfig = JSON.parse(JSON.stringify(defaultConfig));

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
                        activityLog.date = timeStamp.toLocaleDateString();
                        data.push([
                            timeStamp.toLocaleDateString(),
                            activityLog.clientId,
                            activityLog.appId,
                            activityLog.activityCode,
                            activityLog.category,
                            activityLog.elapsed,
                            activityLog.textParams,
                            activityLog.eventId

                        ]);
                    }
                    $('#reportSection').pivotUI(activityLogs, reportDefaultConfigTimesheet, true);
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