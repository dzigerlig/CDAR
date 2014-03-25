app.factory('KnowledgeTreeService', function($resource) {
	return $resource('../webapi/ktree/:action/1/:treeid', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		},
		'postEntry' : {
			method : 'POST'
		},
		'removeTree' : {
			method : 'GET',
			params: {
				action: 'delete'
					}
		},
		'getTree' : {
			method : 'GET',
			isArray : false
		},
		'getNodes' : {
			method : 'GET',
			isArray : true,
			params: {
				action: 'nodes'
			}
		}
	});
});

app.controller("HomeProducerController", [ '$scope', '$location', 'KnowledgeTreeService', 'AuthenticationService', 'UserService',
                                      		function($scope, $location, KnowledgeTreeService, AuthenticationService, UserService) {
	$scope.knowledgeTrees;
	$scope.newTreeName = "";
	$scope.UserService = UserService;
	
	var reloadTrees = function() {
		KnowledgeTreeService.query(function(response) {
			$scope.knowledgeTrees = response;
		});
	};
	
	reloadTrees();
	
	$scope.addNewTree = function() {
		KnowledgeTreeService.postEntry($scope.newTreeName, function(response) {
			if (response[0] == 1) {
				$scope.newTreeName = '';
				reloadTrees();
			} else {
				alert("Error: KnowledgeTree NOT added!");
			}
		});
	};
	
	$scope.deleteTree = function(id) {
		KnowledgeTreeService.removeTree({treeid : id}, function(response) {
			reloadTrees();
		});
	};
}]);

app.controller("KnowledgeTreeController", [ '$scope', '$routeParams', 'KnowledgeTreeService', 'AuthenticationService', 'WikiService', 'UserSerivce',
                                        		function($scope, $routeParams, KnowledgeTreeService, AuthenticationService, WikiService, UserService) {
	$scope.knowledgetree;
	$scope.nodes;
	$scope.UserService = UserService;
	
	$scope.wikiText = "no wiki entry selected";
	
	KnowledgeTreeService.getTree({treeid:$routeParams.treeId}, function(response) {
		$scope.knowledgetree = response;
	});
	
	KnowledgeTreeService.getNodes({treeid:$routeParams.treeId}, function(response) {
		$scope.nodes = response;
	});
	
	$scope.changeNode = function(id) {
		$scope.wikiText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
		WikiService.getWikiEntry({role:'producer', nodeid: id}, function(response) {
			$scope.wikiText = response.wikiContentHtml;
		});
	};
}]);

/*

app.controller("TreeController", [
    '$scope',
    'AuthenticationService',
    'TreeService',
    function ($scope, AuthenticationService, TreeService) {
        initializeJsPlumb();
        $scope.title = "TEST";
        $scope.message = "Mouse Over these images to see a directive at work!";

        $scope.hello = function (att) {
            console.log(att);
        };

        $scope.logout = function () {
            AuthenticationService.logout();
        };

        $scope.removeNode = function (id) {
            TreeService.removeNodes.removeNodes({ id: id });
        };

        TreeService.getNodes.getNodes(function (response) {
            myFunction(response);
        });
    } ]);


app.factory('TreeService',function ($resource) {
    return{
        removeNodes: $resource('http://localhost:8080/CDAR/webapi/nodes/removeNode/:id', {}, {
            'removeNodes': {
                params:{id:'@id'},
                method: 'OPTIONS'
            }
        }),

        getNodes: $resource('http://localhost:8080/CDAR/webapi/nodes', {}, {
            'getNodes': {
                method: 'GET',
                isArray: true
            }
        })   }
});
*/