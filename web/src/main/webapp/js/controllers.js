'use strict';

angular.module('evasionVisiteurApp.controllers', ['ui.bootstrap', 'evasionVisiteurApp.services'])
        .config(['apiProvider', function(apiProvider) {
                apiProvider.setServerUrl('http://localhost:8080');
            }])
        .controller('EditStaticContent', ['$scope', function($scope) {
                $scope.tinymceOptions = {
                    handle_event_callback: function(e) {
                        // put logic here for keypress
                    }
                };

                $scope.edit = function() {
                    $scope.title = angular.copy($scope.view.title);
                    $scope.content = angular.copy($scope.view.content);
                    $scope.onEditContent = true;
                }
                $scope.update = function(content, title) {
                    $scope.view.title = angular.copy(title);
                    $scope.view.content = angular.copy(content);
                    $scope.onEditContent = false;
                };

                $scope.reset = function() {
                    $scope.onEditContent = false;
                };

                $scope.isUnchanged = function(content, title) {
                    return angular.equals(content, $scope.view.content) && angular.equals(title, $scope.view.title);
                };

                $scope.reset();
            }])
        .controller('CallbackCtrl', ['api', function(api) {

                api.user.token();


            }])
        .controller('LoginCtrl', ['$rootScope', '$scope', 'api', function($rootScope, $scope, api) {
                $scope.openLoginForm = function() {
                    api.user.auth(function(data) {
                        $rootScope.auth = data;
                    });
                };

                $scope.closeLoginForm = function() {
                    $scope.showLoginBox = false;
                };

                $scope.optsLoginForm = {
                    backdropFade: true,
                    dialogFade: true
                };

            }])
        .controller('AppCtrl', ['$scope', '$document', '$location', '$http', function($scope, $document, $location, $http) {
                console.log('init app Ctrl');

                $scope.init = function() {
                    $scope.loggin = false;
                    $document.ready(function() {

                    });
                };
            }]);