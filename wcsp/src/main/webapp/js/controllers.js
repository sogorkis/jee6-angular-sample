'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
    .controller('RegisterCtrl', ['$scope', '$rootScope', '$location', 'Register', function ($scope, $rootScope, $location, Register) {

        $scope.username = null;
        $scope.email = null;
        $scope.password = null;
        $scope.repeatPassword = null;
        $scope.validationMessages = null;

        $scope.reset = function () {
            $scope.username = null;
            $scope.email = null;
            $scope.password = null;
            $scope.repeatPassword = null;
        };

        $scope.register = function () {
            Register.register($scope.email, $scope.username, $scope.password,
                function () {
                    // TODO: autologin after registration
                    //$rootScope.$broadcast('userRegistered', { username: $scope.email, password: $scope.password });
                    $location.path("/");
                },
                function (response) {
                    $scope.validationMessages = response;
                });
        };
    }])
    .controller('TryCtrl', ['$scope', '$http', function ($scope, $http) {

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
    .controller('LoginCtrl', ['$scope', 'Authenticator', function ($scope, Authenticator) {

        $scope.userData = null;
        $scope.loggedIn = false;

        $scope.login = function () {
            Authenticator.login($scope.username, $scope.password)
                .success(function (userData) {
                    setUserData(userData)
                })
                .failure(function (response) {
                    alert(response);
                });
        };

        $scope.logout = function () {
            Authenticator.logout();

            setUserData(null);
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
    }])
    .controller('RequestsCtrl', ['$scope', '$http', '$location', '$cookies', function ($scope, $http, $location, $cookies) {

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
    }])
;


