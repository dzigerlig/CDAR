var DICTIONARY = 'dictionary';
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
			id = id.replace(DICTIONARY, "");
			scope.changeNode(id);
		}
	});

	$('#jstree').on(
			"rename_node.jstree",
			function(e, data) {
				var id = data.node.id;
				id = id.replace(DICTIONARY, "");
				if (data.node.type !== 'default') {
					id = id.replace(NODE, "");
					scope.renameNode(id, data.text, data.node.parent.replace(
							DICTIONARY, ""));
				} else {
					scope.renameDictionary(id, data.text);
				}
			});

	$('#jstree').on("delete_node.jstree", function(e, data) {
		var id = data.node.id;
		id = id.replace(DICTIONARY, "");
		if (data.node.type !== 'default') {
			id = id.replace(NODE, "");

			scope.deleteNode(id);
		} else {
			scope.deleteDictionary(id);

		}
	});

	$('#jstree').on("create_node.jstree", function(e, data) {
		var id = data.node.id;
		var parentId = data.parent.replace(DICTIONARY, "");
		id = id.replace(DICTIONARY, "");
		if (data.node.type !== 'default') {
			id = id.replace(NODE, "");
			scope.moveNode(id, parentId);
		} else {
			if (parentId === '#') {
				parentId = id;
			}
			scope.moveDictionary(id, parentId);
		}
	});
});

function jstree_createNode() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		return false;
	} else {
		scope.addNode(sel[0].replace(DICTIONARY, ""));
	}
};

function jstree_createFolder() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		scope.addDictionary(0);
	} else {
		scope.addDictionary(sel[0].replace(DICTIONARY, ""));
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

function drawDictionary(treeArray) {
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

function dictionaryDataToArray(resDictionary, resNodes) {
	var treeArray = [];
	var parentId;

	resDictionary.forEach(function(entry) {
		if (entry.parentid === 0) {
			parentId = "#";
		} else {
			parentId = "dictionary" + entry.parentid;
		}
		treeArray.push({
			"id" : "dictionary" + entry.id,
			"parent" : parentId,
			"text" : entry.title,
		});
	});

	resNodes.forEach(function(node) {
		treeArray.push({
			"id" : "dictionarynode" + node.id,
			"parent" : "dictionary" + node.did,
			"text" : node.title,
			"type" : "file"
		});
	});
	drawDictionary(treeArray);
};

function createNode(response) {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	sel = sel[0];
	sel = ref.create_node(sel, {
		"type" : "file",
		"id" : "dictionarynode" + response.id
	});
	if (sel) {
		ref.edit(sel);
	}
};

function createDictionary(response) {
	var sel;
	var ref = $('#jstree').jstree(true);

	if (response.parentid === 0) {
		sel = $("#jstree").jstree('create_node', '#', {
			"id" : "dictionary" + response.id,
			'type' : "default",
			'text': 'new Folder'

		}, 'last');
	} else {
		sel = ref.get_selected();
		sel = sel[0];
		sel = ref.create_node(sel, {
			"type" : "default",
			'text': 'new Folder',
			"id" : "dictionary" + response.id
		});
	}
	if (sel) {
		ref.edit(sel);
	}
};

