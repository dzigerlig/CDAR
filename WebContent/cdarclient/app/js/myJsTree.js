$(function() {
	// 6 create an instance when the DOM is ready

	$('#jstree').jstree(
			{
				"themes" : {

				},
				"core" : {
					// so that create works
					"check_callback" : true
				},

				"types" : {
						"default" : {
							"icon" :  "http://jstree.com/tree.png"
							
						}
					

				},
				"plugins" : [ "contextmenu", "dnd", "search", "sort", "types",
						"themes" ]
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
