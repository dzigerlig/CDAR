app.controller("ProjectTreeImportExportController", [ '$scope', '$routeParams', 'TreeService', 'AuthenticationService', 'UserService', '$route',
		function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route) {
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
			
			$scope.setXmlTree = function(xmlTreeId) {
				TreeService.setExport({
					entity1 : 'ptrees',
					id1 : $routeParams.treeId,
					id2 : xmlTreeId
				}, function(response) {
					alert("ok!");
				}, function(error) {
					alert("not ok!");
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
					$scope.newFullTreeName = "";
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot add tree',
						timeout : 1500
					});
				});	
			};
			
			$scope.saveExport = function(xmltree) {
				alert(JSON.stringify(xmltree));
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