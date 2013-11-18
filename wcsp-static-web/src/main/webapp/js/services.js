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
                            if (status == 401 && loginFn.failureFn != null) {
                                loginFn.failureFn(data, status, headers, config);
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
                        // HTTP 400 bad request
                        if (status == 400 && registerFn.failureFn != null) {
                            registerFn.failureFn(data, status, headers, config);
                        }
                    });

                return registerFn;
            };

            return Registration;
        }
    ])
    .factory('GlobalErrors', ['$rootScope', '$location', function ($rootScope, $location) {
        var GlobalErrors = {};

        GlobalErrors.handleHttpErrorResponse = function (response) {
            switch (response.status) {
                case 400:
                    // BAD REQUEST - do nothing
                    break;
                case 401:
                    // AUTHENTICATION failure - do nothing
                    break;
                case 403:
                    $location.path("/error403");
                    break;
                default:
                    $location.path("/error");
            }
        };

        return GlobalErrors;
    }])
    .factory('GlobalMessages', function () {
        var GlobalMessages = {};

        var defaultGrowlOptions = {
            type: 'error',
            offset: {from: 'top', amount: 50},
            align: 'center',
            width: 'auto'
        };

        GlobalMessages.showErrorMessage = function (message) {
            $.bootstrapGrowl(message, defaultGrowlOptions);
        };

        GlobalMessages.showWarnMessage = function (message) {
            var growlOptions = angular.copy(defaultGrowlOptions);
            growlOptions.type = 'warn';
            $.bootstrapGrowl(message, growlOptions);
        };

        GlobalMessages.showInfoMessage = function (message) {
            var growlOptions = angular.copy(defaultGrowlOptions);
            growlOptions.type = 'info';
            $.bootstrapGrowl(message, growlOptions);
        };

        return GlobalMessages;
    });