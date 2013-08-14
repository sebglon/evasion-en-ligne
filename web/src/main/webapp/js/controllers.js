'use strict';

angular.module('evasionVisiteurApp.controllers', []).
  controller('SiteCtrl', ['$scope','Site', function($scope, Site) {
    $scope.site = Site.query();
  }]);

