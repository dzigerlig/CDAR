var lastConnectionID = -1;
var scope;
var isInizialized = false;
var NODE = 'node';
var LINK = 'link';
var SUBNODE = 'subnode';

function initializeJsPlumb() {
	scope = angular.element(document.getElementById("wrapper")).scope();
	setDefaultSettings();
	makePopupEvents();
	bindDetachConnectorEvent();
	bindConnection();
	$('html').click(function() {
		$('[id^=popup-box-]').hide();
	});
};

function addHTMLNode(response, e) {
	var newState = $('<div>').attr('id', NODE + response.id).addClass('w').data(SUBNODE, {
		subnode : null
	});
	var title = $('<div>').addClass('title').text(response.title);
	var connect = $('<div>').addClass('ep');
	var option = $('<div>').addClass('option').hide();
	var list = $('<ul>').addClass('optionList');
	option.append(list);

	newState.css({
		// calculate coordinates of the cursor in element
		'top' : e.pageY - $('#jsplumb-container').offset().top,
		'left' : e.pageX - $('#jsplumb-container').offset().left
	});
	bindClickEndpoint(newState);
	makeNodesDraggable(newState);
	removeNodeEvent(newState);
	showNodeWikiEvent(newState);
	makeTarget(newState);
	makeSource(connect, newState);
	appendElements(title, connect, newState, option);
	scope.getSubnodesOfNode(response.id);
}

// imported Nodes
function drawExistingNodes(data, resSubnodes) {
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
			var newState = $('<div>').attr('id', NODE + this.id).addClass('w')
					.data(SUBNODE, {
						subnode : map[this.id]
					});
			var option = $('<div>').addClass('option').hide();
			var title = $('<div>').addClass('title').text(this.title);
			var connect = $('<div>').addClass('ep');
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

			bindClickEndpoint(newState);
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
		lineWidth : 2,
		strokeStyle : '#1e8151',

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
			strokeStyle : "#5c96bc",
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
	if (subnode === undefined) {
		jsPlumb.connect({
			source : stateSource,
			target : stateTarget,
			parameters : {
				"id" : id
			},
			anchors : 'Perimeter',
			overlays : [ [ "Arrow", {
				location : 1,
				id : "arrow",
				length : 14,
				foldback : 0.8
			} ] ],

			connector : [ "StateMachine", {
				curviness : 20
			} ],
			endpoint : [ "Dot", {
				radius : 2
			} ],
			endpointStyle : {
				fillStyle : "blue",
				outlineColor : "black",
				outlineWidth : 1
			}
		});
	} else {
		jsPlumb.connect({
			source : stateSource,
			target : stateTarget,
			parameters : {
				"id" : id
			},
			anchors : 'Perimeter',
			overlays : [ [ "Arrow", {
				location : 1,
				id : "arrow",
				length : 14,
				foldback : 0.8
			} ], [ "Label", {
				label : subnode.title,
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
				fillStyle : "blue",
				outlineColor : "black",
				outlineWidth : 1
			}

		});
	}
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

function bindClickEndpoint(element) {
	element.bind("click", function(endpoint) {
		$('#' + endpoint.currentTarget.id + ' .option').toggle();
		jsPlumb.repaintEverything();
	});
};


function removeNodeEvent(newState) {
	newState.dblclick(function(e) {
		detachNode(newState[0].id.replace(NODE, ""));
	});
};

function detachNode(id){
	var newState = $('#'+NODE+id);
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

function showNodeWikiEvent(newState) {
	newState.click(function(e) {
		scope.changeNode(newState[0].id.replace(NODE, ""),
				newState[0].textContent);
	});
};

function makeNodeHierarchy(data, resSubNodes) {
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
	jsPlumb
			.bind(
					"connection",
					function(info) {
						if (!isInizialized) {
							setLinkId(info.connection, info.connection
									.getParameter("id"));
						} else {
							$('#radio-form').empty();
							$
									.each(
											info.connection.source
													.data(SUBNODE).subnode,
											function(object) {
												$('#radio-form')
														.append(
																"<input type=\"radio\" id=\""
																		+ SUBNODE
																		+ this.id
																		+ "\" name=\"option\" class=\"radio_item\" value=\""
																		+ this.title
																		+ "\">"
																		+ this.title
																		+ "<br>");
											});

							scope.addLink(1, info.sourceId.replace(NODE, ""),
									info.targetId.replace(NODE, ""),
									info.connection);
							$('#popup-box-1').show();

						}
					});
};

function updateSubnodesOfNode(resSubnode, nodeId) {
	var options = $("#node" + nodeId).data("subnode");
	//if (options !== null) {
		var optionList = $('#' + NODE + nodeId + ' .optionList');
		options.subnode = resSubnode;
		optionList.empty();
		jQuery.each(resSubnode, function(object) {
			optionList.append($('<li>').text(this.title));
		});
	//}
};

