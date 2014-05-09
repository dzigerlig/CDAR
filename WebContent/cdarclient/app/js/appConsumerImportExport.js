app.controller("ProjectTreeImportExportController", [ '$scope', '$routeParams', 'TreeService', 'AuthenticationService', 'UserService', '$route', '$location',
		function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route, $location) {
			$scope.UserService = UserService;
			
			$scope.projecttree = "";
			$scope.xmlTrees = "";
			
			$scope.newSimpleTreeName = "";
			$scope.newFullTreeName = "";
			
			TreeService.getTree({
				entity1 : 'ptrees',
				id1 : $routeParams.treeId
			}, function(response) {
				$scope.projecttree = response;
			}, function(error) {
				noty({
					type : 'alert',
					text : 'cannot get projecttree',
					timeout : 1500
				});
			});
			
			var reloadXmlTrees = function() {
					TreeService.getExports({
					entity1 : 'ptrees',
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
					entity1 : 'ptrees',
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
					entity1 : 'ptrees',
					id1 : $routeParams.treeId,
					id2 : xmlTreeId,
					cleantree : cleanBool
				}, function(response) {
					noty({
						type : 'success',
						text : "Import successfully",
						timeout : 1500
					});
					$location.path('/projecttree/' + $routeParams.treeId);
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
					entity1: 'ptrees',
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
					entity1: 'ptrees',
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
			    
			    if (data.indexOf("projectTreeFull") > -1) {
			    	$scope.addNewFullXmlTree("imported xml", data);
				};
				
				if (data.indexOf("projectTreeSimple") > -1) {
					$scope.addNewSimpleXmlTree("imported xml", data);
				};
			  }
			  r.readAsBinaryString(f);
			}
			
			$scope.getXmlFileString = function(xmlId) {
				return "../webapi/ptrees/" + $routeParams.treeId + " /exports/" + xmlId + "/filexml?uid=" + UserService.getUserId() + "&accesstoken=" + UserService.getAccesstoken();
			};
			
			reloadXmlTrees();
} ]);