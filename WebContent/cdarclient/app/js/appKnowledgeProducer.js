/*app.controller("KnowledgeTreesController", [ '$scope', '$location', 'TreeService', 'AuthenticationService',
                               		function($scope, $location, TreeService, AuthenticationService) {
	$scope.knowledgeTrees;
	$scope.newTreeName = "";
	
	var reloadTrees = function() {
		TreeService.query(function(response) {
			$scope.knowledgeTrees = response;
		});
	};
	
	reloadTrees();
	
	$scope.addNewTree = function() {
		TreeService.postEntry($scope.newTreeName, function(response) {
			if (response[0] == 1) {
				$scope.newTreeName = '';
				reloadTrees();
			} else {
				alert("Error: KnowledgeTree NOT added!");
			}
		});
	};
	
	$scope.deleteTree = function(id) {
		TreeService.removeTree({treeid : id}, function(response) {
			reloadTrees();
		});
	};
	
	$scope.showTree = function(id) {
		$location.path('/knowledgetree/' + id);
	};
	
	$scope.logout = function () {
        AuthenticationService.logout();
    };

}]);*/

