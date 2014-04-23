var lastConnectionID = -1;
var scope;
var isInizialized = false;
var NODE = 'node';
var LINK = 'link';
var SUBNODE = 'subnode';
var selectedElement = null;

function initializeJsPlumb() {
	scope = angular.element(document.getElementById("wrapper")).scope();
	setDefaultSettings();
	register();
	makePopupEvents();
	bindDetachConnectorEvent();
	bindConnection();
	$('html').click(function() {
		$('[id^=popup-box-]').hide();
	});
};

function addHTMLNode(response, e) {
	var newState = $('<div>').attr('id', NODE + response.id).addClass('w')
			.data(SUBNODE, {
				subnode : null
			});
	var title = $('<div>').addClass('title').text(response.title);
	var connect = $('<div>').addClass('ep draglink');
	var option = $('<div>').addClass('option').hide();
	var list = $('<ul>').addClass('optionList');
	var downtree = $('<div>').addClass('downtree');
	var uptree = $('<div>').addClass('uptree');
	newState.append(downtree);
	newState.append(uptree);
	option.append(list);

	newState.css({
		// calculate coordinates of the cursor in element
		'top' : e.pageY - $('#jsplumb-container').offset().top,
		'left' : e.pageX - $('#jsplumb-container').offset().left
	});
	zoomDownEvent(downtree, newState);
	zoomUpEvent(uptree, newState);
	makeNodesDraggable(newState);
	removeNodeEvent(newState);
	showNodeWikiEvent(newState);
	makeTarget(newState);
	makeSource(connect, newState);
	appendElements(title, connect, newState, option);
	scope.getSubnodesOfNode(response.id);
}

function buildContent() {
	jsPlumb.detachEveryConnection();
	jsPlumb.deleteEveryEndpoint();

	$("#jsplumb-container").empty();
	var container = $("#jsplumb-container");
	var popup = $('<div>').addClass('popup-box').attr('id', 'popup-box-1');
	container.append(popup);
	var top = $('<div>').addClass('top');
	popup.append(top);
	var text = $('<h4>').html('Choose decision');
	top.append(text);
	var bottom = $('<div>').addClass('bottom');
	popup.append(bottom);
	var form = $('<form>').attr('id', 'radio-form');
	bottom.append(form);
}

// imported Nodes
function drawExistingNodes(data, resSubnodes) {
	buildContent();
	makePopupEvents();
	isInizialized = false;
	var map = {};
	jQuery.each(resSubnodes, function(object) {
		if (map[this.knid] === undefined) {
			var arr = [ this ];
			map[this.knid] = arr;
		} else {
			var arr = map[this.knid];
			arr.push(this);
		}
	});

	jQuery.each(data, function(object) {
		if (this.dynamicTreeFlag) {
			if (map[this.id] !== undefined) {
				map[this.id].sort(function(a, b) {
					return parseInt(a.position) - parseInt(b.position);
				});
			}
			var newState = $('<div>').attr('id', NODE + this.id).addClass('w')
					.data(SUBNODE, {
						subnode : map[this.id]
					});
			var option = $('<div>').addClass('option').hide();
			var title = $('<div>').addClass('title').text(this.title);
			var connect = $('<div>').addClass('ep draglink');
			var downtree = $('<div>').addClass('downtree');
			var uptree = $('<div>').addClass('uptree');
			newState.append(downtree);
			newState.append(uptree);
			newState.css({
				'top' : 100,
				'left' : 100
			});

			if (map[this.id] !== undefined) {
				var list = $('<ul>').addClass('optionList');
				jQuery.each(map[this.id], function(object) {
					list.append($('<li>').text(this.title));
				});
				option.append(list);
			}
			zoomDownEvent(downtree, newState);
			zoomUpEvent(uptree, newState);
			makeNodesDraggable(newState);

			removeNodeEvent(newState);
			showNodeWikiEvent(newState);

			makeTarget(newState);
			makeSource(connect, newState);

			appendElements(title, connect, newState, option);
		}

	});
};

// Functions
function setDefaultSettings() {
	jsPlumb.Defaults.PaintStyle = {
		connectorStyle : {
			strokeStyle : "#5c96bc",
			lineWidth : 2,
			outlineColor : "transparent",
			outlineWidth : 4
		},

		connectorOverlays : [ [ "Arrow", {
			location : 1,
			id : "arrow",
			length : 14,
			foldback : 0.8
		} ], [ "Label", {
			id : "label",
			cssClass : "aLabel"
		} ] ]
	};
};

