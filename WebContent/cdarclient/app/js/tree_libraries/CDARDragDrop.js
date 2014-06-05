var CDARDragDrop = (function () {
    var DIRECTORY = 'directory';
    var NODE = 'node';
    var movedNode = null;
    var scope = angular.element(document.getElementById('wrapper')).scope();
    var mouseOverFlag = false;
    var eleme = $('#jsplumb-container');

    //private Methods
    eleme.mouseover(function () {
        mouseOverFlag = true;
    }).mouseout(function () {
            mouseOverFlag = false;
        });


    function getMovedNode() {
        return movedNode;
    }

    function isMouseOverContainer() {
        return mouseOverFlag;
    }

    //Listener start drag and drop
    $(document).bind('dnd_start.vakata', function (e, data) {
        var id = data.data.nodes[0];
        var type = data.data.origin._model.data[id].type;
        if (type !== 'default' && type !== 'root') {
            id = id.replace(NODE, '');
            id = id.replace(DIRECTORY, '');
            scope.getNode(id);
        }
    });
    
    //Listener stop drag and drop
    // Drop Node in Graph
    $(document).bind(
        'dnd_stop.vakata',
        function (e, data) {
            var id = data.data.nodes[0];
            var type = data.data.origin._model.data[id].type;
            if (getMovedNode() !== null) {
                var movedNode = getMovedNode();
                if (isMouseOverContainer() && type !== 'default' && type !== 'root' && movedNode.dynamicTreeFlag !== 1) {
                    id = id.replace(NODE, '');
                    id = id.replace(DIRECTORY, '');
                    scope.dropNode(data.event, id);
                }
                CDARDragDrop.setMovedNode(null);
            }
        });

    //Listener while move drag and drop
    //Check if Node is already dropped in Graph
    $(document).bind(
        'dnd_move.vakata',
        function (e, data) {
            var nodeId = data.data.nodes[0];
            var type = data.data.origin._model.data[nodeId].type;
            if (getMovedNode() !== null) {
                var movedNode = getMovedNode();
                if (isMouseOverContainer() && type !== 'default' && type !== 'root' && movedNode.dynamicTreeFlag !== 1) {
                    data.helper.find('.jstree-icon:eq(0)').removeClass('jstree-er').addClass('jstree-ok');
                    return;
                }
            }
        });

    //Listener move Node / Folder inside jsTree
    //Update parentId
    $('#jstree').on('move_node.jstree', function (e, data) {
        var id = data.node.id;
        var type = data.node.type;

        var parentId = data.parent.replace(DIRECTORY, '');
        id = id.replace(DIRECTORY, '');
        if (type !== 'default') {
            id = id.replace(NODE, '');
            scope.moveNode(id, parentId);
        } else {
            if (parentId === '#') {
                parentId = id;
            }
            scope.moveDirectory(id, parentId);
        }
    });

    //public Methods
    return{
        setMovedNode: function (node) {
            movedNode = node;
        }
    };
})();








