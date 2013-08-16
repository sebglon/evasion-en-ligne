'use strict';

angular.module('evasionVisiteurApp.controllers', []).
  controller('SiteCtrl', ['$scope','Site','$location', function($scope, Site, $location) {
    $scope.site = Site.query();
    $scope.$location = $location;
  }]);

