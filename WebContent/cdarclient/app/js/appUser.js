app.controller("LoginController", [ '$scope', '$location',
		'AuthenticationService', 'UserService',
		function($scope, $location, AuthenticationService, UserService) {
			UserService.removeCookies();
			$scope.chkbKnowledgeProducer = "";

			$scope.loading = false;

			$scope.credentials = {
				username : '',
				password : ''
			};

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

app.controller('ConfirmationController', ['$scope', '$modalInstance', 'data', 
                                          function ($scope, $modalInstance, data) {

                                          $scope.data = data;
                                          
                                          $scope.ok = function() {
                                              $modalInstance.close();
                                          };

                                          $scope.cancel = function() {
                                              $modalInstance.dismiss();
                                          };    
                                      }]);

app
		.controller(
				"RegistrationController",
				[
						'$scope',
						'$location',
						'AuthenticationService',
						'UserService',
						function($scope, $location, AuthenticationService,
								UserService) {
							$scope.loading = false;

							UserService.removeCookies();

							$scope.credentials = {
								username : "",
								password : "",
								confirmpassword : ""
							};

							$scope.register = function() {

								if ($scope.credentials.password !== $scope.credentials.confirmpassword) {
									noty({
										type : 'information',
										text : "Passwords aren't equal",
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
						} ]);

app.controller("AccountController",
		[
				'$scope',
				'$location',
				'AuthenticationService',
				'UserService',
				'$filter',
				'$modal',
				function($scope, $location, AuthenticationService, UserService,
						$filter, $modal) {
					$scope.UserService = UserService;
					$scope.newPw = '';
					$scope.confirmPw = '';
					$scope.drillHierarchy = UserService.getDrillHierarchy();
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
									text : "Drill hierarchy has been changed to " + $scope.drillHierarchy,
									timeout : 3500
								});
							}, function(error) {
								noty({
									type : 'warning',
									text : "failed to change drill hierarchy",
									timeout : 3500
								});
							});
						}
					};

					$scope.changePw = function() {
						if ($scope.newPw !== $scope.confirmPw) {
							noty({
								type : 'warning',
								text : "Passwords aren't equal",
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
									text : "Password have been changed",
									timeout : 3500
								});
								$scope.newPw = '';
								$scope.confirmPw = '';
							}, function(error) {
								noty({
									type : 'error',
									text : "Password change failed",
									timeout : 3500
								});
							});
					    });
					};
				} ]);

app.controller("AccessController", [
		'$scope',
		'$routeParams',
		'$location',
		'AuthenticationService',
		'UserService',
		'TreeService',
		'$modal',
		function($scope, $routeParams, $location, AuthenticationService,
				UserService, TreeService, $modal) {
			$scope.UserService = UserService;
			$scope.isProducer = UserService.getIsProducer();
			$scope.treeId = $routeParams.treeId;
			$scope.selectedUserId = "";
			$scope.users = "";
			$scope.tree = "";
			
			var roleEntity = "";
			if ($scope.isProducer === 'true') {
				roleEntity = 'ktrees';
			} else {
				roleEntity = 'ptrees';
			}
			
			var getAllUsers = function() {
				TreeService.getAllUsersWithTreeRight({
					entity1 : roleEntity,
					id1 : $routeParams.treeId
				}, function(response) {
					$scope.users = response;
				}, function(error) {
					noty({
						type : 'error',
						text : 'error getting users',
						timeout : 1500
					});
				});
			};
			
			TreeService.getTree({
				entity1 : roleEntity,
				id1 : $routeParams.treeId
			}, function(response) {
				$scope.tree = response;
			}, function(error) {
				noty({
					type : 'error',
					text : 'error getting tree',
					timeout : 1500
				});
			});
			
			getAllUsers();
			$scope.addAccessRight = function() {
				if ($scope.selectedUserId.length !== 0) {
					TreeService.setUserRight({
						entity1 : roleEntity,
						id1 : $routeParams.treeId,
						id2 : $scope.selectedUserId
					}, {
						treeaccess : true
					}, function(response) {
						getAllUsers();
					}, function(error) {
						noty({
							type : 'error',
							text : 'cannot add this user',
							timeout : 1500
						});
					});
				}
			};
			$scope.removeAccessRight = function(userid) {
				$modal.open({ 
		            templateUrl: 'templates/confirmation.html',
		            backdrop: 'static',
		            keyboard: false,
		            resolve: {
		                data: function() { 
		                    return {
		                        title: 'Delete User',
		                        message: 'Do you really want to delete this User from your Tree?' 
		                    };
		                }
			            },
			            controller: 'ConfirmationController' 
			    }).result.then(function(result) {
			    	TreeService.setUserRight({
						entity1 : roleEntity,
						id1 : $routeParams.treeId,
						id2 : userid
					}, {
						treeaccess : false
					}, function(response) {
						getAllUsers();
					}, function(error) {
						noty({
							type : 'error',
							text : 'access right change failed!',
							timeout : 1500
						});
					});
			    });
			};
		} ]);
