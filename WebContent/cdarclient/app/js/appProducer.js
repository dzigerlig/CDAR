app.factory('TreeService', function($resource) {
	return $resource('../webapi/1/ktree/:entity/:action/:ktreeid/', {}, {
		// Tree

		'getTrees' : {
			method : 'GET',
			isArray : true
		},
		'postEntry' : {
			method : 'POST'
		},
		'removeTree' : {
			method : 'GET',
			params : {
				entity : 'delete',
			}
		},
		'getTree' : {
			method : 'GET',
			params : {}
		},

		// Dictionaries
		'getDictionaries' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'dictionaries',
			}
		},
		'addDictionary' : {
			method : 'POST',
			params : {
				entity : 'dictionaries',
				action : 'add',
			}
		},
		'deleteDictionary' : {
			method : 'POST',
			params : {
				entity : 'dictionaries',
				action : 'delete',
			}
		},
		'renameDictionary' : {
			method : 'POST',
			params : {
				entity : 'dictionaries',
				action : 'rename',
			}
		},

		'moveDictionary' : {
			method : 'POST',
			params : {
				entity : 'dictionaries',
				action : 'move',
			}
		},

		// Nodes
		'getNodes' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'nodes',
			}
		},
		'addNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'add',
			}
		},
		'deleteNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'delete',
			}
		},
		'dropNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'drop',
			}
		},
		'renameNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'rename',
			}
		},
		'undropNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'undrop',
			}
		},

		'moveNode' : {
			method : 'POST',
			params : {
				entity : 'nodes',
				action : 'move',
			}
		},

		// Links
		'getLinks' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'links',
			}
		},
		'addLink' : {
			method : 'POST',
			params : {
				entity : 'links',
				action : 'add',
			}
		},
		'deleteLink' : {
			method : 'POST',
			params : {
				entity : 'links',
				action : 'delete',
			}
		}
	});
});
/*
 * app.factory('KnowledgeTreeService', function($resource) { return
 * $resource('../webapi/ktree/:action/1/:treeid', {}, { 'query' : { method :
 * 'GET', isArray : true }, 'postEntry' : { method : 'POST' }, 'removeTree' : {
 * method : 'GET', params: { action: 'delete' } }, 'getTree' : { method : 'GET',
 * isArray : false }, 'getNodes' : { method : 'GET', isArray : true, params: {
 * action: 'nodes' } } }); });
 */

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
					if (response[0] == 1) {
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
						function($scope, $routeParams, TreeService,
								AuthenticationService, WikiService, UserService) {
							initializeJsPlumb();

							$scope.knowledgetree;
							$scope.nodes;
							$scope.UserService = UserService;
							$scope.selectedNode = 0;

							$scope.wikiHtmlText = "no wiki entry selected";
							$scope.wikiEntry;

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
							}, function(resDictionary) {
								dictionaryDataToArray(resDictionary);
							});
														
							TreeService.getNodes({
								ktreeid : $routeParams.treeId
							}, function(response) {
								drawExistingNodes(response);
								$scope.getLinks(TreeService);
							});

							$scope.getLinks = function(TreeService) {
								TreeService.getLinks({
									ktreeid : $routeParams.treeId
								}, function(response) {
									makeNodeHierarchy(response);
									w_launch();
								});
							};

							$scope.addNode = function() {
								TreeService.addNode({
									ktreeid : $routeParams.treeId
								}, {
									refTreeId : 1
								}, function(response) {
									createNode(response);
									// get Node and set id TODO
									// addHTMLNode(response, e);
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

							$scope.renameNode = function(id, newTitle) {
								TreeService.renameNode({
									ktreeid : $routeParams.treeId
								}, {
									id : id,
									title : newTitle
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
									knid : id,
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

							$scope.renameDictionary = function(id, newTitle) {
								TreeService.renameDictionary({
									ktreeid : $routeParams.treeId
								}, {
									id : id,
									title : newTitle
								});
							};

							$scope.deleteDictionary = function(id, newTitle) {
								// TODO beachten mit cascade in datanbank oder
								// ohne
								TreeService.deleteDictionary({
									ktreeid : $routeParams.treeId
								}, id);
							};

							$scope.moveDictionary = function(id, newParentId) {
								TreeService.moveDictionary({
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
									setLoading();
									WikiService.postEntry({
										role : 'producer'
									}, $scope.wikiEntry, function(response) {
										changeWikiFields(response);
									});
								}
							};
						} ]);

/*
 * 
 * app.controller("TreeController", [ '$scope', 'AuthenticationService',
 * 'TreeService', function ($scope, AuthenticationService, TreeService) {
 * initializeJsPlumb(); $scope.title = "TEST"; $scope.message = "Mouse Over
 * these images to see a directive at work!";
 * 
 * $scope.hello = function (att) { console.log(att); };
 * 
 * $scope.logout = function () { AuthenticationService.logout(); };
 * 
 * $scope.removeNode = function (id) { TreeService.removeNodes.removeNodes({ id:
 * id }); };
 * 
 * TreeService.getNodes.getNodes(function (response) { myFunction(response); }); }
 * ]);
 * 
 * 
 * app.factory('TreeService',function ($resource) { return{ removeNodes:
 * $resource('http://localhost:8080/CDAR/webapi/nodes/removeNode/:id', {}, {
 * 'removeNodes': { params:{id:'@id'}, method: 'OPTIONS' } }),
 * 
 * getNodes: $resource('http://localhost:8080/CDAR/webapi/nodes', {}, {
 * 'getNodes': { method: 'GET', isArray: true } }) } });
 */