var scope = angular.element(document.getElementById("treeControllerDiv"));

var mouseOverFlag = false;
var eleme = $("#tree-container");
eleme.mouseover(function() {
	mouseOverFlag = true;
}).mouseout(function() {
	mouseOverFlag = false;
});

function isMouseOverContainer() {
	return mouseOverFlag;
}

$(document).bind('dnd_stop.vakata', function(e, data) {
	if (isMouseOverContainer()) {
		scope.addNode(data.event,data);
	}
});

$(document).bind(
		'dnd_move.vakata',
		function(e, data) {		
			if (isMouseOverContainer()) {
				data.helper.find('.jstree-icon:eq(0)').removeClass('jstree-er')
						.addClass('jstree-ok');
				return;
			}
		});