'use strict';

// Declare app level module which depends on filters, and services
var evasionVisiteurApp = angular.module('evasionVisiteurApp', ['restangular','evasionVisiteurApp.controllers']);

evasionVisiteurApp.config(['$routeProvider', 'RestangularProvider', function($routeProvider, RestangularProvider) {
        // $routeProvider.when('/', {templateUrl: 'partials/basic-page.html'});
        //$routeProvider.when('/booktravel', {templateUrl: 'partials/booktravel.html'});
        $routeProvider.otherwise({redirectTo: '/'});
        RestangularProvider.setBaseUrl("default-data");
    }]);

angular.injector(['ng', 'restangular']).invoke(function(Restangular, $rootScope) {
    evasionVisiteurApp.run(function($rootScope, Restangular, $route, $location) {
        Restangular.one('site.json').get().then(function(response) {
            $rootScope.site = response;
            console.log(response);
            function createRoute(views) {
                angular.forEach(views, function(view) {
                    var route = { controller: function($rootScope) {
                            console.log('test ' + angular.toJson(view.blocs));
                            $rootScope.view = view;
                        }
                    };
                    if (view.content !== undefined) {
                        route.template = "<ng:include src=\"'staticContent_TPL.html'\">";
                    } else if (view.template !== undefined) {
                        route.template = view.content;
                    } else {
                        route.templateUrl = view.templateUrl;
                    }
                    $route.routes[view.url] = route;
                    if (view.views !== undefined) {
                        createRoute(view.views);
                    }
                });
            }
            ;
            createRoute(response.views);
            $route.reload();
        });
        $rootScope.$location = $location;
    });
}
);

angular.injector(['ng']).invoke(function($rootScope) {
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
