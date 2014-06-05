var reload = false;
function getReload() {
	return reload;
}
function setReload(value) {
	reload = value;
}

//overview controller producer
app.controller('HomeProducerController', ['$scope', '$location', 'TreeService', 'AuthenticationService', 'UserService', '$modal', 'DescriptionService', 
                                          function($scope, $location, TreeService, AuthenticationService, UserService, $modal, DescriptionService) {
	$scope.knowledgeTrees = '';
	$scope.newTreeName = '';
	$scope.UserService = UserService;
	$scope.DescriptionService = DescriptionService;

	//define locking notification (text in server) 
	$scope.showLockingNotification = function(error) {
		if (error.status === 409) {
			noty({
				type : 'error',
				text : error.data,
				timeout : 5000
			});
			return true;
		} else
			return false;
	};
	
	var reloadTrees = function() {
		TreeService.getTrees({
			entity1 : 'ktrees'
		}, function(response) {
			$scope.knowledgeTrees = response;
		}, function(error) {
			UserService.checkResponseUnauthorized(error);
			noty({
				type : 'error',
				text : 'cannot get trees',
				timeout : 1500
			});
		});
	};
	
	reloadTrees();
	
	//add new Tree
	$scope.addNewTree = function() {
		if ($scope.newTreeName.length > 45) {
			noty({
				type : 'warning',
				text : 'Please enter a text with less than 45 Characters',
				timeout : 3000
			});
		} else {
			TreeService.addTree({
				entity1 : 'ktrees'
			}, {
				title : $scope.newTreeName
			}, function(response) {
				$scope.newTreeName = '';
				reloadTrees();
			}, function(error) {
				UserService.checkResponseUnauthorized(error);
				noty({
					type : 'error',
					text : 'cannot add tree',
					timeout : 1500
				});
			});
		}
	};
	
	//delete tree with confirmation
	$scope.deleteTree = function(treeid) {
		$modal.open({
			templateUrl : 'templates/confirmation.html',
			backdrop : 'static',
			keyboard : false,
			resolve : {
				data : function() {
					return {
						title : 'Delete Tree',
						message : 'Do you really want to delete this Tree?'
					};
				}
			},
			controller : 'ConfirmationController'
		}).result.then(function(result) {
			TreeService.deleteTree(
					{
						entity1 : 'ktrees'
					},	{
						id : treeid
					},
					function(response) {
						reloadTrees();
						noty({
							type : 'success',
							text : 'knowledge tree deleted successfully',
							timeout : 1500
						});
					},
					function(error) {
						UserService.checkResponseUnauthorized(error);
						if (!$scope.showLockingNotification(error)) {
							noty({
								type : 'error',
								text : 'delete tree failed',
								timeout : 1500
							});
						}
					});
		});
	};

    //update projecttitle
	$scope.saveKnowledgeTreeTitle = function(data, id) {
		if (data.length > 45) {
			noty({
				type : 'warning',
				text : 'Please enter a text with less than 45 Characters',
				timeout : 3000
			});
			return '';
		} else {
			var tree = $.grep($scope.knowledgeTrees,function(t) {
				return t.id === id;
			})[0];
			
			var oldTitle = tree.title;
			tree.title = data;
			TreeService.updateTree(
					{
						entity1 : 'ktrees',
						id1 : tree.id
					},
					tree,
					function(response) {
					},
					function(error) {
						UserService.checkResponseUnauthorized(error);
						if (!$scope.showLockingNotification(error)) {
							tree.title = oldTitle;
							noty({
								type : 'error',
								text : 'error while saving tree title',
								timeout : 1500
							});
						}
					});
		}
	};
} ]);