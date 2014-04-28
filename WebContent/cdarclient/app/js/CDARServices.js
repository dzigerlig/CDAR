app.factory('ProjectTreeService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/ptree/:action/:treeid/:ktreeid/',
					{}, {
						'getTrees' : {
							headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
							method : 'GET',
							isArray : true
						},
						'addTree' : {
							headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
							method : 'POST',
							params : {
								action : 'add'
							}
						},
						'removeTree' : {
							headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
							method : 'POST',
							params : {
								action : 'delete'
							}
						},
						'getTree' : {
							headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
							method : 'GET',
							isArray : false
						},

						// action: nodes?
						'getNodes' : {
							headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
							method : 'GET',
							params : {
								action : "nodes"
							},
							isArray : true
						},
						'copyTree' : {
							headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
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
                            headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
							method : 'GET'
						},
						'postEntry' : {
                            headers: {'uid': UserService.getUserId(),
                                'accesstoken': UserService.getAccesstoken()},
							method : 'POST'
						}
					});
		} ]);

app.factory('TreeService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/ktree/:entity/:action/:ktreeid/:entityid/', {}, {
				// Tree

				'getTrees' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true
				},
				'addTree' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'add'
					}
				},
				'renameTree' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'rename'
					}
				},
				'removeTree' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						action : 'delete'
					}
				},
				'getTree' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					params : {}
				},
				// EXPORT export/simple/{ktreeid}
				'getXmlTreesSimple' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'simpleexport'
					}
				},
				'removeXmlTreeSimple' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'simpleexport',
						action : 'delete'
					}
				},
				'setXmlTreeSimple' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'singleexport',
						action : 'set'
					}
				},
				'addXmlTreeSimple' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					params : {
						entity : 'simpleexport',
						action : 'add'
					}
				},

				// Directories
				'getDirectories' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'directories'
					}
				},
				'addDirectory' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'add'
					}
				},
				'deleteDirectory' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'delete'
					}
				},
				'renameDirectory' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'rename'
					}
				},

				'moveDirectory' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'move'
					}
				},

				// Nodes
				'getNodes' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes'
					}
				},
				'getNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					params : {
						entity : 'nodes'
					}
				},
				'addNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'add'
					}
				},
				'deleteNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'delete'
					}
				},
				'dropNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'drop'
					}
				},
				'renameNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'rename'
					}
				},
				'undropNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'undrop'
					}
				},

				'moveNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'move'
					}
				},
				'zoomUpNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomUp'
					}
				},
				'zoomDownNode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomDown'
					}
				},

				// Links
				'getLinks' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links'
					}
				},
				'addLink' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'links',
						action : 'add'
					}
				},
				'deleteLink' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'links',
						action : 'delete'
					}
				},
				'updateLink' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'links',
						action : 'update'
					}
				},
				
				'zoomUpLink' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomUp'
					}
				},
				'zoomDownLink' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomDown'
					}
				},

				// Templates
				'getTemplates' : {
					headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'templates'
					}
				},
				'getTemplate' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : false,
					params : {
						entity : 'templates'
					}
				},
				'addTemplate' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'add'
					}
				},
				'editTemplate' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'edit'
					}
				},
				'renameTemplate' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'rename'
					}
				},
				'setDefaultTemplate' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					isArray : true,
					params : {
						entity : 'templates',
						action : 'default'
					}
				},
				// Subnodes
				'getSubnodes' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes'
					}
				},
				'addSubnode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'add'
					}
				},
				'renameSubnode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'rename'
					}
				},
				'moveSubnodeUp' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'moveup'
					}
				},
				'moveSubnodeDown' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'movedown'
					}
				},
				'deleteSubnode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'delete'
					}
				},
				'deleteTemplate' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'delete'
					}
				},'zoomUpSubnode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes',
						action : 'zoomUp'
					}
				},
				'zoomDownSubnode' : {
                    headers: {'uid': UserService.getUserId(),
                        'accesstoken': UserService.getAccesstoken()},
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
