var CDARJsTree = (function () {
    var DIRECTORY = 'directory';
    var NODE = 'node';
    var scope = angular.element(document.getElementById('wrapper')).scope();
    var copiedId = [];
    var quantitiyOfCopies = 0;
    var editedCopies = 0;
    var lastRenamed;

    var mouseOverJsTreeFlag = false;
    var eleme = $('#jstree-container');

    // private Methods
    eleme.mouseover(function () {
        mouseOverJsTreeFlag = true;
    }).mouseout(function () {
            mouseOverJsTreeFlag = false;
        });

    $(function () {
        $('#jstree').jstree('get_selected');

        var to = false;
        //Listener keyup jsTree Search
        $('#jsTreeSearch').keyup(function () {
            if (to) {
                clearTimeout(to);
            }
            to = setTimeout(function () {
                var v = $('#jsTreeSearch').val();
                $('#jstree').jstree(true).search(v);
            }, 250);
        });

        //Listener on select node in jsTree
        //Show wiki content
        $('#jstree').on('select_node.jstree', function (e, data) {
            if (data.node.type !== 'default' && data.node.type !== 'root') {
                var id = data.selected[0];
                id = id.replace(NODE, '');
                id = id.replace(DIRECTORY, '');
                scope.changeNode(id, data.node.text);
            }
        });

        //Listener on node- or foldercopy in jsTree
        //add new Node with Subnodes or Folder copies
        $('#jstree').on('copy_node.jstree', function (e, data) {
        	quantitiyOfCopies = 1;
            var node = data.node;
            if (node.type === 'default') {
                scope.addDirectoryCopy(node);
                if (node.children_d.length) {
                    quantitiyOfCopies += node.children_d.length;
                    dndCopyCreateSubnodes(data);
                }
            } else {
            	var id = data.original.id.replace(NODE, '').replace(DIRECTORY, '');
                scope.addNodeCopy(id,node);
            }
        });

        //Listener on rename node or folder
        //Check new name length
        //Rename selected Element in jsTree
        $('#jstree').on(
            'rename_node.jstree',
            function (e, data) {        	
                if (data.text === ''||lastRenamed!==undefined&&data.text===lastRenamed.old&&data.old===lastRenamed.text) {
                   return false;
                }
                else  if (data.text.length > 45) {
					 noty({
						 type : 'warning',
						 text : 'Please enter a text with less than 45 Characters',
						 timeout : 3000
					 });
			        	$('#jstree').jstree('rename_node', $('#'+data.node.id) , data.old);

					 return false;
				}
                lastRenamed=data;
                var id = data.node.id;
                id = id.replace(DIRECTORY, '');
                if (data.node.type !== 'default') {
                    id = id.replace(NODE, '');
                   scope.renameNode(id, data, data.node.parent.replace(
                        DIRECTORY, ''));
                } else {
                    scope.renameDirectory(id, data);
                }
            });

        //Listener on delete node or folder
        $('#jstree').on('delete_node.jstree', function (e, data) {
            scope.selectedNodeId = 0;
            scope.selectednodename = '';
        });

        
        //Listener on create node or folder
        //Set parentid after creation
        $('#jstree').on('create_node.jstree', function (e, data) {
        	console.log('create node');
            var id = data.node.id;
            var parentId = data.parent.replace(DIRECTORY, '');
            id = id.replace(DIRECTORY, '');
            if (data.node.type !== 'default') {
                id = id.replace(NODE, '');
                scope.moveNode(id, parentId);
            } else {
                if (parentId === '#') {
                    parentId = id;
                }
                scope.moveDirectory(id, parentId);
            }
        });
    });

    //Set jsTree Configuration and draw it with nodes and folders and expand jsTree
    function drawDirectory(treeArray, rootid) {
        $('#jstree').jstree(
            {
                'core': {
                    'animation': 0,
                    'check_callback': true,
                    'themes': {
                        'stripes': true
                    },
                    'data': treeArray
                },
                'types': {

                    '#': {
                        'icon': '../cdarclient/app/img/tree.png',
                        'valid_children': [ 'default' ]
                    },
                    'root': {
                        'icon': '../cdarclient/app/img/tree.png',
                        'valid_children': [ 'default' ]
                    },
                    'default': {
                        'valid_children': [ 'default', 'file' ]
                    },
                    'file': {
                        'icon': 'glyphicon glyphicon-file',
                        'valid_children': []
                    }
                },
                'plugins': ['dnd', 'search', 'sort', 'types',
                    'themes' ]
            });
        if(scope.DescriptionService.getExpandedLevel()>0){
        	$('#jstree').jstree('open_node', $('#' + rootid));
        
        	var children =  $('#jstree').jstree('get_children_dom', $('#' + rootid));
        	openDirectories(scope.DescriptionService.getExpandedLevel()-1,children);
        }
    }
    
    //expand directories
    function openDirectories(val,children){
    	$.each(children, function( index, value ) {
    		if(val>=1)
			{
        		$('#jstree').jstree('open_node',(value.id));
    			openDirectories(val-1, $('#jstree').jstree('get_children_dom', value.id));
			}
    	});
    }

    //delete each children of a directory
    function deleteChildNodes(data) {
        data.children_d.forEach(function (nodeId) {
        	node = $('#jstree').jstree(true).get_node(nodeId);
            nodeId = nodeId.replace(DIRECTORY, '');
            if (node.type === 'default') {
               scope.deleteDirectory(nodeId);
            } else {
               scope.deleteNode(nodeId.replace(NODE, ''));
            }
        });
    }

    //copy each node/folder of a directory
    function dndCopyCreateSubnodes(data) {
    	var i = 0;
        data.node.children_d.forEach(function (nodeId) {
            var node = data.instance._model.data[nodeId];
            if (node.type === 'default') {
                scope.addDirectoryCopy(node);
            } else {
            	var id = data.original.children_d[i].replace(NODE, '').replace(DIRECTORY, '');
                scope.addNodeCopy(id,node);
            }
            i++;
        });
    }

    //set parentid of each copied element
    function setId() {
        for (var i in copiedId) {
            var nodeParent = $('#jstree').jstree(true).get_node(copiedId[i]);
            var id = copiedId[i].replace(DIRECTORY, '');
            if (nodeParent.type === 'default') {
                scope.moveDirectory(id, nodeParent.parent.replace(DIRECTORY, ''));

            } else {
                scope.moveNode(id.replace(NODE, ''), nodeParent.parent.replace(DIRECTORY, ''));
            }
        }
        copiedId = [];
        editedCopies = 0;
        quantitiyOfCopies = 0;
    }
    
    //delete an element with confirmation
    function deleteElement(selected){
    	scope.modal.open({
    		templateUrl : 'templates/confirmation.html',
    		backdrop : 'static',
    		keyboard : false,
    		resolve : {
    			data : function() {
    				return {
    					title : 'Delete '+scope.DescriptionService.getDirectoryDescription(),
    					message : 'Do you really want to delete selected '+scope.DescriptionService.getDirectoryDescription()+'s or '+scope.DescriptionService.getNodeDescription()+'s ?'
    				};
    			}
    		},
    		controller : 'ConfirmationController'
    	}).result.then(function(result) {	
    	      selected.forEach(function (name) {    	       
    	    	  var id = name.replace(DIRECTORY, '');
    	    	  if(id.indexOf('node')>=0)	
    	    	  {
    	    		  scope.deleteNode(id.replace(NODE, ''));
    	    	  }
    	    	  else
    	    	  {
    	    		  var data = $('#jstree').jstree('get_node', $('#'+name));
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
    	
    	//Buttonclick createNode
        createNode: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length || sel.length > 1 || ref._model.data[sel].type !== 'default') {
                noty({type: 'information', text: 'Please select a '+scope.DescriptionService.getDirectoryDescription(), timeout: 5000});
                return false;
            } else {
                scope.addNode(sel[0].replace(DIRECTORY, ''));
            };
        },
        
      //Buttonclick createDirectory
        createDirectory: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length || sel.length > 1
                || ref._model.data[sel].type !== 'default' && ref._model.data[sel].type !== 'root') {
                noty({type: 'information', text: 'Please select a '+scope.DescriptionService.getDirectoryDescription(), timeout: 5000});
                return false;
            } else {
                scope.addDirectory(sel[0].replace(DIRECTORY, ''));
            }
        },
        
        //Buttonclick drillDown (showRoute)
        drillDown: function () {
        	var ref = $('#jstree').jstree(true), sel = ref.get_selected();
        	if (!sel.length || sel.length > 1
        			|| ref._model.data[sel].type === 'default' || ref._model.data[sel].type === 'root') {
        		noty({type: 'information', text: 'Please select a '+ scope.DescriptionService.getNodeDescription(), timeout: 5000});
        		return false;
        	} else {
        		var nodeId = sel[0];
        		nodeId=nodeId.replace(NODE, '');
        		nodeId=nodeId.replace(DIRECTORY, '');
        		scope.drillDownNode(nodeId);
        	}
        },

        //Buttonclick rename element
        rename: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length) {
                noty({type: 'information', text: 'Please select a '+ scope.DescriptionService.getDirectoryDescription()+' or a '+ scope.DescriptionService.getNodeDescription(), timeout: 5000});
                return false;
            }
            else  if (sel.length>1) {
            	noty({type: 'information', text: 'Please select just one '+ scope.DescriptionService.getDirectoryDescription()+' or one '+ scope.DescriptionService.getNodeDescription(), timeout: 5000});
            	return false;
            }               
            sel = sel[0];
            if($('#jstree').jstree('get_node', $('#'+sel)).parent==='#'){
            	 noty({type: 'information', text: 'Please select a '+ scope.DescriptionService.getDirectoryDescription()+' or a '+ scope.DescriptionService.getNodeDescription(), timeout: 5000});            	return false;
            }
            ref.edit(sel);
        },
        
        //Buttonclick delete element
        delete: function () {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            if (!sel.length) {
                noty({type: 'information', text: 'Please select a '+ scope.DescriptionService.getDirectoryDescription()+' or a '+ scope.DescriptionService.getNodeDescription(), timeout: 5000});
                return false;
            }
            if (ref._model.data[sel[0]].parent === '#') {
                noty({type: 'warning', text: 'You cannot delete a root '+scope.DescriptionService.getDirectoryDescription(), timeout: 5000});
                return false;
            }

            var selected = sel.slice(0);
            
            deleteElement(selected);
        },

        //prepare all Directories and Nodes for jsTree visualization
        directoryDataToArray: function (resDirectory, resNodes) {
            var treeArray = [];
            var parentId;
            var type;
            var rootid = null;
            resDirectory.forEach(function (entry) {
                if (entry.parentId === 0) {
                    parentId = '#';
                    type = 'root';
                    rootid = DIRECTORY + entry.id;
                } else {
                    parentId = DIRECTORY + entry.parentId;
                    type = 'default';
                }
                treeArray.push({
                    'id': DIRECTORY + entry.id,
                    'parent': parentId,
                    'text': entry.title,
                    'type': type
                });
            });

            resNodes.forEach(function (node) {
                treeArray.push({
                    'id': DIRECTORY + NODE + node.id,
                    'parent': DIRECTORY + node.directoryId,
                    'text': node.title,
                    'type': 'file'
                });
            });
            drawDirectory(treeArray, rootid);
        },
        
        //draw new node after create
        drawNewNode: function (response) {
            var ref = $('#jstree').jstree(true), sel = ref.get_selected();
            sel = sel[0];
            sel = ref.create_node(sel, {
                'type': 'file',
                'id': DIRECTORY + NODE
                + response.id,
                'text': 'new '+scope.DescriptionService.getNodeDescription()
            });
            if (sel) {
                ref.edit(sel);
            }
        },

        //draw new directory after create
        drawNewDirectory: function(response){
            var sel;
            var ref = $('#jstree').jstree(true);
            sel = ref.get_selected();
            sel = sel[0];
            sel = ref.create_node(sel, {
                'type': 'default',
                'text': 'new '+scope.DescriptionService.getDirectoryDescription(),
                'id': DIRECTORY + response.id
            });
            if (sel) {
                ref.edit(sel);
            }
        },

        //set id of copied elements
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
