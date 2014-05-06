app.controller("HomeConsumerController", ['$scope', 'AuthenticationService', 'TreeService', 'UserService', function ($scope, AuthenticationService, TreeService, UserService) {
    $scope.projectTrees = "";
    $scope.newTreeName = "";
    $scope.UserService = UserService;

    var reloadTrees = function () {
        TreeService.getTrees({entity1: 'ptrees'}, function (response) {
            $scope.projectTrees = response;
        }, function (error) {
            //error handling
        });
    };

    reloadTrees();

    $scope.addNewTree = function () {
        TreeService.addTree({ entity1: 'ptrees' }, {title: $scope.newTreeName}, function (response) {
            $scope.newTreeName = '';
            reloadTrees();
        }, function (error) {
            alert("Error: ProjectTree NOT added!");
        });
    };

    $scope.deleteTree = function (treeid) {
        TreeService.deleteTree({ entity1: 'ptrees' }, { id: treeid }, function (response) {
            reloadTrees();
        }, function (error) {
            //error handling
        });
    };

    $scope.logout = function () {
        AuthenticationService.logout();
    };
}]);

app.controller("ProjectTreeController", ['$scope', '$routeParams', 'AuthenticationService', 'TreeService', 'TreeService', 'UserService', function ($scope, $routeParams, AuthenticationService, TreeService, UserService) {
    $scope.UserService = UserService;
    $scope.projecttree = "";
    $scope.nodes = "";
    $scope.trees = "";
    $scope.selectedktreeId = "";

    $scope.wikiText = "no wiki entry selected";

    var reloadTree = function () {
        TreeService.getTree({entity1: 'ptrees', id1: $routeParams.treeId}, function (response) {
            $scope.projecttree = response;
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
        });
    };

    $scope.getLinks = function (resSubnodes) {
        TreeService.getLinks({
            entity1: 'ptrees',
            id1: $routeParams.treeId
        }, resSubnodes, function (response) {
            myJsPlumb.makeNodeHierarchy(response, resSubnodes);
            w_launch();
        });
    };


    $scope.changeNode = function (nodeid) {
        $scope.wikiText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
        TreeService.getNodeWiki({entity1: 'ptrees', id1: $routeParams.treeId, id2: nodeid}, function (response) {
            $scope.wikiText = response.wikiContentHtml;
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
            });
        }
    };
}]);