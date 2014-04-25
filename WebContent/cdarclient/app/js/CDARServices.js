app.factory('ProjectTreeService', [
		'$resource',
		'UserService',
		function($resource, UserService) {
			return $resource('../webapi/ptree/:action/:treeid/:ktreeid/',
					{}, {
						'query' : {
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
							method : 'GET',
							isArray : true
						},
						'addTree' : {
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
							method : 'POST',
							params : {
								action : 'add'
							}
						},
						'removeTree' : {
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
							method : 'POST',
							params : {
								action : 'delete'
							}
						},
						'getTree' : {
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
							method : 'GET',
							isArray : false
						},

						// action: nodes?
						'getNodes' : {
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
							method : 'GET',
							params : {
								action : "nodes"
							},
							isArray : true
						},
						'copyTree' : {
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
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
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
							method : 'GET'
						},
						'postEntry' : {
                            headers: {'uid':UserService.user.id,
                                'auth-token': UserService.user.accesstoken},
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
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true
				},
				'addTree' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'add'
					}
				},
				'renameTree' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'tree',
						action : 'rename'
					}
				},
				'removeTree' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						action : 'delete'
					}
				},
				'getTree' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					params : {}
				},
				// EXPORT export/simple/{ktreeid}
				'getXmlTreesSimple' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'simpleexport'
					}
				},
				'removeXmlTreeSimple' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'simpleexport',
						action : 'delete'
					}
				},
				'setXmlTreeSimple' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'singleexport',
						action : 'set'
					}
				},
				'addXmlTreeSimple' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					params : {
						entity : 'simpleexport',
						action : 'add'
					}
				},

				// Directories
				'getDirectories' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'directories'
					}
				},
				'addDirectory' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'add'
					}
				},
				'deleteDirectory' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'delete'
					}
				},
				'renameDirectory' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'rename'
					}
				},

				'moveDirectory' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'directories',
						action : 'move'
					}
				},

				// Nodes
				'getNodes' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes'
					}
				},
				'getNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					params : {
						entity : 'nodes'
					}
				},
				'addNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'add'
					}
				},
				'deleteNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'delete'
					}
				},
				'dropNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'drop'
					}
				},
				'renameNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'rename'
					}
				},
				'undropNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'undrop'
					}
				},

				'moveNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'nodes',
						action : 'move'
					}
				},
				'zoomUpNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomUp'
					}
				},
				'zoomDownNode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'nodes',
						action : 'zoomDown'
					}
				},

				// Links
				'getLinks' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links'
					}
				},
				'addLink' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'links',
						action : 'add'
					}
				},
				'deleteLink' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'links',
						action : 'delete'
					}
				},
				'updateLink' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'links',
						action : 'update'
					}
				},
				
				'zoomUpLink' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomUp'
					}
				},
				'zoomDownLink' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'links',
						action : 'zoomDown'
					}
				},

				// Templates
				'getTemplates' : {
                    headers: {'uid':UserService.user.id,
                'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'templates'
					}
				},
				'getTemplate' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : false,
					params : {
						entity : 'templates'
					}
				},
				'addTemplate' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'add'
					}
				},
				'editTemplate' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'edit'
					}
				},
				'renameTemplate' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'rename'
					}
				},
				'setDefaultTemplate' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					isArray : true,
					params : {
						entity : 'templates',
						action : 'default'
					}
				},
				// Subnodes
				'getSubnodes' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes'
					}
				},
				'addSubnode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'add'
					}
				},
				'renameSubnode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'rename'
					}
				},
				'moveSubnodeUp' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'moveup'
					}
				},
				'moveSubnodeDown' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'movedown'
					}
				},
				'deleteSubnode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'subnodes',
						action : 'delete'
					}
				},
				'deleteTemplate' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'POST',
					params : {
						entity : 'templates',
						action : 'delete'
					}
				},'zoomUpSubnode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes',
						action : 'zoomUp'
					}
				},
				'zoomDownSubnode' : {
                    headers: {'uid':UserService.user.id,
                        'auth-token': UserService.user.accesstoken},
					method : 'GET',
					isArray : true,
					params : {
						entity : 'subnodes',
						action : 'zoomDown'
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
