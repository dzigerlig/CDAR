var CDARJsPlumb = (function() {

	var lastConnectionID = -1;
	var scope;
	var isInitialized = false;
	var NODE = 'node';
	var LINK = 'link';
	var SUBNODE = 'subnode';
	var STATUS = 'status';
	var selectedElement = null;
	var newLinkFired=true;

	// private Methods
	function buildContent() {
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
	}

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
									if ($(element.currentTarget).attr("id")
											.replace(SUBNODE, "") != -1) {
										scope.updateLink(this.id.replace(LINK,
												""), $(element.currentTarget)
												.attr("id")
												.replace(SUBNODE, ""));
									}
									$('[id^=popup-box-]').hide();
								}
							});
							element.stopPropagation();

						});
	}

	function makeSource(connect, newState) {
		jsPlumb.makeSource(connect, {
			parent : newState,
			anchor : 'Continuous',
			type : "change",
			data : {
				color : "#5C96BC",
				label : ""
			},
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
	}

	function makeTarget(newState) {
		jsPlumb.makeTarget(newState, {
			anchor : 'Continuous',
			endpoint : "Blank"
		});
	}

	function connectNodes(stateSource, stateTarget, id, subnode) {
		var label = 'all '+ scope.DescriptionService.getSubnodeDescription() + 's';
		if (subnode !== undefined) {
			label = subnode.title;
		}

		jsPlumb.connect({
			source : stateSource,
			target : stateTarget,
			parameters : {
				"id" : id
			},
			anchors : 'Continuous',
			type : "change",
			data : {
				color : "#5C96BC",
				label : label
			}
		});
	}

	function appendElements(title, connect, newState, subnode) {
		newState.append(title);
		newState.append(subnode);
		newState.append(connect);
		$('#jsplumb-container').append(newState);
	}

	function makeNodesDraggable(newState) {
		jsPlumb.draggable(newState, {
			containment : 'parent'
		});
	}

	function toggleSubnode(newState) {
		newState.dblclick(function(e) {
			$('#' + newState[0].id + ' .option').toggle();
			jsPlumb.repaintEverything();
		});
	}

	function clickNodeEvent(newState) {
		newState.click(function(e) {
			scope.changeNode(newState[0].id.replace(NODE, ""), $(
					'#' + newState[0].id + ' .title').text());

			resetSelectDesign();
			newState.addClass("selectednode");

			var connections = getConnections(newState);

			jQuery.each(connections, function(object) {
				this.setType("change", {
					color : "#beebff",
					label : this.getOverlay("label").getLabel()
				});
			});
			selectedElement = newState[0].id;
			e.stopPropagation();
		});
	}

	function resetSelectDesign() {
		if (selectedElement !== null) {
			if (selectedElement.indexOf(NODE) > -1) {
				if ($("#" + selectedElement).size() !== 0) {
					var newState = $('#' + selectedElement);
					newState.removeClass("selectednode");
					var connections = getConnections(newState);
					jQuery.each(connections, function(object) {
						this.setType("change", {
							color : "#5C96BC",
							label : this.getOverlay("label").getLabel()
						});
					});
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
		selectedElement = null;
	}

	function drillUpEvent(uptree, newState) {
		uptree.click(function(e) {
			e.stopPropagation();
			scope.drillUpNode(newState[0].id.replace(NODE, ""));
		});
	}

	function drillDownEvent(downtree, newState) {
		downtree.click(function(e) {
			e.stopPropagation();
			scope.drillDownNode(newState[0].id.replace(NODE, ""));
		});
	}

	function bindNewConnection() {
		jsPlumb.bind("connection", function(info) {
			if (!isInitialized) {
				CDARJsPlumb.setLinkId(info.connection, info.connection
						.getParameter("id"));
				bindClickConnection(info);
			} else {
				scope.addLink(scope.treeId, info.sourceId.replace(NODE, ""),
						info.targetId.replace(NODE, ""), info.connection);
				showSubnodePopup(info);
				bindClickConnection(info);
				newLinkFired=true;
			}
		});
	}
	
	function bindDropConnection(info)
	{
		jsPlumb.bind("beforeDrop", function(connection) {
			if(!isInitialized);
			else if(newLinkFired)
				{newLinkFired=false;}
			else 
				{noty({
					  text: 'An error has occurred. You have to reload!', 
					  buttons: [
					    {addClass: 'btn btn-primary', text: 'Reload', onClick: function($noty) {
					    	location.reload();
					        $noty.close();
					      }
					    },
					    {addClass: 'btn btn-danger', text: 'Cancel', onClick: function($noty) {
					        $noty.close();
					        noty({text: 'Your changes may not be stored correctly', type: 'error'});
					      }
					    }
					  ]
					});}
			return !newLinkFired ;
        });
	}

	function bindClickConnection(info) {
		info.connection.bind("click", function(c) {
			resetSelectDesign();
			var label = info.connection.getOverlay("label").getLabel();

			info.connection.setType("change", {
				color : "#BEEBFF",
				label : label
			});
			selectedElement = info.connection.id;
		});
	}

	function showSubnodePopup(info) {
		$('#popup-box-1').show();
		$('#radio-form').empty();
		if (info.connection.source.data(SUBNODE).subnode) {
			$.each(info.connection.source.data(SUBNODE).subnode, function(
					object) {
				buildPopupContent(this.id, this.title);
			});
		}
		buildPopupContent(-1, 'all '+ scope.DescriptionService.getSubnodeDescription() + 's');
	}

	function buildPopupContent(id, title) {
		$('#radio-form').append(
				"<input type=\"radio\" id=\"" + SUBNODE + id
						+ "\" name=\"option\" class=\"radio_item\" value=\""
						+ title + "\">" + title + "<br>");
	};

	function registerLinkTemplate() {
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

	function getConnections(state) {
		var allTargetConnection = jsPlumb.getConnections({
			target : state
		});
		var allSourceConnection = jsPlumb.getConnections({
			source : state
		});
		return allTargetConnection.concat(allSourceConnection);
	}

	function getStatusImage(status) {
		var state;
		switch (status) {
		case 1:
			state = "open.png";
			break;
		case 2:
			state = "decided.png";
			break;
		case 3:
			state = "accepted.png";
			break;
		case 4:
			state = "rejected.png";
			break;
		case 5:
			state = "closed.png";
			break;
		default:
			state = "open.png";
			break;
		}
		return 'app/img/' + state;
	}
	
	function prepareRemoveLink(id){
		scope.deleteLink(id.replace(LINK, ""));
	}

    function detachNode(id) {
        var node = $('#' + NODE + id);
        if (node.size() !== 0) {
            var connections = getConnections(node);
            jQuery.each(connections, function() {
                scope.deleteLink(this.id.replace(LINK, ""));
            });
            scope.undropNode(node[0].id.replace(NODE, ""));
        }
    }

	// public Methods
	return {
		initialize : function() {
			scope = angular.element(document.getElementById("wrapper")).scope();
			setDefaultSettings();
			registerLinkTemplate();
			buildContent();
			makePopupEvents();
			bindNewConnection();
			bindDropConnection()
			$('html').click(function(e) {		
				if ((e.target.type!=='radio')&&$('[id^=popup-box-]').is(':visible')) {
					prepareRemoveLink(lastConnectionID);
					$('[id^=popup-box-]').hide();
				}
			});
		},

		drawNewNode : function(response, e) {
			isInitialized = true;
			var newState = $('<div>').attr('id', NODE + response.id).addClass(
					'w').data(SUBNODE, {
				subnode : null
			});
			var title = $('<div>').addClass('title').text(response.title);
			var connect = $('<div>').addClass('ep draglink');
			var option = $('<div>').addClass('option').hide();
			var list = $('<ul>').addClass('optionList');
			var downtree = $('<div>').addClass('downtree');
			var uptree = $('<div>').addClass('uptree');
			if (!scope.isProducer) {
				var status = $('<div>').attr('id', STATUS + response.id)
						.addClass('status');
				status.css('background-image', 'url('
						+ getStatusImage(1) + ')');
				newState.append(status);				
			}
			newState.append(downtree);
			newState.append(uptree);
			option.append(list);

			newState.css({
				// calculate coordinates of the cursor in element
				'top' : e.pageY - $('#jsplumb-container').offset().top,
				'left' : e.pageX - $('#jsplumb-container').offset().left
			});
			drillDownEvent(downtree, newState);
			drillUpEvent(uptree, newState);
			makeNodesDraggable(newState);
			toggleSubnode(newState);
			clickNodeEvent(newState);
			makeTarget(newState);
			makeSource(connect, newState);
			appendElements(title, connect, newState, option);
			scope.getSubnodesOfNode(response.id);
		},

		drawExistingNodes : function(data, resSubnodes) {
			buildContent();
			makePopupEvents();

			isInitialized = false;
			newLinkFired=true;

			selectedElement = null;
			var map = {};
			jQuery.each(resSubnodes, function(object) {
				if (map[this.nodeId] === undefined) {
					var arr = [ this ];
					map[this.nodeId] = arr;
				} else {
					var arr = map[this.nodeId];
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
					var newState = $('<div>').attr('id', NODE + this.id)
							.addClass('w').data(SUBNODE, {
								subnode : map[this.id]
							});
					var option = $('<div>').addClass('option').hide();
					var title = $('<div>').addClass('title').text(this.title);
					var connect = $('<div>').addClass('ep draglink');
					var downtree = $('<div>').addClass('downtree');
					var uptree = $('<div>').addClass('uptree');

					if (!scope.isProducer) {

						var status = $('<div>').attr('id', STATUS + this.id)
								.addClass('status').css(
										'background-image',
										'url(' + getStatusImage(this.status)
												+ ')');
						newState.append(status);
					}
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
					
					drillDownEvent(downtree, newState);
					drillUpEvent(uptree, newState);
					makeNodesDraggable(newState);

					toggleSubnode(newState);
					clickNodeEvent(newState);

					makeTarget(newState);
					makeSource(connect, newState);

					appendElements(title, connect, newState, option);
					scope.getSubnodesOfNode(this.id);


				}
			});
        },
		removeSelected : function() {
			if (selectedElement !== null) {
				if (selectedElement.indexOf(NODE) > -1) {
					detachNode(selectedElement.replace(NODE, ""));
				} else {
					prepareRemoveLink(selectedElement);
				}
			} else {
				noty({
					type : 'information',
					text : 'Please select a node or a connection',
					timeout : 5000
				});
			}
		},
		
		removeNode: function(node){
			if (node.size() !== 0) {
				var connections = getConnections(node);
				jQuery.each(connections, function(object) {
						jsPlumb.detach(this);
				});
				jsPlumb.detachAllConnections($(node));
				jsPlumb.removeAllEndpoints($(node));
				$(node).remove();
			}
		},

		removeLink: function(id) {
			var connections = jsPlumb.getConnections();
			jQuery.each(connections, function(object) {
				if (id === this.id) {
					jsPlumb.detach(this);
				}
			});
		},
		
		makeNodeHierarchy : function(data, resSubNodes) {
			$("#dot-src").empty();
			var map = {};
			jQuery.each(resSubNodes, function(object) {
				map[this.id] = this;
			});

			var direction = "digraph chargraph {node[shape=box, margin=0, width=2, height=1];";
			jQuery.each(data, function(object) {
				connectNodes(NODE + this.sourceId, NODE + this.targetId,
						this.id, map[this.subnodeId]);
				direction += NODE + this.sourceId + " -> " + NODE
						+ this.targetId + ";";
			});
			direction += "}";
			$('#dot-src').val(direction);
			isInitialized = true;
		},

		setLinkId : function(connection, id) {
			connection.id = LINK + id;
			lastConnectionID = LINK + id;
		},

		renameNode : function(id, newTitle) {
			var title = $('#' + NODE + id + ' .title');
			if (title.size() !== 0) {
				title[0].innerHTML = newTitle;
			}
		},

		setStatusImage : function(node) {
			var imageDiv = $('#' + STATUS + node.id);
			imageDiv.css('background-image', 'url('
					+ getStatusImage(node.status) + ')');
		},

		updateSubnodesOfNode : function(newSubnode, nodeId, changes) {
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
							linkTitle = jQuery.removeFromArray(this.title,
									linkTitle);
						}
					});
					jQuery.each(allSourceConnection,
							function(object) {
								if (map[this.id.replace(LINK, "")]) {
									if (changes.operation === 'delete') {
										jsPlumb.detach(this);
									} else if (changes.operation === 'update') {
										this.getOverlay("label").setLabel(
												linkTitle[0]);
									}
								}
							});
				}
			}
		},

		drillDownButton : function() {
			if (selectedElement&&selectedElement.indexOf(NODE) > -1) {
				scope.drillDownNode(selectedElement.replace(NODE, ""));
			}
			else
			{
				noty({
					type : 'information',
					text : 'Please select a node',
					timeout : 5000
				});
			}
		},

		drillUpButton : function() {
			if (selectedElement&&selectedElement.indexOf(NODE) > -1) {
				scope.drillUpNode(selectedElement.replace(NODE, ""));
			}
			else{
				noty({
					type : 'information',
					text : 'Please select a node',
					timeout : 5000
				});
			}
		}

	};
})();
