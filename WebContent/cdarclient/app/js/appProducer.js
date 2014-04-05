var reload = false;
function getReload() {
	return reload;
}
function setReload(value) {
	reload = value;
}

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
				entity : 'directories',
			}
		},
		'addDirectory' : {
			method : 'POST',
			params : {
				entity : 'directories',
				action : 'add',
			}
		},
		'deleteDirectory' : {
			method : 'POST',
			params : {
				entity : 'directories',
				action : 'delete',
			}
		},
		'renameDirectory' : {
			method : 'POST',
			params : {
				entity : 'directories',
				action : 'rename',
			}
		},

		'moveDirectory' : {
			method : 'POST',
			params : {
				entity : 'directories',
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
		},
		'getTemplates' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'templates'
			}
		},
		'addTemplate' : {
			method : 'POST',
			params : {
				entity : 'templates',
				action : 'add'
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
							}, function(resDirectory) {
								TreeService.getNodes({
									ktreeid : $routeParams.treeId
								}, function(resNodes) {
									drawExistingNodes(resNodes);
									$scope.getLinks(TreeService);
									directoryDataToArray(resDirectory,
											resNodes);
								});

							});

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
									did :did
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
									ktrid:$routeParams.treeId,
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

app.controller("TemplatesController", [
		'$scope',
		'$routeParams',
		'TreeService',
		'AuthenticationService',
		'WikiService',
		'UserService',
		'$route',
		function($scope, $routeParams, TreeService, AuthenticationService,
				WikiService, UserService, $route) {
			$scope.knowledgetree;
			$scope.templates;
			$scope.template;
			$scope.newTemplateName;

			TreeService.getTree({
				ktreeid : $routeParams.treeId
			}, function(response) {
				$scope.knowledgetree = response;
			});

			var reloadTemplates = function() {
				TreeService.getTemplates({
					ktreeid : $routeParams.treeId
				}, function(response) {
					$scope.templates = response;
				});
			};

			reloadTemplates();

			$scope.addNewTemplate = function() {
				TreeService.addTemplate({
					ktreeid : $routeParams.treeId
				}, {
					treeid : $routeParams.treeId,
					title : $scope.newTemplateName
				}, function(response) {
					if (response.id != -1) {
						alert(JSON.stringify(response));
						reloadTemplates();
						$scope.newTemplateName = '';
					} else {
						alert("exception");
					}
				});
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
