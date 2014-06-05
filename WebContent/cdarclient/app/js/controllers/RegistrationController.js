//controller for registration
app.controller('RegistrationController', ['$scope', '$location', 'AuthenticationService', 'UserService',
						function($scope, $location, AuthenticationService, UserService) {
			$scope.loading = false;

			UserService.removeCookies();

			$scope.credentials = {
					username : '',
					password : '',
					confirmpassword : ''
			};
			
			$scope.register = function() {
				
				if ($scope.credentials.password !== $scope.credentials.confirmpassword) {
					noty({
						type : 'information',
						text : 'Passwords are not equal',
						timeout : 3500
					});
					return;
				}
				$scope.loading = true;
				AuthenticationService.addUser({
					username : $scope.credentials.username,
					password : $scope.credentials.password
				}, function(response) {
					noty({
						type : 'success',
						text : 'user "'+$scope.credentials.username+'" created',
						timeout : 4000
					});
					$location.path('/login');
				}, function(error) {
					noty({
						type : 'warning',
						text : 'user creation failed',
						timeout : 4000
					});
					$scope.loading = false;
				});
			};
} 
]);