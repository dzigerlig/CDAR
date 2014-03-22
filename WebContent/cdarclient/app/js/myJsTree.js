$(function() {
	// 6 create an instance when the DOM is ready

	$('#jstree').jstree({
		"themes" : {

		},
		"core" : {
			// so that create works
			"check_callback" : true
		},
		 "dnd" : {
			    drop_target     : "#tree-container",
			    drop_check      : function (data) { return true; },
			    drag_target     : "#tree-container",
			    drag_check      : function (data) { return { after : true, before : true, inside : true }; }
			 },
		"types" : {
			"#" : {
				"valid_children" : [ "default" ]
			},
			"folder" : {
				"valid_children" : [ "default", "file" ]
			},
			"file" : {
				"icon" : "dist/themes/default/File.png",
				"valid_children" : []
			}
		},
		"plugins" : [ "contextmenu", "dnd", "search", "sort", "types" ]
	});

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
		console.log(data.selected);
	});
});
