'use strict';

/* Directives */


angular.module('myApp.directives', [])
    .directive('sameAs', function () {
        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {
                ctrl.$parsers.unshift(function (viewValue) {
                    if (viewValue === scope[attrs.sameAs]) {
                        ctrl.$setValidity('sameAs', true);
                        return viewValue;
                    } else {
                        ctrl.$setValidity('sameAs', false);
                        return undefined;
                    }
                });
            }
        };
    })
    .directive("controlGroup", function () {
        return {
            template: '<div class="control-group" ng-class="controlGroupClass">\
                    <label class="control-label" for="{{for}}">{{label}}</label>\
                    <div class="controls">\
                        <span ng-transclude></span>\
                        <div class="help-inline" ng-show="isInvalid">\
                            <label for="{{for}}" class="error">&#10008; {{errorMessage}}</label>\
                        </div>\
                        <div class="help-inline" ng-show="isSuccess">\
                            <span>&#x2713;</span>\
                        </div>\
                    </div>\
                    </div>',

            replace: true,
            transclude: true,
            require: "^form",

            scope: {
                label: "@" // Gets the string contents of the `label` attribute
            },

            link: function (scope, element, attrs, formController) {
                // The <label> should have a `for` attribute that links it to the input.
                // Get the `id` attribute from the input element
                // and add it to the scope so our template can access it.
                var inputElement = element.find("input");
                var id = inputElement.attr("id");
                scope.for = id;

                // Get the `name` attribute of the input
                var inputName = inputElement.attr("name");

                // e.g. "form.example.
                var evalExpr = [formController.$name, inputName].join(".");

                // placeholder for control-group div classes
                scope.controlGroupClass = "";
                scope.isInvalid = false;
                scope.isSuccess = false;
                scope.errorMessage = "";

                // TODO: this function should be extracted to separate service
                var getErrorMessage = function (errors) {
                    for (var errorKey in errors) {
                        if (errors[errorKey]) {
                            switch (errorKey) {
                                case 'required':
                                    return "Required field";
                                case 'email':
                                    return "Invalid email";
                                case 'nonUniqueEmail':
                                    return "Provided email is already registered";
                                default:
                                    return errorKey;
                            }
                        }
                    }
                    return "Unknown error";
                };

                // function updating control-group div classes bases od input state
                var updateControlGroupClass = function () {
                    var input = scope.$parent.$eval(evalExpr);
                    var hasFocus = document.activeElement.getAttribute('id') == id;

                    if (hasFocus) {
                        scope.controlGroupClass = "";
                        scope.isInvalid = false;
                        scope.isSuccess = false;

                        return;
                    }

                    if (input.$dirty) {
                        if (input.$invalid) {
                            scope.controlGroupClass = "error";
                            scope.isInvalid = true;
                            scope.isSuccess = false;
                            scope.errorMessage = getErrorMessage(input.$error);
                        }
                        else {
                            scope.controlGroupClass = "success";
                            scope.isInvalid = false;
                            scope.isSuccess = true;
                        }
                    }
                    else {
                        scope.isInvalid = false;
                        scope.isSuccess = false;
                    }
                };

                //  TODO: available in higher version of AngularJS - right now use jQuery
                // watch for blur event
                // inputElement.on('blur', function () {
                //    updateControlGroupClass();
                // });

                // watch for any input changes
                jQuery('#' + id).on('blur keypress', function () {
                    scope.$apply(function () {
                        updateControlGroupClass();
                    });
                });

                // validation state for input might change in other place as well (i.e. controller)
                var watchExpression = '[' + evalExpr + '.$invalid, ' + evalExpr + '$dirty]';
                scope.$parent.$watch(watchExpression,
                    function () {
                        updateControlGroupClass();
                    },
                    true);
            }

        };
    });

