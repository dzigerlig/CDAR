// Important: call getNodes before getLinks

app
		.controller(
				"TreeController",
				[
						'$scope',
						'AuthenticationService',
						'TreeService',
						function($scope, AuthenticationService, 
								TreeService) {
							initializeJsPlumb();
							$scope.title = "TEST";
							$scope.message = "Mouse Over these images to see a directive at work!";

							$scope.hello = function(att) {
								console.log(att);
							};

							$scope.logout = function() {
								AuthenticationService.logout();
							};

							TreeService.getNodes.getNodes(function(response) {
								drawExistingNodes(response);
								getLinks(TreeService);
							});

							$scope.addNode = function(e,data) {
								console.log(data);
								TreeService.addNode.addNode({refTreeId:1,title:data.element.innerText},function(response) {
									addHTMLNode(response,e,data);
								});
							};

							$scope.removeNodeById = function(id) {
								TreeService.removeNode.removeNode(id);
							};							

							$scope.addLink = function(treeId, sourceId, targetId,connection) {
								TreeService.addLink.addLink({refTreeId:treeId, sourceId:sourceId,targetId:targetId},function(response) {
									setLinkId(response,connection);
								});
							};

							$scope.removeLinkById = function(id) {
								TreeService.removeLink.removeLink(id);
							};
						} ]);

app.factory('TreeService', function($resource) {
	return {
		getNodes : $resource('../webapi/nodes', {}, {
			'getNodes' : {
				method : 'GET',
				isArray : true
			}
		}),

		addNode : $resource('../webapi/nodes/addNode',
				{}, {
					'addNode' : {
						method : 'POST',
							isArray:false
					}
				}),

		removeNode : $resource(
				'../webapi/nodes/removeNode', {}, {
					'removeNode' : {
						method : 'POST'
					}
				}),

		getLinks : $resource('../webapi/links', {}, {
			'getLinks' : {
				method : 'GET',
				isArray : true
			}
		}),

		addLink : $resource('../webapi/links/addLink',
				{}, {
					'addLink' : {
						method : 'POST',
						params: {},
						isArray: false
					}
				}),

		removeLink : $resource(
				'../webapi/links/removeLink', {}, {
					'removeLink' : {
						method : 'POST'
					}
				})
	};
});

function getLinks(TreeService){
	TreeService.getLinks.getLinks(function(response) {
		makeNodeHierarchy(response);
		w_launch();
	});
};
