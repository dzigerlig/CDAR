app.controller("LoginController", ['$scope', '$location', 'AuthenticationService', 'UserService', function($scope, $location,
		AuthenticationService, UserService) {
	UserService.removeCookies();
	
	$scope.chkbKnowledgeProducer = "";

	$scope.credentials = {
		username : '',
		password : ''
	};
	
	$scope.login = function() {
		AuthenticationService.login.loginuser({username:$scope.credentials.username,password: $scope.credentials.password}, function(response) {
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
		});
	};
}]);

app.controller("RegistrationController", ['$scope', '$location', 'AuthenticationService', 'UserService', function($scope, $location,
		AuthenticationService, UserService) {
	UserService.removeCookies();

	$scope.credentials = {
		username : "",
		password : ""
	};
	
	$scope.register = function() {
		AuthenticationService.addUser.post({username: $scope.credentials.username, password: $scope.credentials.password}, function(response) {
			if (response.id != -1) {
				noty({type: 'success', text : 'user created', timeout: 4000});
				$location.path('/login');
			} else {
				noty({type: 'alert', text : 'user already exists', timeout: 4000});
			}
		});
	};
}]);

app.controller("AccountController", ['$scope', '$location', 'AuthenticationService', 'UserService', function($scope, $location,
		AuthenticationService, UserService) {
	$scope.UserService = UserService;
	$scope.newPw = '';
	
	$scope.changePw = function() {
		AuthenticationService.edit.changepw({id : UserService.getUserId(), username : UserService.getUsername(), password : $scope.newPw}, function(response) {
			if (response.id != -1) {
				alert("pw changed!");
				$scope.newPw = '';
			} else {
				alert("pw change failed!");
			}
		});
	};
}]);
