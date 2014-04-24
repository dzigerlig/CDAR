var DIRECTORY = 'directory';
var NODE = 'node';
var scope = angular.element(document.getElementById("wrapper")).scope();
var copiedId = [];
var quantitiyOfCopies = 0;
var editedCopies = 0;

var mouseOverJsTreeFlag = false;
var eleme = $("#jstree-container");
eleme.mouseover(function() {
	mouseOverJsTreeFlag = true;
}).mouseout(function() {
	mouseOverJsTreeFlag = false;
});

$('html').click(function() {
	if (!mouseOverJsTreeFlag) {
		$("#jstree").jstree("deselect_all");
	}
});

$(function() {
	$('#jstree').jstree('get_selected');

	var to = false;
	$('#plugins4_q').keyup(function() {
		if (to) {
			clearTimeout(to);
		}
		to = setTimeout(function() {
			var v = $('#plugins4_q').val();
			$('#jstree').jstree(true).search(v);
		}, 250);
	});

	$('#jstree').on("select_node.jstree", function(e, data) {
		if (data.node.type !== 'default' && data.node.type !== 'root') {
			var id = data.selected[0];
			id = id.replace(NODE, "");
			id = id.replace(DIRECTORY, "");
			scope.changeNode(id, data.node.text);
		}
	});

	$('#jstree').on("copy_node.jstree", function(e, data) {
		quantitiyOfCopies = 1;
		var node = data.node;
		if (node.type === 'default') {
			scope.addDirectoryCopy(node);
			if (node.children_d.length) {
				quantitiyOfCopies += node.children_d.length;
				dndCopyCreateSubnodes(data);
			}
		} else {
			scope.addNodeCopy(node);
		}
	});

	$('#jstree').on(
			"rename_node.jstree",
			function(e, data) {
				if (data.text === "") {
					return false;
				}
				var id = data.node.id;
				id = id.replace(DIRECTORY, "");
				if (data.node.type !== 'default') {
					id = id.replace(NODE, "");
					scope.renameNode(id, data.text, data.node.parent.replace(
							DIRECTORY, ""));
				} else {
					scope.renameDirectory(id, data.text);
				}
			});

	$('#jstree').on("delete_node.jstree", function(e, data) {
		scope.selectedNodeId = 0;
		$scope.selectednodename="";
		var node = data.node;
		var id = data.node.id;
		id = id.replace(DIRECTORY, "");
		if (node.type === 'default') {
			scope.deleteDirectory(id);
			if (node.children_d.length) {
				deleteSubnodes(data);
			}
		} else {
			scope.deleteNode(id.replace(NODE, ""));
		}
	});

	$('#jstree').on("create_node.jstree", function(e, data) {
		var id = data.node.id;
		var parentId = data.parent.replace(DIRECTORY, "");
		id = id.replace(DIRECTORY, "");
		if (data.node.type !== 'default') {
			id = id.replace(NODE, "");
			scope.moveNode(id, parentId);
		} else {
			if (parentId === '#') {
				parentId = id;
			}
			scope.moveDirectory(id, parentId);
		}
	});
});

function jstree_createNode() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length || sel.length > 1
			|| ref._model.data[sel].type !== "default") {
		noty({type: 'success', text : 'Please select a folder', timeout: 5000});
		return false;
	} else {
		scope.addNode(sel[0].replace(DIRECTORY, ""));
	}
};

function jstree_createDirectory() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length || sel.length > 1
			|| ref._model.data[sel].type !== "default"
			&& ref._model.data[sel].type !== "root") {
		noty({type: 'success', text : 'Please select a folder', timeout: 5000});
		return false;
	} else {
		scope.addDirectory(sel[0].replace(DIRECTORY, ""));
	}
};

function jstree_rename() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		noty({type: 'success', text : 'Please select a folder or a node', timeout: 5000});
		return false;
	}
	sel = sel[0];
	ref.edit(sel);
};
function jstree_delete() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		noty({type: 'success', text : 'Please select a folder or a node', timeout: 5000});
		return false;
	}
	if (ref._model.data[sel[0]].parent === '#') {
		noty({type: 'success', text : "You can't delete a root node", timeout: 5000});
		return false;
	}

	var selected = sel.slice(0);
	selected.forEach(function(name) {
		ref.delete_node(name);
	});
};

