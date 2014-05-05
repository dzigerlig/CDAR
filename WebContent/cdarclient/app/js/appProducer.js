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
			$scope.knowledgeTrees = "";
			$scope.newTreeName = "";
			$scope.UserService = UserService;

			var reloadTrees = function() {
				TreeService.getTrees({
					entity1 : 'ktrees'
				}, function(response) {
					$scope.knowledgeTrees = response;
				}, function(error) {
					// error handling
				});
			};

			reloadTrees();

			$scope.addNewTree = function() {
				TreeService.addTree({
					entity1 : 'ktrees'
				}, {
					title : $scope.newTreeName
				}, function(response) {
					$scope.newTreeName = '';
					reloadTrees();
				}, function(error) {
					// todo error handling
					// noty({type: 'alert', text : 'knowledge not added
					// successfully', timeout: 1500});
				});
			};

			$scope.deleteTree = function(treeid) {
				TreeService.deleteTree({
					entity1 : 'ktrees'
				}, {
					id : treeid
				}, function(response) {
					reloadTrees();
					noty({
						type : 'success',
						text : 'knowledge tree deleted successfully',
						timeout : 1500
					});
				}, function(error) {
					// todo error handling
				});
			};

			$scope.saveKnowledgeTreeTitle = function(data, id) {
				var tree = $.grep($scope.knowledgeTrees, function(t) {
					return t.id === id;
				})[0];
				tree.title = data;

				TreeService.updateTree({
					entity1 : 'ktrees',
					id1 : tree.id
				}, tree, function(response) {
					// noty({type: 'success', text : 'knowledge tree renamed
					// successfully', timeout: 1500});
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
						'UserService',
						'$route',
						function($scope, $routeParams, TreeService,
								AuthenticationService, UserService, $route) {

							// Workaround draw links not correct
							if (getReload()) {
								setReload(false);
								location.reload();
							}
							setReload(true);
							//

							myJsPlumb.initialize();

							$scope.treeId = $routeParams.treeId;
							$scope.UserService = UserService;
							$scope.knowledgetree = "";
							$scope.nodes = "";
							$scope.selectedNode = "";
							$scope.selectedNodeId = 0;
							$scope.selectedNodeName = '';

							// SUBNODES //
							$scope.subnodes = "";
							$scope.selectedSubnode = "";
							$scope.selectedSubnodeId = 0;
							$scope.selectedSubnodeName = '';
							$scope.newSubnodeName = 'Subnode';
							$scope.subnodeHtmlText = "";

							// TREE TITLE
							$scope.saveKnowledgeTreeTitle = function(title) {
								TreeService.updateTree({
									entity1 : 'ktrees',
									id1 : $scope.knowledgetree.id
								}, $scope.knowledgetree, function(response) {
									// noty({type: 'success', text :
									// 'knowledge tree renamed
									// successfully', timeout: 1500});
								}, function(error) {
									// todo error handling
								});
							};

							var getSubnodes = function() {
								TreeService.getSubnodes({
									entity1 : 'ktrees',
									id1 : $scope.knowledgetree.id,
									id2 : $scope.selectedNodeId,
								}, function(response) {
									$scope.subnodes = response;
								}, function(error) {
									// todo error handling
								});
							};

							$scope.getSubnodesOfNode = function(idObject) {
								var identity;
								var changes = null;
								if (typeof idObject === 'object' || idObject === undefined) {
									if (typeof idObject === 'object') {
										changes = idObject;
									}
									identity = $scope.selectedNodeId;
								} else {
									identity = idObject;
								}
								TreeService.getSubnodes({
									entity1 : 'ktrees',
									id1 : $scope.knowledgetree.id,
									id2 : identity,
								}, function(response) {
									$scope.subnodes = response;
									myJsPlumb.updateSubnodesOfNode(response,
											identity, changes);
								}, function(error) {
									// todo error handling
								});
							};

							// END SUBNODES //

							$scope.nodeTitle = "";
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
									entity1 : 'ktrees',
									id1 : $scope.knowledgetree.id,
									id2 : $scope.selectedNodeId
								}, {
									nodeId : $scope.selectedNodeId,
									title : $scope.newSubnodeName
								}, function(response) {
									$scope.getSubnodesOfNode();
									// getSubnodes();
									$scope.newSubnodeName = 'Subnode';
									// noty({type: 'success', text : 'subnode
									// added successfully', timeout: 1500});
								}, function(error) {
									// todo error handling
								});
							};

							$scope.changeSubnode = function(subnodeid, name) {
								setLoadingSubnode();
								$scope.selectedSubnodeId = subnodeid;
								$scope.selectedSubnodeName = name;
								TreeService.getSubnodeWiki({
									entity1 : 'ktrees',
									id1 : $scope.knowledgetree.id,
									id2 : $scope.selectedNodeId,
									id3 : subnodeid
								}, function(response) {
									$scope.selectedSubnode = response;
									changeWikiFieldsSubnode();
									// load wiki fields
								}, function(error) {
									// todo error handling
								});
							};

							var changeWikiFieldsSubnode = function() {
								$scope.subnodeHtmlText = $scope.selectedSubnode.wikiContentHtml;
								$("#wikiSubnodeArea")
										.val(
												$scope.selectedSubnode.wikiContentPlain);
							};

							$scope.saveWikiSubnodeEntry = function() {
								if ($scope.selectedSubnode !== 0) {
									$scope.selectedSubnode.wikiContentPlain = $(
											"#wikiSubnodeArea").val();
									switchSubnodeToRead();
									setLoadingSubnode();
									TreeService
											.updateSubnodeWiki(
													{
														entity1 : 'ktrees',
														id1 : $scope.knowledgetree.id,
														id2 : $scope.selectedNodeId,
														id3 : $scope.selectedSubnode.id
													},
													$scope.selectedSubnode,
													function(response) {
														$scope.selectedSubnode = response;
														changeWikiFieldsSubnode();
														noty({
															type : 'success',
															text : 'subnode text edited successfully',
															timeout : 1500
														});
													}, function(error) {
														// todo error handling
													});
								}
							};

							$scope.deleteSubnode = function(subnodeId) {
								TreeService.deleteSubnode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : $scope.selectedNodeId,
								}, {
									id : subnodeId
								}, function(response) {
									$scope.getSubnodesOfNode(response);
									noty({
										type : 'success',
										text : 'subnode deleted successfully',
										timeout : 1500
									});
								}, function(error) {
									// todo errorhandling
								});
							};

							var showNodeTitle = function() {
								if ($scope.selectedNodeId !== 0) {
									$scope.nodeTitle = "Selected node: " + $scope.selectedNodeName;
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
								TreeService.getNodeWiki({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : id
								}, function(response) {
									$scope.selectedNode = response;
									changeWikiFields();
								}, function(error) {
									// todo error handling
								});
							};

							$scope.saveWikiNodeEntry = function() {
								if ($scope.selectedNode !== 0) {
									$scope.selectedNode.wikiContentPlain = $(
											"#wikiArea").val();
									switchNodeToRead();
									setLoadingNode();
									TreeService
											.updateNodeWiki(
													{
														entity1 : 'ktrees',
														id1 : $routeParams.treeId,
														id2 : $scope.selectedNode.id
													},
													$scope.selectedNode,
													function(response) {
														$scope.selectedNode = response;
														changeWikiFields(response);
														noty({
															type : 'success',
															text : 'node text edited successfully',
															timeout : 1500
														});
													}, function(error) {
														// todo errorhandling
													});
								}
							};

							TreeService.getTree({
								entity1 : 'ktrees',
								id1 : $routeParams.treeId
							}, function(response) {
								$scope.knowledgetree = response;
							}, function(error) {
								// todo error handling
							});

							$scope.logout = function() {
								AuthenticationService.logout();
							};

							TreeService.getDirectories({
								entity1 : 'ktrees',
								id1 : $routeParams.treeId
							}, function(resDirectory) {
								TreeService.getNodes({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								}, function(resNodes) {
									myJsTree.directoryDataToArray(resDirectory,
											resNodes);
									$scope.getSubnodes(resNodes);
								}, function(error) {
									// todo error handling
								});
							}, function(error) {
								// todo error handling
							});

							$scope.getSubnodes = function(resNodes) {
								TreeService.getSubnodesFromTree({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
								}, function(resSubnodes) {
									myJsPlumb.drawExistingNodes(resNodes,
											resSubnodes);
									$scope.getLinks(resSubnodes);
								});
							};

							$scope.getLinks = function(resSubnodes) {
								TreeService.getLinks({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
								}, resSubnodes, function(response) {
									myJsPlumb.makeNodeHierarchy(response,
											resSubnodes);
									w_launch();
								});
							};

							$scope.updateLink = function(linkId, subnodeId) {
								TreeService.updateLink({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : linkId

								}, {
									id : linkId,
									subnodeId : subnodeId
								}, function(response) {
									// noty({type: 'success', text : 'link added
									// successfully', timeout: 1500});
								}, function(error) {
									// todo error handling
								});
							};

							$scope.addNode = function(did) {
								TreeService.addNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								}, {
									treeId : $routeParams.treeId,
									directoryId : did
								}, function(response) {
									myJsTree.drawNewNode(response);
									// noty({type: 'success', text : 'node added
									// successfully', timeout: 1500});
								}, function(error) {
									// todo error
								});
							};

							$scope.addNodeCopy = function(node) {
								TreeService.addNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								}, {
									treeId : $routeParams.treeId,
									title : node.text,
									directoryId : 0
								},
										function(response) {
											myJsTree.prepareForSetId(node,
													response.id);
										}, function(error) {
											// todo error handling
										});
							};

							$scope.deleteNode = function(nodeId) {
								myJsPlumb.detachNode(nodeId);
								TreeService.deleteNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								}, {
									id : nodeId
								}, function(response) {
									myJsPlumb.detachNode(nodeId);
									noty({
										type : 'success',
										text : 'node deleted successfully',
										timeout : 1500
									});
								}, function(error) {
									// todo error handling
								});
							};

							$scope.getNode = function(nodeId) {
								TreeService.getNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeId
								}, function(node) {
									myDragDrop.setMovedNode(node);
								}, function(error) {
									// todo error handling
								});
							};

							$scope.dropNode = function(e, nodeId) {
								TreeService.updateNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeId
								}, {
									id : nodeId,
									dynamicTreeFlag : 1
								}, function(response) {
									myJsPlumb.addHTMLNode(response, e);
								});
							};

							$scope.undropNode = function(nodeId) {
								TreeService.updateNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeId
								}, {
									id : nodeId,
									dynamicTreeFlag : 0
								}, function(response) {
									// todo
								}, function(error) {
									// todo
								});
							};

							$scope.renameNode = function(id, newTitle, did) {
								TreeService.renameNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : id
								}, {
									id : id,
									title : newTitle,
									directoryId : did
								}, function(response) {
									myJsPlumb.renameNode(id, newTitle);
									// noty({type: 'success', text : 'node
									// renamed successfully', timeout: 1500});
								}, function(error) {
									// todo: error handling
								});
							};

							$scope.moveNode = function(id, newParentId) {
								TreeService.updateNode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : id
								}, {
									id : id,
									directoryId : newParentId
								}, function(response) {
									// todo
								}, function(error) {
									// todo error handling
								});
							};

							$scope.addLink = function(treeId, sourceId,
									targetId, connection) {
								TreeService.addLink({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								}, {
									treeId : treeId,
									sourceId : sourceId,
									targetId : targetId
								},
										function(response) {
											myJsPlumb.setLinkId(connection,
													response.id);
										}, function(error) {
											// todo error handling
										});
							};

							$scope.deleteLink = function(linkId) {
								TreeService.deleteLink({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
								}, {
									id : linkId
								}, function(response) {
									// noty({type: 'success', text : 'link
									// deleted successfully', timeout: 1500});
								}, function(error) {
									// todo error handling
								});
							};

							$scope.addDirectory = function(parentid) {
								TreeService.addDirectory({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								}, {
									treeId : $routeParams.treeId,
									parentId : parentid
								}, function(response) {
									alert(JSON.stringify(response));
									myJsTree.drawNewDirectory(response);
									// noty({type: 'success', text : 'directory
									// added successfully', timeout: 1500});
								}, function(error) {
									// todo error handling
								});
							};

							$scope.addDirectoryCopy = function(node) {
								TreeService.addDirectory({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								}, {
									treeId : $routeParams.treeId,
									title : node.text,
									parentid : 0
								},
										function(response) {
											myJsTree.prepareForSetId(node,
													response.id);
										}, function(error) {
											// todo error handling
										});
							};

							$scope.renameDirectory = function(directoryId,
									newTitle) {
								TreeService.updateDirectory({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : directoryId
								}, {
									id : directoryId,
									title : newTitle
								}, function(response) {
									// noty({type: 'success', text : 'directory
									// renamed successfully', timeout: 1500});
								}, function(error) {
									// error handling
								});
							};

							$scope.deleteDirectory = function(directoryId) {
								TreeService
										.deleteDirectory(
												{
													entity1 : 'ktrees',
													id1 : $routeParams.treeId
												},
												{
													id : directoryId
												},
												function(response) {
													noty({
														type : 'success',
														text : 'directory deleted successfully',
														timeout : 1500
													});
												}, function(error) {
													// todo error handling
												});
							};

							$scope.moveDirectory = function(directoryId,
									newParentId) {
								TreeService.updateDirectory({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : directoryId
								}, {
									id : directoryId,
									parentid : newParentId
								}, function(response) {
									// noty({type: 'success', text : 'directory
									// moved successfully', timeout: 1500});
								}, function(error) {
									// todo error handling
								});
							};

							$scope.zoomUpNode = function(nodeid) {
								TreeService.nodeZoomUp({
									entity : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resNodes) {
									$scope.zoomUpSubnode(nodeid, resNodes);
								}, function(error) {
									// todo error handling
								});
							};

							$scope.zoomDownNode = function(nodeid) {
								TreeService.nodeZoomDown({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resNodes) {
									$scope.zoomDownSubnode(nodeid, resNodes);
								}, function(error) {
									// todo error handling
								});
							};

							$scope.zoomUpSubnode = function(nodeid, resNodes) {
								TreeService.subnodeZoomUp({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resSubnodes) {
									myJsPlumb.drawExistingNodes(resNodes,
											resSubnodes);
									$scope.zoomUpLink(nodeid, resSubnodes);
								}, function(error) {
									// todo error handling
								});
							};

							$scope.zoomDownSubnode = function(nodeid, resNodes) {
								TreeService.subnodeZoomDown({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resSubnodes) {
									myJsPlumb.drawExistingNodes(resNodes,
											resSubnodes);
									$scope.zoomDownLink(nodeid, resSubnodes);
								}, function(error) {
									// todo error handling
								});
							};

							$scope.zoomUpLink = function(nodeid, resSubnodes) {
								TreeService.linkZoomUp({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id3 : nodeid
								}, function(resLinks) {
									myJsPlumb.makeNodeHierarchy(resLinks,
											resSubnodes);
									w_launch();
								}, function(error) {
									// todo error handling
								});
							};

							$scope.zoomDownLink = function(nodeid, resSubnodes) {
								TreeService.linkZoomDown({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id3 : nodeid
								}, function(resLinks) {
									myJsPlumb.makeNodeHierarchy(resLinks,
											resSubnodes);
									w_launch();
								}, function(error) {
									// todo error handling
								});
							};

							$scope.editSubnodeTitle = function(data, id) {
								var subnode = $.grep($scope.subnodes, function(
										t) {
									return t.id === id;
								})[0];
								subnode.title = data;

								TreeService.renameSubnode( { entity1 : 'ktrees', id1 : $routeParams.treeId, id2 : $scope.selectedNodeId, id3 : id },subnode, function(
										response) {
									if (response.bool) {
										$scope.getSubnodesOfNode(response);
										// noty({type: 'success', text :
										// 'subnode renamed successfully',
										// timeout: 1500});
									}
								});
							};

							// todo change logic
							$scope.moveSubnodeUp = function(id) {
								TreeService.moveSubnodeUp({
									id : id
								}, function(response) {
									if (response.bool) {
										$scope.getSubnodesOfNode();
										// noty({type: 'success', text :
										// 'subnode position changed
										// successfully', timeout: 1500});
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
										// noty({type: 'success', text :
										// 'subnode position changed
										// successfully', timeout: 1500});
										// getSubnodes();
									}
								});
							};
						} ]);