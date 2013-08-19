'use strict';

angular.module('evasionVisiteurApp.controllers', []).
        controller('SiteCtrl', ['$scope', 'Site', '$location', '$route', function($scope, Site, $location, $route) {
        Site.get().then(function(response) {
            $scope.site = response;
            console.log(response);
            function createRoute(views) {
                angular.forEach(views, function(view) {
                    var route = { controller: function($rootScope) {
                            console.log('test ' + angular.toJson(view.blocs));
                            $rootScope.view = view;
                        }
                    };
                    if (view.content !== undefined) {
                        route.template = view.content;
                    } else if (view.template !== undefined) {
                        route.template = view.content;template;
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
        $scope.$location = $location;
    }]);

