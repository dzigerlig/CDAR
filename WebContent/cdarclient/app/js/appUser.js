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
		logout: function() {
			$.removeCookie('cdar');
			$location.path('/login');
		}
	};
});

app.factory('UserService', [ function () {
	return {
		user : $.cookie('cdar'),
		isLoggedIn : function() { return this.user.id!=-1; }
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
		AuthenticationService.login.loginuser({username:$scope.credentials.username,password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1 && response.accesstoken.length == 40) {
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
