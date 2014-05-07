app.controller("LoginController", ['$scope', '$location', 'AuthenticationService', 'UserService', function($scope, $location,
		AuthenticationService, UserService) {
	UserService.removeCookies();
	$scope.chkbKnowledgeProducer = "";
	
	$scope.loading = false;

	$scope.credentials = {
		username : '',
		password : ''
	};
	
	$scope.login = function() {
		$scope.loading = true;
		AuthenticationService.login.user({username:$scope.credentials.username,password: $scope.credentials.password}, function(response) {
				UserService.setUsername(response.username);
				UserService.setAccesstoken(response.accesstoken);
				UserService.setUserId(response.id);
				
				customHeaders.uid = response.id;
				customHeaders.accesstoken = response.accesstoken;
				
				if ($scope.chkbKnowledgeProducer) {
					UserService.setIsProducer('true');
					$location.path('/homeproducer');
				} else {
					UserService.setIsProducer('false');
					$location.path('/homeconsumer');
				}
		}, function(error) {
			noty({type: 'alert', text : 'wrong username/password combination', timeout: 4000});
			$scope.loading = false;
		});
	};
}]);

app.controller("RegistrationController", ['$scope', '$location', 'AuthenticationService', 'UserService', function($scope, $location,
		AuthenticationService, UserService) {
	$scope.loading = false;
	
	UserService.removeCookies();

	$scope.credentials = {
		username : "",
		password : ""
	};
	
	$scope.register = function() {
		$scope.loading = true;
		AuthenticationService.add.user({username: $scope.credentials.username, password: $scope.credentials.password}, function(response) {
				noty({type: 'success', text : 'user created', timeout: 4000});
				$location.path('/login');
		}, function(error) {
			noty({type: 'alert', text : 'user creation failed', timeout: 4000});
			$scope.loading = false;
		});
	};
}]);

app.controller("AccountController", ['$scope', '$location', 'AuthenticationService', 'UserService', function($scope, $location,
		AuthenticationService, UserService) {
	$scope.UserService = UserService;
	$scope.newPw = '';
	
	$scope.changePw = function() {
		AuthenticationService.edit.changepw({ "userid" : UserService.getUserId() }, {id : UserService.getUserId(), username : UserService.getUsername(), password : $scope.newPw}, function(response) {
				alert("pw changed!");
				$scope.newPw = '';
			} , function(error) {
				alert("pw change failed!");
			});
	};
}]);
