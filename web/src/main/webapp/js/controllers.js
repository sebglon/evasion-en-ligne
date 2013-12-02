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
        .controller('EditStaticContent', ['$scope', '$rootScope', 'api', function($scope, $rootScope, api) {

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
                    this.description = angular.copy($rootScope.view.description);
                    this.onEditContent = true;
                }
                this.update = function() {
                    var i;
                    // Mise à jour du site;
                    for (i=0; $rootScope.site.views.length;i++) {
                        if ($rootScope.site.views[i].key === $rootScope.view.key) {
                            $rootScope.site.views[i].title = angular.copy(this.title);
                            $rootScope.site.views[i].content = angular.copy(this.content);
                            $rootScope.site.views[i].description = angular.copy(this.description);
                            $rootScope.view = $rootScope.site.views[i];
                            break;
                        }
                    }
                    // persistence 
                    api.site.update($rootScope.site);
                    this.onEditContent = false;
                };
                this.reset = function() {
                    this.onEditContent = false;
                };
                this.isUnchanged = function() {
                    return angular.equals(this.content, $rootScope.view.content) 
                            && angular.equals(this.title, $rootScope.view.title) 
                            && angular.equals(this.description, $rootScope.view.description);
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