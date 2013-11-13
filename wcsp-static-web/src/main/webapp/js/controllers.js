'use strict';

/* Controllers */
angular.module('myApp.controllers', [])
    .controller('RegisterCtrl', ['$scope', '$rootScope', '$location', 'Register', function ($scope, $rootScope, $location, Register) {

        $scope.user = {};

        $scope.register = function (user) {
            Register.register(user.email, user.username, user.password)
                .success(function () {
                    $location.path("/");

                    $rootScope.$broadcast('userRegistered', user);
                })
                .failure(function (response) {
                    if (response.validationErrors.email == 'nonUniqueEmail') {
                        $scope.registrationForm.email.$setValidity('nonUniqueEmail', false);

                        var passedEmail = user.email;
                        $scope.$watch('registrationForm.email.$viewValue', function (inputValue) {
                            if (inputValue != passedEmail) {
                                $scope.registrationForm.email.$setValidity('nonUniqueEmail', true);
                            }
                        });
                    }
                    else {
                        // TODO: global validation error handling
                        alert('Unexpected error: ' + response);
                    }
                });
        };

        $scope.reset = function () {
            $scope.user = {};
        };
    }])
    .controller('LoginCtrl', ['$scope', '$location', 'Authenticator', function ($scope, $location, Authenticator) {

        $scope.userData = null;
        $scope.loggedIn = false;

        var loginFn = function (username, password) {
            Authenticator.login(username, password)
                .success(function (userData) {
                    setUserData(userData)
                })
                .failure(function (response) {
                    alert(response);
                });
        };

        $scope.login = function () {
            loginFn($scope.username, $scope.password);
        };

        $scope.logout = function () {
            Authenticator.logout();

            setUserData(null);

            $location.path("/");
        };

        $scope.init = function () {
            Authenticator.getUserFromSession(function (userData) {
                setUserData(userData)
            });
        };

        function setUserData(userData) {
            $scope.userData = userData;
            $scope.loggedIn = userData != null;
        }

        $scope.$on('userRegistered', function (event, userData) {
            loginFn(userData.email, userData.password);
        });
    }])
    .controller('TryCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.loadURL = null;
        $scope.loadedHTML = null;

        $scope.load = function () {
            $http({url: '/crawler', method: 'GET',
                params: {
                    url: $scope.loadURL
                }})
                .success(function (response) {
                    $scope.loadedHTML = response;
                });
        };
    }])
    .controller('RequestsCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.requests = $http({url: '/rest/user/crawl-requests', method: 'GET', params: {
            urlPart: $scope.searchURL
        }})
            .success(function (response) {
                $scope.requests = response;
            });

        $scope.search = function () {
            $scope.requests = $http({url: '/rest/user/crawl-requests', method: 'GET', params: {
                urlPart: $scope.searchURL}
            })
                .success(function (response) {
                    $scope.requests = response;
                });
        };

        $scope.resetSearch = function () {
            $scope.searchURL = null;
            $scope.requests = $http({url: '/rest/user/crawl-requests', method: 'GET', params: {
                urlPart: $scope.searchURL}})
                .success(function (response) {
                    $scope.requests = response;
                });
        };
    }]);
