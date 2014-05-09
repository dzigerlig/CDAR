app.factory('TreeService',['$resource', function($resource) {
	return $resource('../webapi/:entity1/:id1/:entity2/:id2/:entity3/:id3/:action',
			{}, {
				//TREES
				'getTrees' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
				}, 'addTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST'
				}, 'getTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false
				}, 'updateTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST'
				}, 'deleteTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						action : 'delete'
					}
				}, 'copyTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						action : 'copy'
					}
				},
				'getAllUsersWithTreeRight' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'users'
					}
				},'setUserRight' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'users',
					}
				},
				// SIMPLEEXPORT
				'getExports' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'exports'
					}
				},
				'setExport' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						entity2 : 'exports',
						action : 'set'
					}
				},
				'addExport' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'exports'
					}
				},
				'updateExport' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'exports'
					}
				},
				'deleteExport' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'exports',
						action : 'delete'
					}
				},
				
				//DIRECTORIES
				'getDirectories' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'directories'
					}
				}, 'addDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'directories'
					}
				}, 'getDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'directories'
					}
				}, 'updateDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'directories'
					}
				}, 'deleteDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'directories',
						action : 'delete'
					}
				},
				//NODES
				'getNodes' : {
					headers : CDAR.getCustomHeader(),
					method  : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes'
					}
				}, 'addNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes'
					}
				}, 'getNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'nodes'
					}
				}, 'updateNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes'
					}
				}, 'deleteNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'delete'
					}
				}, 'getNodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						entity2 : 'nodes',
						action : 'wiki'
					}
				}, 'renameNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'rename'
					}
				}, 'updateNodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'wiki'
					}
				}, 'nodeZoomUp' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						action : 'zoomup'
					}
				}, 'nodeZoomDown' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						action : 'zoomdown'
					}
				},
				//SUBNODES
				'getSubnodes' : {
					headers : CDAR.getCustomHeader(),
					method  : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				},'getSubnodesFromTree' : {
					headers : CDAR.getCustomHeader(),
					method  : 'GET',
					isArray : true,
					params : {
						entity2 : 'subnodes'
					}
				}, 'addSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				}, 'getSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				}, 'updateSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				}, 'deleteSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'delete'
					}
				}, 'getSubnodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'wiki'
					}
				}, 'updateSubnodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'wiki'
					}
				}, 'subnodeZoomUp' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'zoomup'
					}
				}, 'subnodeZoomDown' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'zoomdown'
					}
				}, 'renameSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'rename'
					}
				}, 
				//LINKS
				'getLinks' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'links'
					}
				},
				'addLink' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'links'
					}
				}, 'deleteLink' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'links',
						action : 'delete'
					}
					
				},'updateLink' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'links'
					}
				}, 'linkZoomDown' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'links',
						entity3 : 'nodes',
						action : 'zoomdown'
					}
				}, 'linkZoomUp' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'links',
						entity3 : 'nodes',
						action : 'zoomup'
					}
				}, 
				//Templates
				'getTemplates' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'templates'
					}
				}, 'addTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'templates'
					}
				}, 'getTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'templates'
					}
				}, 'updateTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'templates'
					}
					
				}, 'deleteTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'templates',
						action : 'delete'
					}
				}, 
				//Comments
				'getComments' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'comments'
					}
					
				}, 'addComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'comments'
					}
					
				}, 'getComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'comments'
					}
					
				}, 'updateComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'comments'
					}
					
				}, 'deleteComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'comments',
						action: 'delete'
					}
				}
			});
}]);

//app.factory('ProjectTreeService', [
//		'$resource',
//		'UserService',
//		function($resource, UserService) {
//			return $resource('../webapi/ptree/:treeid/:action/:ktreeid/',
//					{}, {
//						'getTrees' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'GET',
//							isArray : true
//						},
//						'addTree' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'POST',
//							params : {
//								action : 'add'
//							}
//						},
//						'removeTree' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'POST',
//							params : {
//								action : 'delete'
//							}
//						},
//						'getTree' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'GET',
//							isArray : false
//						},
//
//						// action: nodes?
//						'getNodes' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'GET',
//							params : {
//								action : "nodes"
//							},
//							isArray : true
//						},
//						'copyTree' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'GET',
//							isArray : false,
//							params : {
//								action : 'copy'
//							}
//						}
//					});
//		} ]);

//app.factory('WikiService', [
//		'$resource',
//		'UserService',
//		function($resource, UserService) {
//			return $resource('../webapi/wiki/:role/:entity/:nodeid/', {},
//					{
//						'getWikiEntry' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'GET'
//						},
//						'postEntry' : {
//							headers: CDAR.getCustomHeader(),
//							method : 'POST'
//						}
//					});
//		} ]);