function makePopupEvents() {
	$('#radio-form')
			.on(
					'change',
					'.radio_item',
					function(element) {
						var connections = jsPlumb.getAllConnections().jsPlumb_DefaultScope;
						jQuery.each(connections, function(object) {
							if (this.id === lastConnectionID) {
								this.getOverlay("label").setLabel(
										$(element.currentTarget).val());
								scope.updateLink(this.id.replace(LINK, ""), $(
										element.currentTarget).attr("id")
										.replace(SUBNODE, ""));
								$('[id^=popup-box-]').hide();

							}
						});
					});
};

function makeSource(connect, newState) {
	jsPlumb.makeSource(connect, {
		parent : newState,
		anchor : 'Continuous',
		connectorStyle : {
			strokeStyle : "#5C96BC",
			lineWidth : 2,
			outlineColor : "transparent",
			outlineWidth : 4
		},

		connector : [ "StateMachine", {
			curviness : 20
		} ],
		endpoint : [ "Dot", {
			radius : 2
		} ],
		connectorOverlays : [ [ "Arrow", {
			location : 1,
			id : "arrow",
			length : 14,
			foldback : 0.8
		} ], [ "Label", {
			id : "label",
			cssClass : "aLabel"
		} ] ]
	});
};

function makeTarget(newState) {
	jsPlumb.makeTarget(newState, {
		anchor : 'Continuous',
		endpoint : "Blank",
		dropOptions : {
			hoverClass : "dragHover"
		}
	});
};

function connectNodes(stateSource, stateTarget, id, subnode) {
	var label = "";
	if (subnode !== undefined) {
		label = subnode.title;
	}

	jsPlumb.connect({
		source : stateSource,
		target : stateTarget,
		parameters : {
			"id" : id
		},
		anchors : 'Perimeter',
		type : "change",
		data : {
			color : "#5C96BC",
			label : label
		}
	});
};

function appendElements(title, connect, newState, option) {
	newState.append(title);
	newState.append(option);
	newState.append(connect);
	$('#jsplumb-container').append(newState);
};

function makeNodesDraggable(newState) {
	jsPlumb.draggable(newState, {
		containment : 'parent'
	});
};

function bindDetachConnectorEvent() {
	jsPlumb.bind("dblclick", function(c) {
		jsPlumb.detach(c);
		scope.deleteLink(c.id.replace(LINK, ""));
	});
};

function removeNodeEvent(newState) {
	newState.dblclick(function(e) {
		$('#' + newState[0].id + ' .option').toggle();
		jsPlumb.repaintEverything();

		// detachNode(newState[0].id.replace(NODE, ""));
	});
};

function detachNode(id) {
	var newState = $('#' + NODE + id);
	if (newState.size() !== 0) {
		var allTargetConnection = jsPlumb.getConnections({
			target : newState
		});
		var allSourceConnection = jsPlumb.getConnections({
			source : newState
		});
		jQuery.each(allTargetConnection, function() {
			scope.deleteLink(this.id.replace(LINK, ""));
		});

		jQuery.each(allSourceConnection, function() {
			scope.deleteLink(this.id.replace(LINK, ""));
		});

		scope.undropNode(newState[0].id.replace(NODE, ""));
		jsPlumb.detachAllConnections($(newState));
		$(newState).remove();
	}
}

function showNodeWikiEvent(newState) {
	newState.click(function(e) {
		scope.changeNode(newState[0].id.replace(NODE, ""), $(
				'#' + newState[0].id + ' .title').text());

		resetSelectedDesign();
		$(newState).css('background-color', '#beebff');
		selectedElement = newState[0].id;

	});
};

function resetSelectedDesign(){
	if (selectedElement !== null) {
		if (selectedElement.indexOf(NODE) > -1) {
			if ($("#" + selectedElement).size() !== 0) {
				 $('#' + selectedElement).css('background-color', 'white');
			}
		} else {
			var connections = jsPlumb.getConnections();
			jQuery.each(connections, function(object) {
				if (selectedElement === this.id) {
					var label = this.getOverlay("label").getLabel();

					this.setType("change", {
						color : "#5C96BC",
						label : label
					});
				}
			});
		}
	}
}


function zoomUpEvent(uptree, newState) {
	uptree.click(function(e) {
		e.stopPropagation();
		scope.zoomUpNode(newState[0].id.replace(NODE, ""));
	});
};
function zoomDownEvent(downtree, newState) {
	downtree.click(function(e) {
		e.stopPropagation();
		scope.zoomDownNode(newState[0].id.replace(NODE, ""));
	});
};

