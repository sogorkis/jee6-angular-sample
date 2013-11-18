'use strict';

/* Directives */


angular.module('myApp.directives', [])
    .directive('sameAs', function () {
        return {
            restrict: 'A',
            require: "ngModel",
            link: function (scope, element, attrs, ctrl) {
                var trackedElement = jQuery('#' + attrs.sameAs);
                var trackingElement = jQuery('#' + element.attr("id"));

                var checkValuesEqual = function () {
                    scope.$apply(function () {
                        if (trackedElement.val() == trackingElement.val()) {
                            ctrl.$setValidity(attrs.notSameErrorKey, true);
                        } else {
                            ctrl.$setValidity(attrs.notSameErrorKey, false);
                        }
                    });
                };

                // watch for any input changes on tracked element
                trackedElement.on('blur keypress', function () {
                    checkValuesEqual();
                });

                // watch for any input changes on tracking element
                trackingElement.on('blur keypress', function () {
                    checkValuesEqual();
                });
            }
        };
    })
    .directive("controlGroup", function () {
        return {
            templateUrl: '/partials/templates/controlGroup.html',

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
                                // TODO: read min, max length from element
                                case 'minlength':
                                    return "Field should have more than 5 characters";
                                case 'maxlength':
                                    return "Field should have less than 255 characters";
                                case 'notSamePassword':
                                    return "Passwords are not equal";
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
    })
    .directive("loader", [function () {
        return function ($scope, element) {
            var minLoadTime = 600;
            var loaderElement = jQuery('#' + element.attr("id"));

            $scope.$on("loader_show", function () {
                if (!loaderElement.is(':visible')) {
                    loaderElement.data('lastShownTime', new Date().getTime());
                }
                return loaderElement.show();
            });
            $scope.$on("loader_hide", function () {
                var lastShownTime = loaderElement.data('lastShownTime');
                var loadingTime = new Date().getTime() - lastShownTime;

                if (loadingTime > minLoadTime) {
                    return loaderElement.hide();
                }
                else {
                    setTimeout(function () {
                        return loaderElement.hide();
                    }, minLoadTime - loadingTime);
                }
            });
        };
    }]);

