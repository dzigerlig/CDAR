app.controller("ProjectTreeImportExportController", [ '$scope', '$routeParams', 'TreeService', 'AuthenticationService', 'UserService', '$route',
		function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route) {
			$scope.UserService = UserService;
			
			$scope.projecttree = "";
			$scope.xmlTrees = "";
			
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
					TreeService.getSimpleExports({
					entity1 : 'ptrees',
					id1 : $routeParams.treeId
				}, function(response) {
					$scope.xmlTrees = response;
					alert(JSON.stringify($scope.xmlTrees));
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot get xml trees',
						timeout : 1500
					});
				});
			}
			
			$scope.deleteXmlTree = function(xmlTreeId) {
				TreeService.deleteSimpleExport({
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
				})
			};
			
			$scope.addNewSimpleXmlTree = function() {
				TreeService.addSimpleExport({
					entity1: 'ptrees',
					id1 : $routeParams.treeId
				}, {id : 1}, function(response) {
					reloadXmlTrees();
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot add tree',
						timeout : 1500
					});
				});			
			};
			
			reloadXmlTrees();
			
} ]);