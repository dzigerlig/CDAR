/*app.factory('TreeService', function($resource) {
	return $resource('http://localhost:8080/CDAR/webapi/ktree/:action/1/:treeid', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		},
		'postEntry' : {
			method : 'POST'
		},
		'removeTree' : {
			method : 'GET',
			params: {
				action: 'delete'
					}
		},
		'getTree' : {
			method : 'GET',
			isArray : false
		}
	});
});

app.factory('UserService', function($resource) {
	return $resource('../webapi/users', {}, {
		'getUsers' : {
			method : 'GET',
			isArray : true
		}
	});
});

app.controller("KnowledgeTreeController", [ '$scope', '$routeParams', '$location', 'TreeService', 'AuthenticationService', 'UserService',
                                        		function($scope, $routeParams, $location, TreeService, AuthenticationService, UserService) {
	$scope.knowledgetree;
	$scope.users;
	
	TreeService.getTree({treeid:$routeParams.treeId}, function(response) {
		$scope.knowledgetree = response;
	});
	
	UserService.getUsers(function(response) {
		$scope.users = response;
	});
}]);*/