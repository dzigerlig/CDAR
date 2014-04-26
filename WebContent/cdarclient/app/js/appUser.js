app.controller("LoginController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {

	UserService.removeCookies();
	
	$scope.chkbKnowledgeProducer = "";

	$scope.credentials = {
		username : '',
		password : ''
	};
	
	$scope.login = function() {
		AuthenticationService.login.loginuser({username:$scope.credentials.username,password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1 && response.accesstoken.length == 40) {
				UserService.setUsername(response.username);
				UserService.setAccesstoken(response.accesstoken);
				UserService.setUserId(response.id);
				
				if ($scope.chkbKnowledgeProducer) {
					UserService.setIsProducer('true');
					$location.path('/homeproducer');
				} else {
					UserService.setIsProducer('false');
					$location.path('/homeconsumer');
				}
			} else {
				noty({type: 'alert', text : 'wrong username/password combination', timeout: 4000});
			}
		});
	};
}]);

app.controller("RegistrationController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	UserService.removeCookies();

	$scope.credentials = {
		username : "",
		password : ""
	};
	
	$scope.register = function() {
		AuthenticationService.addUser.post({username: $scope.credentials.username, password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1) {
				noty({type: 'success', text : 'user created', timeout: 4000});
				$location.path('/login');
			} else {
				noty({type: 'alert', text : 'user already exists', timeout: 4000});
			}
		});
	};
}]);

app.controller("AccountController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$scope.UserService = UserService;
	$scope.newPw = '';
	
	$scope.changePw = function() {
		AuthenticationService.edit.changepw({id : UserService.getUserId(), username : UserService.getUsername(), password : md5.createHash($scope.newPw)}, function(response) {
			if (response.id != -1) {
				alert("pw changed!");
				$scope.newPw = '';
			} else {
				alert("pw change failed!");
			}
		});
	};
}]);
