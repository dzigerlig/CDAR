app.controller("HomeConsumerController", ['$scope', 'AuthenticationService', 'TreeService', 'UserService', function ($scope, AuthenticationService, TreeService, UserService) {
    $scope.projectTrees = "";
    $scope.newTreeName = "";
    $scope.UserService = UserService;
    $scope.knowledgetrees = "";
    $scope.selectedktreeId = "";
    
    
    TreeService.getTrees({entity1: 'ktrees' }, function (response) {
        $scope.knowledgetrees = response;
    });
    
    var reloadTrees = function () {
        TreeService.getTrees({entity1: 'ptrees'}, function (response) {
            $scope.projectTrees = response;
        }, function (error) {
        	noty({
				type : 'alert',
				text : 'error getting trees',
				timeout : 1500
			});
        });
    };

    reloadTrees();

    $scope.addNewTree = function() {
    	if ($scope.selectedktreeId.length!==0) {
	        TreeService.addTree({ entity1: 'ptrees' }, { copyTreeId : $scope.selectedktreeId, title: $scope.newTreeName }, function (response) {
	            $scope.newTreeName = '';
	            reloadTrees();
	        }, function (error) {
	        	noty({
					type : 'alert',
					text : 'error while adding tree',
					timeout : 1500
				});
	        });
    	}
    };

    $scope.deleteTree = function (treeid) {
        TreeService.deleteTree({ entity1: 'ptrees' }, { id: treeid }, function (response) {
            reloadTrees();
        }, function (error) {
        	noty({
				type : 'alert',
				text : 'cannot delete tree',
				timeout : 1500
			});
        });
    };
    
    $scope.saveProjectTreeTitle = function(data, id) {
    	var tree = $.grep($scope.projectTrees, function(t) {
    		return t.id === id;
    	})[0];
    	
    	var oldTitle = tree.title;
    	tree.title = data;
    	
    	TreeService.updateTree({
    		entity1 : 'ptrees',
    		id1 : tree.id
    	}, tree, function(response) {}, function(error) {
    		tree.title = oldTitle;
			noty({
				type : 'alert',
				text : 'error while saving tree title',
				timeout : 1500
			});
    	});
    }

    $scope.logout = function () {
        AuthenticationService.logout();
    };
}]);

app.controller("ProjectTreeController", ['$scope', '$routeParams', 'AuthenticationService', 'TreeService', 'UserService', function ($scope, $routeParams, AuthenticationService, TreeService, UserService) {
	$scope.isProducer = false;
    $scope.UserService = UserService;
    $scope.projecttree = "";
    
    $scope.nodetabs = [ { title : "READ" }, { title : "WRITE" } ];
	$scope.subnodetabs = [ { title : "READ" }, { title : "WRITE" } ];
    
	$scope.nodeTitle = "";
	$scope.wikiHtmlText = "";
	
	$scope.selectedNodeId = 0;
	$scope.selectedSubnodeId = 0;
	
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


    $scope.changeNode = function (nodeid) {
        $scope.wikiText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
        TreeService.getNodeWiki({entity1: 'ptrees', id1: $routeParams.treeId, id2: nodeid}, function (response) {
            $scope.wikiText = response.wikiContentHtml;
        }, function(error) {
        	noty({
				type : 'alert',
				text : 'cannot get node wiki entry',
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
	
	var getSubnodes = function() {
		TreeService.getSubnodes({
			entity1 : 'ptrees',
			id1 : $scope.projecttree.id,
			id2 : $scope.selectedNodeId,
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
		$scope.selectedNodeId = id;
		$scope.selectedNodeName = name;
		showNodeTitle();
		getSubnodes();
		TreeService.getNodeWiki({
			entity1 : 'ptrees',
			id1 : $routeParams.treeId,
			id2 : id
		}, function(response) {
			$scope.selectedNode = response;
			changeWikiFields();
		}, function(error) {
			noty({
				type : 'alert',
				text : 'error getting wiki entry',
				timeout : 1500
			});
		});
	};
	
	var changeWikiFields = function() {
		$scope.wikiHtmlText = $scope.selectedNode.wikiContentHtml;
		$("#wikiArea").val(
				$scope.selectedNode.wikiContentPlain);
	};
	
	$scope.saveWikiNodeEntry = function() {
		if ($scope.selectedNode !== 0) {
			$scope.selectedNode.wikiContentPlain = $("#wikiArea").val();
			switchNodeToRead();
			setLoadingNode();
			TreeService
					.updateNodeWiki(
							{
								entity1 : 'ptrees',
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
								alert(JSON.stringify(response));
							}, function(error) {
								changeWikiFields($scope.selectedNode);
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
			id2 : $scope.selectedNodeId
		}, {
			nodeId : $scope.selectedNodeId,
			title : this.newSubnodeName
		}, function(response) {
			$scope.getSubnodesOfNode();
			$scope.newSubnodeName = 'Subnode';
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
			identity = $scope.selectedNodeId;
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
			id2 : $scope.selectedNodeId,
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
}]);
