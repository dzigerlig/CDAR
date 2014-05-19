app.controller("ProjectTreeController", ['$scope', '$routeParams', 'AuthenticationService', 'TreeService', 'UserService', '$filter', 'DescriptionService', '$modal', function ($scope, $routeParams, AuthenticationService, TreeService, UserService, $filter, DescriptionService, $modal) {
	$scope.isProducer = false;
	myJsPlumb.initialize();
	$scope.treeId = $routeParams.treeId;
    $scope.UserService = UserService;
    $scope.DescriptionService = DescriptionService;
    $scope.defaultDirectoryName = DescriptionService.getDirectoryDescription();
    $scope.defaultNodeName = DescriptionService.getNodeDescription();
    $scope.defaultLinkName = 'all ' +DescriptionService.getSubnodeDescription()+'s';
    $scope.projecttree = "";
    $scope.nodetabs = [ { title : "READ" }, { title : "WRITE" } ];
	$scope.subnodetabs = [ { title : "READ" }, { title : "WRITE" } ];
	$scope.wikiHtmlText = "";
	$scope.selectedNode = { id : 0 , title : "" };
	$scope.selectedNodeWiki = "";
	$scope.selectedSubnode = { id : 0, title : "" };
	$scope.subnodes = "";
	$scope.newSubnodeName = DescriptionService.getSubnodeDescription();
	$scope.subnodeHtmlText = "";
	
	$scope.showLockingNotification = function(error) {
		 if (error.status === 409) {
			noty({
				type : 'error',
				text : error.data,
				timeout : 5000
			});
			return true;
		 } else {
			 return false;
		 }
	 };

    var reloadTree = function () {
        TreeService.getTree({entity1: 'ptrees', id1: $routeParams.treeId}, function (response) {
            $scope.projecttree = response;
        }, function(error) {
        	noty({
				type : 'alert',
				text : 'error getting tree',
				timeout : 1500
			});
        });


        TreeService.getDirectories({
            entity1: 'ptrees',
            id1: $routeParams.treeId
        }, function (resDirectory) {
            TreeService.getNodes({
                    entity1: 'ptrees', 
                    id1: $routeParams.treeId
                },
                function (resNodes) {
                    myJsTree.directoryDataToArray(resDirectory,
                        resNodes);
					$scope.drillDownNode(0);
                   // $scope.getSubnodes(resNodes);
                }, function(error) {
                	noty({
        				type : 'alert',
        				text : 'error getting ' + DescriptionService.getNodeDescription() + 's',
        				timeout : 1500
        			});
                });
        }, function(error) {
        	noty({
				type : 'alert',
				text : 'error getting directories',
				timeout : 1500
			});
        });
    };
   /* $scope.getSubnodes = function (resNodes) {
        TreeService.getSubnodesFromTree({
            entity1: 'ptrees',
            id1: $routeParams.treeId
        }, function (resSubnodes) {
            myJsPlumb.drawExistingNodes(resNodes, resSubnodes);
            $scope.getLinks(resSubnodes);
        }, function(error) {
        	noty({
				type : 'alert',
				text : 'error getting ' + DescriptionService.getSubnodeDescription() + 's',
				timeout : 1500
			});
        });
    };

    $scope.getLinks = function (resSubnodes) {
        TreeService.getLinks({
            entity1: 'ptrees',
            id1: $routeParams.treeId
        }, function (response) {
            myJsPlumb.makeNodeHierarchy(response, resSubnodes);
            w_launch();
        }, function(error) {
        	noty({
				type : 'alert',
				text : 'error getting links',
				timeout : 1500
			});
        });
    };*/

    $scope.updateLink = function(linkId, subnodeid) {
        TreeService.updateLink({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId,
            id2 : linkId
        }, {
            id : linkId,
            subnodeId : subnodeid
        }, function(response) {
            // noty({type: 'success', text : 'link added
            // successfully', timeout: 1500});
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'cannot update link',
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.addNode = function(did) {
        TreeService.addNode({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId
        }, {
            treeId : $routeParams.treeId,
            directoryId : did
        }, function(response) {
            myJsTree.drawNewNode(response);
            // noty({type: 'success', text : 'node added
            // successfully', timeout: 1500});
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'error adding ' + DescriptionService.getNodeDescription(),
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.addNodeCopy = function(node) {
        TreeService.addNode({
                entity1 : 'ptrees',
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
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'error adding ' + DescriptionService.getNodeDescription(),
						 timeout : 1500
					 });
				 }
            });
    };

    $scope.deleteNode = function(nodeId) {
        myJsPlumb.detachNode(nodeId);
        TreeService.deleteNode({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId
        }, {
            id : nodeId
        }, function(response) {
            myJsPlumb.detachNode(nodeId);
            noty({
                type : 'success',
                text : DescriptionService.getNodeDescription() + ' deleted successfully',
                timeout : 1500
            });
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
                	type : 'alert',
                	text : 'cannot delete ' + DescriptionService.getNodeDescription(),
                	timeout : 1500
            	});
			 }
        });
    };

    $scope.getNode = function(nodeId) {
        TreeService.getNode({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId,
            id2 : nodeId
        }, function(node) {
            myDragDrop.setMovedNode(node);
        }, function(error) {
            noty({
                type : 'alert',
                text : 'error getting ' + DescriptionService.getNodeDescription(),
                timeout : 1500
            });
        });
    };

    $scope.dropNode = function(e, nodeId) {
        TreeService.updateNode({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId,
            id2 : nodeId
        }, {
            id : nodeId,
            dynamicTreeFlag : 1
        }, function(response) {
            myJsPlumb.addHTMLNode(response, e);
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'error dropping ' + DescriptionService.getNodeDescription(),
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.undropNode = function(nodeId) {
        TreeService.updateNode({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId,
            id2 : nodeId
        }, {
            id : nodeId,
            dynamicTreeFlag : 0
        }, function(response) {
            // todo
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'error undropping ' + DescriptionService.getNodeDescription(),
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.renameNode = function(id, newTitle, did) {
        TreeService.renameNode({
            entity1 : 'ptrees',
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
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'cannot rename ' + DescriptionService.getNodeDescription(),
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.moveNode = function(id, newParentId) {
        TreeService.updateNode({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId,
            id2 : id
        }, {
            id : id,
            directoryId : newParentId
        }, function(response) {
            // todo
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'error moving ' + DescriptionService.getNodeDescription(),
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.addLink = function(treeId, sourceId,
                              targetId, connection) {
        TreeService.addLink({
                entity1 : 'ptrees',
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
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'error adding link',
						 timeout : 1500
					 });
				 }
            });
    };

    $scope.deleteLink = function(linkId) {
        TreeService.deleteLink({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId
        }, {
            id : linkId
        }, function(response) {
            // noty({type: 'success', text : 'link
            // deleted successfully', timeout: 1500});
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'cannot delete link',
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.addDirectory = function(parentid) {
        TreeService.addDirectory({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId
        }, {
            treeId : $routeParams.treeId,
            parentId : parentid
        }, function(response) {
            myJsTree.drawNewDirectory(response);
            // noty({type: 'success', text : 'directory
            // added successfully', timeout: 1500});
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'error adding directory',
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.addDirectoryCopy = function(node) {
        TreeService.addDirectory({
                entity1 : 'ptrees',
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
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'error adding directory copy',
						 timeout : 1500
					 });
				 }
            });
    };

    $scope.renameDirectory = function(directoryId,
                                      newTitle) {
        TreeService.updateDirectory({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId,
            id2 : directoryId
        }, {
            id : directoryId,
            title : newTitle
        }, function(response) {
            // noty({type: 'success', text : 'directory
            // renamed successfully', timeout: 1500});
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'error renaming directory',
					 timeout : 1500
				 });
			 }
        });
    };

    $scope.deleteDirectory = function(directoryId) {
        TreeService.deleteDirectory(
            {
                entity1 : 'ptrees',
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
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'error deleting directory',
						 timeout : 1500
					 });
				 }
            });
    };

    $scope.moveDirectory = function(directoryId,
                                    newParentId) {
        TreeService.updateDirectory({
            entity1 : 'ptrees',
            id1 : $routeParams.treeId,
            id2 : directoryId
        }, {
            id : directoryId,
            parentId : newParentId
        }, function(response) {
            // noty({type: 'success', text : 'directory
            // moved successfully', timeout: 1500});
        }, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'error moving directory',
					 timeout : 1500
				 });
			 }
        });
    };
    
    $scope.drillUpNode = function(nodeid) {
		TreeService.nodeDrillUp({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resNodes) {
			if(resNodes.length){
				$scope.drillUpSubnode(nodeid, resNodes);
			}
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot drill up',
				timeout : 1500
			});
		});
	};

	$scope.drillDownNode = function(nodeid) {
		TreeService.nodeDrillDown({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resNodes) {
			if(resNodes.length){
				$scope.drillDownSubnode(nodeid, resNodes);
			}
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot drill down',
				timeout : 1500
			});
		});
	};

	$scope.drillUpSubnode = function(nodeid, resNodes) {
		TreeService.subnodeDrillUp({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resSubnodes) {
			myJsPlumb.drawExistingNodes(resNodes,
					resSubnodes);
			$scope.drillUpLink(nodeid, resSubnodes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot drill up',
				timeout : 1500
			});
		});
	};

	$scope.drillDownSubnode = function(nodeid, resNodes) {
		TreeService.subnodeDrillDown({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resSubnodes) {
			myJsPlumb.drawExistingNodes(resNodes,
					resSubnodes);
			$scope.drillDownLink(nodeid, resSubnodes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot drill down',
				timeout : 1500
			});
		});
	};

	$scope.drillUpLink = function(nodeid, resSubnodes) {
		TreeService.linkDrillUp({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id3 : nodeid
		}, function(resLinks) {
			myJsPlumb.makeNodeHierarchy(resLinks,
					resSubnodes);
			w_launch();
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot drill up',
				timeout : 1500
			});
		});
	};

	$scope.drillDownLink = function(nodeid, resSubnodes) {
		TreeService.linkDrillDown({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id3 : nodeid
		}, function(resLinks) {
			myJsPlumb.makeNodeHierarchy(resLinks,
					resSubnodes);
			w_launch();
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot drill down',
				timeout : 1500
			});
		});
	};

    $scope.editSubnodeTitle = function(data, id) {
    	if (data.length>45) {
    		noty({
    			type : 'alert',
    			text : 'Please enter a text with less than 45 Characters',
    			timeout : 3000
    		});
    		return "";
    	} else {
	        var subnode = $.grep($scope.subnodes, function(
	            t) {
	            return t.id === id;
	        })[0];
	        subnode.title = data;
	
	        TreeService.renameSubnode( { entity1 : 'ptrees', id1 : $routeParams.treeId, id2 : $scope.selectedNode.id, id3 : id },subnode, function(
	            response) {
	            $scope.getSubnodesOfNode(response);
	            // noty({type: 'success', text :
	            // 'subnode renamed successfully',
	            // timeout: 1500});
	        }, function (error) {
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'error renaming ' + DescriptionService.getSubnodeDescription(),
						 timeout : 1500
					 });
				 }
	        });
    	}
    };

    reloadTree();

    $scope.saveProjectTreeTitle = function(title) {
    	if (title.length>45) {
    		noty({
    			type : 'alert',
    			text : 'Please enter a text with less than 45 Characters',
    			timeout : 3000
    		});
    		return "";
    	} else {
	    	TreeService.updateTree({
				entity1 : 'ptrees',
				id1 : $scope.projecttree.id
			}, $scope.projecttree, function(response) { }, function(error) {
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'error while saving tree title',
						 timeout : 1500
					 });
				 }
			});
    	}
    };
    
    
    //SELECTED NODE / SUBNODE
    var updateNodeTitle = function() {
		if ($scope.selectedNode.id !== 0) {
			$scope.nodeTitle = "Selected " + DescriptionService.getNodeDescription() + ": " + $scope.selectedNode.title;
		} else {
			$scope.nodeTitle = "Selected " + DescriptionService.getNodeDescription() + ": no " + DescriptionService.getNodeDescription() + " selected";
		}
	};
	
	var updateSubnodeTitle = function() {
		if ($scope.selectedSubnode.id !== 0) {
			$scope.subnodeTitle = "Selected " + DescriptionService.getSubnodeDescription() + ": " + $scope.selectedSubnode.title;
		} else {
			$scope.subnodeTitle = "Selected " + DescriptionService.getSubnodeDescription() + ": no " + DescriptionService.getSubnodeDescription() + " selected";
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
	
	var getSubnodes = function() {
		TreeService.getSubnodes({
			entity1 : 'ptrees',
			id1 : $scope.projecttree.id,
			id2 : $scope.selectedNode.id
		}, function(response) {
			$scope.subnodes = response;
		}, function(error) {
			noty({
				type : 'alert',
				text : 'error getting ' + DescriptionService.getSubnodeDescription() + 's',
				timeout : 1500
			});
		});
	};
	
	$scope.changeNode = function(id) {
		setLoadingNode();
		TreeService.getNode({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : id
		}, function(response) {
			$scope.selectedNode = response;
			updateNodeTitle();
			$scope.selectedSubnode = {id : 0, title : ""};
			updateSubnodeTitle();
			getSubnodes();
			getComments();
			TreeService.getNodeWiki({
				entity1 : 'ptrees',
				id1 : $routeParams.treeId,
				id2 : id
			}, function(response) {
				$scope.selectedNodeWiki = response;
				changeWikiFields();
			}, function(error) {
				noty({
					type : 'alert',
					text : 'error getting wiki entry',
					timeout : 1500
				});
			});
		}, function(error) {
			noty({
				type : 'alert',
				text : 'error getting ' + DescriptionService.getNodeDescription(),
				timeout : 1500
			});
		});
	};
	
	var changeWikiFields = function() {
		$scope.wikiHtmlText = $scope.selectedNodeWiki.wikiContentHtml;
		$("#wikiArea").val(
				$scope.selectedNodeWiki.wikiContentPlain);
	};
	
	$scope.saveWikiNodeEntry = function() {
		if ($scope.selectedNodeWiki !== 0) {
			$scope.selectedNodeWiki.wikiContentPlain = $("#wikiArea").val();
			switchNodeToRead();
			setLoadingNode();
			TreeService
					.updateNodeWiki(
							{
								entity1 : 'ptrees',
								id1 : $routeParams.treeId,
								id2 : $scope.selectedNodeWiki.id
							},
							$scope.selectedNodeWiki,
							function(response) {
								$scope.selectedNodeWiki = response;
								changeWikiFields(response);
								noty({
									type : 'success',
									text : DescriptionService.getNodeDescription() + ' text edited successfully',
									timeout : 1500
								});
							}, function(error) {
								changeWikiFields($scope.selectedNodeWiki);
								noty({
									type : 'alert',
									text : 'cannot edit wiki text',
									timeout : 1500
								});
							});
		}
	};
	
	$scope.addNewSubnode = function() {
		if (this.newSubnodeName.length>45) {
			noty({
				type : 'alert',
				text : 'Please enter a text with less than 45 Characters',
				timeout : 3000
			});
		} else {
			TreeService.addSubnode({
				entity1 : 'ptrees',
				id1 : $scope.projecttree.id,
				id2 : $scope.selectedNode.id
			}, {
				nodeId : $scope.selectedNode.id,
				title : this.newSubnodeName
			}, function(response) {
				$scope.getSubnodesOfNode();
			}, function(error) {
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'cannot add ' + DescriptionService.getSubnodeDescription(),
						 timeout : 1500
					 });
				 }
			});
		}
	};
	
	$scope.newSubnodeWikiName = "";
	$scope.newSubnodeWikiTitle = "";
	
	$scope.addSubnodeByWikiTitle = function() {
		if (this.newSubnodeWikiName.length>45) {
			noty({
				type : 'alert',
				text : 'Please enter a text with less than 45 Characters',
				timeout : 3000
			});
		} else {
			TreeService.addSubnode({
				entity1 : 'ptrees',
				id1 : $scope.projecttree.id,
				id2 : $scope.selectedNode.id
			}, {
				nodeId : $scope.selectedNode.id,
				title : this.newSubnodeWikiName,
				wikititle : this.newSubnodeWikiTitle
			}, function(response) {
				$scope.getSubnodesOfNode();
			}, function(error) {
				 if (!$scope.showLockingNotification(error)) {	
					 noty({
						 type : 'alert',
						 text : 'cannot add ' + DescriptionService.getSubnodeDescription(),
						 timeout : 1500
					 });
				 }
			});
		}
	};
	
	$scope.getSubnodesOfNode = function(idObject) {
		var identity;
		var changes = null;
		if (typeof idObject === 'object' || idObject === undefined) {
			if (typeof idObject === 'object') {
				changes = idObject;
			}
			identity = $scope.selectedNode.id;
		} else {
			identity = idObject;
		}
		TreeService.getSubnodes({
			entity1 : 'ptrees',
			id1 : $scope.projecttree.id,
			id2 : identity
		}, function(response) {
			$scope.subnodes = response;
			myJsPlumb.updateSubnodesOfNode(response,
					identity, changes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'error getting ' + DescriptionService.getSubnodeDescription() + 's', 
				timeout : 1500
			});
		});
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
			TreeService.deleteSubnode({
				entity1 : 'ptrees',
				id1 : $routeParams.treeId,
				id2 : $scope.selectedNode.id
			}, {
				id : subnodeId
			}, function(response) {
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
			}, function(error) {
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'error deleting ' + DescriptionService.getSubnodeDescription(),
						 timeout : 1500
					 });
				 }
			});
		});
	};
	
	$scope.changeSubnode = function(subnodeid, name) {
		setLoadingSubnode();
		$scope.selectedSubnode.id = subnodeid;
		$scope.selectedSubnode.title = name;
		TreeService.getSubnodeWiki({
			entity1 : 'ptrees',
			id1 : $scope.projecttree.id,
			id2 : $scope.selectedNode.id,
			id3 : subnodeid
		}, function(response) {
			$scope.selectedSubnode = response;
			updateSubnodeTitle();
			changeWikiFieldsSubnode();
		}, function(error) {
			noty({
				type : 'alert',
				text : 'error getting wiki entry',
				timeout : 1500
			});
		});
	};
	
	var changeWikiFieldsSubnode = function() {
		$scope.subnodeHtmlText = $scope.selectedSubnode.wikiContentHtml;
		$("#wikiSubnodeArea").val($scope.selectedSubnode.wikiContentPlain);
	};
	
	$scope.saveWikiSubnodeEntry = function() {
		if ($scope.selectedSubnode !== 0) {
			$scope.selectedSubnode.wikiContentPlain = $("#wikiSubnodeArea").val();
			switchSubnodeToRead();
			setLoadingSubnode();
			TreeService.updateSubnodeWiki(
							{
								entity1 : 'ptrees',
								id1 : $scope.projecttree.id,
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
							}, function(error) {
								 if (!$scope.showLockingNotification(error)) {
									 noty({
										 type : 'alert',
										 text : 'cannot edit wiki text',
										 timeout : 1500
									 });
								 }
							});
		}
	};
	
	$scope.statuses = [
	                   {value: 1, text: 'undecided', show: false},
	                   {value: 2, text: 'accepted', show: true},
	                   {value: 3, text: 'declined', show: true},
                       {value: 4, text: 'revoked', show: true}
                      ]; 
	
	$scope.showStatus = function() {
		var selected = $filter('filter')($scope.statuses, {value: $scope.selectedNode.status});
		return ($scope.selectedNode.status && selected.length) ? selected[0].text : 'undecided';
	};

	$scope.updateNodeStatus = function(status) {
		var oldStatus = $scope.selectedNode.status;
		$scope.selectedNode.status = status;
		myJsPlumb.setStatusImage($scope.selectedNode);
		TreeService.updateNode({entity1:'ptrees',
			id1 : $scope.projecttree.id,
			id2 : $scope.selectedNode.id}, $scope.selectedNode, function(response) {
				$scope.selectedNode = response;
				$scope.changeNode(response.id);
			}, function(error) {
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'cannot update ' + DescriptionService.getNodeDescription() + ' status',
						 timeout : 1500
					 });
				 }
			});
	};
	
	$scope.moveSubnodeUp = function(id) {
		var subnode = $.grep($scope.subnodes, function(t) {
			return t.id === id;
		})[0];
		
		subnode.position = subnode.position - 1;
		
		TreeService.updateSubnode({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : $scope.selectedNode.id,
			id3 : subnode.id
		}, subnode, function(response) {
				$scope.getSubnodesOfNode();
		}, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'cannot drill up',
					 timeout : 1500
				 });
			 }
		});
	};

	$scope.moveSubnodeDown = function(id) {
		var subnode = $.grep($scope.subnodes, function(
				t) {
			return t.id === id;
		})[0];
		
		subnode.position = subnode.position + 1;
		
		TreeService.updateSubnode({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : $scope.selectedNode.id,
			id3 : subnode.id
		}, subnode, function(response) {
				$scope.getSubnodesOfNode();
		}, function(error) {
			 if (!$scope.showLockingNotification(error)) {
				 noty({
					 type : 'alert',
					 text : 'cannot drill down',
					timeout : 1500
				 });
			 }
		});
	};
	
	var getComments = function(nodeId) {
		TreeService.getComments({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : $scope.selectedNode.id
		}, function(response) {
			$scope.comments = response;
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot get comments',
				timeout : 1500
			});
		});
	};
	
	$scope.comments = "";
	$scope.newCommentText = "";
	
	$scope.addComment = function() {
		if (this.newCommentText.length>200) {
			noty({
				type : 'alert',
				text : 'Please enter a comment with less than 200 Characters',
				timeout : 3000
			});
		} else {
			TreeService.addComment({
				entity1 : 'ptrees',
				id1 : $routeParams.treeId,
				id2 : $scope.selectedNode.id
			}, {nodeid : $scope.selectedNode.id, comment : this.newCommentText}, function(response) {
				getComments();
			}, function(error) {
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'cannot add comment',
						 timeout : 1500
					 });
				 }
			});
		}
	};
	
	$scope.deleteComment = function(commentId) {
		$modal.open({ 
            templateUrl: 'templates/confirmation.html',
            backdrop: 'static',
            keyboard: false,
            resolve: {
                data: function() { 
                    return {
                        title: 'Delete Comment',
                        message: 'Do you really want to delete this Comment?' 
                    };
                }
                },
                controller: 'ConfirmationController' 
		}).result.then(function(result) {
			TreeService.deleteComment({
				entity1 : 'ptrees',
				id1 : $routeParams.treeId,
				id2 : $scope.selectedNode.id
			}, {id : commentId}, function(response) {
				getComments();
			}, function (error) {
				 if (!$scope.showLockingNotification(error)) {
					 noty({
						 type : 'alert',
						 text : 'cannot delete comment',
						 timeout : 1500
					 });
				 }
			});
		});
	};
}]);
