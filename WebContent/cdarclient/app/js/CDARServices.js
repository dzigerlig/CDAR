app.factory('ProjectTreeService', function($resource) {
	return $resource('../webapi/ptree/:action/1/:treeid/:ktreeid/', {}, {
		'query' : {
			method : 'GET',
			isArray : true
		},
		'postEntry' : {
			method : 'POST'
		},
		'removeTree' : {
			method : 'GET',
			params: {
				action: 'delete'
					}
		},
		'getTree' : {
			method : 'GET',
			isArray : false
		},
		'getNodes' : {
			method : 'GET',
			params: {
				action: "nodes"
			},
			isArray : true
		},
		'copyTree' : {
			method : 'GET',
			isArray : false,
			params: {
				action: 'copy'
			}
		}
	});
});

app.factory('WikiService', function($resource) {
	return $resource('../webapi/wiki/:role/:entity/:nodeid/', {}, {
		'getWikiEntry' : {
			method : 'GET'
		},
		'postEntry' : {
			method : 'POST'
		}
	});
});

app.factory('TreeService', function($resource) {
	return $resource('../webapi/1/ktree/:entity/:action/:ktreeid/:entityid/', {}, {
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
		// Subnodes
		'getSubNodes' : {
			method : 'GET',
			isArray : true,
			params : {
				entity : 'subnodes'
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
});

app.factory('AuthenticationService', function($log, $resource, $location) {
	return {
		addUser: $resource('../webapi/users/registration', {}, {
			post:
				{
				method: 'POST',
				params: {},
				isArray: false
				}
		}),
		login: $resource('../webapi/users/login/:user/:pw', {}, {
			loginuser: {
				method: 'GET',
				isArray: false
			}
		}),
		edit: $resource('../webapi/users/edit', {}, {
			changepw: {
				method: 'POST',
				params: {},
				isArray: false
			}
		}),
		logout: function() {
			$.removeCookie('cdar');
			$location.path('/login');
		}
	};
});

app.factory('UserService', ['$location', function ($location) {
	return {
		user : $.cookie('cdar'),
		isLoggedIn : function() { return this.user.id!=-1; },
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