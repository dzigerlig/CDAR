app.controller("ImportExportController", [ '$scope', '$routeParams', 'TreeService', 'AuthenticationService', 'UserService', '$route', '$location',
		function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route, $location) {
			$scope.UserService = UserService;
			
			$scope.tree = "";
			$scope.xmlTrees = "";
			
			$scope.newSimpleTreeName = "";
			$scope.newFullTreeName = "";
			var userRole = "";
			
			if ($scope.UserService.getIsProducer()==='true') {
				userRole = "ktrees";
			} else {
				userRole = "ptrees";
			}
			
			TreeService.getTree({
				entity1 : userRole,
				id1 : $routeParams.treeId
			}, function(response) {
				$scope.tree = response;
			}, function(error) {
				noty({
					type : 'alert',
					text : 'cannot get projecttree',
					timeout : 1500
				});
			});
			
			var reloadXmlTrees = function() {
					TreeService.getExports({
					entity1 : userRole,
					id1 : $routeParams.treeId
				}, function(response) {
					$scope.xmlTrees = response;
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot get xml trees',
						timeout : 1500
					});
				});
			};
			
			$scope.deleteXmlTree = function(xmlTreeId) {
				TreeService.deleteExport({
					entity1 : userRole,
					id1 : $routeParams.treeId,
				}, { id : xmlTreeId}, function(response) {
					reloadXmlTrees();
				}, function (error) {
					noty({
						type : 'alert',
						text : 'cannot delete tree',
						timeout : 1500
					});
				});
			};
			
			 $scope.saveXmlTreeTitle = function(data, treeId) {
				 var xmltree = $.grep($scope.xmlTrees, function(t) {
			    		return t.id === treeId;
			    	})[0];
			    
			    	xmltree.title = data;
				 
			    	TreeService.updateExport({
						entity1 : userRole,
						id1 : $routeParams.treeId,
						id2 : treeId
					}, xmltree, function(response) { }, function(error) {
						noty({
							type : 'alert',
							text : 'error while saving title',
							timeout : 1500
						});
					});
			    };
			
			$scope.importTitle = "";
			$scope.importId = "";
			
			$scope.showImportTreeModal = function(title, treeId) {
				$scope.importTitle = title;
				$scope.importId = treeId;
				$('#importTreeModal').modal().show();
			};
			
			$scope.setExport = function(cleanBool) { 
				setXmlTree($scope.importId, cleanBool);
				$('#importTreeModal').modal('hide');
			};
			
			var setXmlTree = function(xmlTreeId, cleanBool) {
				TreeService.setExport({
					entity1 : userRole,
					id1 : $routeParams.treeId,
					id2 : xmlTreeId,
					cleantree : cleanBool
				}, function(response) {
					noty({
						type : 'success',
						text : "Import successfully",
						timeout : 1500
					});
					if ($scope.UserService.getIsProducer()==='true') {
						$location.path('/knowledgetree/' + $routeParams.treeId);
					} else {
						$location.path('/projecttree/' + $routeParams.treeId);
					}
				}, function(error) {
					noty({
						type : 'alert',
						text : "Couldn't import :'" + $scope.importTitle + "'successfully",
						timeout : 1500
					});
				});
			};
			
			$scope.addNewSimpleXmlTree = function(treetitle, xml) {
				if (!treetitle) {
					treetitle = $scope.newSimpleTreeName;
				}
				TreeService.addExport({
					entity1: userRole,
					id1 : $routeParams.treeId
				}, {isFull : false, title : treetitle, xmlString : xml}, function(response) {
					reloadXmlTrees();
					noty({
						type : 'success',
						text : 'Export "' + treetitle + '" added successfully!',
						timeout : 1500
					});
					$scope.newSimpleTreeName = "";
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot add tree',
						timeout : 1500
					});
				});			
			};
			
			$scope.addNewFullXmlTree = function(treetitle, xml) {
				if (!treetitle) {
					treetitle = $scope.newFullTreeName;
				}
				TreeService.addExport({
					entity1: userRole,
					id1 : $routeParams.treeId
				}, {isFull : true, title : treetitle, xmlString : xml}, function(response) {
					reloadXmlTrees();
					noty({
						type : 'success',
						text : 'Export "' + treetitle + '" added successfully!',
						timeout : 1500
					});
					$scope.newFullTreeName = "";
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot add tree',
						timeout : 1500
					});
				});	
			};
			
			$scope.add = function() {
			  var f = document.getElementById('file').files[0], r = new FileReader();
			  r.onloadend = function(e) {
			    var data = e.target.result;
			    
			    
			    if ($scope.UserService.getIsProducer()==='true') {
				    if (data.indexOf("treeFull") > -1) {
				    	$scope.addNewFullXmlTree("imported xml", data);
				    }
				    
				    else if (data.indexOf("treeSimple") > -1) {
				    	$scope.addNewSimpleXmlTree("imported xml", data);
				    }
				    else {
				    	noty({
							type : 'alert',
							text : 'specified file does not match - maybe you are trying to add a consumer export to a producer tree?',
							timeout : 8000
						});
				    }
			    } if ($scope.UserService.getIsProducer()==='false') {
				    if (data.indexOf("projectTreeFull") > -1) {
				    	$scope.addNewFullXmlTree("imported xml", data);
				    }
				    else if (data.indexOf("projectTreeSimple") > -1) {
				    	$scope.addNewSimpleXmlTree("imported xml", data);
				    }
				    else {
				    	noty({
							type : 'alert',
							text : 'specified file does not match - maybe you are trying to add a producer export to a consumer tree?',
							timeout : 8000
						});
				    }
			    }
			  }
			  r.readAsBinaryString(f);
			}
			
			$scope.getXmlFileString = function(xmlId) {
				return "../webapi/" + userRole + "/" + $routeParams.treeId + " /exports/" + xmlId + "/filexml?uid=" + UserService.getUserId() + "&accesstoken=" + UserService.getAccesstoken();
			};
			
			reloadXmlTrees();
} ]);