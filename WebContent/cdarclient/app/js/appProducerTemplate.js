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
			$scope.selectedTemplateId = 0;
			
			$scope.templateHtml = '';
			$scope.templatePlain = '';
			
			$scope.UserService = UserService;

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
			
			$scope.setDefaultTemplate = function(id) {
				TreeService.setDefaultTemplate({ktreeid : $routeParams.treeId}, id, function(response) {
					$scope.templates = response;
				});
			};
			
			$scope.deleteTemplate = function(id) {
				TreeService.deleteTemplate({ktreeid : $routeParams.treeId}, id, function(response) {
					reloadTemplates();
					if ($scope.selectedTemplateId==id) {
						$scope.selectedTemplateId = 0;
					};
				});
			};
			
			$scope.updateTemplateId;
			$scope.updateTemplateTitle;
			
			$scope.editTemplateTitle = function(id, title) {
				$scope.updateTemplateId = id;
				$scope.updateTemplateTitle = title;
				$('#templateModal').modal().show();
			};
			
			$scope.updateTemplateName = function() {
				TreeService.renameTemplate({ktreeid : $routeParams.treeId}, { id : $scope.updateTemplateId, title : $scope.updateTemplateTitle }, function(response) {
					reloadTemplates();
					$('#templateModal').modal('hide');
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
				$scope.selectedTemplateId = id;
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
				if ($scope.selectedTemplate.id != 0) {
					$scope.templatePlain = $("#templateArea").val();
					$scope.selectedTemplate.templatetext = $scope.templatePlain;
					switchToRead();
					setLoading();
					TreeService.editTemplate({
						entityid : $scope.selectedTemplate.id
					}, $scope.selectedTemplate, function(response) {
						changeTemplateFields(response);
					});
				}
			};
			
			$scope.tabs = [
			               { title:"READ" },
			               { title:"WRITE" }
			             ];
			
			var switchToRead = function() {
				$scope.tabs[0].active = true;
				$scope.tabs[1].active = true;
			};
		} ]);