function makeNodeHierarchy(data, resSubNodes) {
	$("#dot-src").empty();
	var map = {};
	jQuery.each(resSubNodes, function(object) {
		map[this.id] = this;
	});

	var direction = "digraph chargraph {node[shape=box, margin=0, width=2, height=1];";
	jQuery.each(data,
			function(object) {
				connectNodes(NODE + this.sourceId, NODE + this.targetId,
						this.id, map[this.ksnid]);
				direction += NODE + this.sourceId + " -> " + NODE
						+ this.targetId + ";";
			});
	direction += "}";
	$('#dot-src').val(direction);
	isInizialized = true;
};

function setLinkId(connection, id) {
	connection.id = LINK + id;
	lastConnectionID = LINK + id;
};

function bindConnection() {
	jsPlumb.bind("connection", function(info) {
		if (!isInizialized) {
			setLinkId(info.connection, info.connection.getParameter("id"));
			bindClickConnection(info);
		} else {
			scope.addLink(scope.treeId, info.sourceId.replace(NODE, ""),
					info.targetId.replace(NODE, ""), info.connection);
			showSubnodePopup(info);
			bindClickConnection(info);
		}

	});
};

function bindClickConnection(info) {
	info.connection.bind("click", function(c) {
		resetSelectedDesign();

		var label = info.connection.getOverlay("label").getLabel();

		info.connection.setType("change", {
			color : "#BEEBFF",
			label : label
		});
		selectedElement = info.connection.id;

		// TODO Show popup on connection click. conflict with html click
		// showSubnodePopup(info);
	});
}

function showSubnodePopup(info) {
	$('#popup-box-1').show();
	$('#radio-form').empty();
	$.each(info.connection.source.data(SUBNODE).subnode, function(object) {
		$('#radio-form').append(
				"<input type=\"radio\" id=\"" + SUBNODE + this.id
						+ "\" name=\"option\" class=\"radio_item\" value=\""
						+ this.title + "\">" + this.title + "<br>");
	});
	$('#popup-box-1').show();
}

function renameNode(id, newTitle) {
	var title = $('#' + NODE + id + ' .title');
	if (title.size() !== 0) {
		title[0].innerHTML = newTitle;
	}
}

function updateSubnodesOfNode(newSubnode, nodeId, changes) {
	if ($("#" + NODE + nodeId).size() !== 0) {
		var options = $("#" + NODE + nodeId).data("subnode");
		if (changes !== null && changes.changedEntities !== null) {
			var oldSubnodes = options.subnode.slice(0);
		}
		var optionList = $('#' + NODE + nodeId + ' .option');
		options.subnode = newSubnode;
		optionList.empty();
		newSubnode.sort(function(a, b) {
			return parseInt(a.position) - parseInt(b.position);
		});

		jQuery.each(newSubnode, function(object) {
			optionList.append($('<li>').text(this.title));
		});

		if (changes !== null && changes.changedEntities !== null) {
			var allSourceConnection = jsPlumb.getConnections({
				source : $("#" + NODE + nodeId)
			});

			var map = {};
			jQuery.each(changes.changedEntities, function(object) {
				map[this.id] = true;
			});
			// get delte from new to old
			var linkTitle = [];
			jQuery.each(newSubnode, function(object) {
				linkTitle.push(this.title);
			});
			jQuery.each(oldSubnodes, function(object) {
				if ($.inArray(this.title, linkTitle) !== -1) {
					linkTitle = jQuery.removeFromArray(this.title, linkTitle);
				}
			});
			//
			jQuery.each(allSourceConnection, function(object) {
				if (map[this.id.replace(LINK, "")]) {
					if (changes.operation === 'delete') {
						jsPlumb.detach(this);
					} else if (changes.operation === 'update') {
						this.getOverlay("label").setLabel(linkTitle[0]);
					}
				}
			});
		}
	}
};

function registerLinkTemplate() {
	console.log('registered');
	jsPlumb.registerConnectionType("change", {
		paintStyle : {
			strokeStyle : "${color}",
			lineWidth : 2
		},
		overlays : [ [ "Arrow", {
			location : 1,
			id : "arrow",
			length : 14,
			foldback : 0.8
		} ], [ "Label", {
			label : "${label}",
			id : "label",
			cssClass : "aLabel"
		} ] ],

		connector : [ "StateMachine", {
			curviness : 20
		} ],
		endpoint : [ "Dot", {
			radius : 2
		} ],
		endpointStyle : {
			fillStyle : "${color}",
			outlineColor : "black",
			outlineWidth : 1
		}
	});

}

jQuery.removeFromArray = function(value, arr) {
	return jQuery.grep(arr, function(elem, index) {
		return elem !== value;
	});
};
