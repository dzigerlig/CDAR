app.controller("HomeConsumerController", ['$scope', 'AuthenticationService', 'ProjectTreeService', 'UserService', function ($scope, AuthenticationService, ProjectTreeService, UserService) {
	$scope.projectTrees = "";
	$scope.newTreeName = "";
	$scope.UserService = UserService;
	
	var reloadTrees = function() {
		ProjectTreeService.query(function(response) {
			$scope.projectTrees = response;
		});
	};
	
	reloadTrees();
	
	$scope.addNewTree = function() {
		ProjectTreeService.addTree($scope.newTreeName, function(response) {
			if (response.id != -1) {
				$scope.newTreeName = '';
				reloadTrees();
			} else {
				alert("Error: ProjectTree NOT added!");
			}
		});
	};
	
	$scope.deleteTree = function(id) {
		ProjectTreeService.removeTree(id, function(response) {
			if (response.bool) {
				reloadTrees();
			} else {
				// error
			}
		});
	};

    $scope.logout = function () {
        AuthenticationService.logout();
    };
}]);

app.controller("ProjectTreeController", ['$scope', '$routeParams', 'AuthenticationService', 'ProjectTreeService', 'TreeService', 'UserService', 'WikiService', function ($scope, $routeParams, AuthenticationService, ProjectTreeService, TreeService, UserService, WikiService) {
	$scope.UserService = UserService;
	$scope.projecttree = "";
	$scope.nodes = "";
	$scope.trees = "";
	$scope.selectedktreeId = "";
	
	$scope.wikiText = "no wiki entry selected";
	
	var reloadTree = function() {
		ProjectTreeService.getTree({treeid:$routeParams.treeId}, function(response) {
			$scope.projecttree = response;
		});
		
		ProjectTreeService.getNodes({treeid:$routeParams.treeId}, function(response) {
			$scope.nodes = response;
		});
	};
	
	$scope.changeNode = function(id) {
		$scope.wikiText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
		WikiService.getWikiEntry({role:'consumer', entity:'node', nodeid: id}, function(response) {
			$scope.wikiText = response.wikiContentHtml;
		});
	};
	
	reloadTree();

    TreeService.getDirectories({
        ktreeid : $routeParams.treeId
    }, function(resDirectory) {
        TreeService.getNodes({
                ktreeid : $routeParams.treeId
            },
            function(resNodes) {

                myJsTree.directoryDataToArray(resDirectory,
                    resNodes);
                $scope.getSubnodes(resNodes);

            });

    });

    $scope.getSubnodes = function(resNodes) {
        TreeService.getSubnodes({
            ktreeid : $routeParams.treeId
        }, resNodes, function(resSubnodes) {
            myJsPlumb.drawExistingNodes(resNodes, resSubnodes);
            $scope.getLinks(resSubnodes);
        });
    };

    $scope.getLinks = function(resSubnodes) {
        TreeService.getLinks({
            ktreeid : $routeParams.treeId
        }, resSubnodes, function(response) {
            myJsPlumb.makeNodeHierarchy(response, resSubnodes);
            w_launch();
        });
    };
	
	TreeService.query(function(response) {
		$scope.knowledgetrees = response;
	});
	
	$scope.addKnowledgeTree = function() {
		if (typeof($scope.selectedktreeId) != "undefined") {
			ProjectTreeService.copyTree({treeid : $routeParams.treeId, ktreeid : $scope.selectedktreeId}, function(response) {
				reloadTree();
			});
		}
	};
	
    $scope.logout = function () {
        AuthenticationService.logout();
    };
}]);