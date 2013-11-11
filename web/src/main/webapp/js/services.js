'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('evasionVisiteurApp.services', ['restangular']).
        value('version', '0.1')
        .provider('api', function() {
            var serverUrl = "";
            this.setServerUrl = function(url) {
                serverUrl = url;
            };
            this.$get = ['$http', '$window', '$q', function($http, $window, $q) {
                    var userAuth = function(callback) {
                        var popup = window.open(serverUrl + "/ws/user/auth?redirect=" + window.location.href, 'name', 'height=600,width=450');
                        if (window.focus) {
                            popup.focus();
                        }
                        angular.element($window).bind('message', function(event) {
                            if (event.source == popup && event.origin == window.location.origin) {
                                callback(event.data);
                            }
                        });
                    };

                    var userToken = function() {
                        $http({
                            method: 'GET',
                            url: serverUrl + '/ws/user/token' + window.location.search
                        }).
                                success(function(data, status, headers, config) {
                                    window.opener.postMessage(data, "*");
                                    window.close();
                                }).
                                error(function(data, status, headers, config) {
                                    window.opener.postMessage(data, "*");
                                    window.close();
                                });
                    };

                    var userLogout = function() {
                        var deferred = $q.defer();
                        $http({
                            method: 'GET',
                            url: serverUrl + '/ws/user/logout'
                        }).
                                success(function(data) {
                                    deferred.resolve(data);
                                }).
                                error(function() {
                                    deferred.reject("error");
                                });
                        return deferred.promise;
                    };

                    var userInfo = function() {
                        var deferred = $q.defer();
                        $http({
                            method: 'GET',
                            url: serverUrl + '/ws/user/info'
                        }).
                                success(function(data) {
                                    deferred.resolve(data);
                                }).
                                error(function() {
                                    deferred.reject("error");
                                });
                        return deferred.promise;
                    };

                    return {
                        getServerUrl: serverUrl,
                        user: {
                            auth: userAuth,
                            token: userToken,
                            logout: userLogout,
                            info: userInfo
                        }
                    };

                }];
        });

