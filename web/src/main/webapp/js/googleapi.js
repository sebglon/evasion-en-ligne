'use strict';

/*
 * Module d'authentification à Google API.
 */
var googleapi = angular.module('evasionVisiteurApp.googleapi', []);
googleapi.provider('GoogleApi', function() {

    // Create a special object for config fields that are required and missing.
    // If any config items still contain it when Token is used, raise an error.
    var REQUIRED_AND_MISSING = {};

    var config = {
        clientId: REQUIRED_AND_MISSING,
        apiKey: REQUIRED_AND_MISSING,
        hdDomain: '',
        cookiepolicy: '',
        scopes: []
    };

    this.extendConfig = function(configExtension) {
        config = angular.extend(config, configExtension);
    };

    this.$get = function($timeout, $rootScope, $q) {
        var requiredAndMissing = [];
        angular.forEach(config, function(value, key) {
            if (value === REQUIRED_AND_MISSING) {
                requiredAndMissing.push(key);
            }
        });

        if (requiredAndMissing.length) {
            throw new Error("GoogleApiProvider is insufficiently configured.  Please " +
                    "configure the following options using " +
                    "GoogleApiProvider.extendConfig: " + requiredAndMissing.join(", "))
        }

        if (!config.clientId) {
            throw new Error("clientId needs to be configured using GoogleApiProvider.");
        }

        var deferred = $q.defer();

        var getParams = function(immediate) {

            return {
                clientid: config.clientId,
                client_id: config.clientId,
                        immediate: immediate,
                cookiepolicy: config.cookiepolicy,
                scope: config.scopes.join(" ")//,
                        // hd: config.hdDomain
            };
        };

        var handleAuthResult = function(authResult) {
            if (authResult && !authResult.error) {
                var data = {};
                data.authResult = authResult;
                gapi.auth.setToken(authResult);
                // Charger les bibliothèques OAuth2 pour activer les méthodes userinfo.
                gapi.client.load('oauth2', 'v2', function() {
                    var request = gapi.client.oauth2.userinfo.get();
                    request.execute(function(obj) {
                        $rootScope.$apply(function() {
                            data.upn = obj;
                        });
                        
                    });
                });
                deferred.resolve(data);
            } else {
                deferred.reject('error');
            }
            $rootScope.googleAuth = data;

        };

        return {
            /**
             * Authorize Google Cloud Storage API.
             */
            checkAuth: function(immediate) {
                gapi.auth.authorize(
                        getParams(immediate)
                        , handleAuthResult);
                return deferred.promise;
            },
            clientLoadPlus: function() {
                gapi.client.setApiKey(config.apiKey);
                var param = getParams(false);
                param.callback=handleAuthResult;
                gapi.client.load('plus', 'v1', function() {
                    gapi.signin.render('googleSignIn',param) ;
                }); 
            },
            clientLoadOAuth2: function() {
                gapi.client.setApiKey(config.apiKey);
                return this.checkAuth(true);
            }
        }
    };
});


googleapi.config(function(GoogleApiProvider) {
    GoogleApiProvider.extendConfig({
        cookiepolicy: 'single_host_origin',
        scopes: ["https://www.googleapis.com/auth/plus.login","https://www.googleapis.com/auth/plus.me"]
    });
});

googleapi.directive('gsignin', ['$rootScope', 'GoogleApi', function($rootScope, GoogleApi) {
        return {
            restrict: 'A',
            scope: true,
            template: '<a>Google SignIn</a>',
            replace: true,
            controller: function($scope, GoogleApi, $document) {
               // GoogleApi.clientLoad();
                
            },
            link: function(scope, element, attrs) {
                element.bind("click", function() {
                    GoogleApi.checkAuth(false);
                    return true;
                });
            }
        };
    }
]);