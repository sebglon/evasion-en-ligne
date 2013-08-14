'use strict';

// Declare app level module which depends on filters, and services
var evasionVisiteurApp = angular.module('evasionVisiteurApp', ['evasionVisiteurApp.services','evasionVisiteurApp.siteServices', 'evasionVisiteurApp.controllers']);

evasionVisiteurApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/', {templateUrl: 'partials/basic-page.html'});
        $routeProvider.when('/booktravel', {templateUrl: 'partials/booktravel.html'});
        $routeProvider.otherwise({redirectTo: '/'});
    }]);

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
