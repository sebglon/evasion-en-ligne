'use strict';

// Declare app level module which depends on filters, and services
var evasionVisiteurApp = angular.module('evasionVisiteurApp', ['ui.tinymce','ngRoute', 'evasionVisiteurApp.controllers','ngSanitize']);
var $routeProviderReference;
evasionVisiteurApp.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true);
        $locationProvider.hashPrefix = '';
        $routeProvider.when('/test', {templateUrl: 'partials/basic-page.html', controller:function(){this.test='xxxxx';}});
        $routeProvider.otherwise({redirectTo: '/'});
        $routeProviderReference = $routeProvider;
    }]);

angular.injector(['ng', 'ngRoute']).invoke(function($rootScope) {
    evasionVisiteurApp.run(function($rootScope, api, $route, $location) {
        console.log('run app');

        // Récupération du subdomain
        $rootScope.subdomain = $location.host().split('.').shift();
        api.site.bySubdomain($rootScope.subdomain).then(function(response) {
            $rootScope.site = response;
            function createRoute(views) {
                angular.forEach(views, function(view) {
                    var routeConfig = {template: undefined, templateUrl: undefined,view:view, controller:function() {
                        console.log('test ' + angular.toJson($route.current.view));
                            $rootScope.view = $route.current.view;
                    }};

                    if (view.content !== undefined) {
                        routeConfig.template = "<ng-include src=\"'staticContent_TPL.html'\"></ng-include>";
                    } else if (view.template !== undefined) {
                        routeConfig.template = view.content;
                    } else {
                        routeConfig.templateUrl = view.templateUrl;
                    }
                    $routeProviderReference.when(view.url, routeConfig);
                    if (view.views !== undefined) {
                        createRoute(view.views);
                    }
                });
            };
            createRoute(response.views);
            $route.reload();
        });
        $rootScope.$location = $location;
    });
}
);

angular.injector(['ng','ngRoute']).invoke(function($rootScope) {
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