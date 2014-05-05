app.controller("TemplatesController", [
		'$scope',
		'$routeParams',
		'TreeService',
		'AuthenticationService',
		'UserService',
		'$route',
		function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route) {
			$scope.knowledgetree = "";
			$scope.templates = "";
			$scope.selectedTemplate = "";
			$scope.selectedTemplateId = 0;
			
			$scope.templateHtml = '';
			$scope.templatePlain = '';
			
			$scope.updateTemplateId = "";
			$scope.updateTemplateTitle = "";
			
			$scope.UserService = UserService;

			TreeService.getTree({
				entity1 : 'ktrees',
				id1 : $routeParams.treeId
			}, function(response) {
				$scope.knowledgetree = response;
			}, function(error) {
				//todo error handling
			});

			var reloadTemplates = function() {
				TreeService.getTemplates({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId
				}, function(response) {
					$scope.templates = response;
				}, function(error) {
					//todo error handling
				});
			};
			
			$scope.setDefaultTemplate = function(id) {
				var template = $.grep($scope.templates, function(t) { return t.id === id; })[0];
				
				template.isDefault = !template.isDefault;
				
				alert(JSON.stringify(template));
				
				TreeService.updateTemplate({entity1 : 'ktrees', id1 : $routeParams.treeId, id2 : template.id}, template, function(response) {
					reloadTemplates();
				});
			};
			
			$scope.deleteTemplate = function(templateId) {
				TreeService.deleteTemplate({entity1 : 'ktrees', id1 : $routeParams.treeId}, { id : templateId }, function(response) {
					reloadTemplates();
					if ($scope.selectedTemplateId==templateId) {
						$scope.selectedTemplateId = 0;
					}
					noty({type: 'success', text : 'template deleted successfully', timeout: 1500});
				}, function(error) {
					//todo error handling
				});
			};
			
			$scope.editTemplateTitle = function(data, id) {
				var template = $.grep($scope.templates, function(t) { return t.id === id; })[0];
				template.title = data;
				
				TreeService.updateTemplate({entity1 : 'ktrees', id1 : $routeParams.treeId, id2 : template.id}, template, function(response) {
					reloadTemplates();
					noty({type: 'success', text : 'template renamed successfully', timeout: 1500});
				}, function(error) {
					//todo error handling
				});
			};
			
			reloadTemplates();

			$scope.addNewTemplate = function(decisionMade) {
				var templateName;
				if (decisionMade) {
					templateName = $scope.newConsumerTemplateName;
				} else {
					templateName = $scope.newProducerTemplateName;
				}
				
				TreeService.addTemplate({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId
				}, {
					treeId : $routeParams.treeId,
					title : templateName,
					decisionMade : decisionMade
				}, function(response) {
					reloadTemplates();
					if(decisionMade) {
						$scope.newConsumerTemplateName = '';
					} else {
						$scope.newProducerTemplateName = '';
					}
					noty({type: 'success', text : 'template "'+ templateName + '" added successfully', timeout: 1500});
				}, function(error) {
					//todo error handling
				});
			};
			
			$scope.changeTemplate = function(id) {
				$scope.selectedTemplateId = id;
				setLoading();
				TreeService.getTemplate({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId,
					id2 : id
				}, function(response) {
					changeTemplateFields(response);
				}, function(error) {
					//todo error handling
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
				if ($scope.selectedTemplate.id !== 0) {
					$scope.templatePlain = $("#templateArea").val();
					$scope.selectedTemplate.templatetext = $scope.templatePlain;
					switchToRead();
					setLoading();
					TreeService.updateTemplate({
						entity1 : 'ktrees',
						id1 : $routeParams.treeId,
						id2 : $scope.selectedTemplate.id
					}, $scope.selectedTemplate, function(response) {
						changeTemplateFields(response);
						noty({type: 'success', text : 'template text edited successfully', timeout: 1500});
					});
				}
			};
			
			$scope.tabs = [{ title:"READ" },{ title:"WRITE" }];
			
			var switchToRead = function() {
				$scope.tabs[0].active = true;
				$scope.tabs[1].active = true;
			};
		} ]);