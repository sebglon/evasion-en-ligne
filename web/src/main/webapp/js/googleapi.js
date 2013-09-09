'use strict';

function google_sign_in_render() {
    angular.element(document).ready(function() {
        angular.element(document).scope().gSignInInit();
    });
}

/*
 * Module d'authentification à Google API.
 */
var googleapi = angular.module('evasionVisiteurApp.googleapi', []);
googleapi.provider('GoogleConfig', function() {
    var apiKey;
    this.setApiKey = function(s) {
        apiKey = s;
    };

    var scopes;
    this.setScopes = function(s) {
        scopes = s;
    };

    var clientId;
    this.setClientId = function(s) {
        clientId = s;
    };

    var handleAuthResult;
    this.setHandleAuthResult = function(s) {
        handleAuthResult = s;
    }
    this.$get = function() {
            var googlesdk = {
                apiKey: apiKey,
                client_id: clientId+'.apps.googleusercontent.com',
                scope: scopes,
                immediate: true,
            };
            return {
                googlesdk: googlesdk,
                handleResult: handleAuthResult
            };
        };
});
googleapi.run(function($rootScope, GoogleConfig) {
    $rootScope.googleConfig = GoogleConfig;
    $rootScope.gSignInInit = function() {
        console.log($rootScope.googleConfig.googlesdk);
        gapi.client.setApiKey($rootScope.googleConfig.googlesdk.apiKey);
        $rootScope.googleConfig.googlesdk.immediate = true;
        gapi.auth.authorize($rootScope.googleConfig.googlesdk, $rootScope.googleConfig.handleResult);

    };
    var js, id = 'google-jssdk', ref = document.getElementsByTagName('script')[0];
    if (document.getElementById(id)) {
        return;
    }
    js = document.createElement('script');
    js.id = id;
    js.async = true;
    js.src = "https://apis.google.com/js/client.js?onload=google_sign_in_render";
    ref.parentNode.insertBefore(js, ref);
});

googleapi.directive('gsignin', ['GoogleConfig', function(GoogleConfig) {
        return {
            restrict: 'A',
            scope: true,
            template: '<a>Google SignIn</a>',
            replace: true,
            link: function(scope, element, attrs, controller) {
                element.bind("click", function() {
                    GoogleConfig.googlesdk.immediate=false;
                    gapi.auth.authorize(GoogleConfig.googlesdk, GoogleConfig.handleAuthResult);
                });
            }
        };
    }
]);
