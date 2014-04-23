angular.module('app.directives.navigation', []).directive('cdarNavigation', function() {
	return {
		restrict: 'E',
		templateUrl: 'directives/navigation.html',
		controller: function() {
			console.log('cdar navigation');
		}
	};
});
