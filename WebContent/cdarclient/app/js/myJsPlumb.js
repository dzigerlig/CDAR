var lastConnectionID = -1;
var scope;
var isInizialized = false;
var NODE = 'node';
var LINK = 'link';

function initializeJsPlumb() {
	scope = angular.element(document.getElementById("wrapper")).scope();
	setDefaultSettings();
	makePopupEvents();
	bindDetachConnectorEvent();
	bindConnection();

	// not allowed
	$('#tree-container').dblclick(function(e) {
		// scope.addNode(e);
	});
};

function addHTMLNode(response, e) {
	var newState = $('<div>').attr('id', NODE + response.id).addClass('w');
	var title = $('<div>').addClass('title').text(response.title);
	var connect = $('<div>').addClass('ep');

	newState.css({
		// calculate coordinates of the cursor in element
		'top' : e.pageY - $('#tree-container').offset().top,
		'left' : e.pageX - $('#tree-container').offset().left
	});

	makeNodesDraggable(newState);
	removeNodeEvent(newState);
	makeTarget(newState);
	makeSource(connect, newState);
	appendElements(title, connect, newState, true);
}

// imported Nodes
function drawExistingNodes(data) {
	isInizialized = false;
	jQuery.each(data, function(object) {
		if (this.dynamicTreeFlag) {
			var newState = $('<div>').attr('id', NODE + this.id).addClass('w');
			var title = $('<div>').addClass('title').text(this.title);
			var connect = $('<div>').addClass('ep');
			newState.css({
				'top' : 100,
				'left' : 100
			});

			makeNodesDraggable(newState);

			removeNodeEvent(newState);
			makeTarget(newState);
			makeSource(connect, newState);

			appendElements(title, connect, newState);
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
			label : "Not Set",
			id : "label",
			cssClass : "aLabel"
		} ] ]
	};
};

function makePopupEvents() {
	$('[class*=popup-box]').click(function(e) {
		/* Stop the link working normally on click if it's linked to a popup */
		e.stopPropagation();
	});
	$('html').click(function() {
		$('#radio-form').empty();
		var scrollPos = $(window).scrollTop();
		/* Hide the popup and blackout when clicking outside the popup */
		$('[id^=popup-box-]').hide();
		$("html,body").css("overflow", "auto");
		$('html').scrollTop(scrollPos);
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
		hoverPaintStyle : {
			strokeStyle : "#1e8151",
			lineWidth : 2
		},
		connectorOverlays : [ [ "Arrow", {
			location : 1,
			id : "arrow",
			length : 14,
			foldback : 0.8
		} ], [ "Label", {
			label : "Not Set Yet",
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

function connectNodes(stateSource, stateTarget, id) {
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
		} ]

	});
};

function appendElements(title, connect, newState) {
	newState.append(title);
	newState.append(connect);
	$('#tree-container').append(newState);
};

function makeNodesDraggable(newState) {
	jsPlumb.draggable(newState, {
		containment : 'parent'
	});
};

function bindDetachConnectorEvent() {
	jsPlumb.bind("dblclick", function(c) {
		jsPlumb.detach(c);
		if (c.id !== lastConnectionID) {
			lastConnectionID = c.id;
			scope.deleteLink(c.id.replace(LINK, ""));
		}
	});
};

// unused
function bindBeforeDroppedConnector() {
	jsPlumb.bind("beforeDrop", function(c) {
		return true;
	});
};

function removeNodeEvent(newState) {
	newState.dblclick(function(e) {

		var allTargetConnection = jsPlumb.getConnections({
			target : newState
		});
		var allSourceConnection = jsPlumb.getConnections({
			source : newState
		});

		jQuery.each(allTargetConnection, function() {
			// scope.deleteLink(this.id.replace(LINK, ""));
		});

		jQuery.each(allSourceConnection, function() {
			//scope.deleteLink(this.id.replace(LINK, ""));
		});

		scope.undropNode(newState[0].id.replace(NODE, ""));
		jsPlumb.detachAllConnections($(this));
		$(this).remove();
		e.stopPropagation();
	});
};

function makeNodeHierarchy(data) {
	var direction = "digraph chargraph {node[shape=box, margin=0, width=2, height=1];";
	jQuery.each(data,
			function(object) {
				connectNodes(NODE + this.sourceId, NODE + this.targetId,
						this.id);
				direction += NODE + this.sourceId + " -> " + NODE
						+ this.targetId + ";";
			});
	direction += "}";
	$('#dot-src').val(direction);
	isInizialized = true;
};

function setLinkId(connection, id) {
	connection.id = LINK + id;
};

// Code not Tested
function bindConnection() {
	jsPlumb.bind("connection", function(info) {
		if (!isInizialized) {
			setLinkId(info.connection, info.connection.getParameter("id"));
		} else {
			scope.addLink(1, info.sourceId.replace(NODE, ""), info.targetId
					.replace(NODE, ""), info.connection);
		}
	});
};

