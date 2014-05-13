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
				AuthenticationService.login.user({
					username : $scope.credentials.username,
					password : $scope.credentials.password
				}, function(response) {
					UserService.setUsername(response.username);
					UserService.setAccesstoken(response.accesstoken);
					UserService.setUserId(response.id);
CDAR.setCustomHeader(response.id,response.accesstoken);

					if ($scope.chkbKnowledgeProducer) {
						UserService.setIsProducer('true');
						$location.path('/homeproducer');
					} else {
						UserService.setIsProducer('false');
						$location.path('/homeconsumer');
					}
				}, function(error) {
					noty({
						type : 'alert',
						text : 'wrong username/password combination',
						timeout : 4000
					});
					$scope.loading = false;
				});
			};
		} ]);

app.controller("RegistrationController", [ '$scope', '$location',
		'AuthenticationService', 'UserService',
		function($scope, $location, AuthenticationService, UserService) {
			$scope.loading = false;

			UserService.removeCookies();

			$scope.credentials = {
				username : "",
				password : "",
				confirmpassword : ""
			};

			$scope.register = function() {
				
				if($scope.credentials.password!==$scope.credentials.confirmpassword){
					noty({
						type : 'warning',
						text : "Passwords aren't equal",
						timeout : 3500
					});	
					return;
				}
				$scope.loading = true;
				AuthenticationService.add.user({
					username : $scope.credentials.username,
					password : $scope.credentials.password
				}, function(response) {
					noty({
						type : 'success',
						text : 'user created',
						timeout : 4000
					});
					$location.path('/login');
				}, function(error) {
					noty({
						type : 'alert',
						text : 'user creation failed',
						timeout : 4000
					});
					$scope.loading = false;
				});
			};
		} ]);

app.controller("AccountController", [ '$scope', '$location',
		'AuthenticationService', 'UserService','$filter',
		function($scope, $location, AuthenticationService, UserService,$filter) {
			$scope.UserService = UserService;
			$scope.newPw = '';
			$scope.confirmPw = '';
			$scope.statuses = [
			                   {value: 2, text: '2', show: true},
			                   {value: 3, text: '3', show: true},
			                   {value: 4, text: '4', show: true},
			                   {value: 5, text: '5', show: true},
			                   {value: 6, text: '6', show: true},
			                   {value: 7, text: '7', show: true},
			                 ]; 
			$scope.showStatus = function() {
				console.log(selected[0]);
			    var selected = $filter('filter')($scope.statuses, {value: 2});
			    return selected[0].text;
			  };

			$scope.changePw = function() {
				if($scope.newPw!==$scope.confirmPw){
					noty({
						type : 'warning',
						text : "Passwords aren't equal",
						timeout : 3500
					});					return;
				}
				AuthenticationService.edit.changepw({
					"userid" : UserService.getUserId()
				}, {
					id : UserService.getUserId(),
					username : UserService.getUsername(),
					password : $scope.newPw
				}, function(response) {
					alert("pw changed!");
					$scope.newPw = '';
				}, function(error) {
					alert("pw change failed!");
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
		function($scope, $routeParams, $location, AuthenticationService,
				UserService, TreeService) {
			$scope.UserService = UserService;
			$scope.isProducer = UserService.getIsProducer();
			$scope.treeId = $routeParams.treeId;
			$scope.selectedUserId = "";
			$scope.users = "";
			// | orderBy:'id':false
			// TreeService.getUsers().....function(response) { $scope.users =
			// response; }
			
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
						type : 'alert',
						text : 'error getting subnodes',
						timeout : 1500
					});
				});
			};
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
							type : 'alert',
							text : 'cannot update node status',
							timeout : 1500
						});
					});
				}
			};
			$scope.removeAccessRight = function(userid) {
				TreeService.setUserRight({
					entity1 : roleEntity,
					id1 : $routeParams.treeId,
					id2 : userid
				}, {
					treeaccess : false
				}, function(response) {
					getAllUsers();
				}, function(error) {
					alert("access right change failed!");
				});
			};

		} ]);
