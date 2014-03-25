// Important: call getNodes before getLinks

app
		.controller(
				"TreeController",
				[
						'$scope',
						'AuthenticationService',
						'TreeService',
						function($scope, AuthenticationService, TreeService) {
							initializeJsPlumb();
							$scope.title = "TEST";
							$scope.message = "Mouse Over these images to see a directive at work!";

							$scope.hello = function(att) {
								console.log(att);
							};

							$scope.logout = function() {
								AuthenticationService.logout();
							};

							TreeService.getNodes(function(response) {
								drawExistingNodes(response);
								getLinks(TreeService);
							});

							$scope.addNode = function(e, data) {
								console.log(data);
								TreeService.addNode({
									refTreeId : 1,
									title : data.element.innerText
								}, function(response) {
									addHTMLNode(response, e, data);
								});
							};

							$scope.removeNodeById = function(id) {
								TreeService.deleteNode(id);
							};

							$scope.addLink = function(treeId, sourceId,
									targetId, connection) {
								TreeService.addLink({
									refTreeId : treeId,
									sourceId : sourceId,
									targetId : targetId
								}, function(response) {
									setLinkId(connection, response.id);
								});
							};

							$scope.removeLinkById = function(id) {
								TreeService.deleteLink(id);
							};
						} ]);

app.factory('TreeService', function($resource) {
	return $resource('../webapi/1/ktree/:entity/:action/1', {}, {

		// Nodes
		'getNodes' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'nodes'
			}
		},
		'addNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'add'
			}
		},
		'deleteNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'delete'
			}
		},

		// Links
		'getLinks' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'links'
			}
		},
		'addLink' : {
			method : 'POST',
			params : {
				entity : 'links',
				action : 'add'
			}
		},
		'deleteLink' : {
			method : 'POST',
			params : {
				entity : 'links',
				action : 'delete'
			}
		}
	});
});

function getLinks(TreeService) {
	TreeService.getLinks(function(response) {
		makeNodeHierarchy(response);
		w_launch();
	});
};
