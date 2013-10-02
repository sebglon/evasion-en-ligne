'use strict';

angular.module('evasionVisiteurApp.controllers', ['ui.bootstrap', 'evasionVisiteurApp.googleapi'])
        .config(['GoogleApiProvider', function(GoogleApiProvider) {
        GoogleApiProvider.extendConfig({
            clientId: '148280693971.apps.googleusercontent.com',
            apiKey: 'AIzaSyC1B4WyTqDhWJaRotdugXiDYGN2XEq7lIE',
            scopes: ['https://www.googleapis.com/auth/userinfo.email',
                'https://www.googleapis.com/auth/userinfo.profile']
        });
    }])
        .controller('EditStaticContent', ['$scope', function($scope) {
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
    }])

        .controller('LoginCtrl', ['$scope', '$dialog', function($scope, $dialog) {
        $scope.openLoginForm = function() {
            $scope.showLoginBox = true;
        };

        $scope.closeLoginForm = function() {
            $scope.showLoginBox = false;
        };

        $scope.optsLoginForm = {
            backdropFade: true,
            dialogFade: true
        };

    }])
        .controller('AppCtrl', ['$scope', '$document', 'GoogleApi', function($scope, $document, GoogleApi) {
        console.log('init app Ctrl');

        $scope.init = function() {
            $document.ready(function() {
                //GoogleApi.clientLoadPlus();
                 GoogleApi.clientLoadOAuth2();
            });
        };
    }]);