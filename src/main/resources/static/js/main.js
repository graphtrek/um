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
      pageLoaded();
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

    function convertFormToJSON(form) {
      const array = $(form).serializeArray(); // Encodes the set of form elements as an array of names and values.
      const json = {};
      $.each(array, function () {
        if(this.name !== "_csrf")
          json[this.name] = this.value || "";
      });
      return json;
    }

    function pageLoaded() {
      setNav();
      /**
       * Easy selector helper function
       */
      const select = (el, all = false) => {
        el = el.trim()
        if (all) {
          return [...document.querySelectorAll(el)]
        } else {
          return document.querySelector(el)
        }
      }

      /**
       * Easy event listener function
       */
      const on = (type, el, listener, all = false) => {
        if (all) {
          select(el, all).forEach(e => e.addEventListener(type, listener))
        } else {
          select(el, all).addEventListener(type, listener)
        }
      }
      /**
       * Mobile nav toggle
       */
      on('click', '.mobile-nav-toggle', function(e) {
        select('#navbar').classList.toggle('navbar-mobile')
        this.classList.toggle('bi-list')
        this.classList.toggle('bi-x')
      })

      /**
       * Mobile nav dropdowns activate
       */
      on('click', '.navbar .dropdown > a', function(e) {
        if (select('#navbar').classList.contains('navbar-mobile')) {
          e.preventDefault()
          this.nextElementSibling.classList.toggle('dropdown-active')
        }
      }, true)

      $("#logout").on("click", function (event) {
        localStorage.removeItem("token");
        location.href = "/";
        event.preventDefault();
      });
    }

  });
})();