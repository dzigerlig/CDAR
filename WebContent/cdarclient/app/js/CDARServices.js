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

app.service('DescriptionService', ['$resource', '$cookieStore', function($resource, $cookieStore) {
	var descriptionResource = $resource('../webapi/descriptions');
	
	descriptionResource.get().$promise.then(function(response) {
	   $cookieStore.put('cdarNodeDescription', response.nodeDescription);
	   $cookieStore.put('cdarSubnodeDescription', response.subnodeDescription);
	   $cookieStore.put('cdarWikiUrl', response.wikiUrl);
    });
	
	this.getNodeDescription = function() {
		return $cookieStore.get('cdarNodeDescription');
	};
	
	this.getSubnodeDescription = function() {
		return $cookieStore.get('cdarSubnodeDescription');
	};
	
	this.getWikiUrl = function() {
		return $cookieStore.get('cdarWikiUrl');
	};
}])

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
