var scope = angular.element(document.getElementById("treeControllerDiv"));

var mouseOverFlag = false;
var eleme = $("div.myClass");
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

function allowDrop(ev) {
	ev.preventDefault();
}

function drag(ev) {
	ev.dataTransfer.setData("Text", ev.target.id);
}

function drop(ev) {
	if (ev.target.id.indexOf("node") == -1) {
		scope.addNode(ev);
	}
}

$(document).bind(
		'dnd_move.vakata',
		function(e, data) {

			if (isMouseOverContainer()) {
				data.helper.find('.jstree-icon:eq(0)').removeClass('jstree-er')
						.addClass('jstree-ok');
				return;
			}
		});