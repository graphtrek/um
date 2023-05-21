(function() {
  "use strict";
  console.log('main .js loaded');
  $(document).ready(function(){

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

    setNav();

  });
})();