'use strict';
angular.module('evasionVisiteurApp.controllers', ['ui.bootstrap', 'evasionVisiteurApp.services'])
        .config(['$httpProvider', function($httpProvider) {
                $httpProvider.defaults.withCredentials = true;
                $httpProvider.defaults.useXDomain = true;
                delete $httpProvider.defaults.headers.common['X-Requested-With'];
            }])
        .config(['apiProvider', function(apiProvider) {
                apiProvider.setServerUrl('http://evasion-en-ligne.fr:8080');
            }])
        .controller('EditStaticContent', ['$scope', '$rootScope', function($scope, $rootScope) {

                this.tinymceOptions = {
                    plugins: [
                        "advlist autolink lists link image charmap print preview anchor",
                        "searchreplace visualblocks code fullscreen",
                        "insertdatetime media table contextmenu paste"
                    ],
                    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
                    browser_spellcheck: true, object_resizing: true, menubar: false, content_css: "/css/app.css, /css/lib/bootstrap/bootstrap.css",
                    handle_event_callback: function(e) {
                        // put logic here for keypress
                    }
                };
                this.edit = function() {
                    this.title = angular.copy($rootScope.view.title);
                    this.content = angular.copy($rootScope.view.content);
                    this.onEditContent = true;
                }
                this.update = function(content, title) {
                    $rootScope.view.title = angular.copy(title);
                    $rootScope.view.content = angular.copy(content);
                    this.onEditContent = false;
                };
                this.reset = function() {
                    this.onEditContent = false;
                };
                this.isUnchanged = function(content, title) {
                    return angular.equals(content, $scope.view.content) && angular.equals(title, $scope.view.title);
                };
                this.reset();
            }])
        .controller('CallbackCtrl', ['api', function(api) {

                api.user.token();
            }])
        .controller('LoginCtrl', ['$rootScope', '$scope', 'api', function($rootScope, $scope, api) {
                $scope.loginForm = function() {
                    api.user.auth(function(data) {
                        $rootScope.auth = {token: data};
                        api.user.info().then(function(data) {
                            $rootScope.auth.user = data;
                        });
                    });
                };
                $scope.logoutForm = function() {
                    api.user.logout().then(function() {
                        $rootScope.auth = undefined;
                    });
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