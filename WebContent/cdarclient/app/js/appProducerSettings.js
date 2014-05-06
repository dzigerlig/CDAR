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
			}, function(error) {
				noty({
					type : 'alert',
					text : 'cannot get tree',
					timeout : 1500
				});
			});
			
			var reloadXmlTrees = function() {
				TreeService.getXmlTreesSimple({ktreeid : $routeParams.treeId}, function(response) {
					$scope.treesXml = response;
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot get exported trees',
						timeout : 1500
					});
				});
			};
			
			$scope.addNewSimpleXmlTree = function(id) {
				TreeService.addXmlTreeSimple({ktreeid : $routeParams.treeId}, 
				function (response) {
					reloadXmlTrees();
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot add new exported tree',
						timeout : 1500
					});
				});
				
			};
			
			$scope.setXmlTree = function(id) {
				TreeService.setXmlTreeSimple({ktreeid : $routeParams.treeId}, id, function(response) {
					console.log('tree set');
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot set this export',
						timeout : 1500
					});
				});
			};
			
			$scope.deleteXmlTree = function(id) {
				TreeService.removeXmlTreeSimple({ktreeid : $routeParams.treeId }, id, function(response) {
					reloadXmlTrees();
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot delete exported tree',
						timeout : 1500
					});
				});
			};
			
			reloadXmlTrees();
		} ]);