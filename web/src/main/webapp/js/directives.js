/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';
angular.module('evasionVisiteurApp.directives', ['ui.bootstrap'])
        .directive('evEditor', function() {
            return {
                restrict: 'E',
                transclude: true,
                scope: {auth: '=auth', name: '@', update: '&'},
                template: '<div name="{{name}}" class="content">' +
                        '<div ng-switch on="auth">' +
                        '<div ng-switch-when="true">' +
                        '<div class="options">' +
                        '<a class="glyphicon glyphicon-edit" href="" ng-click="edit()"></a>' +
                        '<a class="glyphicon glyphicon-move" href=""></a>' +
                        '<a class="glyphicon glyphicon-trash" href=""></a>' +
                        '</div></div></div>' +
                        '<form name="{{name}}" novalidate ng-submit="updateData()"><div ng-transclude class=></div><div class="options2">' +
                        '<button ng-show="getEditMode()" ng-click="reset()" >Annuler</button>' +
                        '<button type="submit" class="btn btn-primary" ng-show="getEditMode()" ng-disabled="{{name}}.$invalid" title="Enregistrer">Enregistrer</button>' +
                        '</div></form>' +
                        '</div>',
                controller: function($scope, $attrs) {
                    $scope.editMode = false;
                    $scope.edit = function() {
                        if ($scope.auth) {
                            $scope.editMode = true;
                        }

                        angular.forEach(entries, function(entry) {
                            entry.oldValue = angular.copy(entry.value);
                        });

                        $scope.changeMode();

                    };

                    $scope.updateData = function() {
                        // call save passed on param
                        $scope.editMode = false;
                        $scope.changeMode();
                        $scope.$eval($attrs.update);
                    };

                    $scope.getEditMode = function() {
                        return $scope.editMode;
                    };

                    $scope.changeMode = function() {
                        angular.forEach(entries, function(entry) {
                            entry.editMode = $scope.editMode;
                        });
                    };
                    $scope.tinymceOptions = {
                        plugins: [
                            "advlist autolink lists link image charmap print preview anchor",
                            "searchreplace visualblocks code fullscreen",
                            "insertdatetime media table contextmenu paste"
                        ],
                        toolbar: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
                        browser_spellcheck: true, object_resizing: true, menubar: false, content_css: "/css/app.css, //cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.6.0/ui-bootstrap.min.js",
                        handle_event_callback: function(e) {
                            // put logic here for keypress
                        }
                    };

                    var entries = $scope.entries = [];
                    this.addEntry = function(entry) {
                        entries.push(entry);
                        entry.editMode = $scope.editMode;
                    };

                    $scope.reset = function() {
                        angular.forEach(entries, function(entry) {
                            entry.reset();
                        });
                        $scope.editMode = false;
                        $scope.changeMode();
                    };
                },
                link: function(scope) {
                    scope.$$nextSibling.editMode= scope.editMode = false;
                }
            };
        })
        .directive('evText', ['$compile', '$sce', function($compile, $sce) {
                var getTemplate = function(contentType, onEdit) {
                    var startTpl = '<div class="field ">' +
                            '<label for="{{name}}" class="label-value {{classes}}">{{label}}</label>';
                    var endTpl = '<span class="invalid" ng-show="">{{errorMessage}}</span>' +
                            '</div>';
                    var inputText = '<input class="text {{classes}} " name="{{name}}" id="{{name}}" type="text"  ng-model="value" ng-required="true">';
                    var inputTextarea = '<textarea class="form-control textarea {{clssses}}" name="{{name}}" id="{{name}}" type="text" ng-model="value" required>';
                    var inputHTMLarea = '<textarea ui-tinymce="{{tinymceOptions}}" class="form-control textarea {{classes}}" name="{{name}}" id="{{name}}" ng-model="value" required>';
                    var inputDate = '<input type="text" class="form-control date {{classes}}" ui-date-format={{format}}" datepicker-popup="{{format}}" ng-model="value" is-open="opened" datepicker-options="dateOptions" required close-text="Close" />';
                    var tpl;
                    switch (contentType) {
                        case 'text':
                            tpl = onEdit ? (startTpl + inputText + endTpl) : '<span class={{classes}}>{{value}}</span>';
                            break;
                        case 'textarea':
                            tpl = onEdit ? (startTpl + inputTextarea + endTpl) : '<div class={{classes}}>{{value}}</div>';
                            break;
                        case 'htmlarea':
                            tpl = onEdit ? (startTpl + inputHTMLarea + endTpl) : '<div class={{classes}} ng-bind-html="trustedContent()"></div>';
                            break;
                        case 'date':
                            tpl = onEdit ? (startTpl + inputDate + endTpl) : '<span class={{classes}}>{{value | date:format}}</span>';
                            break;
                    }
                    ;

                    return tpl;
                };
                var linker = function(scope, element, attrs, EditorCtrl) {
                    EditorCtrl.addEntry(scope);
                    scope.$watch('editMode', function(value) {
                        element.html(getTemplate(attrs.type, value));
                        $compile(element.contents())(scope);
                    });

                    scope.reset = function() {
                        if (scope.oldValue !== undefined) {
                            scope.value = scope.oldValue;
                        }
                    };
                };

                var controller = function($scope) {
                    $scope.trustedContent = function() {
                        return   $sce.trustAsHtml($scope.value);
                    };

                };

                return {
                    restrict: 'E',
                    require: '^evEditor',
                    scope: {type: '@type', label: '@', style: '@', name: '@', value: '=', classes: '@', format: '='},
                    link: linker,
                    controller: controller
                };
            }]);