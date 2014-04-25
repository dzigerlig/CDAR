app.controller("LoginController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$.removeCookie('cdar');
	UserService.user = '';
	
	$scope.chkbKnowledgeProducer = "";

	$scope.credentials = {
		username : '',
		password : ''
	};
	
	$scope.login = function() {
		UserService.user = '';
		
		AuthenticationService.login.loginuser({username:$scope.credentials.username,password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1 && response.accesstoken.length == 40) {
				response.isProducer = $scope.chkbKnowledgeProducer;
				$.cookie('cdar', response, {
					expires : 7
				});
				UserService.user = $.cookie('cdar');
				if ($scope.chkbKnowledgeProducer) {
					$location.path('/homeproducer');
				} else {
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
	$.removeCookie('cdar');
	UserService.user = '';

	$scope.credentials = {
		username : "",
		password : ""
	};
	
	$scope.updateUser = function(data) {
		alert(data);
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




