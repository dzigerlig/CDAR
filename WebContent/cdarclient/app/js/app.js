//workaround used for ressource service headers
var customHeaders = {
		'uid': 'none',
		'accesstoken': 'no-token'
};

var app = angular.module("app", [ 'ngRoute', 'ngResource', 'ngSanitize', 'ui.bootstrap', 'xeditable', 'ngCookies' ]);

app.config(function ($routeProvider,$httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
    
    $routeProvider.when('/login', {
        templateUrl: 'user/login.html',
        controller: 'LoginController',
        needsLogin: false
    });

    $routeProvider.when('/registration', {
        templateUrl: 'user/register.html',
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
    
    $routeProvider.when('/projecttree/:treeId', {
    	templateUrl: 'knowledgeconsumer/projecttree.html',
    	controller: 'ProjectTreeController',
    	needsLogin: true
    });
    
    $routeProvider.when('/projecttree/:treeId/importexport', {
    	templateUrl: 'knowledgeconsumer/importexport.html',
    	controller: 'ProjectTreeImportExportController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId', {
    	templateUrl: 'knowledgeproducer/knowledgetree.html',
    	controller: 'KnowledgeTreeController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId/templates', {
    	templateUrl: 'knowledgeproducer/templates.html',
    	controller: 'TemplatesController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId/settings', {
    	templateUrl: 'knowledgeproducer/settings.html',
    	controller: 'TreeSettingsController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId/users', {
    	templateUrl: 'user/users.html',
    	controller: 'AccessController',
    	needsLogin: true
    });
    
    $routeProvider.when('/projecttree/:treeId/users', {
    	templateUrl: 'user/users.html',
    	controller: 'AccessController',
    	needsLogin: true
    });
    
    $routeProvider.when('/account', {
    	templateUrl: 'user/account.html',
    	controller: 'AccountController',
    	needsLogin: true
    });
    
    $routeProvider.otherwise({
        redirectTo: '/login'
    });

});


app.run(function ($rootScope, $location, editableOptions, UserService) {
    $rootScope.$on('$routeChangeStart', function (evt, next) {
        if (!UserService.isLoggedIn() && next.needsLogin) {
            $location.url("/login");
        }
        event.preventDefault();
    });
    
    editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
    
    customHeaders.uid = UserService.getUserId();
    customHeaders.accesstoken = UserService.getAccesstoken();
});
