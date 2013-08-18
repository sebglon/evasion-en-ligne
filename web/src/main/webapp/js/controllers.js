'use strict';

angular.module('evasionVisiteurApp.controllers', []).
        controller('SiteCtrl', ['$scope', 'Site', '$location', '$route', function($scope, Site, $location, $route) {
        Site.get().then(function(response) {
            $scope.site = response;
            console.log(response);
            function createRoute(views) {
                angular.forEach(views, function(view) {
                    $route.routes['/' + view.url] = {templateUrl: 'partials/basic-page.html', controller: function($rootScope) {
                            console.log('test ' + angular.toJson(view.blocs));
                            $rootScope.view = view;
                        }
                    };
                    if (view.views !== undefined) {
                        createRoute(view.views);
                    }
                });
            }
            ;
            createRoute(response.views);
        });
        $scope.$location = $location;
    }]);

