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


function getLinks(TreeService) {
	TreeService.getLinks(function(response) {
		makeNodeHierarchy(response);
		w_launch();
	});
};
