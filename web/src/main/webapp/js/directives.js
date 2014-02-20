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
                scope: {auth: '=auth', name:'='},
                template: '<div>' +
                        '<div ng-switch on="auth">' +
                        '<div ng-switch-when="true">' +
                        '<div class="options">' +
                        '<a class="icon icon-edit" href="" ng-click="edit()"></a>' +
                        '<a class="icon icon-move" href=""></a>' +
                        '<a class="icon-black icon-trash" href=""></a>' +
                        '</div></div></div>' +
                        '<form name="{{name}}" novalidate><div ng-transclude></div><div>' +
                        '<button ng-show="getEditMode()" ng-click="reset()" >Annuler</button>' +
                        '<button type="submit" class="btn btn-primary" ng-show="getEditMode()" ng-disabled="!{{name}}.$valid" title="Enregistrer">Enregistrer</button>' +
                        '</div></form>' +
                        '</div>',
                controller: function($scope, $attrs) {
                    $scope.editMode = false;
                    $scope.edit = function() {
                        if ($scope.auth) {
                            $scope.editMode = true;
                        }
                        $scope.changeMode();

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
                        $scope.editMode = false;
                        $scope.changeMode();
                    };

                    $scope.$watch('directiveModel', function() {
                        $scope.$parent[$attrs.value] = $scope.directiveModel;
                    });
                    $scope.directiveModel = $attrs.value;
                }
            };
        })
        .directive('evText', ['$compile', '$templateCache', function($compile) {
                var getTemplate = function(contentType, onEdit) {
                    var startTpl = '<div class="form-group ">' +
                            '<label for="{{name}}" class="label-value">{{label}}</label>';
                    var endTpl = '<span class="{{statusClass}}">{{errorMessage}}</span>' +
                            '</div>';
                    var inputText = '<input class="form-control text {{class}} ' + contentType + ' name="{{name}}" id="{{name}}" type="text" value="{{value}}">';
                    var inputTextarea = '<textarea class="form-control textarea {{class}}" name="{{name}}" id="{{name}}" type="text" value="{{value}}">';
                    var inputHTMLarea = '<textarea ui-tinymce="{{tinymceOptions}}" class="form-control textarea {{class}}" name="{{name}}" id="{{name}}" ng-model="directiveModel"></textarea>';
                    var inputDate = '<input type="text" class="form-control date" datepicker-popup="dd/MM/yyyy" ng-model="directiveModel" is-open="opened" datepicker-options="dateOptions" ng-required="true" close-text="Close" />';
                    var tpl;
                    switch (contentType) {
                        case 'text':
                            tpl = onEdit ? (startTpl + inputText + endTpl) : '<span class={{class}}>{{value}}</span>';
                            break;
                        case 'textarea':
                            tpl = onEdit ? (startTpl + inputTextarea + endTpl) : '<div class={{class}}>{{value}}</div>';
                            break;
                        case 'htmlarea':
                            tpl = onEdit ? (startTpl + inputHTMLarea + endTpl) : '<div class={{class}}>{{value}}</div>';
                            break;
                        case 'date':
                            tpl = onEdit ? (startTpl + inputDate + endTpl) : '<div class={{class}}>{{value}}</div>';
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

                };
                return {
                    restrict: 'E',
                    require: '^evEditor',
                    scope: {type: '@type', label: '@', style: '@', name: '@', value: '='},
                    link: linker
                };
            }]);