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
                    }

                    loginFn.failure = function (fn) {
                        loginFn.failureFn = fn;
                        return loginFn;
                    }

                    loginFn.error = function (fn) {
                        loginFn.errorFn = fn;
                        return loginFn;
                    }

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
                            if (status == 401)  {
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

            Registration.register = function (email, username, password, success, error) {
                $http({url: '/rest/register', method: 'POST',
                    params: {
                        username: username,
                        password: password,
                        email: email
                    }
                })
                    .success(function (response) {
                        success(response);
                    })
                    .error(function (response) {
                        error(response);
                    });
            };

            return Registration;
        }
    ]);
