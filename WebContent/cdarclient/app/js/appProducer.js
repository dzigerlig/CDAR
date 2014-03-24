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


/*app.factory('TreeService', function($resource) {
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

app.factory('UserService', function($resource) {
	return $resource('../webapi/users', {}, {
		'getUsers' : {
			method : 'GET',
			isArray : true
		}
	});
});

app.controller("KnowledgeTreeController", [ '$scope', '$routeParams', '$location', 'TreeService', 'AuthenticationService', 'UserService',
                                        		function($scope, $routeParams, $location, TreeService, AuthenticationService, UserService) {
	$scope.knowledgetree;
	$scope.users;
	
	TreeService.getTree({treeid:$routeParams.treeId}, function(response) {
		$scope.knowledgetree = response;
	});
	
	UserService.getUsers(function(response) {
		$scope.users = response;
	});
}]);*/

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