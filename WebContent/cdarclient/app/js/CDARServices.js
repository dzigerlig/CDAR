app.factory('ProjectTreeService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/' + UserService.user.accesstoken + '/'
					+ UserService.user.id + '/ptree/:action/:treeid/:ktreeid/',
					{}, {
						'query' : {
							method : 'GET',
							isArray : true
						},
						'addTree' : {
							method : 'POST',
							params : {
								action : 'add'
							}
						},
						'removeTree' : {
							method : 'POST',
							params : {
								action : 'delete'
							}
						},
						'getTree' : {
							method : 'GET',
							isArray : false
						},

						// action: nodes?
						'getNodes' : {
							method : 'GET',
							params : {
								action : "nodes"
							},
							isArray : true
						},
						'copyTree' : {
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
			return $resource('../webapi/' + UserService.user.accesstoken + '/'
					+ UserService.user.id + '/wiki/:role/:entity/:nodeid/', {},
					{
						'getWikiEntry' : {
							method : 'GET'
						},
						'postEntry' : {
							method : 'POST'
						}
					});
		} ]);

app.factory('TreeService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/' + UserService.user.accesstoken + '/'
					+ UserService.user.id
					+ '/ktree/:entity/:action/:ktreeid/:entityid/', {}, {
				// Tree

				'getTrees' : {
					method : 'GET',
					isArray : true
				},
				'addTree' : {
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'add'
					}
				},
				'renameTree' : {
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'rename'
					}
				},
				'removeTree' : {
					method : 'POST',
					params : {
						action : 'delete',
					}
				},
				'getTree' : {
					method : 'GET',
					params : {}
				},
				// EXPORT export/simple/{ktreeid}
				'getXmlTreesSimple' : {
					method : 'GET',
					isArray : true,
					params : {
						entity : 'simpleexport',
					}
				},
				'removeXmlTreeSimple' : {
					method : 'POST',
					params : {
						entity : 'simpleexport',
						action : 'delete'
					}
				},
				'setXmlTreeSimple' : {
					method : 'POST',
					params : {
						entity : 'singleexport',
						action : 'set'
					}
				},
				'addXmlTreeSimple' : {
					method : 'GET',
					params : {
						entity : 'simpleexport',
						action : 'add'
					}
				},

				// Directories
				'getDirectories' : {
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
				'getNode' : {
					method : 'GET',
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
				'zoomUpNode' : {
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomUp',
					}
				},
				'zoomDownNode' : {
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomDown',
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
				'updateLink' : {
					method : 'POST',
					params : {
						entity : 'links',
						action : 'update',
					}
				},
				
				'zoomUpLink' : {
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomUp',
					}
				},
				'zoomDownLink' : {
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomDown',
					}
				},

				// Templates
				'getTemplates' : {
					method : 'GET',
					isArray : true,
					params : {
						entity : 'templates'
					}
				},
				'getTemplate' : {
					method : 'GET',
					isArray : false,
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
				},
				'editTemplate' : {
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'edit'
					}
				},
				'renameTemplate' : {
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'rename'
					}
				},
				'setDefaultTemplate' : {
					method : 'POST',
					isArray : true,
					params : {
						entity : 'templates',
						action : 'default'
					}
				},
				// Subnodes
				'getSubnodes' : {
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes'
					}
				},
				'addSubnode' : {
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'add'
					}
				},
				'renameSubnode' : {
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'rename'
					}
				},
				'moveSubnodeUp' : {
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'moveup'
					}
				},
				'moveSubnodeDown' : {
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'movedown'
					}
				},
				'deleteSubnode' : {
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'delete'
					}
				},
				'deleteTemplate' : {
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'delete'
					}
				}
			});
		} ]);

app.factory('AuthenticationService', [ '$log', '$resource', '$location',
		function($log, $resource, $location) {
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
					$.removeCookie('cdar');
					$location.path('/login');
				}
			};
		} ]);

app.factory('UserService', [ '$location', function($location) {
	return {
		user : $.cookie('cdar'),
		isLoggedIn : function() {
			return this.user.id != -1;
		},
		redirectUrl : function() {
			this.user.isProducer = !this.user.isProducer;
			$.cookie('cdar', this.user, {
				expires : 7
			});
			if (!this.user.isProducer) {
				$location.path('/homeconsumer');
			} else {
				$location.path('/homeproducer');
			}
		}
	};
} ]);
