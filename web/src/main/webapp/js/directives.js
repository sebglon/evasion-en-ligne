/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
'use strict';
angular.module('evasionVisiteurApp.directives', [])
        .directive('evEditor', function() {
            return {
                restrict: 'E',
                transclude: true,
                scope: {auth: '=auth'},
                template: '<div>' +
                        '<div ng-switch on="auth">' +
                        '<div ng-switch-when="true">' +
                        '<div class="options">' +
                        '<a class="icon icon-edit" href="" ng-click="edit()"></a>' +
                        '<a class="icon icon-move" href=""></a>' +
                        '<a class="icon-black icon-trash" href=""></a>' +
                        '</div></div></div>' +
                        ' <div ng-transclude></div>' +
                        '</div>',
                controller: function($scope) {
                    var editMode = $scope.editMode = false;
                    $scope.edit = function() {
                        if ($scope.auth) {
                            editMode = true;
                        }
                        $scope.changeMode();

                    };

                    $scope.changeMode = function() {
                        angular.forEach(entries, function(entry) {
                            entry.editMode = editMode;
                        });
                    };

                    var entries = $scope.entries = [];
                    this.addEntry = function(entry) {
                        entries.push(entry);
                        entry.editMode = editMode;
                    };
                }
            };
        })
        .directive('evText', ['$compile', '$templateCache', function($compile) {
                var getTemplate = function(contentType, onEdit) {
                    var startTpl = '<div class="field">' +
                            '<label for="{{}}" class="label-value {{}}">{{label}}</label>';
                    var endTpl = '<span class="{{statusClass}}">{{errorMessage}}</span>' +
                            '</div>';
                    var inputText = '<input class="text {{class}} ' + contentType + ' name="{{name}}" id="{{name}}" type="text" value="{{value}}">';
                    var inputTextarea = '<textarea class="textarea {{class}}" name="{{name}}" id="{{name}}" type="text" value="{{value}}">';
                    var tpl;
                    switch (contentType) {
                        case 'title-h2':
                            tpl = onEdit ? (startTpl + inputText + endTpl) : '<h2 class={{class}}>{{value}}</h2>';
                            break;
                    }

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
                    scope: {type: '@type', label: '=', style: '=', name: '=', value: '='},
                    link: linker
                };
            }]);