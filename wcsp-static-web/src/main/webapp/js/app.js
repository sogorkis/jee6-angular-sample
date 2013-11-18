'use strict';


// Declare app level module which depends on filters, and services
var myApp = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers', 'ngRoute', 'ngCookies', 'ui.bootstrap'])
    .config(['$routeProvider', '$provide', '$httpProvider', function ($routeProvider, $provide, $httpProvider) {
        $routeProvider.when('/', {templateUrl: 'partials/home.html'});
        $routeProvider.when('/about', {templateUrl: 'partials/about.html'});
        $routeProvider.when('/try', {templateUrl: 'partials/try.html', controller: 'TryCtrl'});
        $routeProvider.when('/register', {templateUrl: 'partials/register.html', controller: 'RegisterCtrl'});
        $routeProvider.when('/requests', {templateUrl: 'partials/requests.html', controller: 'RequestsCtrl'});
        $routeProvider.when('/error', {templateUrl: 'partials/error.html'});
        $routeProvider.when('/error403', {templateUrl: 'partials/error403.html'});
        $routeProvider.otherwise({redirectTo: '/view1'});

        $provide.factory('myHttpInterceptor', ['$q', '$rootScope', 'GlobalErrors', function ($q, $rootScope, GlobalErrors) {
            var numLoadings = 0;

            return {
                request: function (config) {

                    numLoadings++;

                    // Show loader
                    $rootScope.$broadcast("loader_show");
                    return config || $q.when(config)

                },
                response: function (response) {

                    if ((--numLoadings) === 0) {
                        // Hide loader
                        $rootScope.$broadcast("loader_hide");
                    }

                    return response || $q.when(response);

                },
                responseError: function (response) {

                    GlobalErrors.handleHttpErrorResponse(response);

                    if (!(--numLoadings)) {
                        // Hide loader
                        $rootScope.$broadcast("loader_hide");
                    }

                    return $q.reject(response);
                }
            }
        }
        ]);

        $httpProvider.interceptors.push('myHttpInterceptor');

    }]);

