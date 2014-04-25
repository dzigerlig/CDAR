app.controller("TreeSettingsController", [
		'$scope',
		'$routeParams',
		'TreeService',
		'AuthenticationService',
		'UserService',
		'$route',
		function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route) {
			$scope.knowledgetree = "";
			$scope.treesXml = "";
			
			
			$scope.UserService = UserService;

			TreeService.getTree({
				ktreeid : $routeParams.treeId
			}, function(response) {
				$scope.knowledgetree = response;
			});
			
			var reloadXmlTrees = function() {
				TreeService.getXmlTreesSimple({ktreeid : $routeParams.treeId}, function(response) {
					$scope.treesXml = response;
				});
			};
			
			$scope.addNewSimpleXmlTree = function(id) {
				TreeService.addXmlTreeSimple({ktreeid : $routeParams.treeId}, 
				function (response) {
					if (response.id != -1) {
						reloadXmlTrees();
					}
				});
				
			};
			
			$scope.setXmlTree = function(id) {
				TreeService.setXmlTreeSimple({ktreeid : $routeParams.treeId}, id, function(response) {
					if (response.id != -1) {
						console.log('tree set');
					}
				});
			};
			
			$scope.deleteXmlTree = function(id) {
				TreeService.removeXmlTreeSimple({ktreeid : $routeParams.treeId }, id, function(response) {
					if (response.bool) {
						reloadXmlTrees();
					}
				});
			};
			
			reloadXmlTrees();

			
		} ]);