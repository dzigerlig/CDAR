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

app.factory('KnowledgeTreeService', function($resource) {
	return $resource('http://localhost:8080/CDAR/webapi/ktree/:action/1/:treeid', {}, {
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
		}
	});
});

app.controller("HomeProducerController", [ '$scope', '$location', 'KnowledgeTreeService', 'AuthenticationService',
                                      		function($scope, $location, KnowledgeTreeService, AuthenticationService) {
	$scope.knowledgeTrees;
	$scope.newTreeName = "";
	
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

app.controller("KnowledgeTreeController", [ '$scope', '$routeParams', 'KnowledgeTreeService', 'AuthenticationService', 'UserService',
                                        		function($scope, $routeParams, KnowledgeTreeService, AuthenticationService, UserService) {
	$scope.knowledgetree;
	
	KnowledgeTreeService.getTree({treeid:$routeParams.treeId}, function(response) {
		$scope.knowledgetree = response;
	});
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