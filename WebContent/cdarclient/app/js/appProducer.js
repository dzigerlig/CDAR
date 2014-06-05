var reload = false;
function getReload() {
	return reload;
}
function setReload(value) {
	reload = value;
}

app.controller('HomeProducerController', ['$scope', '$location', 'TreeService', 'AuthenticationService', 'UserService', '$modal', 'DescriptionService', 
                                          function($scope, $location, TreeService, AuthenticationService, UserService, $modal, DescriptionService) {
	$scope.knowledgeTrees = '';
	$scope.newTreeName = '';
	$scope.UserService = UserService;
	$scope.DescriptionService = DescriptionService;

	//define locking notification (text in server) 
	$scope.showLockingNotification = function(error) {
		if (error.status === 409) {
			noty({
				type : 'error',
				text : error.data,
				timeout : 5000
			});
			return true;
		} else
			return false;
	};
	
	var reloadTrees = function() {
		TreeService.getTrees({
			entity1 : 'ktrees'
		}, function(response) {
			$scope.knowledgeTrees = response;
		}, function(error) {
			UserService.checkResponseUnauthorized(error);
			noty({
				type : 'error',
				text : 'cannot get trees',
				timeout : 1500
			});
		});
	};
	
	reloadTrees();
	
	$scope.addNewTree = function() {
		if ($scope.newTreeName.length > 45) {
			noty({
				type : 'warning',
				text : 'Please enter a text with less than 45 Characters',
				timeout : 3000
			});
		} else {
			TreeService.addTree({
				entity1 : 'ktrees'
			}, {
				title : $scope.newTreeName
			}, function(response) {
				$scope.newTreeName = '';
				reloadTrees();
			}, function(error) {
				UserService.checkResponseUnauthorized(error);
				noty({
					type : 'error',
					text : 'cannot add tree',
					timeout : 1500
				});
			});
		}
	};
	
	$scope.deleteTree = function(treeid) {
		$modal.open({
			templateUrl : 'templates/confirmation.html',
			backdrop : 'static',
			keyboard : false,
			resolve : {
				data : function() {
					return {
						title : 'Delete Tree',
						message : 'Do you really want to delete this Tree?'
					};
				}
			},
			controller : 'ConfirmationController'
		}).result.then(function(result) {
			TreeService.deleteTree(
					{
						entity1 : 'ktrees'
					},	{
						id : treeid
					},
					function(response) {
						reloadTrees();
						noty({
							type : 'success',
							text : 'knowledge tree deleted successfully',
							timeout : 1500
						});
					},
					function(error) {
						UserService.checkResponseUnauthorized(error);
						if (!$scope.showLockingNotification(error)) {
							noty({
								type : 'error',
								text : 'delete tree failed',
								timeout : 1500
							});
						}
					});
		});
	};

	$scope.saveKnowledgeTreeTitle = function(data, id) {
		if (data.length > 45) {
			noty({
				type : 'warning',
				text : 'Please enter a text with less than 45 Characters',
				timeout : 3000
			});
			return '';
		} else {
			var tree = $.grep($scope.knowledgeTrees,function(t) {
				return t.id === id;
			})[0];
			
			var oldTitle = tree.title;
			tree.title = data;
			TreeService.updateTree(
					{
						entity1 : 'ktrees',
						id1 : tree.id
					},
					tree,
					function(response) {
					},
					function(error) {
						UserService.checkResponseUnauthorized(error);
						if (!$scope.showLockingNotification(error)) {
							tree.title = oldTitle;
							noty({
								type : 'error',
								text : 'error while saving tree title',
								timeout : 1500
							});
						}
					});
		}
	};
} ]);