app.factory('ProducerTreeService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/ktree/:ktreeid/:entity/:action/:entityid/', {}, {
				// Tree

				'getTrees' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true
				},
				'addTree' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'add'
					}
				},
				'renameTree' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'rename'
					}
				},
				'removeTree' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						action : 'delete'
					}
				},
				'getTree' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					params : {}
				},
				// EXPORT export/simple/{ktreeid}
				'getXmlTreesSimple' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'simpleexport'
					}
				},
				'removeXmlTreeSimple' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'simpleexport',
						action : 'delete'
					}
				},
				'setXmlTreeSimple' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'singleexport',
						action : 'set'
					}
				},
				'addXmlTreeSimple' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						entity : 'simpleexport',
						action : 'add'
					}
				},

				// Directories
				'getDirectories' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'directories'
					}
				},
				'addDirectory' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'add'
					}
				},
				'deleteDirectory' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'delete'
					}
				},
				'renameDirectory' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'rename'
					}
				},

				'moveDirectory' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'move'
					}
				},

				// Nodes
				'getNodes' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes'
					}
				},
				'getNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						entity : 'nodes'
					}
				},
				'addNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'add'
					}
				},
				'deleteNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'delete'
					}
				},
				'dropNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'drop'
					}
				},
				'renameNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'rename'
					}
				},
				'undropNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'undrop'
					}
				},

				'moveNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'move'
					}
				},
				'zoomUpNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomUp'
					}
				},
				'zoomDownNode' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomDown'
					}
				},

				// Links
				'getLinks' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links'
					}
				},
				'addLink' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'links',
						action : 'add'
					}
				},
				'deleteLink' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'links',
						action : 'delete'
					}
				},
				'updateLink' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'links',
						action : 'update'
					}
				},
				
				'zoomUpLink' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomUp'
					}
				},
				'zoomDownLink' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomDown'
					}
				},

				// Templates
				'getTemplates' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'templates'
					}
				},
				'getTemplate' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity : 'templates'
					}
				},
				'addTemplate' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'add'
					}
				},
				'editTemplate' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'edit'
					}
				},
				'renameTemplate' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'rename'
					}
				},
				'setDefaultTemplate' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					isArray : true,
					params : {
						entity : 'templates',
						action : 'default'
					}
				},
				// Subnodes
				'getSubnodes' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes'
					}
				},
				'addSubnode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'add'
					}
				},
				'renameSubnode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'rename'
					}
				},
				'moveSubnodeUp' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'moveup'
					}
				},
				'moveSubnodeDown' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'movedown'
					}
				},
				'deleteSubnode' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'delete'
					}
				},
				'deleteTemplate' : {
					headers: CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'delete'
					}
				},'zoomUpSubnode' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes',
						action : 'zoomUp'
					}
				},
				'zoomDownSubnode' : {
					headers: CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes',
						action : 'zoomDown'
					}
				}
			});
		} ]);

app.factory('AuthenticationService', [ '$log', '$resource', '$location', 'UserService',
		function($log, $resource, $location, UserService) {
			return {
				add : $resource('../webapi/users', {}, {
					user : {
						method : 'POST',
						params : {},
						isArray : false
					}
				}),
				login : $resource('../webapi/users/login/:user/:pw', {}, {
					user : {
						method : 'GET',
						isArray : false
					}
				}),
				edit : $resource('../webapi/users/:userid', {}, {
					changepw:{
						method : 'POST',
						params : {},
						isArray : false},
				changeRights:{
					method : 'POST',
					params : {},
					isArray : false}

				}),
				logout : function() {
					UserService.removeCookies();
					$location.path('/login');
				}
			};
		} ]);


app.factory('UserService', [ '$location', '$cookieStore', function($location, $cookieStore) {
	return {
		getUsername : function() {
			return $cookieStore.get('cdarUsername');
		},
		getAccesstoken : function() {
			return $cookieStore.get('cdarAccesstoken');
		},
		getUserId : function() {
			return $cookieStore.get('cdarId');
		},
		getIsProducer : function() {
			return $cookieStore.get('cdarProducer');
		},
		isLoggedIn : function() {
			return this.getUserId() !== -1;
		},
		setUsername : function(val) {
			$cookieStore.put('cdarUsername', val);
		},
		setAccesstoken : function(val) {
			$cookieStore.put('cdarAccesstoken', val);
		},
		setUserId : function(val) {
			$cookieStore.put('cdarId', val);
		},
		setIsProducer : function(val) {
			$cookieStore.put('cdarProducer', val);
		},
		removeCookies : function() {
			CDAR.setCustomHeader('none','no-token');
			$cookieStore.remove('cdarUsername');
			$cookieStore.remove('cdarAccesstoken');
			$cookieStore.remove('cdarId');
			$cookieStore.remove('cdarProducer');
		},
		redirectUrl : function() {
			if (this.getIsProducer()=='true') {
				this.setIsProducer('false');
				$location.path('/homeconsumer');
			} else {
				this.setIsProducer('true');
				$location.path('/homeproducer');
			}
		}
	};
} ]);
