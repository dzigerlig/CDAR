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
			$scope.selectedTemplate;
			
			$scope.templateHtml = '';
			$scope.templatePlain = '';

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
				TreeService.getTemplate({
					ktreeid : $routeParams.treeId,
					entityid : id
				}, function(response) {
					changeTemplateFields(response);
				});
			};
			
			var changeTemplateFields = function(response) {
				$scope.selectedTemplate = response;
				$scope.templateHtml = $scope.selectedTemplate.templatetexthtml;
				$("#templateArea").val($scope.selectedTemplate.templatetext);
			};
			
			var setLoading = function() {
				$scope.wikiHtmlText = "<img degrees='angle' rotate id='image' src='app/img/ajax-loader.gif'/>";
			};
			
			$scope.saveTemplate = function() {
				$scope.readTab();
				if ($scope.selectedTemplate.id != 0) {
					$scope.templatePlain = $("#templateArea").val();
					$scope.selectedTemplate.templatetext = $scope.templatePlain;
					setLoading();
					TreeService.editTemplate({
						entityid : $scope.selectedTemplate.id
					}, $scope.selectedTemplate, function(response) {
						changeTemplateFields(response);
					});
				}
			};
		} ]);