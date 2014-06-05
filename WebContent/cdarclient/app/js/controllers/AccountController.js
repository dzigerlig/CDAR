//account controller
app.controller('AccountController',	['$scope', '$location',	'AuthenticationService', 'UserService', '$filter', '$modal', 'DescriptionService',
				function($scope, $location, AuthenticationService, UserService,	$filter, $modal, DescriptionService) {
		$scope.UserService = UserService;
		$scope.newPw = '';
		$scope.confirmPw = '';
		$scope.drillHierarchy = UserService.getDrillHierarchy();
		$scope.DescriptionService = DescriptionService;
		
		$scope.statuses = [ {
			value : 2,
			text : '2',
		}, {
			value : 3,
			text : '3',
		}, {
			value : 4,
			text : '4',
		}, {
			value : 5,
			text : '5',
		} ];
		$scope.showStatus = function() {
			return $scope.drillHierarchy;
		};
		
		$scope.updateDrillHierarchy = function(newValue) {
			if ($scope.drillHierarchy !== newValue) {
				$scope.drillHierarchy = newValue;
				UserService.setDrillHierarchy(newValue);
				AuthenticationService.updateUser({
					userid : UserService.getUserId()
				}, {
					id : UserService.getUserId(),
					drillHierarchy: $scope.drillHierarchy
				}, function(response) {
					noty({
						type : 'success',
						text : 'Drill hierarchy has been changed to ' + $scope.drillHierarchy,
						timeout : 3500
					});
				}, function(error) {
					noty({
						type : 'warning',
						text : 'failed to change drill hierarchy',
						timeout : 3500
					});
				});
			}
		};
		
		$scope.changePw = function() {
			if ($scope.newPw !== $scope.confirmPw) {
				noty({
					type : 'warning',
					text : 'Passwords are not equal',
					timeout : 3500
				});
				return;
			}
			
			$modal.open({ 
				templateUrl: 'templates/confirmation.html',
				backdrop: 'static',
				keyboard: false,
				resolve: {
					data: function() { 
						return {
							title: 'Change password',
							message: 'If you change your password, you need to change it in mediawiki as well in order to login in to CDAR again.' 
						};
					}
				},
				controller: 'ConfirmationController' 
			}).result.then(function(result) {
				AuthenticationService.updateUser({
					userid : UserService.getUserId()
				}, {
					id : UserService.getUserId(),
					username : UserService.getUsername(),
					password : $scope.newPw
				}, function(response) {
					noty({
						type : 'success',
						text : 'Password have been changed',
						timeout : 3500
					});
					$scope.newPw = '';
					$scope.confirmPw = '';
				}, function(error) {
					noty({
						type : 'error',
						text : 'Password change failed',
						timeout : 3500
					});
				});
			});
		};
} ]);