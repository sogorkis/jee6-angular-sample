'use strict';


// Declare app level module which depends on filters, and services
var myApp = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers', 'ngRoute', 'ngCookies', 'ui.bootstrap'])
    .config(['$routeProvider', '$provide', '$httpProvider', function ($routeProvider, $provide, $httpProvider) {
        $routeProvider.when('/', {templateUrl: 'partials/home.html'});
        $routeProvider.when('/about', {templateUrl: 'partials/about.html'});
        $routeProvider.when('/try', {templateUrl: 'partials/try.html', controller: 'TryCtrl'});
        $routeProvider.when('/register', {templateUrl: 'partials/register.html', controller: 'RegisterCtrl'});
        $routeProvider.when('/requests', {templateUrl: 'partials/requests.html', controller: 'RequestsCtrl'});
        $routeProvider.otherwise({redirectTo: '/view1'});

//        $provide.factory('myHttpInterceptor', ['$q', function ($q) {
//            return function (promise) {
//                return promise.then(function (response) {
//                    return response;
//                }, function (response) {

//                    return $q.reject(response);
//                });
//            }
//        }]);
//
//        $httpProvider.responseInterceptors.push('myHttpInterceptor');

        $provide.factory('myHttpInterceptor', ['$q', '$rootScope', function ($q, $rootScope) {
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

                    if (!(--numLoadings)) {
                        // Hide loader
                        $rootScope.$broadcast("loader_hide");
                    }

                    switch (response.status) {
                        case 400:
                            // BAD REQUEST - do nothing
                            break;
                        case 401:
                            // AUTHENTICATION failure - do nothing
                            break;
                        case 403:
                            alert('You do not have permissions for this action');
                            break;
                        case 500:
                            alert('Internal server error: ' + response.data);
                            break;
                        default:
                            alert('Error ' + response.status + " : " + response.data);
                    }

                    return $q.reject(response);
                }
            }
        }
        ])
        ;

        $httpProvider.interceptors.push('myHttpInterceptor');

    }]);

