'use strict';

angular.module('evasionVisiteurApp.controllers', ['ui.bootstrap', 'evasionVisiteurApp.googleapi'])
        .config(['TokenProvider', function(TokenProvider) {
        var baseUrl = window.location.protocol+'//'+window.location.host+'/';
        TokenProvider.extendConfig({
            clientId: '148280693971.apps.googleusercontent.com',
            redirectUri: baseUrl + 'oauth2callback.html', // allow lunching demo from a mirror
            scopes: ['https://www.googleapis.com/auth/userinfo.email', 'https://www.googleapis.com/auth/userinfo.profile']
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
        .controller('AppCtrl', ['$scope', 'Token', function($scope, Token) {
        console.log('init app Ctrl');

        $scope.init = function() {
            $scope.$on('handleAuthResultSuccess', function(event, result) {
                $scope.token = result;
                console.log('event auth success' + angular.toJson($scope.token));
            });
            $scope.$on('handleAuthResultError', function(event, result) {
                $scope.token = result;
                console.log('event auth error' + angular.toJson($rootScope.token));
            });
            $scope.authenticate();
            $scope.accessToken = Token.get();
            
            
            
        };
         $scope.authenticate = function() {
      var extraParams = $scope.askApproval ? {approval_prompt: 'auto'} : {};
      Token.getTokenByPopup(extraParams)
        .then(function(params) {
          // Success getting token from popup.

          // Verify the token before setting it, to avoid the confused deputy problem.
          Token.verifyAsync(params.access_token).
            then(function(data) {
              $rootScope.$apply(function() {
                $scope.accessToken = params.access_token;
                $scope.expiresIn = params.expires_in;

                Token.set(params.access_token);
              });
            }, function() {
              alert("Failed to verify token.")
            });

        }, function() {
          // Failure getting token from popup.
          alert("Failed to get token from popup.");
        });
         };
    }]);