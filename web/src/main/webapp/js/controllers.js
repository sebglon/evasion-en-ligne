'use strict';

angular.module('evasionVisiteurApp.controllers', []).
        controller('SiteCtrl', ['$scope', 'Site', '$location', '$route', function($scope, Site, $location, $route) {
        Site.get().then(function(response) {
            $scope.site = response;
            console.log(response);
            angular.forEach(response.views, function(view) {
                $route.routes['/' + view.url] = {templateUrl: 'partials/basic-page.html', controller: function($rootScope) {
                        console.log('test '+ angular.toJson(view.blocs));
                        $rootScope.view = view;
                    }
                };
            });
        });
        $scope.$location = $location;
    }]);

