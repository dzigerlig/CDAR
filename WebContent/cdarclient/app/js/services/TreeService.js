//Rest Treecalls
app.factory(
		'TreeService', ['$resource', function($resource) {
			return $resource('../webapi/:entity1/:id1/:entity2/:id2/:entity3/:id3/:action', {}, {
				'getTrees' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
				},
				
				'addTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST'
				},
			
				'getTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false
				},
				
				'updateTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST'
				},
				
				'deleteTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						action : 'delete'
					}
				},
				
				'copyTree' : {
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
				},
				
				'setUserRight' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'users',
					}
				},
			
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
				
				'getDirectories' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'directories'
					}
				},
				
				'addDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'directories'
					}
				},
				
				'getDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'directories'
					}
				},
		
				'updateDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'directories'
					}
				},
				
				'deleteDirectory' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'directories',
						action : 'delete'
					}
				},
	
				'getNodes' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes'
					}
				},
				'addNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes'
					}	
				},
				'addNodeCopy' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'copy'
					}
				},
				'getNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'nodes'
					}
				},
				
				'updateNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes'
					}
				},
				
				'deleteNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'delete'
					}
				},
				
				'getNodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						entity2 : 'nodes',
						action : 'wiki'
					}
				},
			
				'renameNode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'rename'
					}
				},
			
				'updateNodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						action : 'wiki'
					}
				},
			
				'nodeDrillUp' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						action : 'drillup'
					}
				},
				
				'nodeDrillDown' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						action : 'drilldown'
					}
				},
				
				'getSubnodes' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				},
				
				'getSubnodesFromTree' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'subnodes'
					}
				},
				
				'addSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				},
				
				'getSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				},
				
				'updateSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes'
					}
				},
				
				'deleteSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'delete'
					}
				},
				
				'getSubnodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'wiki'
					}
				},
										
				'updateSubnodeWiki' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'wiki'
					}
				},
				
				'subnodeDrillUp' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'drillup'
					}
				},
				'subnodeDrillDown' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'drilldown'
					}
				},
				
				'renameSubnode' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'subnodes',
						action : 'rename'
					}
				},
				
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
				},
				
				'deleteLink' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'links',
						action : 'delete'
					}
				},

				'updateLink' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'links'
					}
				},
				
				'linkDrillDown' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'links',
						entity3 : 'nodes',
						action : 'drilldown'
					}
				},
				
				'linkDrillUp' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'links',
						entity3 : 'nodes',
						action : 'drillup'
					}
				},
				
				'getTemplates' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'templates'
					}
				},
				
				'addTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'templates'
					}
				},
			
				'getTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : false,
					params : {
						entity2 : 'templates'
					}
				},
				
				'updateTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'templates'
					}
				},

				'deleteTemplate' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'templates',
						action : 'delete'
					}
				},
				
				'getComments' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'comments'
					}
				},
				
				'addComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'comments'
					}
				},
				
				'getComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'GET',
					isArray : true,
					params : {
						entity2 : 'nodes',
						entity3 : 'comments'
					}
				},
				
				'updateComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'comments'
					}
				},
				
				'deleteComment' : {
					headers : CDAR.getCustomHeader(),
					method : 'POST',
					params : {
						entity2 : 'nodes',
						entity3 : 'comments',
						action : 'delete'
					}
				}
			});
		} ]);