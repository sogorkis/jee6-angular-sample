'use strict';

angular.module('myApp.services', [])
    .factory(
        'Authenticator', ['$http',
            function ($http) {
                var Authenticator = {};

                Authenticator.login = function (username, password) {
                    var loginFn = {};

                    loginFn.successFn = null;
                    loginFn.failureFn = null;
                    loginFn.errorFn = null;

                    loginFn.success = function (fn) {
                        loginFn.successFn = fn;
                        return loginFn;
                    };

                    loginFn.failure = function (fn) {
                        loginFn.failureFn = fn;
                        return loginFn;
                    };

                    loginFn.error = function (fn) {
                        loginFn.errorFn = fn;
                        return loginFn;
                    };

                    $http({url: '/rest/auth/login', method: 'POST',
                        params: {
                            username: username,
                            password: password
                        }
                    })
                        .success(function (data, status, headers, config) {
                            if (loginFn.successFn != null) {
                                loginFn.successFn(data, status, headers, config);
                            }
                        })
                        .error(function (data, status, headers, config) {
                            // HTTP 401 unauthorized
                            if (status == 401) {
                                if (loginFn.failureFn != null) {
                                    loginFn.failureFn(data, status, headers, config);
                                }
                            } else {
                                if (loginFn.errorFn != null) {
                                    loginFn.errorFn(data, status, headers, config);
                                }
                            }
                        });

                    return loginFn;
                };

                Authenticator.logout = function () {
                    $http({url: '/rest/auth/logout', method: 'POST'})
                        .error(function (response) {
                            console.log(response);
                        });
                };

                Authenticator.getUserFromSession = function (success) {
                    $http({url: '/rest/auth/getAppUser', method: 'GET'})
                        .success(function (response) {
                            if (response) {
                                success(response);
                            }
                        })
                };

                return Authenticator;
            }
        ])
    .factory('Register', ['$http',
        function ($http) {
            var Registration = {};

            Registration.register = function (email, username, password) {
                var registerFn = {};

                registerFn.successFn = null;
                registerFn.failureFn = null;

                registerFn.success = function (fn) {
                    registerFn.successFn = fn;
                    return registerFn;
                }

                registerFn.failure = function (fn) {
                    registerFn.failureFn = fn;
                    return registerFn;
                }

                $http({url: '/rest/register', method: 'POST',
                    params: {
                        username: username,
                        password: password,
                        email: email
                    }
                })
                    .success(function (data, status, headers, config) {
                        if (registerFn.successFn != null) {
                            registerFn.successFn(data, status, headers, config);
                        }
                    })
                    .error(function (data, status, headers, config) {
                        if (registerFn.failureFn != null) {
                            registerFn.failureFn(data, status, headers, config);
                        }
                    });

                return registerFn;
            };

            return Registration;
        }
    ]);

//angular
//    .module('globalErrors', [])
//    .config(function ($provide, $httpProvider, $compileProvider) {
//        var elementsList = $();
//
//        var showMessage = function (content, cl, time) {
//            $('<div/>')
//                .addClass('message')
//                .addClass(cl)
//                .hide()
//                .fadeIn('fast')
//                .delay(time)
//                .fadeOut('fast', function () {
//                    $(this).remove();
//                })
//                .appendTo(elementsList)
//                .text(content);
//        };
//
//        $httpProvider.responseInterceptors.push(function ($timeout, $q) {
//            return function (promise) {
//                return promise.then(function (successResponse) {
//                    if (successResponse.config.method.toUpperCase() != 'GET')
//                        showMessage('Success', 'successMessage', 5000);
//                    return successResponse;
//
//                }, function (errorResponse) {
//                    switch (errorResponse.status) {
//                        case 401:
//                            showMessage('Wrong usename or password', 'errorMessage', 20000);
//                            break;
//                        case 403:
//                            showMessage('You don\'t have the right to do this', 'errorMessage', 20000);
//                            break;
//                        case 500:
//                            showMessage('Server internal error: ' + errorResponse.data, 'errorMessage', 20000);
//                            break;
//                        default:
//                            showMessage('Error ' + errorResponse.status + ': ' + errorResponse.data, 'errorMessage', 20000);
//                    }
//                    return $q.reject(errorResponse);
//                });
//            };
//        });
//
//        $compileProvider.directive('appMessages', function () {
//            var directiveDefinitionObject = {
//                link: function (scope, element, attrs) {
//                    elementsList.push($(element));
//                }
//            };
//            return directiveDefinitionObject;
//        });
//    });