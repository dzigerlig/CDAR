//access right controller
app.controller('AccessController', ['$scope', '$routeParams', '$location', 'AuthenticationService',	'UserService', 'TreeService', '$modal', 'DescriptionService',
		function($scope, $routeParams, $location, AuthenticationService, UserService, TreeService, $modal, DescriptionService) {
	$scope.UserService = UserService;
	$scope.isProducer = UserService.getIsProducer();
	$scope.DescriptionService = DescriptionService;
	$scope.treeId = $routeParams.treeId;
	$scope.selectedUserId = '';
	$scope.users = '';
	$scope.tree = '';
	
	var roleEntity = '';
	if ($scope.isProducer === 'true') {
		roleEntity = 'ktrees';
	} else {
		roleEntity = 'ptrees';
	}
	
	$scope.showLockingNotification = function(error) {
		if (error.status === 409) {
			noty({
				type : 'error',
				text : error.data,
				timeout : 5000
			});
			return true;
		} else {
			return false;
		}
	};
	
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
	//add access right to another user
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
	
	
	//remove access right from another user
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
				if (!$scope.showLockingNotification(error)) {
					noty({
						type : 'error',
						text : 'access right change failed!',
						timeout : 1500
					});
				}
			});
		});
	};
} ]);
