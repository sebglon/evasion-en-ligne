'use strict';

// Declare app level module which depends on filters, and services
var evasionVisiteurApp = angular.module('evasionVisiteurApp', ['restangular', 'evasionVisiteurApp.controllers', 'evasionVisiteurApp.googleapi']);

evasionVisiteurApp.config(['$routeProvider', 'RestangularProvider', 'GoogleConfigProvider', function($routeProvider, RestangularProvider, GoogleConfigProvider) {
        // $routeProvider.when('/', {templateUrl: 'partials/basic-page.html'});
        //$routeProvider.when('/booktravel', {templateUrl: 'partials/booktravel.html'});
        $routeProvider.otherwise({redirectTo: '/'});
        RestangularProvider.setBaseUrl("default-data");
        GoogleConfigProvider.setApiKey('AIzaSyCwFjSAZbiEA9YDl0mm2S8oR9uWN3SKeUs');
        GoogleConfigProvider.setClientId('312493519025');
        GoogleConfigProvider.setScopes('https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile');
        GoogleConfigProvider.setHandleAuthResult(
                function handleAuthResult(authResult) {
                    var authorizeButton = document.getElementById('authorize-button');
                    if (authResult && !authResult.error) {
                        console.log('authentification reussi:'+ authResult);
                    } else {
                        console.log('échec d\'authentification');
                    }
                }
        );
    }]);

angular.injector(['ng', 'restangular']).invoke(function(Restangular, $rootScope) {
    evasionVisiteurApp.run(function($rootScope, Restangular, $route, $location) {
        Restangular.one('site.json').get().then(function(response) {
            $rootScope.site = response;
            console.log(response);
            function createRoute(views) {
                angular.forEach(views, function(view) {
                    var route = {controller: function($rootScope) {
                            console.log('test ' + angular.toJson(view.blocs));
                            $rootScope.view = view;
                        }
                    };
                    if (view.content !== undefined) {
                        route.template = "<ng:include src=\"'staticContent_TPL.html'\">";
                    } else if (view.template !== undefined) {
                        route.template = view.content;
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
        $rootScope.$location = $location;
    });
}
);

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

// tinyMCE directive
/**
 * Binds a TinyMCE widget to <textarea> elements.
 */
evasionVisiteurApp
        .value('uiTinymceConfig', {
    plugins: [
        "advlist autolink lists link image charmap print preview anchor",
        "searchreplace visualblocks code fullscreen",
        "insertdatetime media table contextmenu paste"
    ],
    toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
    browser_spellcheck: true, object_resizing: true, menubar: false, content_css: "/css/app.css, /css/lib/bootstrap/bootstrap.css"
})
        .directive('uiTinymce', ['uiTinymceConfig', function(uiTinymceConfig) {
        uiTinymceConfig = uiTinymceConfig || {};
        var generatedIds = 0;
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ngModel) {
                var expression, options, tinyInstance,
                        updateView = function() {
                    ngModel.$setViewValue(elm.val());
                    if (!scope.$$phase) {
                        scope.$apply();
                    }
                };
                // generate an ID if not present
                if (!attrs.id) {
                    attrs.$set('id', 'uiTinymce' + generatedIds++);
                }

                if (attrs.uiTinymce) {
                    expression = scope.$eval(attrs.uiTinymce);
                } else {
                    expression = {};
                }
                options = {
                    // Update model when calling setContent (such as from the source editor popup)
                    setup: function(ed) {
                        var args;
                        ed.on('init', function(args) {
                            ngModel.$render();
                        });
                        // Update model on button click
                        ed.on('ExecCommand', function(e) {
                            ed.save();
                            updateView();
                        });
                        // Update model on keypress
                        ed.on('KeyUp', function(e) {
                            ed.save();
                            updateView();
                        });
                        // Update model on change, i.e. copy/pasted text, plugins altering content
                        ed.on('SetContent', function(e) {
                            if (!e.initial) {
                                ed.save();
                                updateView();
                            }
                        });
                        if (expression.setup) {
                            scope.$eval(expression.setup);
                            delete expression.setup;
                        }
                    },
                    mode: 'exact',
                    elements: attrs.id
                };
                // extend options with initial uiTinymceConfig and options from directive attribute value
                angular.extend(options, uiTinymceConfig, expression);
                setTimeout(function() {
                    tinymce.init(options);
                });


                ngModel.$render = function() {
                    if (!tinyInstance) {
                        tinyInstance = tinymce.get(attrs.id);
                    }
                    if (tinyInstance) {
                        tinyInstance.setContent(ngModel.$viewValue || '');
                    }
                };
            }
        };
    }]);
