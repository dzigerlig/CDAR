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
			
			$scope.addNewSimpleXmlTree = function() {
				TreeService.addExport({
					entity1: 'ptrees',
					id1 : $routeParams.treeId
				}, {isFull : false, title : $scope.newSimpleTreeName}, function(response) {
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
			
			$scope.addNewFullXmlTree = function() {
				TreeService.addExport({
					entity1: 'ptrees',
					id1 : $routeParams.treeId
				}, {isFull : true, title : $scope.newFullTreeName}, function(response) {
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
			  var f = document.getElementById('file').files[0],
			      r = new FileReader();
			  r.onloadend = function(e){
			    var data = e.target.result;
			    //send you binary data via $http or $resource or do anything else with it
			    alert(data);
			  }
			  r.readAsBinaryString(f);
			}
			
			$scope.getXmlFileString = function(xmlId) {
				//http://localhost:8080/CDAR/webapi/ptrees/4/exports/18/filexml?uid=2&accesstoken=16450de3afd4a11e7d16d3493b8a12e9d4355f72
				return "../webapi/ptrees/" + $routeParams.treeId + " /exports/" + xmlId + "/filexml?uid=" + UserService.getUserId() + "&accesstoken=" + UserService.getAccesstoken();
			};
			
			reloadXmlTrees();
} ]);