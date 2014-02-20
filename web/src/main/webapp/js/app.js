'use strict';

// Declare app level module which depends on filters, and services
var evasionVisiteurApp = angular.module('evasionVisiteurApp', ['ui.tinymce', 'ngRoute', 'evasionVisiteurApp.controllers', 'ngSanitize', 'ngCookies', 'evasionVisiteurApp.googleapi', 'evasionVisiteurApp.directives']);
var $routeProviderReference;
evasionVisiteurApp.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $locationProvider.hashPrefix = '!';
        $routeProvider.when('/test', {templateUrl: 'partials/basic-page.html', controller: function() {
                this.test = 'xxxxx';
            }});
        $routeProvider.otherwise({redirectTo: '/'});
        $routeProviderReference = $routeProvider;
    }]);

angular.injector(['ng', 'ngRoute', 'ngCookies']).invoke(function($rootScope) {
    evasionVisiteurApp.run(function($rootScope, api, $route, $location, $cookies) {
        console.log('run app');
        if ($cookies.eelid != undefined) {
            api.user.token(true).then(function(data) {
                var auth = {token: data}
                $rootScope.auth = auth;
                if (data !== undefined) {
                    api.user.info().then(function(data) {
                        $rootScope.auth.user = data;
                    });
                }
            }, function() {
                delete $cookies['eelid'];
            });
        }
        // Récupération du subdomain
        $rootScope.subdomain = $location.host().split('.').shift();
        api.site.bySubdomain($rootScope.subdomain).then(function(response) {
            $rootScope.site = response;
            function createRoute(views) {
                angular.forEach(views, function(view) {
                    var routeConfig = {template: undefined, templateUrl: undefined, view: view, controller: function() {
                            console.log('test ' + angular.toJson($route.current.view));
                            $rootScope.view = $route.current.view;
                        }};
                    if (view.contents !== undefined && view.contents !== null) {
                        switch (view.contents.type) {
                            case 'STATIC':
                                routeConfig.template = "<ng-include src=\"'staticContent_TPL.html'\"></ng-include>";
                                break;
                            case 'TEMPLATE':
                                routeConfig.template = view.contents.value;
                                break;
                            case 'TEMPLATE_URL':
                                routeConfig.templateUrl = view.contents.value;
                                break;
                        }
                    }
                    $routeProviderReference.when(view.url, routeConfig);
                    if (view.views !== undefined) {
                        createRoute(view.views);
                    }
                }
                );
            }
            ;
            createRoute(response.views);
            $route.reload();
        });
        $rootScope.$location = $location;
    });
}
);

angular.injector(['ng', 'ngRoute']).invoke(function($rootScope) {
    $rootScope.$on('$routeChangeStart', function($event, currentRoute, previousRoute) {
        $scope.isViewLoading = true;
    });
    $rootScope.$on('$routeChangeSuccess', function($event, currentRoute, previousRoute) {
        $scope.isViewLoading = false;
    });
    $rootScope.$on('$routeChangeError', function($event, currentRoute, previousRoute, error) {
        $scope.isViewLoading = false;
    });
}
);