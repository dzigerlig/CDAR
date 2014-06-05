//controller for login
app.controller('LoginController', [ '$scope', '$location', 'AuthenticationService', 'UserService', 'DescriptionService',
                                    function($scope, $location, AuthenticationService, UserService, DescriptionService) {
	UserService.removeCookies();
	$scope.chkbKnowledgeProducer = '';
	
	$scope.loading = false;
	
	$scope.credentials = {
			username : '',
			password : ''
	};
	
	$scope.DescriptionService = DescriptionService;

	$scope.login = function() {
		$scope.loading = true;
		AuthenticationService.loginUser({
			username : $scope.credentials.username,
			password : $scope.credentials.password
		}, function(response) {
			UserService.setUsername(response.username);
			UserService.setAccesstoken(response.accesstoken);
			UserService.setUserId(response.id);
			UserService.setDrillHierarchy(response.drillHierarchy);
			CDAR.setCustomHeader(response.id, response.accesstoken);
			
			if ($scope.chkbKnowledgeProducer) {
				UserService.setIsProducer('true');
				$location.path('/homeproducer');
			} else {
				UserService.setIsProducer('false');
				$location.path('/homeconsumer');
			}
		}, function(error) {
			noty({
				type : 'information',
				text : 'wrong username/password combination',
				timeout : 4000
			});
			$scope.loading = false;
		});
	};
} ]);