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
			$scope.newTemplateName;
			$scope.selectedTemplate = 0;
			$scope.wikiHtmlText = "no wiki entry selected";
			$scope.wikiEntry;

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
			
			$scope.changeTemplate = function(id) {
				setLoading();

				$scope.selectedTemplate = id;

				WikiService.getWikiEntry({
					role : 'producer',
					entity : 'template',
					templateid : $scope.selectedTemplate
				}, function(response) {
					changeWikiFields(response);
				});
			};
			
			var changeWikiFields = function(response) {
				$scope.wikiEntry = response;
				$scope.wikiHtmlText = $scope.wikiEntry.wikiContentHtml;
				$("#wikiArea").val(
						$scope.wikiEntry.wikiContentPlain);
			};
			
			var setLoading = function() {
				$scope.wikiHtmlText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
			};
		} ]);