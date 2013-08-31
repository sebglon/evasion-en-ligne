'use strict';

angular.module('evasionVisiteurApp.controllers', []).
        controller('EditStaticContent', ['$scope', function($scope) {
        $scope.tinymceOptions = {
            handle_event_callback: function(e) {
                // put logic here for keypress
            }
        };

        $scope.edit = function() {
            $scope.title = angular.copy($scope.view.title);
            $scope.content = angular.copy($scope.view.content);
            $scope.onEditContent = true;
        }
        $scope.update = function(content, title) {
            $scope.view.title = angular.copy(title);
            $scope.view.content = angular.copy(content);
            $scope.onEditContent = false;
        };

        $scope.reset = function() {
            $scope.onEditContent = false;
        };

        $scope.isUnchanged = function(content, title) {
            return angular.equals(content, $scope.view.content) && angular.equals(title, $scope.view.title);
        };

        $scope.reset();
    }]);

