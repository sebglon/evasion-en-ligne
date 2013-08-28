'use strict';

angular.module('evasionVisiteurApp.controllers', []).
        controller('EditStaticContent', ['$scope', function($scope) {

        $scope.edit = function() {
            $scope.content = angular.copy($scope.view.content);
            $scope.onEditContent=true;
        }
        $scope.update = function(content) {
            $scope.view.content = angular.copy(content);
            $scope.onEditContent=false;
        };

        $scope.reset = function() {
            $scope.onEditContent = false;
        };

        $scope.isUnchanged = function(content) {
            return angular.equals(content, $scope.view.content);
        };

        $scope.reset();
    }]);

