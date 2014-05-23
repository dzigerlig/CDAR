var myJsTree = (function () {
    var DIRECTORY = 'directory';
    var NODE = 'node';
    var scope = angular.element(document.getElementById("wrapper")).scope();
    var copiedId = [];
    var quantitiyOfCopies = 0;
    var editedCopies = 0;
    var lastRenamed;

    var mouseOverJsTreeFlag = false;
    var eleme = $("#jstree-container");

    // private Methods
    eleme.mouseover(function () {
        mouseOverJsTreeFlag = true;
    }).mouseout(function () {
            mouseOverJsTreeFlag = false;
        });

    /*$('html').click(function () {
        if (!mouseOverJsTreeFlag) {
            $("#jstree").jstree("deselect_all");
        }
    });*/


    $(function () {
        $('#jstree').jstree('get_selected');

        var to = false;
        $('#plugins4_q').keyup(function () {
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function () {
                var v = $('#plugins4_q').val();
                $('#jstree').jstree(true).search(v);
            }, 250);
        });

        $('#jstree').on("select_node.jstree", function (e, data) {
            if (data.node.type !== 'default' && data.node.type !== 'root') {
                var id = data.selected[0];
                id = id.replace(NODE, "");
                id = id.replace(DIRECTORY, "");
                scope.changeNode(id, data.node.text);
            }
        });

        $('#jstree').on("copy_node.jstree", function (e, data) {
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
            function (e, data) {        	
                if (data.text === ""||lastRenamed!==undefined&&data.text===lastRenamed.old&&data.old===lastRenamed.text) {
                   return false;
                }
                else  if (data.text.length > 45) {
					 noty({
						 type : 'warning',
						 text : 'Please enter a text with less than 45 Characters',
						 timeout : 3000
					 });
			        	$("#jstree").jstree('rename_node', $('#'+data.node.id) , data.old);

					 return false;
				}
                lastRenamed=data;
                var id = data.node.id;
                id = id.replace(DIRECTORY, "");
                if (data.node.type !== 'default') {
                    id = id.replace(NODE, "");
                   scope.renameNode(id, data, data.node.parent.replace(
                        DIRECTORY, ""));
                } else {
                    scope.renameDirectory(id, data);
                }
            });

        $('#jstree').on("delete_node.jstree", function (e, data) {
            scope.selectedNodeId = 0;
            scope.selectednodename = "";
        });

        $('#jstree').on("create_node.jstree", function (e, data) {
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

    function drawDirectory(treeArray, rootid) {
        $('#jstree').jstree(
            {
                'core': {
                    "animation": 0,
                    "check_callback": true,
                    "themes": {
                        "stripes": true
                    },
                    'data': treeArray
                },
                "types": {

                    "#": {
                        "icon": "../cdarclient/app/img/tree.png",
                        "valid_children": [ "default" ]
                    },
                    "root": {
                        "icon": "../cdarclient/app/img/tree.png",
                        "valid_children": [ "default" ]
                    },
                    "default": {
                        "valid_children": [ "default", "file" ]
                    },
                    "file": {
                        "icon": "glyphicon glyphicon-file",
                        "valid_children": []
                    }
                },
                "plugins": ["dnd", "search", "sort", "types",
                    "themes" ]
            });
        if(scope.expandLevel>0){
        	$("#jstree").jstree("open_node", $("#" + rootid));
        
        	var children =  $("#jstree").jstree("get_children_dom", $("#" + rootid));
        	openRecursiv(scope.expandLevel-1,children);	
        }
    }
    
    function openRecursiv(val,children){
    	$.each(children, function( index, value ) {
    		if(val>=1)
			{
        		$("#jstree").jstree("open_node",(value.id));
    			openRecursiv(val-1, $("#jstree").jstree("get_children_dom", value.id));
			}
    	});
    }

    function deleteChildNodes(data) {
        data.children_d.forEach(function (nodeId) {
        	node = $('#jstree').jstree(true).get_node(nodeId);
            nodeId = nodeId.replace(DIRECTORY, "");
            if (node.type === 'default') {
               scope.deleteDirectory(nodeId);
            } else {
               scope.deleteNode(nodeId.replace(NODE, ""));
            }
        });
    }

    function dndCopyCreateSubnodes(data) {
        data.node.children_d.forEach(function (nodeId) {
            var node = data.instance._model.data[nodeId];
            if (node.type === 'default') {
                scope.addDirectoryCopy(node);
            } else {
                scope.addNodeCopy(node);
            }

        });
    }

    function setId() {
        for (var i in copiedId) {
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
    
    function deleteElement(selected){    
    	
        
        
  
    	
    	
    	scope.modal.open({
    		templateUrl : 'templates/confirmation.html',
    		backdrop : 'static',
    		keyboard : false,
    		resolve : {
    			data : function() {
    				return {
    					title : 'Delete '+scope.defaultDirectoryName,
    					message : 'Do you really want to delete selected '+scope.defaultDirectoryName+'s or '+scope.defaultNodeName+'s ?'
    				};
    			}
    		},
    		controller : 'ConfirmationController'
    	}).result.then(function(result) {	
    	      selected.forEach(function (name) {    	       
    	    	  var id = name.replace(DIRECTORY, "");
    	    	  if(id.indexOf("node")>=0)	
    	    	  {
    	    		  scope.deleteNode(id.replace(NODE, ""));
    	    	  }
    	    	  else
    	    	  {
    	    		  var data = $("#jstree").jstree('get_node', $('#'+name));
    	    		  scope.deleteDirectory(id);
    	    		  if (data.children_d.length) {
    	    			  deleteChildNodes(data);
    	    		  }
    	    	  } 
    		});
    	});          
    }
 	

// public Methods
    return{
        createNode: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length || sel.length > 1 || ref._model.data[sel].type !== "default") {
                noty({type: 'information', text: 'Please select a '+scope.defaultDirectoryName, timeout: 5000});
                return false;
            } else {
                scope.addNode(sel[0].replace(DIRECTORY, ""));
            };
        },
        createDirectory: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length || sel.length > 1
                || ref._model.data[sel].type !== "default" && ref._model.data[sel].type !== "root") {
                noty({type: 'information', text: 'Please select a '+scope.defaultDirectoryName, timeout: 5000});
                return false;
            } else {
                scope.addDirectory(sel[0].replace(DIRECTORY, ""));
            }
        },
        drillDown: function () {
        	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
        	if (!sel.length || sel.length > 1
        			|| ref._model.data[sel].type === "default" || ref._model.data[sel].type === "root") {
        		noty({type: 'information', text: 'Please select a '+ scope.defaultNodeName, timeout: 5000});
        		return false;
        	} else {
        		var nodeId = sel[0];
        		nodeId=nodeId.replace(NODE, "");
        		nodeId=nodeId.replace(DIRECTORY, "");
        		scope.drillDownNode(nodeId);
        	}
        },

        rename: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length) {
                noty({type: 'information', text: 'Please select a '+ scope.defaultDirectoryName+' or a '+ scope.defaultNodeName, timeout: 5000});
                return false;
            }
            else  if (sel.length>1) {
            	noty({type: 'information', text: 'Please select just one '+ scope.defaultDirectoryName+' or one '+ scope.defaultNodeName, timeout: 5000});
            	return false;
            }               
            sel = sel[0];
            if($("#jstree").jstree('get_node', $('#'+sel)).parent==='#'){
            	 noty({type: 'information', text: 'Please select a '+ scope.defaultDirectoryName+' or a '+ scope.defaultNodeName, timeout: 5000});            	return false;
            }
            ref.edit(sel);
        },
        delete: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length) {
                noty({type: 'information', text: 'Please select a '+ scope.defaultDirectoryName+' or a '+ scope.defaultNodeName, timeout: 5000});
                return false;
            }
            if (ref._model.data[sel[0]].parent === '#') {
                noty({type: 'warning', text: "You can't delete a root "+scope.defaultDirectoryName, timeout: 5000});
                return false;
            }

            var selected = sel.slice(0);
            
            deleteElement(selected);
        },

        directoryDataToArray: function (resDirectory, resNodes) {
            var treeArray = [];
            var parentId;
            var type;
            var rootid = null;
            resDirectory.forEach(function (entry) {
                if (entry.parentId === 0) {
                    parentId = "#";
                    type = "root";
                    rootid = DIRECTORY + entry.id;
                } else {
                    parentId = DIRECTORY + entry.parentId;
                    type = "default";
                }
                treeArray.push({
                    "id": DIRECTORY + entry.id,
                    "parent": parentId,
                    "text": entry.title,
                    "type": type
                });
            });

            resNodes.forEach(function (node) {
                treeArray.push({
                    "id": DIRECTORY + NODE + node.id,
                    "parent": DIRECTORY + node.directoryId,
                    "text": node.title,
                    "type": "file"
                });
            });
            drawDirectory(treeArray, rootid);
        },
        drawNewNode: function (response) {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            sel = sel[0];
            sel = ref.create_node(sel, {
                "type": "file",
                "id": DIRECTORY + NODE + response.id,
                "text": 'new '+scope.defaultNodeName
            });
            if (sel) {
                ref.edit(sel);
            }
        },

        drawNewDirectory: function(response){
            var sel;
            var ref = $('#jstree').jstree(true);
            sel = ref.get_selected();
            sel = sel[0];
            sel = ref.create_node(sel, {
                "type": "default",
                'text': 'new '+scope.defaultDirectoryName,
                "id": DIRECTORY + response.id
            });
            if (sel) {
                ref.edit(sel);
            }
        },

        prepareForSetId:function(node,id){
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
        }
    };
})();

