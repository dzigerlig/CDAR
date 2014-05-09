app.controller("HomeConsumerController", ['$scope', 'AuthenticationService', 'TreeService', 'UserService', function ($scope, AuthenticationService, TreeService, UserService) {
    $scope.projectTrees = "";
    $scope.newTreeName = "";
    $scope.UserService = UserService;
    $scope.knowledgetrees = "";
    $scope.selectedktreeId = "";
    
    
    TreeService.getTrees({entity1: 'ktrees' }, function (response) {
        $scope.knowledgetrees = response;
    });
    
    var reloadTrees = function () {
        TreeService.getTrees({entity1: 'ptrees'}, function (response) {
            $scope.projectTrees = response;
        }, function (error) {
        	noty({
				type : 'alert',
				text : 'error getting trees',
				timeout : 1500
			});
        });
    };

    reloadTrees();

    $scope.addNewTree = function() {
    	if ($scope.selectedktreeId.length!==0) {
	        TreeService.addTree({ entity1: 'ptrees' }, { copyTreeId : $scope.selectedktreeId, title: $scope.newTreeName }, function (response) {
	            $scope.newTreeName = '';
	            reloadTrees();
	        }, function (error) {
	        	noty({
					type : 'alert',
					text : 'error while adding tree',
					timeout : 1500
				});
	        });
    	}
    };

    $scope.deleteTree = function (treeid) {
        TreeService.deleteTree({ entity1: 'ptrees' }, { id: treeid }, function (response) {
            reloadTrees();
        }, function (error) {
        	noty({
				type : 'alert',
				text : 'cannot delete tree',
				timeout : 1500
			});
        });
    };
    
    $scope.saveProjectTreeTitle = function(data, id) {
    	var tree = $.grep($scope.projectTrees, function(t) {
    		return t.id === id;
    	})[0];
    	
    	var oldTitle = tree.title;
    	tree.title = data;
    	
    	TreeService.updateTree({
    		entity1 : 'ptrees',
    		id1 : tree.id
    	}, tree, function(response) {}, function(error) {
    		 reloadTrees();
			noty({
				type : 'alert',
				text : 'error while saving tree title',
				timeout : 1500
			});
    	});
    }

    $scope.logout = function () {
        AuthenticationService.logout();
    };
}]);