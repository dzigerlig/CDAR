app.controller("ProjectTreeController", ['$scope', '$routeParams', 'AuthenticationService', 'TreeService', 'UserService', '$filter', function ($scope, $routeParams, AuthenticationService, TreeService, UserService, $filter) {
	$scope.isProducer = false;
	myJsPlumb.initialize();
    $scope.UserService = UserService;
    $scope.projecttree = "";
    
    UserService.setIsProducer(false);
    
    $scope.nodetabs = [ { title : "READ" }, { title : "WRITE" } ];
	$scope.subnodetabs = [ { title : "READ" }, { title : "WRITE" } ];
    
	$scope.wikiHtmlText = "";
	
	$scope.selectedSubnodeId = 0;
	
	$scope.selectedNode = "";
	$scope.selectedNodeWiki = "";
	
	$scope.subnodes = "";
	$scope.newSubnodeName = 'Subnode';
	
	$scope.subnodeHtmlText = "";

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
                    $scope.getSubnodes(resNodes);
                }, function(error) {
                	noty({
        				type : 'alert',
        				text : 'error getting nodes',
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
    $scope.getSubnodes = function (resNodes) {
        TreeService.getSubnodesFromTree({
            entity1: 'ptrees',
            id1: $routeParams.treeId
        }, function (resSubnodes) {
            myJsPlumb.drawExistingNodes(resNodes, resSubnodes);
            $scope.getLinks(resSubnodes);
        }, function(error) {
        	noty({
				type : 'alert',
				text : 'error getting subnodes',
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
    };

    
    $scope.zoomUpNode = function(nodeid) {
		TreeService.nodeZoomUp({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resNodes) {
			$scope.zoomUpSubnode(nodeid, resNodes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot zoom up',
				timeout : 1500
			});
		});
	};

	$scope.zoomDownNode = function(nodeid) {
		TreeService.nodeZoomDown({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resNodes) {
			$scope.zoomDownSubnode(nodeid, resNodes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot zoom down',
				timeout : 1500
			});
		});
	};

	$scope.zoomUpSubnode = function(nodeid, resNodes) {
		TreeService.subnodeZoomUp({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resSubnodes) {
			myJsPlumb.drawExistingNodes(resNodes,
					resSubnodes);
			$scope.zoomUpLink(nodeid, resSubnodes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot zoom up',
				timeout : 1500
			});
		});
	};

	$scope.zoomDownSubnode = function(nodeid, resNodes) {
		TreeService.subnodeZoomDown({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : nodeid
		}, function(resSubnodes) {
			myJsPlumb.drawExistingNodes(resNodes,
					resSubnodes);
			$scope.zoomDownLink(nodeid, resSubnodes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'cannot zoom down',
				timeout : 1500
			});
		});
	};

	$scope.zoomUpLink = function(nodeid, resSubnodes) {
		TreeService.linkZoomUp({
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
				text : 'cannot zoom up',
				timeout : 1500
			});
		});
	};

	$scope.zoomDownLink = function(nodeid, resSubnodes) {
		TreeService.linkZoomDown({
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
				text : 'cannot zoom down',
				timeout : 1500
			});
		});
	};

    reloadTree();

    $scope.saveProjectTreeTitle = function(title) {
    	TreeService.updateTree({
			entity1 : 'ptrees',
			id1 : $scope.projecttree.id
		}, $scope.projecttree, function(response) { }, function(error) {
			noty({
				type : 'alert',
				text : 'error while saving tree title',
				timeout : 1500
			});
		});
    };
    
    
    //SELECTED NODE / SUBNODE
    var showNodeTitle = function() {
		if ($scope.selectedNode.id !== 0) {
			$scope.nodeTitle = "Selected node: " + $scope.selectedNode.title;
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
	
	var getSubnodes = function() {
		TreeService.getSubnodes({
			entity1 : 'ptrees',
			id1 : $scope.projecttree.id,
			id2 : $scope.selectedNode.id,
		}, function(response) {
			$scope.subnodes = response;
		}, function(error) {
			noty({
				type : 'alert',
				text : 'error getting subnodes',
				timeout : 1500
			});
		});
	};
	
	$scope.changeNode = function(id, name) {
		setLoadingNode();
		
		TreeService.getNode({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : id
		}, function(response) {
			$scope.selectedNode = response;
			showNodeTitle();
			getSubnodes();
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
				text : 'error getting node',
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
									text : 'node text edited successfully',
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
			noty({
				type : 'alert',
				text : 'cannot add subnode',
				timeout : 1500
			});
		});
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
			id2 : identity,
		}, function(response) {
			$scope.subnodes = response;
			myJsPlumb.updateSubnodesOfNode(response,
					identity, changes);
		}, function(error) {
			noty({
				type : 'alert',
				text : 'error getting subnodes',
				timeout : 1500
			});
		});
	};
	
	$scope.deleteSubnode = function(subnodeId) {
		TreeService.deleteSubnode({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : $scope.selectedNode.id,
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
			noty({
				type : 'alert',
				text : 'error deleting subnode',
				timeout : 1500
			});
		});
	};
	
	$scope.changeSubnode = function(subnodeid, name) {
		setLoadingSubnode();
		$scope.selectedSubnodeId = subnodeid;
		$scope.selectedSubnodeName = name;
		TreeService.getSubnodeWiki({
			entity1 : 'ptrees',
			id1 : $scope.projecttree.id,
			id2 : $scope.selectedNode.id,
			id3 : subnodeid
		}, function(response) {
			$scope.selectedSubnode = response;
			changeWikiFieldsSubnode();
			// load wiki fields
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
									text : 'subnode text edited successfully',
									timeout : 1500
								});
							}, function(error) {
								noty({
									type : 'alert',
									text : 'cannot edit wiki text',
									timeout : 1500
								});
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
			id2 : $scope.selectedNode.id}, $scope.selectedNode, function(response) { }, function(error) {
				noty({
					type : 'alert',
					text : 'cannot update node status',
					timeout : 1500
				});
			});
	};
	
}]);
