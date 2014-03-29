var DICTIONARY = 'dictionary';
var NODE = 'node';
var scope = angular.element(document.getElementById("wrapper")).scope();

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
		var id = data.selected[0];
		if (data.node.type !== 'default') {
			id = id.replace(NODE, "");
		}
		id = id.replace(DICTIONARY, "");		
		scope.changeNode(id);
	});

	$('#jstree').on("rename_node.jstree", function(e, data) {
		var id = data.node.id;
		id = id.replace(DICTIONARY, "");
		if (data.node.type !== 'default') {
			id = id.replace(NODE, "");

			scope.renameNode(id, data.text);
		} else {
			scope.renameDictionary(id, data.text);

		}
	});
	
	$('#jstree').on("delete_node.jstree", function(e, data) {
		var id = data.node.id;
		id = id.replace(DICTIONARY, "");
		if (data.node.type !== 'default') {
			id = id.replace(NODE, "");

			scope.deleteNode(id, data.text);
		} else {
			scope.renameDictionary(id, data.text);

		}
	});
	
	$('#jstree').on("create_node.jstree", function(e, data) {
		//TODO kommunikation über addNode sowie Mapping hinzufügen
		console.log(data);
	});
});

function jstree_create() {
	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	sel = sel[0];
	sel = ref.create_node(sel, {
		"type" : "file"
	});
	if (sel) {
		ref.edit(sel);
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

function dictionaryDataToArray(resDictionary) {
	var treeArray = [];
	var parentId;

	resDictionary.forEach(function(entry) {
		if (entry.parentId === 0) {
			parentId = "#";
		} else {
			parentId = "dictionary" + entry.parentId;
		}
		treeArray.push({
			"id" : "dictionary" + entry.id,
			"parent" : parentId,
			"text" : entry.title,
		});
	});

	resDictionary.forEach(function(dictionary) {
		if (dictionary.nodes.length !== 0) {

			dictionary.nodes.forEach(function(node) {
				treeArray.push({
					"id" : "dictionarynode" + node.id,
					"parent" : "dictionary" + dictionary.id,
					"text" : node.title,
					"type" : "file"
				});
			});
		}
	});

	drawDictionary(treeArray);

};

