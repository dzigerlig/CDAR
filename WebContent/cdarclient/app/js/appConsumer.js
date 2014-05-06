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

    $scope.logout = function () {
        AuthenticationService.logout();
    };
}]);

app.controller("ProjectTreeController", ['$scope', '$routeParams', 'AuthenticationService', 'TreeService', 'UserService', function ($scope, $routeParams, AuthenticationService, TreeService, UserService) {
    $scope.UserService = UserService;
    $scope.projecttree = "";
    $scope.nodes = "";
    $scope.trees = "";
    $scope.selectedktreeId = "";
    
    $scope.wikiText = "no wiki entry selected";

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

        /*	TreeService.getNodes({entity1 : 'ptrees', id1 : $routeParams.treeId}, function(response) {
         $scope.nodes = response;
         });*/


        TreeService.getDirectories({
            entity1: 'ptrees',
            id1: $routeParams.treeId
        }, function (resDirectory) {
            TreeService.getNodes({
                    entity1: 'ptrees', id1: $routeParams.treeId
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
        }, resNodes, function (resSubnodes) {
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
        }, resSubnodes, function (response) {
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

    //wieso knowledge tree in project tree?
//    TreeService.getDirectories({
//        ktreeid : $routeParams.treeId
//    }, function(resDirectory) {
//        TreeService.getNodes({
//                ktreeid : $routeParams.treeId
//            },
//            function(resNodes) {
//
//                myJsTree.directoryDataToArray(resDirectory,
//                    resNodes);
//                $scope.getSubnodes(resNodes);
//
//            });
//
//    });

//    $scope.getSubnodes = function(resNodes) {
//        TreeService.getSubnodes({
//            ktreeid : $routeParams.treeId
//        }, resNodes, function(resSubnodes) {
//            myJsPlumb.drawExistingNodes(resNodes, resSubnodes);
//            $scope.getLinks(resSubnodes);
//        });
//    };

//    $scope.getLinks = function(resSubnodes) {
//        TreeService.getLinks({
//            ktreeid : $routeParams.treeId
//        }, resSubnodes, function(response) {
//            myJsPlumb.makeNodeHierarchy(response, resSubnodes);
//            w_launch();
//        });
//    };

    TreeService.getTrees({entity1: 'ktrees' }, function (response) {
        $scope.knowledgetrees = response;
    });

    $scope.addKnowledgeTree = function () {
        if (typeof($scope.selectedktreeId) != "undefined") {

            TreeService.copyTree({entity1: 'ptrees', id1: $routeParams.treeId, entity2: 'ktrees', id2: $scope.selectedktreeId}, function (response) {
                reloadTree();
            }, function(error) {
            	noty({
					type : 'alert',
					text : 'cannot copy tree',
					timeout : 1500
				});
            });
        }
    };
}]);
