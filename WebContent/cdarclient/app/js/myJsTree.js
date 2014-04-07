var DIRECTORY = 'directory';
var NODE = 'node';
var scope = angular.element(document.getElementById("wrapper")).scope();

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
		if (data.node.type !== 'default') {
			var id = data.selected[0];
			// if (data.node.type !== 'default') {
			id = id.replace(NODE, "");
			// }
			id = id.replace(DIRECTORY, "");
			scope.changeNode(id);
		}
	});

	$('#jstree').on(
			"rename_node.jstree",
			function(e, data) {
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
		var id = data.node.id;
		id = id.replace(DIRECTORY, "");
		if (data.node.type !== 'default') {
			id = id.replace(NODE, "");

			scope.deleteNode(id);
		} else {
			scope.deleteDirectory(id);

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
	if (!sel.length) {
		return false;
	} else {
		scope.addNode(sel[0].replace(DIRECTORY, ""));
	}
};

function jstree_createDirectory() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		scope.addDirectory(0);
	} else {
		scope.addDirectory(sel[0].replace(DIRECTORY, ""));
	}
};

function jstree_rename() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	sel = sel[0];
	ref.edit(sel);
};
function jstree_delete() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	ref.delete_node(sel);
};

function drawDirectory(treeArray) {
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
						"max_children" : 1,
						"max_depth" : 4,
						"valid_children" : [ "root" ]
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
}

function directoryDataToArray(resDirectory, resNodes) {
	var treeArray = [];
	var parentId;

	resDirectory.forEach(function(entry) {
		if (entry.parentid === 0) {
			parentId = "#";
		} else {
			parentId = DIRECTORY + entry.parentid;
		}
		treeArray.push({
			"id" : DIRECTORY + entry.id,
			"parent" : parentId,
			"text" : entry.title
		});
	});

	resNodes.forEach(function(node) {
		treeArray.push({
			"id" : DIRECTORY+NODE + node.id,
			"parent" : DIRECTORY + node.did,
			"text" : node.title,
			"type" : "file"
		});
	});
	drawDirectory(treeArray);
};

function createNode(response) {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	sel = sel[0];
	sel = ref.create_node(sel, {
		"type" : "file",
		"id" : DIRECTORY+NODE+ response.id
	});
	if (sel) {
		ref.edit(sel);
	}
};

function createDirectory(response) {
	var sel;
	var ref = $('#jstree').jstree(true);

	if (response.parentid === 0) {
		sel = $("#jstree").jstree('create_node', '#', {
			"id" : DIRECTORY + response.id,
			'type' : "default",
			'text': 'new Folder'

		}, 'last');
	} else {
		sel = ref.get_selected();
		sel = sel[0];
		sel = ref.create_node(sel, {
			"type" : "default",
			'text': 'new Folder',
			"id" : DIRECTORY + response.id
		});
	}
	if (sel) {
		ref.edit(sel);
	}
};

