app.controller("TemplatesController", [
		'$scope',
		'$routeParams',
		'TreeService',
		'AuthenticationService',
		'UserService',
		'$route', '$modal',
		function($scope, $routeParams, TreeService, AuthenticationService, UserService, $route, $modal) {
			$scope.knowledgetree = "";
			$scope.templates = "";
			$scope.selectedTemplate = "";
			
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
				noty({
					type : 'alert',
					text : 'cannot get tree',
					timeout : 1500
				});
			});

			var reloadTemplates = function() {
				TreeService.getTemplates({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId
				}, function(response) {
					$scope.templates = response;
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot reload templates',
						timeout : 1500
					});
				});
			};
			
			$scope.setDefaultTemplate = function(id) {
				var template = $.grep($scope.templates, function(t) { return t.id === id; })[0];
				template.isDefault = !template.isDefault;
				TreeService.updateTemplate({entity1 : 'ktrees', id1 : $routeParams.treeId, id2 : template.id}, template, function(response) {
					reloadTemplates();
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot set default template',
						timeout : 1500
					});
				});
			};
			
			$scope.deleteTemplate = function(templateId) {
				$modal.open({ 
		            templateUrl: 'templates/confirmation.html',
		            backdrop: 'static',
		            keyboard: false,
		            resolve: {
		                data: function() { 
		                    return {
		                        title: 'Delete Template',
		                        message: 'Do you really want to delete this Template?' 
		                    };
		                }
			            },
			            controller: 'ConfirmationController' 
			    }).result.then(function(result) {
			    	TreeService.deleteTemplate({entity1 : 'ktrees', id1 : $routeParams.treeId}, { id : templateId }, function(response) {
						reloadTemplates();
						if ($scope.selectedTemplate.id==templateId) {
							$scope.selectedTemplate.id = 0;
						}
						noty({type: 'success', text : 'template deleted successfully', timeout: 1500});
					}, function(error) {
						noty({
							type : 'alert',
							text : 'cannot delete template',
							timeout : 1500
						});
					});
			    });
			};
			
			$scope.editTemplateTitle = function(data, id) {
				if (data.length>45) {
					noty({
						type : 'alert',
						text : 'Please enter a text with less than 45 Characters',
						timeout : 3000
					});
				} else {
					var template = $.grep($scope.templates, function(t) { return t.id === id; })[0];
					template.title = data;
					
					TreeService.updateTemplate({entity1 : 'ktrees', id1 : $routeParams.treeId, id2 : template.id}, template, function(response) {
						reloadTemplates();
						noty({type: 'success', text : 'template renamed successfully', timeout: 1500});
					}, function(error) {
						noty({
							type : 'alert',
							text : 'cannot edit template title',
							timeout : 1500
						});
					});
				}
			};
			
			reloadTemplates();

			$scope.addNewTemplate = function(decisionMade) {
				var templateName;
				if (decisionMade) {
					templateName = $scope.newConsumerTemplateName;
				} else {
					templateName = $scope.newProducerTemplateName;
				}
				
				if (templateName.length>45) {
					noty({
						type : 'alert',
						text : 'Please enter a text with less than 45 Characters',
						timeout : 3000
					});
				} else {
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
						noty({
							type : 'alert',
							text : 'cannot add new template',
							timeout : 1500
						});
					});
				}
			};
			
			$scope.changeTemplate = function(id) {
				$scope.selectedTemplate.id = id;
				setLoading();
				TreeService.getTemplate({
					entity1 : 'ktrees',
					id1 : $routeParams.treeId,
					id2 : id
				}, function(response) {
					changeTemplateFields(response);
				}, function(error) {
					noty({
						type : 'alert',
						text : 'cannot change template',
						timeout : 1500
					});
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
					}, function(response) {
						noty({
							type : 'alert',
							text : 'cannot save template',
							timeout : 1500
						});
					});
				}
			};
			
			$scope.tabs = [{ title:"READ" },{ title:"WRITE" }];
			
			var switchToRead = function() {
				$scope.tabs[0].active = true;
				$scope.tabs[1].active = true;
			};
		} ]);