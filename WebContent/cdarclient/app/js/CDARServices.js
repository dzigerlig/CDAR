app.factory('ProjectTreeService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/ptree/:treeid/:action/:ktreeid/',
					{}, {
						'getTrees' : {
							headers: customHeaders,
							method : 'GET',
							isArray : true
						},
						'addTree' : {
							headers: customHeaders,
							method : 'POST',
							params : {
								action : 'add'
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
							isArray : false
						},

						// action: nodes?
						'getNodes' : {
							headers: customHeaders,
							method : 'GET',
							params : {
								action : "nodes"
							},
							isArray : true
						},
						'copyTree' : {
							headers: customHeaders,
							method : 'GET',
							isArray : false,
							params : {
								action : 'copy'
							}
						}
					});
		} ]);

app.factory('WikiService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/wiki/:role/:entity/:nodeid/', {},
					{
						'getWikiEntry' : {
							headers: customHeaders,
							method : 'GET'
						},
						'postEntry' : {
							headers: customHeaders,
							method : 'POST'
						}
					});
		} ]);

app.factory('TreeService', [
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
				addUser : $resource('../webapi/users/registration', {}, {
					post : {
						method : 'POST',
						params : {},
						isArray : false
					}
				}),
				login : $resource('../webapi/users/login/:user/:pw', {}, {
					loginuser : {
						method : 'GET',
						isArray : false
					}
				}),
				edit : $resource('../webapi/users/edit', {}, {
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
