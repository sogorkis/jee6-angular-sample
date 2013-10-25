'use strict';

angular.module('myApp.services', [])
    .factory(
        'Authenticator', ['$http',
            function ($http) {
                var Authenticator = {};

                Authenticator.login = function (username, password, success, error) {
                    $http({url: '/rest/auth/login', method: 'POST',
                        params: {
                            username: username,
                            password: password
                        }
                    })
                        .success(function (response) {
                            success(response);
                        })
                        .error(function (response) {
                            error(response);
                        });
                };

                Authenticator.logout = function () {
                    $http({url: '/rest/auth/logout', method: 'POST'})
                        .error(function (response) {
                            console.log(response);
                        });
                };

                Authenticator.getSessionUser = function (success) {
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
