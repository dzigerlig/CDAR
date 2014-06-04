app
		.factory(
				'TreeService',
				[
						'$resource',
						function($resource) {
							return $resource(
									'../webapi/:entity1/:id1/:entity2/:id2/:entity3/:id3/:action',
									{}, {
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
										"nodeDrillUp" : {
											headers : CDAR.getCustomHeader(),
											method : 'GET',
											isArray : true,
											params : {
												entity2 : 'nodes',
												action : 'drillup'
											}
										},
										"nodeDrillDown" : {
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
										"subnodeDrillUp" : {
											headers : CDAR.getCustomHeader(),
											method : 'GET',
											isArray : true,
											params : {
												entity2 : 'nodes',
												entity3 : 'subnodes',
												action : 'drillup'
											}
										},
										"subnodeDrillDown" : {
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
										"linkDrillDown" : {
											headers : CDAR.getCustomHeader(),
											method : 'GET',
											isArray : true,
											params : {
												entity2 : 'links',
												entity3 : 'nodes',
												action : 'drilldown'
											}
										},
										"linkDrillUp" : {
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

app.factory('AuthenticationService', [ '$log', '$resource', '$location',
		'UserService', function($log, $resource, $location, UserService) {
			return $resource('../webapi/users/:userid/:action', {}, {
				'addUser' : {
					method : 'POST'
				},
				'updateUser' : {
					method : 'POST'
				},
				'loginUser':{
					method: 'GET',
					params:{
						action: 'login'
					}
				},
				'logoutUser':function(){
					UserService.removeCookies();
					 $location.path('/login'); 
				}
				}				
			);
		} ]);


app.service('DescriptionService', [
                                   '$resource',
                                   function($resource) {
                                      var cdarDescriptions = {};
                                        //initialize local variables on startup of service
                                        var descriptionResource = $resource('../webapi/descriptions');
                                        descriptionResource.get().$promise.then(function(response) {
                                         cdarDescriptions.directory = response.directoryDescription;
                                         cdarDescriptions.node = response.nodeDescription;
                                         cdarDescriptions.subnode = response.subnodeDescription;
                                         cdarDescriptions.wikiurl = response.wikiUrl;
                                         cdarDescriptions.expandedLevel= response.expandedLevel;
                                     });
                                        
                                        cdarDescriptions.getDirectoryDescription = function() {
                                      return cdarDescriptions.directory;
                                     };
                                     cdarDescriptions.getNodeDescription = function() {
                                      return cdarDescriptions.node;
                                     };

                                     cdarDescriptions.getSubnodeDescription = function() {
                                      return cdarDescriptions.subnode;
                                     };

                                     cdarDescriptions.getWikiUrl = function() {
                                      return cdarDescriptions.wikiurl;
                                     };
                                     
                                     cdarDescriptions.getExpandedLevel = function() {
                         				return cdarDescriptions.expandedLevel;
                         			};
                                         
                                        return cdarDescriptions;
                                   } ]);



app.factory('UserService', [ '$location', '$cookieStore',
		function($location, $cookieStore) {
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
				getDrillHierarchy : function() {
					return $cookieStore.get('cdarDrillHierarchy');
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
				setDrillHierarchy : function(val) {
					$cookieStore.put('cdarDrillHierarchy', val);
				},
				setIsProducer : function(val) {
					$cookieStore.put('cdarProducer', val);
				},
				checkResponseUnauthorized : function(error) {
					if (error.status === 401) {
						$location.path('/login');
					}
				},
				removeCookies : function() {
					CDAR.setCustomHeader('none', 'no-token');
					$cookieStore.remove('cdarUsername');
					$cookieStore.remove('cdarAccesstoken');
					$cookieStore.remove('cdarId');
					$cookieStore.remove('cdarProducer');
				},
				redirectUrl : function() {
					if (this.getIsProducer() == 'true') {
						this.setIsProducer('false');
						$location.path('/homeconsumer');
					} else {
						this.setIsProducer('true');
						$location.path('/homeproducer');
					}
				}
			};
		} ]);
