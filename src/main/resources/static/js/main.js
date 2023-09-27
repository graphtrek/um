function convertFormToJSON(form) {
  //let disabled = form.find(':input:disabled');
  //if(disabled)
  //  disabled.removeAttr('disabled');
  const array = $(form).serializeArray(); // Encodes the set of form elements as an array of names and values.
  //if(disabled)
  //  disabled.attr('disabled','disabled');
  const json = {};
  $.each(array, function () {
    if(this.name !== "_csrf")
      json[this.name] = this.value || "";
  });
  return json;
}

function parseJwt (token) {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
}

function checkToken(){
  let isExpired = true;
  try {
    const JWT = localStorage.getItem("token");
    if(JWT) {
      const jwtPayload = JSON.parse(window.atob(JWT.split('.')[1]))
      const datetime = new Date(jwtPayload.exp * 1000);
      //console.log("expiry time (epoch):",jwtPayload.exp * 1000);
      //console.log("expiry time (ISO):", datetime.toISOString());
      console.log("expires in:", Math.round((datetime - Date.now()) / 1000) + " sec");
      isExpired = Date.now() >= jwtPayload.exp * 1000;
    }
    console.log("token is expired:",isExpired);
  } catch (e) {
    console.warn("Can not check token is expired:",isExpired, " error",e);
  }
  return isExpired;
}

function refreshToken() {
  const isExpired = checkToken();
  console.log("JWT isExpired:", isExpired);
  const refreshToken = localStorage.getItem("refreshToken");
  if(isExpired && refreshToken) {
    $.ajax({
      url: "/api/refreshToken",
      method: "POST",
      dataType: "text",
      async: false,
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify({
        refreshToken: refreshToken
      }),
      statusCode: {
        200: function (response) {
          let responseData = JSON.parse(response);
          console.log("response:", responseData);
          if (responseData.accessToken && responseData.refreshToken) {
            localStorage.setItem("token", responseData.accessToken);
            localStorage.setItem("refreshToken", responseData.refreshToken);
            if(typeof initNavbar === 'function')
              initNavbar();

          } else {
            console.log("call ok but no token:", responseData.qrCode);

          }
        },
        400: function () {
          $("#errorMessage").append("HTTP 400 refreshToken not correct. Login again.");
          localStorage.removeItem("token");
          localStorage.removeItem("refreshToken");
        },
        401: function () {
          $("#errorMessage").append("HTTP 401 refreshToken not correct. Login again.");
          localStorage.removeItem("token");
          localStorage.removeItem("refreshToken");
        }
      },
      success: function (response) {
        console.log("Success response:", response);
        $("#errorMessage").empty();
        $("#errorMessage").hide();
      },
      error: function (response) {
        console.log("Error response:", response);
        $("#errorMessage").empty();
        $("#errorMessage").show();
      }
    });
  }
}

(function() {
  "use strict";
  console.log('main.js loaded');
  $(document).ready(function(){

    refreshToken();
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

      if(typeof initNavbar === 'function')
        initNavbar();

      const token = localStorage.getItem("token");
      $("#logout").on("click", function (event) {
        $.ajax({
          url: "/api/logout",
          method: "POST",
          headers: {
            "Authorization": "Bearer " + token
          },
          statusCode: {
            200: function (response) {
              console.log("200 OK response:", response);
              localStorage.removeItem("token");
              localStorage.removeItem("refreshToken");
              location.href = "/";
            },
            400: function () {
              $("#errorMessage").append("HTTP 400 User Already Exists");
            },
            401: function () {
              $("#errorMessage").append("HTTP 401 UnAuthenticated");
            },
            500: function () {
              $("#errorMessage").append("HTTP 500 application error");
            }

          },
          success: function (response) {
            console.log("Success response:", response);
            $("#errorMessage").empty();
            $("#errorMessage").hide();
            $("#successMessage").show();
          },
          error: function (response) {
            console.log("Error response:", response);
            $("#submitButton").attr("disabled", false);
            $("#errorMessage").empty();
            $("#errorMessage").show();
            $("#successMessage").hide();
          }
        });
        event.preventDefault();
      });
    }

  });
})();