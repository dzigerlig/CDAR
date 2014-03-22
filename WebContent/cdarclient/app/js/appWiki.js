
app.controller("WikiController", [ '$scope', 'WikiService',
		function($scope, WikiService) {
			$scope.wiki;

			WikiService.query(function(response) {
				$scope.wiki = response;
			});
		} ]);

app.controller("WikiEditController", [ '$scope', '$location', 'WikiService',
		function($scope, $location, WikiService) {
			$scope.wiki;
			WikiService.query(function(response) {
				$scope.wiki = response;
			});

			$scope.saveWikiEntry = function() {
				WikiService.postEntry($scope.wiki.text, function(response) {
					if (response[0] == 1) {
						alert("wiki entry changed");
						$location.path('/wiki');
					} else {
						alert("wiki entry NOT changed");
					}
				});
			};
		} ]);