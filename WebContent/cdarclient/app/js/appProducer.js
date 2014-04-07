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
							$scope.selectedNode;
							$scope.selectedNodeId = 0;
							$scope.selectedNodeName = '';
							
							// SUBNODES //
							$scope.subnodes;
							$scope.selectedSubnode;
							$scope.selectedSubnodeId = 0;
							$scope.selectedSubnodeName = '';
							$scope.newSubNodeName = '',
							
							$scope.addNewSubNode = function() {
								alert($scope.newSubNodeName);
							};
							
							TreeService.getSubNodes({
								ktreeid : 1,
								entityid : 1
							}, function(response) {
								alert(JSON.stringify(response));
							});
							
							// END SUBNODES //

							$scope.nodeTitle;
							$scope.wikiHtmlText = "";
							
							$scope.nodetabs = [
							               { title:"READ" },
							               { title:"WRITE" }
							];
							
							$scope.subnodetabs = [
								               { title:"READ" },
								               { title:"WRITE" }
							];
							
							var showNodeTitle = function() {
								if ($scope.selectedNodeId!=0) {
									$scope.nodeTitle = "Selected node: " + $scope.selectedNodeName;
								} else {
									$scope.nodeTitle = "Selected node: no node selected";
								}
							};
							
							var showSubNodeTitle = function() {
								if ($scope.selectedSubNodeId!=0) {
									$scope.subNodeTitle = "Selected subnode: " + $scope.selectedSubnodeName;
								} else {
									$scope.subNodeTitle = "Selected node: no subnode selected";
								}
							};
							
							showNodeTitle();
							showSubNodeTitle();
							
							var switchNodeToRead = function() {
								$scope.nodetabs[0].active = true;
								$scope.nodetabs[1].active = true;
							};
							
							var setLoading = function() {
								$scope.wikiHtmlText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
							};

							var changeWikiFields = function() {
								$scope.wikiHtmlText = $scope.selectedNode.wikiContentHtml;
								$("#wikiArea").val(
										$scope.selectedNode.wikiContentPlain);
							};

							$scope.changeNode = function(id, name) {
								setLoading();
								$scope.selectedNodeId = id;
								$scope.selectedNodeName = name;
								showNodeTitle();
								
								WikiService.getWikiEntry({
									role : 'producer',
									entity : 'node',
									nodeid : id
								}, function(response) {
									$scope.selectedNode = response;
									changeWikiFields();
								});
							};

							$scope.saveWikiEntry = function() {
								if ($scope.selectedNode != 0) {
									$scope.wikiMarkupText = $("#wikiArea")
											.val();
									$scope.selectedNode.wikiContentPlain = $scope.wikiMarkupText;
									switchNodeToRead();
									setLoading();
									WikiService.postEntry({
										role : 'producer',
										entity: 'node'
									}, $scope.selectedNode, function(response) {
										changeWikiFields(response);
									});
								};
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

											directoryDataToArray(resDirectory,
													resNodes);
											$scope.getSubNodes(resNodes, TreeService);

										});

							});

							$scope.getSubNodes = function(resNodes, TreeService) {
								TreeService.getSubNodes({
									ktreeid : $routeParams.treeId
								},resNodes, function(resSubNodes) {
									drawExistingNodes(resNodes, resSubNodes);
									$scope.getLinks(TreeService);

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
} ]);