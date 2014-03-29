var DICTIONARY='dictionary';
var scope = angular.element(document.getElementById("wrapper")).scope();


$(function() {
	
	$(document).on("dnd_move.vakata", function(e, data) {
		// console.log(data);
	});

	$('#jstree').jstree('get_selected');
	// 6 create an instance when the DOM is ready
	//

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

	// 7 bind to events triggered on the tree
	$('#jstree').on("changed.jstree", function(e, data) {
		scope.changeNode(data.selected[0].replace(DICTIONARY, ""));		
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
		if (dictionary.nodes.length!==0) {
		
			dictionary.nodes.forEach(function(node) {
				treeArray.push({
					"id" : "dictionaryNode" + node.id,
					"parent" : "dictionary" + dictionary.id,
					"text" : node.title	,	
					"type": "file"
				});
			});
		}
	});

	drawDictionary(treeArray);

};

