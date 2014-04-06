app.controller("TemplatesController", [
		'$scope',
		'$routeParams',
		'TreeService',
		'AuthenticationService',
		'WikiService',
		'UserService',
		'$route',
		function($scope, $routeParams, TreeService, AuthenticationService,
				WikiService, UserService, $route) {
			$scope.knowledgetree;
			$scope.templates;
			$scope.template;
			$scope.newTemplateName;

			TreeService.getTree({
				ktreeid : $routeParams.treeId
			}, function(response) {
				$scope.knowledgetree = response;
			});

			var reloadTemplates = function() {
				TreeService.getTemplates({
					ktreeid : $routeParams.treeId
				}, function(response) {
					$scope.templates = response;
				});
			};
			
			$scope.deleteTemplate = function(id) {
				TreeService.deleteTemplate({ktreeid : $routeParams.treeId}, id, function(response) {
					console.log(response);
					reloadTemplates();
				});
			};

			reloadTemplates();

			$scope.addNewTemplate = function() {
				TreeService.addTemplate({
					ktreeid : $routeParams.treeId
				}, {
					treeid : $routeParams.treeId,
					title : $scope.newTemplateName
				}, function(response) {
					if (response.id != -1) {
						reloadTemplates();
						$scope.newTemplateName = '';
					} else {
						alert("exception");
					}
				});
			};
		} ]);