var DIRECTORY = 'directory';
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
	var id = data.data.nodes[0];
	var type =data.data.origin._model.data[id].type;	
	if (isMouseOverContainer()&&type!=='default') {
		id = id.replace(NODE, "");
		id = id.replace(DIRECTORY, "");
		scope.dropNode(data.event, id);
	}
});


$('#jstree').on("move_node.jstree", function(e, data) {
	var id = data.node.id;
	var parentId=data.parent.replace(DIRECTORY, "");
	id = id.replace(DIRECTORY, "");
	if (data.node.type !== 'default') {
		console.log(id);
		id = id.replace(NODE, "");
		scope.moveNode(id, parentId);
	} else {
		if(parentId==='#'){parentId=id;}
		scope.moveDictionary(id, parentId);
	}
});

$(document).bind(
		'dnd_move.vakata',
		function(e, data) {
			var nodeId = data.data.nodes[0];
			if (isMouseOverContainer()&&data.data.origin._model.data[nodeId].type!=='default') {
				data.helper.find('.jstree-icon:eq(0)').removeClass('jstree-er')
						.addClass('jstree-ok');
				return;
			}
		});