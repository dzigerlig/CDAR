app.factory('AuthenticationService', function($log, $resource, $location) {
	return {
		addUser: $resource('../webapi/users/registration', {}, {
			post:
				{
				method: 'POST',
				params: {},
				isArray: false
				}
		}),
		login: $resource('../webapi/users/login/:user/:pw', {}, {
			loginuser: {
				method: 'GET',
				isArray: false
			}
		}),
		edit: $resource('../webapi/users/edit', {}, {
			changepw: {
				method: 'POST',
				params: {},
				isArray: false
			}
		}),
		logout: function() {
			$.removeCookie('cdar');
			$location.path('/login');
		}
	};
});

app.factory('UserService', ['$location', function ($location) {
	return {
		user : $.cookie('cdar'),
		isLoggedIn : function() { return this.user.id!=-1; },
		redirectUrl : function() {
			this.user.isProducer = !this.user.isProducer;
			$.cookie('cdar', this.user, {
				expires : 7
			});
			if (!this.user.isProducer) {
				$location.path('/homeconsumer');
			} else {
				$location.path('/homeproducer');
			}
		}
	};
} ]);

app.controller("LoginController", function($scope, $location,
		AuthenticationService, md5, UserService) {
	$.removeCookie('cdar');
	UserService.user = '';
	
	$scope.chkbKnowledgeProducer;

	$scope.credentials = {
		username : "",
		password : ""
	};
	
	$scope.login = function() {
		AuthenticationService.login.loginuser({username:$scope.credentials.username,password: md5.createHash($scope.credentials.password),isProducer: $scope.chkbKnowledgeProducer}, function(response) {
			if (response.id != -1 && response.accesstoken.length == 40) {
				$.cookie('cdar', response, {
					expires : 7
				});
				console.log(response);
				UserService.user = $.cookie('cdar');
				if (UserService.user.isProducer) {
					$location.path('/homeproducer');
				} else {
					$location.path('/homeconsumer');
				}
			} else {
				alert("wrong username/password combination");
			}
		});
	};
});

app.controller("RegistrationController", function($scope, $location,
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
});

app.controller("AccountController", function($scope, $location,
		AuthenticationService, md5, UserService) {
	$scope.user = UserService.user;
	$scope.newPw = '';
	
	alert("IS PRODUCER: " + $scope.user.isProducer);
	
	$scope.changePw = function() {
		console.log($scope.user);
		$scope.user.password = md5.createHash($scope.newPw);
		AuthenticationService.edit.changepw($scope.user, function(response) {
			if (response.id != -1) {
				alert("pw changed!");
				$scope.newPw = '';
			} else {
				alert("pw change failed!");
			}
		});
	};
});