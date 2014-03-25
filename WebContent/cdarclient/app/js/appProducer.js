app.factory('TreeService', function($resource) {
	return $resource('../webapi/1/ktree/:entity/:action/:ktreeid', {}, {
//Tree
		'query' : {
			method : 'GET',
			isArray : true,
		},
		'postEntry' : {
			method : 'POST'

		},
		'removeTree' : {
			method : 'GET',
			params: {
				entity: 'delete',
				ktreeid:'1'
					}
		},
		'getTree' : {
			method : 'GET',
			isArray : true,
		},
		
		// Nodes
		'getNodes' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'nodes',
				ktreeid:'1'
			}
		},
		'addNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'add',
				ktreeid:'1'
			}
		},
		'deleteNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'delete',
				ktreeid:'1'
			}
		},

		// Links
		'getLinks' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'links',
				ktreeid:'1'
			}
		},
		'addLink' : {
			method : 'POST',
			params : {
				entity : 'links',
				action : 'add',
				ktreeid:'1'
			}
		},
		'deleteLink' : {
			method : 'POST',
			params : {
				entity : 'links',
				action : 'delete',
				ktreeid:'1'
			}
		}
	});
});
/*
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
});*/

app.controller("HomeProducerController", [ '$scope', '$location', 'TreeService', 'AuthenticationService', 'UserService',
                                      		function($scope, $location, TreeService, AuthenticationService, UserService) {
	$scope.knowledgeTrees;
	$scope.newTreeName = "";
	$scope.UserService = UserService;
	
	var reloadTrees = function() {
		TreeService.query(function(response) {
			console.log(response);

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
		TreeService.removeTree( function(response) {
			reloadTrees();
		});
	};
}]);

app.controller("KnowledgeTreeController", [ '$scope', '$routeParams', 'TreeService', 'AuthenticationService', 'WikiService', 'UserService',
                                        		function($scope, $routeParams, TreeService, AuthenticationService, WikiService, UserService) {
	$scope.knowledgetree;
	$scope.nodes;
	$scope.UserService = UserService;
	$scope.selectedNode = 0;
	
	$scope.wikiHtmlText = "no wiki entry selected";
	$scope.wikiEntry;
	
	TreeService.getTree( function(response) {
		console.log(response);
		$scope.knowledgetree = response;
		
	});
	
	TreeService.getNodes(function(response) {
		console.log(response);

		$scope.nodes = response;
	});
	
	var setLoading = function() {
		$scope.wikiHtmlText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
	};
	
	var changeWikiFields = function(response) {
		$scope.wikiEntry = response;
		$scope.wikiHtmlText = $scope.wikiEntry.wikiContentHtml;
		$("#wikiArea").val($scope.wikiEntry.wikiContentPlain);
	};
	
	$scope.changeNode = function(id) {
		setLoading();

		$scope.selectedNode = id;
		
		WikiService.getWikiEntry({role:'producer', nodeid: $scope.selectedNode}, function(response) {
			changeWikiFields(response);
		});
	};
	
	$scope.saveWikiEntry = function() {
		if ($scope.selectedNode!=0) {
			$scope.wikiMarkupText = $("#wikiArea").val();
			$scope.wikiEntry.wikiContentPlain = $scope.wikiMarkupText;
			setLoading();
			WikiService.postEntry({role:'producer'}, $scope.wikiEntry, function(response) {
				changeWikiFields(response);
			});
		}
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