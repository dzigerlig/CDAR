var reload = false;
function getReload() {
	return reload;
}
function setReload(value) {
	reload = value;
}

app.controller("HomeProducerController", [
		'$scope',
		'$location',
		'TreeService',
		'AuthenticationService',
		'UserService',
		function($scope, $location, TreeService, AuthenticationService,
				UserService) {
			$scope.knowledgeTrees;
			$scope.newTreeName = "";
			$scope.UserService = UserService;

			var reloadTrees = function() {
				TreeService.getTrees(function(response) {
					$scope.knowledgeTrees = response;
				});
			};

			reloadTrees();

			$scope.addNewTree = function() {
				TreeService.postEntry($scope.newTreeName, function(response) {
					if (response.id != 0) {
						$scope.newTreeName = '';
						reloadTrees();
					} else {
						alert("Error: KnowledgeTree NOT added!");
					}
				});
			};

			$scope.deleteTree = function(id) {
				TreeService.removeTree({
					ktreeid : id
				}, function(response) {
					reloadTrees();
				});
			};
		} ]);

app
		.controller(
				"KnowledgeTreeController",
				[
						'$scope',
						'$routeParams',
						'TreeService',
						'AuthenticationService',
						'WikiService',
						'UserService',
						'$route',
						function($scope, $routeParams, TreeService,
								AuthenticationService, WikiService,
								UserService, $route) {

							// Workaround draw links not correct
							if (getReload()) {
								setReload(false);
								location.reload();
							}
							setReload(true);
							//

							initializeJsPlumb();

							$scope.knowledgetree;
							$scope.nodes;
							$scope.UserService = UserService;
							$scope.selectedNode = 0;

							$scope.wikiHtmlText = "no wiki entry selected";
							$scope.wikiEntry;
							
							$scope.tabs = [
							               { title:"READ" },
							               { title:"WRITE" }
							             ];
							
							var switchToRead = function() {
								$scope.tabs[0].active = true;
								$scope.tabs[1].active = true;
							};
							
							TreeService.getTree({
								ktreeid : $routeParams.treeId
							}, function(response) {
								$scope.knowledgetree = response;
							});

							$scope.logout = function() {
								AuthenticationService.logout();
							};

							TreeService.getDictionaries({
								ktreeid : $routeParams.treeId
							}, function(resDirectory) {
								TreeService.getNodes({
									ktreeid : $routeParams.treeId
								},
										function(resNodes) {

											$scope.getLinks(TreeService);
											directoryDataToArray(resDirectory,
													resNodes);
											$scope.getSubNodes(resNodes, TreeService);
										});

							});

							$scope.getSubNodes = function(resNodes, TreeService) {
								TreeService.getSubNodes({
									ktreeid : $routeParams.treeId
								},resNodes, function(resSubNodes) {
									console.log(resNodes);
									console.log(resSubNodes);
									drawExistingNodes(resNodes, resSubNodes);
								});
							};

							$scope.getLinks = function(TreeService) {
								TreeService.getLinks({
									ktreeid : $routeParams.treeId
								}, function(response) {
									makeNodeHierarchy(response);
									w_launch();
								});
							};

							$scope.addNode = function(did) {
								TreeService.addNode({
									ktreeid : $routeParams.treeId
								}, {
									refTreeId : $routeParams.treeId,
									did : did

								}, function(response) {
									createNode(response);
								});
							};

							$scope.deleteNode = function(id) {
								TreeService.deleteNode({
									ktreeid : $routeParams.treeId
								}, id);
							};

							$scope.dropNode = function(e, id) {
								TreeService.dropNode({
									ktreeid : $routeParams.treeId
								}, id, function(response) {
									addHTMLNode(response, e);
								});
							};

							$scope.renameNode = function(id, newTitle, did) {
								TreeService.renameNode({
									ktreeid : $routeParams.treeId
								}, {
									id : id,
									title : newTitle,
									did : did
								});
							};

							$scope.undropNode = function(id) {
								TreeService.undropNode({
									ktreeid : $routeParams.treeId
								}, id);
							};

							$scope.moveNode = function(id, newParentId) {
								TreeService.moveNode({
									ktreeid : $routeParams.treeId
								}, {
									id : id,
									did : newParentId
								});
							};

							$scope.addLink = function(treeId, sourceId,
									targetId, connection) {
								TreeService.addLink({
									ktreeid : $routeParams.treeId
								}, {
									refTreeId : treeId,
									sourceId : sourceId,
									targetId : targetId
								}, function(response) {
									setLinkId(connection, response.id);
								});
							};

							$scope.deleteLink = function(id) {
								TreeService.deleteLink({
									ktreeid : $routeParams.treeId
								}, id);
							};

							$scope.addDirectory = function(parentid) {
								TreeService.addDirectory({
									ktreeid : $routeParams.treeId
								}, {
									ktrid : $routeParams.treeId,
									parentid : parentid
								}, function(response) {
									createDirectory(response);
								});
							};

							$scope.renameDirectory = function(id, newTitle) {
								TreeService.renameDirectory({
									ktreeid : $routeParams.treeId
								}, {
									id : id,
									title : newTitle
								});
							};

							$scope.deleteDirectory = function(id) {
								// TODO beachten mit cascade in datanbank oder
								// ohne
								TreeService.deleteDirectory({
									ktreeid : $routeParams.treeId
								}, id);
							};

							$scope.moveDirectory = function(id, newParentId) {
								TreeService.moveDirectory({
									ktreeid : $routeParams.treeId
								}, {
									id : id,
									parentId : newParentId
								});
							};

							var setLoading = function() {
								$scope.wikiHtmlText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
							};

							var changeWikiFields = function(response) {
								$scope.wikiEntry = response;
								$scope.wikiHtmlText = $scope.wikiEntry.wikiContentHtml;
								$("#wikiArea").val(
										$scope.wikiEntry.wikiContentPlain);
							};

							$scope.changeNode = function(id) {
								setLoading();

								$scope.selectedNode = id;

								WikiService.getWikiEntry({
									role : 'producer',
									entity : 'node',
									nodeid : $scope.selectedNode
								}, function(response) {
									changeWikiFields(response);
								});
							};

							$scope.saveWikiEntry = function() {
								if ($scope.selectedNode != 0) {
									$scope.wikiMarkupText = $("#wikiArea")
											.val();
									$scope.wikiEntry.wikiContentPlain = $scope.wikiMarkupText;
									switchToRead();
									setLoading();
									WikiService.postEntry({
										role : 'producer',
										entity: 'node'
									}, $scope.wikiEntry, function(response) {
										changeWikiFields(response);
									});
								}
							};
							
						} ]);