app.controller('KnowledgeTreeController', ['$scope', '$routeParams', 'TreeService', 'AuthenticationService', 'UserService', '$route', 'DescriptionService', '$modal',
                                           function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route, DescriptionService, $modal) {
			 
	//Workaround jstree not selectable
	if (getReload()) { 
		setReload(false);
		location.reload(); 
	} 
	setReload(true);							 
	$scope.isProducer = true;
	$scope.modal = $modal;
	$scope.DescriptionService = DescriptionService;
	CDARJsPlumb.initialize();
	$scope.treeId = $routeParams.treeId;
	$scope.UserService = UserService;
	$scope.knowledgetree = '';
	$scope.nodes = '';
	$scope.selectedNode = {
			id : 0,
			title : ''
	};
	$scope.updatedWikiTitle = '';

	$scope.updateWikiTitle = function() {
		$scope.selectedNode.wikititle = this.updatedWikiTitle;
		TreeService.updateNode({
			entity1 : 'ktrees',
			id1 : $routeParams.treeId,
			id2 : $scope.selectedNode.id
		}, $scope.selectedNode, function(response) {
			$scope.changeNode(response.id, response.title);
		}, function(error) {
			UserService.checkResponseUnauthorized(error);
			noty({
				type : 'alert',
				text : 'cannot edit wiki title',
				timeout : 1500
			});
		});
	};
	
	$scope.showLockingNotification = function(error) {
		if (error.status === 409) {
			noty({
				type : 'error',
				text : error.data,
				timeout : 5000
			});
			return true;
		} else
			return false;
	};
	
	$scope.subnodes = '';
	$scope.selectedSubnode = {
			id : 0,
			title : ''
	};
				
	$scope.subnodeHtmlText = '';
	$scope.nodeitle = '';
	$scope.wikiHtmlText = '';
	$scope.nodetabs = [ {
		title : 'READ'
	}, {
		title : 'WRITE'
	} ];
	$scope.subnodetabs = [ {
		title : 'READ'
	}, {
		title : 'WRITE'
			} ];
	
	$scope.saveKnowledgeTreeTitle = function(title) {
		if (title.length > 45) {
			noty({
				type : 'warning',
				text : 'Please enter a text with less than 45 Characters',
				timeout : 3000
			});
			return '';
		} else {
			TreeService.updateTree({
				entity1 : 'ktrees',
				id1 : $scope.knowledgetree.id
			},
			$scope.knowledgetree,	function(response) {
			},
			function(error) {
				UserService.checkResponseUnauthorized(error);
				if (!$scope.showLockingNotification(error)) {
					noty({
						type : 'error',
						text : 'error while saving tree title',
						timeout : 1500
					});
				}	
			});	
		}	
	};			
	
	var getSubnodes = function() {
		TreeService.getSubnodes(
				{
					entity1 : 'ktrees',
					id1 : $scope.knowledgetree.id,
					id2 : $scope.selectedNode.id
				},
				function(response) {
					$scope.subnodes = response;
				},
				function(error) {
					UserService.checkResponseUnauthorized(error);
					noty({
						type : 'error',
						text : 'error getting ' + DescriptionService.getSubnodeDescription()	+ 's',
						timeout : 1500
					});
				});
	};	
	$scope.getSubnodesOfNode = function(idObject) {
		var identity;
		var changes = null;
		if (typeof idObject === 'object'	|| idObject === undefined) {
			if (typeof idObject === 'object') {
				changes = idObject;
			}
			identity = $scope.selectedNode.id;
		} else {
			identity = idObject;
		}
		TreeService.getSubnodes(
				{
					entity1 : 'ktrees',
					id1 : $scope.knowledgetree.id,
					id2 : identity
				},
				function(response) {
					$scope.subnodes = response;
					CDARJsPlumb.updateSubnodesOfNode(	response,	identity,	changes);
				},
				function(error) {
					UserService.checkResponseUnauthorized(error);
					noty({
						type : 'error',
						text : 'error getting '	+ DescriptionService.getSubnodeDescription()	+ 's',
						timeout : 1500
					});
				});
			};
			
			$scope.addNewSubnode = function() {
				if (this.newSubnodeName.length > 45) {
					noty({
						type : 'warning',
						text : 'Please enter a text with less than 45 Characters',
						timeout : 3000
					});
				} else {
					TreeService.addSubnode({
						entity1 : 'ktrees',
						id1 : $scope.knowledgetree.id,
						id2 : $scope.selectedNode.id
					},	{
						nodeId : $scope.selectedNode.id,
						title : this.newSubnodeName
					},	function(response) {
						$scope.getSubnodesOfNode();
					},	function(error) {
						UserService.checkResponseUnauthorized(error);
						if (!$scope.showLockingNotification(error)) {
							noty({
								type : 'error',
								text : 'cannot add '+ DescriptionService.getSubnodeDescription(),
								timeout : 1500
							});
						}
					});
				}
			};
			
			$scope.newSubnodeWikiName = '';
			$scope.newSubnodeWikiTitle = '';
			
			$scope.addSubnodeByWikiTitle = function() {
				if (this.newSubnodeWikiName.length>45) {
					noty({
						type : 'warning',
						text : 'Please enter a text with less than 45 Characters',
						timeout : 3000
					});
				} else {
					TreeService.addSubnode({
						entity1 : 'ktrees',
						id1 : $scope.knowledgetree.id,
						id2 : $scope.selectedNode.id
					}, {
						nodeId : $scope.selectedNode.id,
						title : this.newSubnodeWikiName,
						wikititle : this.newSubnodeWikiTitle
					}, function(response) {
						$scope.getSubnodesOfNode();
					}, function(error) {
						UserService.checkResponseUnauthorized(error);
						if (!$scope.showLockingNotification(error)) {
							noty({
								type : 'error',
								text : 'cannot add ' + DescriptionService.getSubnodeDescription(),
								timeout : 1500
							});
						}
					});
				}
			};
			
			$scope.changeSubnode = function(subnodeid, name) {
				setLoadingSubnode();
				$scope.selectedSubnode.id = subnodeid;
				$scope.selectedSubnode.title = name;
				TreeService.getSubnodeWiki({
					entity1 : 'ktrees',
					id1 : $scope.knowledgetree.id,
					id2 : $scope.selectedNode.id,
					id3 : subnodeid
				}, function(response) {
					$scope.selectedSubnode = response;
					updateSubnodeTitle();
					changeWikiFieldsSubnode();
				}, function(error) {
					UserService.checkResponseUnauthorized(error);
					noty({
						type : 'error',
						text : 'error getting wiki entry',
						timeout : 1500
					});
				});
			};
			
			var changeWikiFieldsSubnode = function() {
				$scope.subnodeHtmlText = $scope.selectedSubnode.wikiContentHtml;
				$('#wikiSubnodeArea').val($scope.selectedSubnode.wikiContentPlain);
			};

			$scope.saveWikiSubnodeEntry = function() {
				if ($scope.selectedSubnode !== 0) {
					$scope.selectedSubnode.wikiContentPlain = $('#wikiSubnodeArea').val();
					switchSubnodeToRead();
					setLoadingSubnode();
					TreeService.updateSubnodeWiki(
							{
								entity1 : 'ktrees',
								id1 : $scope.knowledgetree.id,
								id2 : $scope.selectedNode.id,
								id3 : $scope.selectedSubnode.id
							},
							$scope.selectedSubnode,
							function(response) {
								$scope.selectedSubnode = response;
								changeWikiFieldsSubnode();
								noty({
									type : 'success',
									text : DescriptionService.getSubnodeDescription() + ' text edited successfully',
									timeout : 1500
								});
							},
							function(error) {
								UserService.checkResponseUnauthorized(error);
								noty({
									type : 'error',
									text : 'cannot edit wiki text',
									timeout : 1500
								});
							});
				}
			};
			
			$scope.deleteSubnode = function(subnodeId) {
				$modal.open({ 
					templateUrl: 'templates/confirmation.html',
					backdrop: 'static',
					keyboard: false,
					resolve: {
						data: function() { 
							return {
								title: 'Delete ' + DescriptionService.getSubnodeDescription(),
								message: 'Do you really want to delete this ' + DescriptionService.getSubnodeDescription() 
							};
						}
					},
					controller: 'ConfirmationController' 
				}).result.then(function(result) {
					TreeService.deleteSubnode(
							{
								entity1 : 'ktrees',
								id1 : $routeParams.treeId,
								id2 : $scope.selectedNode.id
							},
							{
								id : subnodeId
							},
							function(response) {
								$scope.getSubnodesOfNode(response);
								noty({
									type : 'success',
									text : DescriptionService.getSubnodeDescription() + ' deleted successfully',
									timeout : 1500
								});
								if ($scope.selectedSubnode.id === subnodeId) {
									$scope.selectedSubnode.id = 0;
									updateSubnodeTitle();
								}
							},
							function(error) {
								UserService.checkResponseUnauthorized(error);
								if (!$scope.showLockingNotification(error)) {
									noty({
										type : 'error',
										text : 'error deleting ' + DescriptionService.getSubnodeDescription(),
										timeout : 1500
									});
								}
							});
				});
			};
			
			var updateNodeTitle = function() {
				if ($scope.selectedNode.id !== 0) {
					$scope.nodeTitle = 'Selected ' + DescriptionService.getNodeDescription() + ': ' + $scope.selectedNode.title;
				} else {
					$scope.nodeTitle = 'Selected ' + DescriptionService.getNodeDescription() + ': no ' + DescriptionService.getNodeDescription()+ ' selected';
				}
			};
			
			var updateSubnodeTitle = function() {
				if ($scope.selectedSubnode.id !== 0) {
					$scope.subnodeTitle = 'Selected ' + DescriptionService.getSubnodeDescription()+ ': ' + $scope.selectedSubnode.title;
					} else {
						$scope.subnodeTitle = 'Selected ' + DescriptionService.getSubnodeDescription() + ': no ' + DescriptionService.getSubnodeDescription() + ' selected';
					}
			};
			
			updateNodeTitle();
			
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
				$('#wikiArea').val($scope.selectedNode.wikiContentPlain);
			};
			
			$scope.changeNode = function(id, name) {
				setLoadingNode();
				TreeService.getNodeWiki({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId,
					id2 : id
				}, function(response) {
					$scope.selectedNode = response;
					updateNodeTitle();
					$scope.selectedSubnode.id = 0;
					updateSubnodeTitle();
					getSubnodes();
					changeWikiFields();
				}, function(error) {
					UserService.checkResponseUnauthorized(error);
					noty({
						type : 'error',
						text : 'error getting wiki entry',
						timeout : 1500
					});
				});
			};
			
			$scope.saveWikiNodeEntry = function() {
				if ($scope.selectedNode.id !== 0) {
					$scope.selectedNode.wikiContentPlain = $('#wikiArea').val();
					switchNodeToRead();
					setLoadingNode();
					TreeService.updateNodeWiki(
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
									text : DescriptionService.getNodeDescription() + ' text edited successfully',
									timeout : 1500
								});
							},
							function(error) {
								UserService.checkResponseUnauthorized(error);
								changeWikiFields($scope.selectedNode);
								noty({
									type : 'error',
									text : 'cannot edit wiki text',
									timeout : 1500
								});
							});
				}
			};
			
			TreeService.getTree({
				entity1 : 'ktrees',
				id1 : $routeParams.treeId
			}, function(response) {
				$scope.knowledgetree = response;
			}, function(error) {
				UserService.checkResponseUnauthorized(error);
				noty({
					type : 'error',
					text : 'error getting tree',
					timeout : 1500
				});
			});
			
			TreeService.getDirectories({
				entity1 : 'ktrees',
				id1 : $routeParams.treeId
			}, function(resDirectory) {
				TreeService.getNodes({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId
				}, function(resNodes) {
					CDARJsTree.directoryDataToArray(resDirectory, resNodes);
					$scope.drillDownNode(0);
				}, function(error) {
					UserService.checkResponseUnauthorized(error);
					noty({
						type : 'error',
						text : 'error getting '	+ DescriptionService.getNodeDescription() + 's',
						timeout : 1500
					});
				});
			}, function(error) {
				UserService.checkResponseUnauthorized(error);
				noty({
					type : 'error',
					text : 'error getting directories',
					timeout : 1500
				});
			});
			
			$scope.updateLink = function(linkId, subnodeid) {
				TreeService.updateLink({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId,
					id2 : linkId
				}, {
					id : linkId,
					subnodeId : subnodeid
				}, function(response) {
				}, function(error) {
					UserService.checkResponseUnauthorized(error);
					if (!$scope.showLockingNotification(error)) {
						noty({
							type : 'error',
							text : 'cannot update link',
							timeout : 1500
						});
					}
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
					CDARJsTree.drawNewNode(response);
				}, function(error) {
					UserService.checkResponseUnauthorized(error);
					if (!$scope.showLockingNotification(error)) {
						noty({
							type : 'error',
							text : 'error adding '	+ DescriptionService.getNodeDescription(),
							timeout : 1500
						});
					}
				});
			};
			
			$scope.addNodeCopy = function(id, node) {
				TreeService.addNodeCopy(
						{
							entity1 : 'ktrees',
							id1 : $routeParams.treeId,
							id2 : id
						},
						{
							treeId : $routeParams.treeId,
							title : node.text,
							directoryId : 0
						},
						function(response) {
							CDARJsTree.prepareForSetId(node, response.id);
							},
							function(error) {
								UserService.checkResponseUnauthorized(error);
								if (!$scope.showLockingNotification(error)) {
									noty({
										type : 'error',
										text : 'error adding '	+ DescriptionService.getNodeDescription(),
										timeout : 1500
									});
								}
							});
					};
								
					$scope.deleteNode = function(nodeId) {
						TreeService.deleteNode({
							entity1 : 'ktrees',
							id1 : $routeParams.treeId
						}, {
							id : nodeId
						}, function(response) {
							$('#jstree').jstree(true).delete_node('directorynode'+nodeId);
							CDARJsPlumb.removeNode($('#node'+nodeId));
							noty({
								type : 'success',
								text : DescriptionService.getNodeDescription() + ' deleted successfully',
								timeout : 1500
							});
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							if (!$scope.showLockingNotification(error)) {
								noty({
									type : 'error',
									text : 'cannot delete '	+ DescriptionService.getNodeDescription(),
									timeout : 1500
								});
							}
						});
					};
					
					$scope.getNode = function(nodeId) {
						TreeService.getNode({
							entity1 : 'ktrees',
							id1 : $routeParams.treeId,
							id2 : nodeId
						}, function(node) {
							CDARDragDrop.setMovedNode(node);
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							noty({
								type : 'error',
								text : 'error getting ' + DescriptionService.getNodeDescription(),
								timeout : 1500
							});
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
							CDARJsPlumb.drawNewNode(response, e);
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							if (!$scope.showLockingNotification(error)) {
								noty({
									type : 'error',
									text : 'error dropping '	+ DescriptionService.getNodeDescription(),
									timeout : 1500
								});
							}
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
							CDARJsPlumb.removeNode($('#node' + nodeId));
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							if (!$scope.showLockingNotification(error)) {
								noty({
									type : 'error',
									text : 'error undropping '	+ DescriptionService.getNodeDescription(),
									timeout : 1500
								});
							}
						});
					};
					
					$scope.renameNode = function(id, data, did) {
						TreeService.renameNode({
							entity1 : 'ktrees',
							id1 : $routeParams.treeId,
							id2 : id
						}, {
							id : id,
							title : data.text,
							directoryId : did
						}, function(response) {
							CDARJsPlumb.renameNode(id, data.text);
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							$('#jstree').jstree('rename_node', $('#directorynode'+id) , data.old);
							if (!$scope.showLockingNotification(error)) {
								noty({
									type : 'error',
									text : 'cannot rename '	+ DescriptionService.getNodeDescription(),
									timeout : 1500
								});
							}
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
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							if (!$scope.showLockingNotification(error)) {
								noty({
									type : 'error',
									text : 'error moving '	+ DescriptionService.getNodeDescription(),
									timeout : 1500
								});
							}
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
							CDARJsPlumb.setLinkId(connection, response.id);
							}, function(error) {
								UserService.checkResponseUnauthorized(error);
								if (!$scope.showLockingNotification(error)) {
									noty({
										type : 'error',
										text : 'error adding link',
										timeout : 1500
									});
								}
							});
					};
					
					$scope.deleteLink = function(linkId) {
						TreeService.deleteLink({
							entity1 : 'ktrees',
							id1 : $routeParams.treeId
						}, {
							id : linkId
						}, function(response) {
							CDARJsPlumb.removeLink('link'+linkId);
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							if (!$scope.showLockingNotification(error)) {
								noty({
									type : 'error',
									text : 'cannot delete link',
									timeout : 1500
								});
							}
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
							CDARJsTree.drawNewDirectory(response);
						}, function(error) {
							UserService.checkResponseUnauthorized(error);
							if (!$scope.showLockingNotification(error)) {
								noty({
									type : 'error',
									text : 'error adding directory',
									timeout : 1500
								});
							}
						});
					};
					
					$scope.addDirectoryCopy = function(node) {
						TreeService.addDirectory(
								{
									entity1 : 'ktrees',
									id1 : $routeParams.treeId
								},	{
									treeId : $routeParams.treeId,
									title : node.text,
									parentid : 0
								},
								function(response) {
									CDARJsTree.prepareForSetId(node, response.id);
								},
								function(error) {
									UserService.checkResponseUnauthorized(error);
									if (!$scope.showLockingNotification(error)) {
										noty({
											type : 'error',
											text : 'error adding directory copy',
											timeout : 1500
										});
									}
								});
							};	

							$scope.renameDirectory = function(directoryId, data) {
								TreeService.updateDirectory({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : directoryId
								}, {
									id : directoryId,
									title : data.text
								}, function(response) {
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									$('#jstree').jstree('rename_node', $('#directory'+directoryId) , data.old);
									if (!$scope.showLockingNotification(error)) {
									noty({
										type : 'error',
										text : 'error renaming directory',
										timeout : 1500
									});
									}
								});
							};

							$scope.deleteDirectory = function(directoryId) {
								TreeService.deleteDirectory(
										{
											entity1 : 'ktrees',
											id1 : $routeParams.treeId
										},
										{
											id : directoryId
										},
										function(response) {
											$('#jstree').jstree(true).delete_node('directory'+directoryId);
											noty({
												type : 'success',
												text : 'directory deleted successfully',
												timeout : 1500
											});
										},
										function(error) {
											UserService.checkResponseUnauthorized(error);
											if (!$scope.showLockingNotification(error)) {
												noty({
													type : 'error',
													text : 'error deleting directory',
													timeout : 1500
												});
												}
										});
							};
							
							$scope.moveDirectory = function(directoryId, newParentId) {
								TreeService.updateDirectory({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : directoryId
								}, {
									id : directoryId,
									parentId : newParentId
								}, function(response) {
								}, function(error) {
									 UserService.checkResponseUnauthorized(error);									
									if (!$scope.showLockingNotification(error)) {
										noty({
											type : 'error',
											text : 'error moving directory',
											timeout : 1500
										});}
								});
							};

							$scope.drillUpNode = function(nodeid) {
								TreeService.nodeDrillUp({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resNodes) {
									if(resNodes.length){
										$scope.drillUpSubnode(nodeid, resNodes);
									}
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									noty({
										type : 'error',
										text : 'cannot drill up',
										timeout : 1500
									});
								});
							};

							$scope.drillDownNode = function(nodeid) {
								TreeService.nodeDrillDown({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resNodes) {
									if(resNodes.length){
										$scope.drillDownSubnode(nodeid, resNodes);
									}
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									noty({
										type : 'error',
										text : 'cannot drill down',
										timeout : 1500
									});
								});
							};

							$scope.drillUpSubnode = function(nodeid, resNodes) {
								TreeService.subnodeDrillUp({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resSubnodes) {
									CDARJsPlumb.drawExistingNodes(resNodes, resSubnodes);
									$scope.drillUpLink(nodeid, resSubnodes);
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									noty({
										type : 'error',
										text : 'cannot drill up',
										timeout : 1500
									});
								});
							};

							$scope.drillDownSubnode = function(nodeid, resNodes) {
								TreeService.subnodeDrillDown({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : nodeid
								}, function(resSubnodes) {
									CDARJsPlumb.drawExistingNodes(resNodes, resSubnodes);
									$scope.drillDownLink(nodeid, resSubnodes);
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									noty({
										type : 'error',
										text : 'cannot drill down',
										timeout : 1500
									});
								});
							};

							$scope.drillUpLink = function(nodeid, resSubnodes) {
								TreeService.linkDrillUp({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id3 : nodeid
								}, function(resLinks) {
									CDARJsPlumb.makeNodeHierarchy(resLinks,	resSubnodes);
									w_launch();
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									noty({
										type : 'error',
										text : 'cannot drill up',
										timeout : 1500
									});
								});
							};

							$scope.drillDownLink = function(nodeid, resSubnodes) {
								TreeService.linkDrillDown({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id3 : nodeid
								}, function(resLinks) {
									CDARJsPlumb.makeNodeHierarchy(resLinks, resSubnodes);
									w_launch();
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									noty({
										type : 'error',
										text : 'cannot drill down',
										timeout : 1500
									});
								});
							};

							$scope.editSubnodeTitle = function(data, id) {
								if (data.length > 45) {
									noty({
										type : 'warning',
										text : 'Please enter a text with less than 45 Characters',
										timeout : 3000
									});
									return '';
								} else {
									var subnode = $.grep($scope.subnodes,	function(t) {
										return t.id === id;
									})[0];
									subnode.title=data;

									TreeService.renameSubnode(
											{
												entity1 : 'ktrees',
												id1 : $routeParams.treeId,
												id2 : $scope.selectedNode.id,
												id3 : id
											},
											subnode,
											function(response) {
												subnode.title = data;
												$scope.getSubnodesOfNode(response);
											},
											function(error) {
												UserService.checkResponseUnauthorized(error);
												if (!$scope.showLockingNotification(error)) {
													noty({
														type : 'error',
														text : 'error renaming ' + DescriptionService.getSubnodeDescription(),
														timeout : 1500
													});}
												return '';
											}
									);
								}
							};

							$scope.moveSubnodeUp = function(id) {
								var subnode = $.grep($scope.subnodes, function(t) {
									return t.id === id;
								})[0];
								var subnodeClone = jQuery.extend({}, subnode);
								subnodeClone.position=subnodeClone.position-1;
								TreeService.updateSubnode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : $scope.selectedNode.id,
									id3 : subnodeClone.id
								}, subnodeClone, function(response) {
									subnode.position = subnode.position - 1;
									$scope.getSubnodesOfNode();
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									if (!$scope.showLockingNotification(error)) {
									noty({
										type : 'error',
										text : 'cannot drill up',
										timeout : 1500
									});}
								});
							};

							$scope.moveSubnodeDown = function(id) {
								var subnode = $.grep($scope.subnodes, function(t) {
									return t.id === id;
								})[0];
								var subnodeClone = jQuery.extend({}, subnode);
								subnodeClone.position=subnodeClone.position+1;
								TreeService.updateSubnode({
									entity1 : 'ktrees',
									id1 : $routeParams.treeId,
									id2 : $scope.selectedNode.id,
									id3 : subnodeClone.id
								}, subnodeClone, function(response) {
									subnode.position = subnode.position + 1;
									$scope.getSubnodesOfNode();
								}, function(error) {
									UserService.checkResponseUnauthorized(error);
									if (!$scope.showLockingNotification(error)) {

									noty({
										type : 'error',
										text : 'cannot drill down',
										timeout : 1500
									});}
								});
							};
							updateSubnodeTitle();
			} ]);
