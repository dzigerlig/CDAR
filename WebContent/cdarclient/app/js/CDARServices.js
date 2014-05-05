app.factory('TreeService',['$resource', function($resource) {
	return $resource('../webapi/:entity1/:id1/:entity2/:id2/:entity3/:id3/:action',
			{}, {
				//TREES
				'getTrees' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
				}, 'addTree' : {
					headers : customHeaders,
					method : 'POST'
				}, 'getTree' : {
					headers : customHeaders,
					method : 'GET',
					isArray : false
				}, 'updateTree' : {
					headers : customHeaders,
					method : 'POST'
				}, 'deleteTree' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						action : 'delete'
					}
				}, 'copyTree' : {
					headers : customHeaders,
					method : 'GET',
					params : {
						action : 'copy'
					}
				},
				// SIMPLEEXPORT
				// noch leer
				
				//DIRECTORIES
				'getDirectories' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'directories'
					}
				}, 'addDirectory' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'directories'
					}
				}, 'getDirectory' : {
					headers : customHeaders,
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'directories'
					}
				}, 'updateDirectory' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'directories'
					}
				}, 'deleteDirectory' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'directories',
						action : 'delete'
					}
				},
				//NODES
				'getNodes' : {
					headers : customHeaders,
					method  : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes'
					}
				}, 'addNode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes'
					}
				}, 'getNode' : {
					headers : customHeaders,
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'nodes'
					}
				}, 'updateNode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes'
					}
				}, 'deleteNode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'delete'
					}
				}, 'getNodeWiki' : {
					headers : customHeaders,
					method : 'GET',
					params : {
						entity2 : 'nodes',
						action : 'wiki'
					}
				}, 'renameNode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'rename'
					}
				}, 'updateNodeWiki' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'wiki'
					}
				}, 'nodeZoomUp' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						action : 'zoomup'
					}
				}, 'nodeZoomDown' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						action : 'zoomdown'
					}
				},
				//SUBNODES
				'getSubnodes' : {
					headers : customHeaders,
					method  : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				},'getSubnodesFromTree' : {
					headers : customHeaders,
					method  : 'GET',
					isArray : true,
					params : {
						entity2 : 'subnodes'
					}
				}, 'addSubnode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				}, 'getSubnode' : {
					headers : customHeaders,
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				}, 'updateSubnode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				}, 'deleteSubnode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'delete'
					}
				}, 'getSubnodeWiki' : {
					headers : customHeaders,
					method : 'GET',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'wiki'
					}
				}, 'updateSubnodeWiki' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'wiki'
					}
				}, 'subnodeZoomUp' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'zoomup'
					}
				}, 'subnodeZoomDown' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'zoomdown'
					}
				}, 'renameSubnode' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'rename'
					}
				}, 
				//LINKS
				'getLinks' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'links'
					}
				},
				'addLink' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'links'
					}
				}, 'deleteLink' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'links',
						action : 'delete'
					}
					
				}, 'linkZoomDown' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'links',
						entity3 : 'nodes',
						action : 'zoomdown'
					}
				}, 'linkZoomUp' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'links',
						entity3 : 'nodes',
						action : 'zoomup'
					}
				}, 
				//Templates
				'getTemplates' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'templates'
					}
				}, 'addTemplate' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'templates'
					}
				}, 'getTemplate' : {
					headers : customHeaders,
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'templates'
					}
				}, 'updateTemplate' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'templates'
					}
					
				}, 'deleteTemplate' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'templates',
						action : 'delete'
					}
				}, 
				//Comments
				'getComments' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'comments'
					}
					
				}, 'addComment' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'comments'
					}
					
				}, 'getComment' : {
					headers : customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'comments'
					}
					
				}, 'updateComment' : {
					headers : customHeaders,
					method : 'POST',
					params : {
						entity2 : 'comments'
					}
					
				}, 'deleteComment' : {
					headers : customHeaders,
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
//							headers: customHeaders,
//							method : 'GET',
//							isArray : true
//						},
//						'addTree' : {
//							headers: customHeaders,
//							method : 'POST',
//							params : {
//								action : 'add'
//							}
//						},
//						'removeTree' : {
//							headers: customHeaders,
//							method : 'POST',
//							params : {
//								action : 'delete'
//							}
//						},
//						'getTree' : {
//							headers: customHeaders,
//							method : 'GET',
//							isArray : false
//						},
//
//						// action: nodes?
//						'getNodes' : {
//							headers: customHeaders,
//							method : 'GET',
//							params : {
//								action : "nodes"
//							},
//							isArray : true
//						},
//						'copyTree' : {
//							headers: customHeaders,
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
//							headers: customHeaders,
//							method : 'GET'
//						},
//						'postEntry' : {
//							headers: customHeaders,
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
					headers: customHeaders,
					method : 'GET',
					isArray : true
				},
				'addTree' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'add'
					}
				},
				'renameTree' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'rename'
					}
				},
				'removeTree' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						action : 'delete'
					}
				},
				'getTree' : {
					headers: customHeaders,
					method : 'GET',
					params : {}
				},
				// EXPORT export/simple/{ktreeid}
				'getXmlTreesSimple' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'simpleexport'
					}
				},
				'removeXmlTreeSimple' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'simpleexport',
						action : 'delete'
					}
				},
				'setXmlTreeSimple' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'singleexport',
						action : 'set'
					}
				},
				'addXmlTreeSimple' : {
					headers: customHeaders,
					method : 'GET',
					params : {
						entity : 'simpleexport',
						action : 'add'
					}
				},

				// Directories
				'getDirectories' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'directories'
					}
				},
				'addDirectory' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'add'
					}
				},
				'deleteDirectory' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'delete'
					}
				},
				'renameDirectory' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'rename'
					}
				},

				'moveDirectory' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'move'
					}
				},

				// Nodes
				'getNodes' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes'
					}
				},
				'getNode' : {
					headers: customHeaders,
					method : 'GET',
					params : {
						entity : 'nodes'
					}
				},
				'addNode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'add'
					}
				},
				'deleteNode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'delete'
					}
				},
				'dropNode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'drop'
					}
				},
				'renameNode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'rename'
					}
				},
				'undropNode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'undrop'
					}
				},

				'moveNode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'move'
					}
				},
				'zoomUpNode' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomUp'
					}
				},
				'zoomDownNode' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomDown'
					}
				},

				// Links
				'getLinks' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links'
					}
				},
				'addLink' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'links',
						action : 'add'
					}
				},
				'deleteLink' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'links',
						action : 'delete'
					}
				},
				'updateLink' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'links',
						action : 'update'
					}
				},
				
				'zoomUpLink' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomUp'
					}
				},
				'zoomDownLink' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomDown'
					}
				},

				// Templates
				'getTemplates' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'templates'
					}
				},
				'getTemplate' : {
					headers: customHeaders,
					method : 'GET',
					isArray : false,
					params : {
						entity : 'templates'
					}
				},
				'addTemplate' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'add'
					}
				},
				'editTemplate' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'edit'
					}
				},
				'renameTemplate' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'rename'
					}
				},
				'setDefaultTemplate' : {
					headers: customHeaders,
					method : 'POST',
					isArray : true,
					params : {
						entity : 'templates',
						action : 'default'
					}
				},
				// Subnodes
				'getSubnodes' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes'
					}
				},
				'addSubnode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'add'
					}
				},
				'renameSubnode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'rename'
					}
				},
				'moveSubnodeUp' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'moveup'
					}
				},
				'moveSubnodeDown' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'movedown'
					}
				},
				'deleteSubnode' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'delete'
					}
				},
				'deleteTemplate' : {
					headers: customHeaders,
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'delete'
					}
				},'zoomUpSubnode' : {
					headers: customHeaders,
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes',
						action : 'zoomUp'
					}
				},
				'zoomDownSubnode' : {
					headers: customHeaders,
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
					changepw : {
						method : 'POST',
						params : {},
						isArray : false
					}
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
			customHeaders.uid = 'none';
			customHeaders.accesstoken = 'no-token';
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
