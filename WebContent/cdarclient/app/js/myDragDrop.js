var DICTIONARY = 'dictionary';
var NODE = 'node';

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
		var id = data.data.nodes[0];
		id = id.replace(NODE, "");
		id = id.replace(DICTIONARY, "");
		scope.dropNode(data.event, id);

		// scope.addNode(data.event,data);
	}
});

$(document).bind(
		'dnd_move.vakata',
		function(e, data) {
			//data.node.type !== 'default'
			var nodeId = data.data.nodes[0];
			if (isMouseOverContainer()&&data.data.origin._model.data[nodeId].type!=='default') {
				data.helper.find('.jstree-icon:eq(0)').removeClass('jstree-er')
						.addClass('jstree-ok');
				return;
			}
		});