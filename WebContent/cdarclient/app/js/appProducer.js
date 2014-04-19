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
				TreeService.addTree($scope.newTreeName, function(response) {
					if (response.id != 0) {
						$scope.newTreeName = '';
						reloadTrees();
					} else {
						alert("Error: KnowledgeTree NOT added!");
					}
				});
			};

			$scope.deleteTree = function(id) {
				TreeService.removeTree(id, function(response) {
					if (response.bool) {
						reloadTrees();
					}
				});
			};

			$scope.updateTreeId;
			$scope.updateTreeTitle;

			$scope.editTreeTitle = function(id, title) {
				$scope.updateTreeId = id;
				$scope.updateTreeTitle = title;
				$('#treeModal').modal().show();
			};

			$scope.saveTreeTitle = function() {
				$('#treeModal').modal('hide');
				TreeService.renameTree({
					id : $scope.updateTreeId,
					title : $scope.updateTreeTitle
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

							$scope.treeId = $routeParams.treeId;
							$scope.UserService = UserService;
							$scope.knowledgetree;
							$scope.nodes;
							$scope.selectedNode;
							$scope.selectedNodeId = 0;
							$scope.selectedNodeName = '';

							// SUBNODES //
							$scope.subnodes;
							$scope.selectedSubnode;
							$scope.selectedSubnodeId = 0;
							$scope.selectedSubnodeName = '';
							$scope.newSubnodeName = '';
							$scope.subnodeHtmlText;

							// TEMPLATES //
							$scope.templates;
							$scope.templateid;

							TreeService.getTemplates({
								ktreeid : $routeParams.treeId
							}, function(response) {
								$scope.templates = response;
							});

							$scope.applyTemplate = function() {
								alert($scope.listSelectedTemplateId);
							};

							$scope.revertTemplate = function() {
								alert($scope.listSelectedTemplateId);
							};

							var getSubnodes = function() {
								TreeService.getSubnodes({
									ktreeid : $scope.knowledgetree.id,
									entityid : $scope.selectedNodeId
								}, function(response) {
									$scope.subnodes = response;
								});
							};

							$scope.getSubnodesOfNode = function(idObject) {
								var identity;
								var changes = null;
								if (typeof idObject === 'object'
										|| idObject === undefined) {
									if (typeof idObject === 'object') {
										changes = idObject;
									}
									identity = $scope.selectedNodeId;
								} else {
									identity = idObject;
								}
								TreeService.getSubnodes({
									ktreeid : $scope.knowledgetree.id,
									entityid : identity
								}, function(response) {
									$scope.subnodes = response;
									updateSubnodesOfNode(response, identity,
											changes);
								});
							};

							// END SUBNODES //

							$scope.nodeTitle;
							$scope.wikiHtmlText = "";

							$scope.nodetabs = [ {
								title : "READ"
							}, {
								title : "WRITE"
							} ];

							$scope.subnodetabs = [ {
								title : "READ"
							}, {
								title : "WRITE"
							} ];

							$scope.addNewSubnode = function() {
								TreeService.addSubnode({
									ktreeid : $scope.knowledgetree.id
								}, {
									knid : $scope.selectedNodeId,
									title : $scope.newSubnodeName
								}, function(response) {
									$scope.getSubnodesOfNode();
									// getSubnodes();
									$scope.newSubnodeName = '';
								});
							};

							$scope.changeSubnode = function(id, name) {
								setLoadingSubnode();
								$scope.selectedSubnodeId = id;
								// 1hans ok nice
								$scope.selectedSubnodeName = name;
								WikiService.getWikiEntry({
									role : 'producer',
									entity : 'subnode',
									nodeid : id
								}, function(response) {
									$scope.selectedSubnode = response;
									changeWikiFieldsSubnode();
									// load wiki fields
								});
							};

							var changeWikiFieldsSubnode = function() {
								$scope.subnodeHtmlText = $scope.selectedSubnode.wikiContentHtml;
								$("#wikiSubnodeArea")
										.val(
												$scope.selectedSubnode.wikiContentPlain);
							};

							$scope.saveWikiSubnodeEntry = function() {
								if ($scope.selectedSubnode != 0) {
									$scope.selectedSubnode.wikiContentPlain = $(
											"#wikiSubnodeArea").val();
									switchSubnodeToRead();
									setLoadingSubnode();
									WikiService.postEntry({
										role : 'producer',
										entity : 'subnode'
									}, $scope.selectedSubnode, function(
											response) {
										$scope.selectedSubnode = response;
										changeWikiFieldsSubnode();
									});
								}
								;
							};

							$scope.deleteSubnode = function(id) {
								TreeService.deleteSubnode({
									ktreeid : $routeParams.treeId
								}, id, function(response) {
									if (response.bool) {
										$scope.getSubnodesOfNode(response);
										// getSubnodes();
									}
								});
							};

							var showNodeTitle = function() {
								if ($scope.selectedNodeId != 0) {
									$scope.nodeTitle = "Selected node: "
											+ $scope.selectedNodeName;
								} else {
									$scope.nodeTitle = "Selected node: no node selected";
								}
							};

							showNodeTitle();

							var switchNodeToRead = function() {
								$scope.nodetabs[0].active = true;
								$scope.nodetabs[1].active = false;
							};

							var switchSubnodeToRead = function() {
								$scope.subnodetabs[0].active = true;
								$scope.subnodetabs[1].active = false;
							};

							var setLoadingNode = function() {
								$scope.wikiHtmlText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
							};

							var setLoadingSubnode = function() {
								$scope.subnodeHtmlText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
							};

							var changeWikiFields = function() {
								$scope.wikiHtmlText = $scope.selectedNode.wikiContentHtml;
								$("#wikiArea").val(
										$scope.selectedNode.wikiContentPlain);
							};

							$scope.changeNode = function(id, name) {
								setLoadingNode();
								$scope.selectedNodeId = id;
								$scope.selectedNodeName = name;
								showNodeTitle();
								getSubnodes();
								WikiService.getWikiEntry({
									role : 'producer',
									entity : 'node',
									nodeid : id
								}, function(response) {
									$scope.selectedNode = response;
									changeWikiFields();
								});
							};

							$scope.saveWikiNodeEntry = function() {
								if ($scope.selectedNode != 0) {
									$scope.selectedNode.wikiContentPlain = $(
											"#wikiArea").val();
									switchNodeToRead();
									setLoadingNode();
									WikiService.postEntry({
										role : 'producer',
										entity : 'node'
									}, $scope.selectedNode, function(response) {
										$scope.selectedNode = response;
										changeWikiFields(response);
									});
								}
								;
							};

							TreeService.getTree({
								ktreeid : $routeParams.treeId
							}, function(response) {
								$scope.knowledgetree = response;
							});

							$scope.logout = function() {
								AuthenticationService.logout();
							};

							TreeService.getDirectories({
								ktreeid : $routeParams.treeId
							}, function(resDirectory) {
								TreeService.getNodes({
									ktreeid : $routeParams.treeId
								},
										function(resNodes) {

											directoryDataToArray(resDirectory,
													resNodes);
											$scope.getSubnodes(resNodes);

										});

							});

							$scope.getSubnodes = function(resNodes) {
								TreeService.getSubnodes({
									ktreeid : $routeParams.treeId
								}, resNodes, function(resSubnodes) {
									drawExistingNodes(resNodes, resSubnodes);
									$scope.getLinks(resSubnodes);
								});
							};

							$scope.getLinks = function(resSubnodes) {
								TreeService.getLinks({
									ktreeid : $routeParams.treeId
								}, resSubnodes, function(response) {
									makeNodeHierarchy(response, resSubnodes);
									w_launch();
								});
							};

							$scope.updateLink = function(linkId, subnodeId) {
								TreeService.updateLink({
									ktreeid : $routeParams.treeId
								}, {
									id : linkId,
									ksnid : subnodeId
								});
							};

							$scope.addNode = function(did) {
								TreeService.addNode({
									ktreeid : $routeParams.treeId
								}, {
									ktrid : $routeParams.treeId,
									did : did
								}, function(response) {
									createNode(response);
								});
							};

							$scope.addNodeCopy = function(node) {
								TreeService.addNode({
									ktreeid : $routeParams.treeId
								}, {
									ktrid : $routeParams.treeId,
									title : node.text,
									did : 0
								}, function(response) {
									prepareForSetId(node, response.id);
								});
							};

							$scope.deleteNode = function(id) {
								detachNode(id);
								TreeService.deleteNode({
									ktreeid : $routeParams.treeId
								}, id);
							};

							$scope.getNode = function(id) {
								TreeService.getNode({
									ktreeid : $routeParams.treeId,
									entityid : id
								}, function(node) {
									setMovedNode(node);
								});
							};

							$scope.dropNode = function(e, id) {
								TreeService.dropNode({
									ktreeid : $routeParams.treeId
								}, id, function(response) {
									addHTMLNode(response, e);
								});
							};

							$scope.renameNode = function(id, newTitle, did) {
								renameNode(id, newTitle);
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
									ktrid : treeId,
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

							$scope.addDirectoryCopy = function(node) {
								TreeService.addDirectory({
									ktreeid : $routeParams.treeId
								}, {
									ktrid : $routeParams.treeId,
									title : node.text,
									parentid : 0
								}, function(response) {
									prepareForSetId(node, response.id);
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
								TreeService.deleteDirectory({
									ktreeid : $routeParams.treeId
								}, id, function(response) {
									if (response.bool) {
										// removeNodes(response.changedEntities);
									}
								});
							};

							$scope.moveDirectory = function(id, newParentId) {
								TreeService.moveDirectory({
									ktreeid : $routeParams.treeId
								}, {
									id : id,
									parentid : newParentId
								});
							};

							$scope.zoomUpNode = function(nodeid) {
								TreeService.zoomUpNode({
									ktreeid : $routeParams.treeId,
									entityid : nodeid
								}, function(resNodes) {
									$scope.zoomUpSubnode(nodeid, resNodes);

								});
							};

							$scope.zoomUpSubnode = function(nodeid, resNodes) {
								TreeService.zoomUpSubnode({
									ktreeid : $routeParams.treeId,
									entityid : nodeid
								}, function(resSubnodes) {
									drawExistingNodes(resNodes, resSubnodes);
									$scope.zoomUpLink(resSubnodes);
								});
							};

							$scope.zoomUpLink = function(resSubnodes) {
								TreeService.zoomUpLink({
									ktreeid : $routeParams.treeId,
									entityid : nodeid
								}, function(resLinks) {
									makeNodeHierarchy(resLinks, resSubnodes);
									w_launch();
								});
							};

							$scope.zoomDownNode = function(nodeid) {
								TreeService.zoomDownNode({
									ktreeid : $routeParams.treeId,
									entityid : nodeid
								}, function(resNodes) {

								});
							};

							$scope.updateSubnodeId;
							$scope.updateSubnodeTitle;

							$scope.editSubnodeTitle = function(id, title) {
								$scope.updateSubnodeId = id;
								$scope.updateSubnodeTitle = title;
								$('#subnodeModal').modal().show();
							};

							$scope.saveSubnodeTitle = function() {
								$('#subnodeModal').modal('hide');
								TreeService.renameSubnode({
									id : $scope.updateSubnodeId,
									title : $scope.updateSubnodeTitle
								}, function(response) {
									if (response.bool) {
										$scope.getSubnodesOfNode(response);
										// getSubnodes();
									}
									// getSubnodes();
								});
							};

							$scope.moveSubnodeUp = function(id) {
								TreeService.moveSubnodeUp({
									id : id
								}, function(response) {
									if (response.bool) {
										$scope.getSubnodesOfNode();
										// getSubnodes();
									}
								});
							};

							$scope.moveSubnodeDown = function(id) {
								TreeService.moveSubnodeDown({
									id : id
								}, function(response) {
									if (response.bool) {
										$scope.getSubnodesOfNode();
										// getSubnodes();
									}
								});
							};
						} ]);
