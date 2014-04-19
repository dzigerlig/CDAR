var app = angular.module("app", [ 'ngRoute', 'ngResource', 'ngSanitize', 'ngMd5', 'ui.bootstrap', 'xeditable' ]);

app.config(function ($routeProvider,$httpProvider) {
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
    $.cookie.json = true;
    
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
    
    $routeProvider.when('/account', {
    	templateUrl: 'user/account.html',
    	controller: 'AccountController',
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
    
    $routeProvider.when('/sortable', {
    	templateUrl: 'sortable.html',
    	controller: 'SortableController',
    	needsLogin: false
    });

    $routeProvider.otherwise({
        redirectTo: '/login'
    });

});


app.run(function ($rootScope, $location, AuthenticationService, $templateCache, editableOptions) {
    $rootScope.$on('$routeChangeStart', function (evt, next) {
        if (!$.cookie('cdar') && next.needsLogin) {
            $location.url("/login");
        }
        event.preventDefault();
    });
    
    editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
});
