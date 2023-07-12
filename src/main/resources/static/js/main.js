(function() {
  "use strict";
  console.log('main.js loaded');
  $(document).ready(function(){


    const token = localStorage.getItem("token");
    if(token) {
      $.ajax({
        url: "/navbar",
        method: "GET",
        headers: {
          "Authorization": "Bearer " + token
        },
        success: function (response) {
          console.log("Success response:", response);
          $("#navbar").html(response);
          pageLoaded();
        },
        error: function(response) {
          console.log("Error response:", response);
          localStorage.removeItem("token");
          pageLoaded();
        }
      });
    } else {
      setNav();
    }

    function setNav() {
      $('#navbar li a').removeClass('active');

      var path = location.pathname;
      path = path.replace(/\/$/, "");
      path = decodeURIComponent(path).trim();
      console.log('path: ', path);
      if (path.length < 2) {
        $("#navbar li a").filter(":first").addClass('active');
      } else {
        $("#navbar li a").each(function () {
          var href = $(this).attr('href');
          if (href.length > 1 && path.indexOf(href) !== -1) {
            $(this).addClass('active');
          }
        });
      }
    }

    function pageLoaded() {
      setNav();

      $("#logout").on("click", function (event) {
        localStorage.removeItem("token");
        location.href = "/";
        event.preventDefault();
      });
    }

  });
})();