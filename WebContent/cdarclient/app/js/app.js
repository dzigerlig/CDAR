//header vor rest communication
var CDAR = (function() {
	var customHeaders = {
			'uid': 'none',
			'accesstoken': 'no-token'
	};
	
	return{ 
		getCustomHeader: function(){
			return customHeaders;
		},
		setCustomHeader: function(uid,accesstoken){
            customHeaders.accesstoken=accesstoken;
            customHeaders.uid=uid;
        }
	};	
})();

var app = angular.module('app', [ 'ngRoute', 'ngResource', 'ngSanitize', 'ui.bootstrap', 'xeditable', 'ngCookies' ]);

//controller assignment
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
    	templateUrl: 'importexport/importexport.html',
    	controller: 'ImportExportController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId/importexport', {
    	templateUrl: 'importexport/importexport.html',
    	controller: 'ImportExportController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId', {
    	templateUrl: 'knowledgeproducer/knowledgetree.html',
    	controller: 'TreeController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId/templates', {
    	templateUrl: 'knowledgeproducer/templates.html',
    	controller: 'TemplatesController',
    	needsLogin: true
    });
    
    $routeProvider.when('/knowledgetree/:treeId/users', {
    	templateUrl: 'user/useraccess.html',
    	controller: 'AccessController',
    	needsLogin: true
    });
    
    $routeProvider.when('/projecttree/:treeId/users', {
    	templateUrl: 'user/useraccess.html',
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

//start up
app.run(function ($rootScope, $location, editableOptions, UserService, DescriptionService) {
    $rootScope.$on('$routeChangeStart', function (evt, next) {
        if (!UserService.isLoggedIn() && next.needsLogin) {
            $location.url('/login');
        }
        event.preventDefault();
    });
    
    editableOptions.theme = 'bs3';
    CDAR.setCustomHeader(UserService.getUserId(), UserService.getAccesstoken());
});