function drawDirectory(treeArray, rootid) {
	$('#jstree').jstree(
			{
				'core' : {
					"animation" : 0,
					"check_callback" : true,
					"themes" : {
						"stripes" : true
					},
					'data' : treeArray
				},
				"types" : {

					"#" : {
						"icon" : "http://jstree.com/tree.png",
						"valid_children" : [ "default" ]
					},
					"root" : {
						"icon" : "http://jstree.com/tree.png",
						"valid_children" : [ "default" ]
					},
					"default" : {
						"valid_children" : [ "default", "file" ]
					},
					"file" : {
						"icon" : "glyphicon glyphicon-file",
						"valid_children" : []
					}
				},
				"plugins" : [ "contextmenu", "dnd", "search", "sort", "types",
						"themes" ]
			});
	$("#jstree").jstree("open_node", $("#" + rootid));

}

function directoryDataToArray(resDirectory, resNodes) {
	var treeArray = [];
	var parentId;
	var type;
	var rootid = null;

	resDirectory.forEach(function(entry) {
		if (entry.parentid === 0) {
			parentId = "#";
			type = "root";
			rootid = DIRECTORY + entry.id;
		} else {
			parentId = DIRECTORY + entry.parentid;
			type = "default";
		}
		treeArray.push({
			"id" : DIRECTORY + entry.id,
			"parent" : parentId,
			"text" : entry.title,
			"type" : type
		});
	});

	resNodes.forEach(function(node) {
		treeArray.push({
			"id" : DIRECTORY + NODE + node.id,
			"parent" : DIRECTORY + node.did,
			"text" : node.title,
			"type" : "file"
		});
	});
	drawDirectory(treeArray, rootid);
};

function createNode(response) {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	sel = sel[0];
	sel = ref.create_node(sel, {
		"type" : "file",
		"id" : DIRECTORY + NODE + response.id,
		"text" : "new Node"
	});
	if (sel) {
		ref.edit(sel);
	}
};

function createDirectory(response) {
	var sel;
	var ref = $('#jstree').jstree(true);
	sel = ref.get_selected();
	sel = sel[0];
	sel = ref.create_node(sel, {
		"type" : "default",
		'text' : 'new Folder',
		"id" : DIRECTORY + response.id
	});
	if (sel) {
		ref.edit(sel);
	}
};

function deleteSubnodes(data) {
	data.node.children_d.forEach(function(nodeId) {
		var node = data.instance._model.data[nodeId];
		nodeId=nodeId.replace(DIRECTORY, "")
		if (node.type === 'default') {
			scope.deleteDirectory(nodeId);
		} else {
			myJsPlumb.detachNode(nodeId.replace(NODE, ""));
			scope.deleteNode(nodeId.replace(NODE, ""));
		}

	});
}
function dndCopyCreateSubnodes(data) {
	data.node.children_d.forEach(function(nodeId) {
		var node = data.instance._model.data[nodeId];
		if (node.type === 'default') {
			scope.addDirectoryCopy(node);
		} else {
			scope.addNodeCopy(node);
		}

	});
}

function prepareForSetId(node, id) {
	editedCopies++;
	var nodeCopy = $('#jstree').jstree(true).get_node(node.id);
	if (node.type === 'default') {
		id = DIRECTORY + id;
	} else {
		id = DIRECTORY + NODE + id;
	}
	$('#jstree').jstree(true).set_id(nodeCopy, id);
	copiedId.push(id);

	if (editedCopies == quantitiyOfCopies) {
		setId();
	}

	function setId() {
		for ( var i in copiedId) {
			var nodeParent = $('#jstree').jstree(true).get_node(copiedId[i]);
			var id = copiedId[i].replace(DIRECTORY, "");
			if (nodeParent.type === 'default') {
				scope.moveDirectory(id, nodeParent.parent
						.replace(DIRECTORY, ""));

			} else {
				scope.moveNode(id.replace(NODE, ""), nodeParent.parent.replace(
						DIRECTORY, ""));
			}
		}
		copiedId = [];
		editedCopies = 0;
		quantitiyOfCopies = 0;
	}
}
