app.controller("LoginController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$.removeCookie('cdar');
	UserService.user = '';
	
	$scope.chkbKnowledgeProducer;

	$scope.credentials = {
		username : "",
		password : ""
	};
	
	$scope.login = function() {
		AuthenticationService.login.loginuser({username:$scope.credentials.username,password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1 && response.accesstoken.length == 40) {
				response.isProducer = $scope.chkbKnowledgeProducer;
				$.cookie('cdar', response, {
					expires : 7
				});
				console.log(response);
				UserService.user = $.cookie('cdar');
				if ($scope.chkbKnowledgeProducer) {
					$location.path('/homeproducer');
				} else {
					$location.path('/homeconsumer');
				}
			} else {
				alert("wrong username/password combination");
			}
		});
	};
}]);

app.controller("RegistrationController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$.removeCookie('cdar');
	UserService.user = '';

	$scope.credentials = {
		username : "",
		password : ""
	};

	$scope.register = function() {
		AuthenticationService.addUser.post({username: $scope.credentials.username, password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1) {
				alert("User created!");
				$location.path('/login');
			} else {
				alert("User already exists!");
			}
		});
	};
}]);

app.controller("AccountController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$scope.UserService = UserService;
	$scope.newPw = '';
	
	$scope.changePw = function() {
		console.log($scope.user);
		$scope.UserService.user.password = md5.createHash($scope.newPw);
		AuthenticationService.edit.changepw($scope.UserService.user, function(response) {
			if (response.id != -1) {
				alert("pw changed!");
				$scope.newPw = '';
			} else {
				alert("pw change failed!");
			}
		});
	};
}]);