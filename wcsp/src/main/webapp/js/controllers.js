'use strict';

/* Controllers */

angular.module('myApp.controllers', []).
    controller('RegisterCtrl', [function () {

    }])
    .controller('TryCtrl', ['$scope', '$http', function ($scope, $http) {
    	
    	$scope.loadedHTML = null;

    	$scope.load = function() {
    		$http({url: '/crawler', method: 'GET',
    			params: {
    				url: $scope.loadURL
    			}})
                .success(function (response) {
                	$scope.loadedHTML = response;
                });
    	};
    }])
    .controller('LoginCtrl', ['$scope' , '$http', function ($scope, $http) {

    	$scope.logggedInUsername = null;
        $scope.loggedIn = false;

        $scope.login = function () {
            $http({url: '/rest/auth/login', method: 'POST',
                params: {
                    username: $scope.username,
                    password: $scope.password
                }
            })
                .success(function (response) {
                	$scope.logggedInUsername = response.name;
                    $scope.loggedIn = true;
                });
        };
        
        $scope.logout = function() {
        	$http({url: '/rest/auth/logout', method: 'POST'})
                .success(function (response) {
                	$scope.logggedInUsername = null;
                    $scope.loggedIn = false;
                });
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
            , headers: {'Authentication' : 'Basic ' + $cookies.JSESSIONID}
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


