var app = angular.module("app", [ 'ngRoute', 'ngResource', 'ngSanitize', 'ngMd5' ]);

app.config(function ($routeProvider,$httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
    $.cookie.json = true;
    
    $routeProvider.when('/login', {
        templateUrl: 'login.html',
        controller: 'LoginController',
        needsLogin: false
    });

    $routeProvider.when('/registration', {
        templateUrl: 'register.html',
        controller: 'RegistrationController',
        needsLogin: false
    });
    
    $routeProvider.when('/homeproducer', {
        templateUrl: 'knowledgeproducer/home.html',
        controller: 'HomeProducerController',
        needsLogin: true
    });
    
    $routeProvider.when('/homeconsumer', {
        templateUrl: 'knowledgeconsumer/home.html',
        controller: 'HomeConsumerController',
        needsLogin: true
    });
    
    $routeProvider.when('/tree', {
        templateUrl: 'tree.html',
        controller: 'TreeController',
        needsLogin: false
    });

    $routeProvider.when('/wiki', {
        templateUrl: 'wiki.html',
        controller: 'WikiController',
        needsLogin: false
    });

    $routeProvider.when('/wikiEdit', {
        templateUrl: 'wikiEdit.html',
        controller: 'WikiEditController',
        needsLogin: false
    });
    
    $routeProvider.when('/projecttree/:treeId', {
    	templateUrl: 'knowledgeconsumer/projecttree.html',
    	controller: 'ProjectTreeController',
    	needsLogin: true
    });
    
    $routeProvider.when('/account', {
    	templateUrl: 'user/account.html',
    	controller: 'AccountController',
    	needsLogin: true
    });
/*
    $routeProvider.when('/knowledgetrees', {
        templateUrl: 'knowledgeproducer/knowledgetrees.html',
        controller: 'KnowledgeTreesController',
        needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId', {
    	templateUrl: 'knowledgeproducer/knowledgetree.html',
    	controller: 'KnowledgeTreeController',
    	needsLogin: true
    });
*/
    $routeProvider.otherwise({
        redirectTo: '/login'
    });

});


app.run(function ($rootScope, $location, AuthenticationService) {
    $rootScope.$on('$routeChangeStart', function (evt, next) {
        if (!$.cookie('cdar') && next.needsLogin) {
            $location.url("/login");
        }
        event.preventDefault();
    });
});

app.controller("HomeProducerController", function ($scope, AuthenticationService) {
    $scope.title = "Hello Producer!";
    $scope.message = "Mouse Over these images to see a directive at work!";

    $scope.logout = function () {
        AuthenticationService.logout();
    };
});

/*

app.controller("TreeController", [
    '$scope',
    'AuthenticationService',
    'TreeService',
    function ($scope, AuthenticationService, TreeService) {
        initializeJsPlumb();
        $scope.title = "TEST";
        $scope.message = "Mouse Over these images to see a directive at work!";

        $scope.hello = function (att) {
            console.log(att);
        };

        $scope.logout = function () {
            AuthenticationService.logout();
        };

        $scope.removeNode = function (id) {
            TreeService.removeNodes.removeNodes({ id: id });
        };

        TreeService.getNodes.getNodes(function (response) {
            myFunction(response);
        });
    } ]);


app.factory('TreeService',function ($resource) {
    return{
        removeNodes: $resource('http://localhost:8080/CDAR/webapi/nodes/removeNode/:id', {}, {
            'removeNodes': {
                params:{id:'@id'},
                method: 'OPTIONS'
            }
        }),

        getNodes: $resource('http://localhost:8080/CDAR/webapi/nodes', {}, {
            'getNodes': {
                method: 'GET',
                isArray: true
            }
        })   }
});
*/