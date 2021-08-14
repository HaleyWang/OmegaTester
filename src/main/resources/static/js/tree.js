/*
	@license Angular Treeview version 0.1.6
	ⓒ 2013 AHN JAE-HA http://github.com/eu81273/angular.treeview
	License: MIT


	[TREE attribute]
	angular-treeview: the treeview directive
	tree-id : each tree's unique id.
	tree-model : the tree model on $scope.
	node-id : each node's id
	node-label : each node's label
	node-children: each node's children

	<div
		data-angular-treeview="true"
		data-tree-id="tree"
		data-tree-model="roleList"
		data-node-id="roleId"
		data-node-label="roleName"
		data-node-children="children" >
	</div>
*/

(function ( angular ) {
	'use strict';

	angular.module( 'angularTreeview', [] ).directive( 'treeModel', ['$compile', function( $compile ) {
		return {
			restrict: 'A',
			link: function ( scope, element, attrs ) {
				//tree id
				var treeId = attrs.treeId;
			
				//tree model
				var treeModel = attrs.treeModel;

				//node id
				var nodeId = attrs.nodeId || 'id';

				//node label
				var nodeLabel = attrs.nodeLabel || 'label';

				//children
				var nodeChildren = attrs.nodeChildren || 'children';

				
				//tree template
				var template =
					'<ul class="tree-ul">' +
						'<li class="tree-li group-{{!!node.groupId}} {{node.selected}} children-{{node.' + nodeChildren + '.length}}" ng-hide="node.hide" data-ng-repeat="node in ' + treeModel + '">' +
							'<i class="collapsed glyphicon glyphicon-folder-close" data-ng-show="node.groupId && node.collapsed" data-ng-click="' + treeId + '.selectNodeHead(node)"></i>' +
							'<i class="expanded glyphicon glyphicon-folder-open {{node.selected}}" data-ng-show="node.groupId && !node.collapsed" data-ng-click="' + treeId + '.selectNodeHead(node)"></i>' +
							'<span data-ng-click="' + treeId + '.selectNodeLabel(node)" class="normal label label-method-{{node.method}}" data-ng-hide="node.' + nodeChildren + '.length">{{node.method}}</span> ' +
							'<span data-ng-class="node.selected" class="req-name center" data-ng-click="' + treeId + '.selectNodeLabel(node)" title="{{node.' + nodeLabel + '}}">{{node.' + nodeLabel + '}}</span>' +
							'<div data-ng-hide="node.collapsed" data-tree-id="' + treeId + '" data-tree-model="node.' + nodeChildren + '" data-node-id=' + nodeId + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + '></div>' +
						
							
							'<div class="btn-group dropright ">' +
							  '<a type="button" class="" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
							  '<span class="glyphicon glyphicon-option-horizontal" aria-hidden="true"></span></a>' +
							  '<ul class="dropdown-menu dropdown-menu-right">' +
							    '<li class="li-edit"><a data-ng-click="reqEdit(node)" data-toggle="modal" data-target=".at-group-title" data-dismiss="modal" aria-label="Close" href="#"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit</a></li>' +
							    '<li class="li-duplicate"><a data-ng-click="reqDuplicate(node)"  href="#"><span class="glyphicon glyphicon-duplicate" aria-hidden="true"></span> Dupicate</a></li>' +
							    '<li class="li-delete"><a data-ng-click="reqDelete(node)" href="#"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</a></li>' +
							  '</ul>' +
							'</div>' +
							
							
							
							
						'</li>' +
					'</ul>';


				//check tree id, tree model
				if( treeId && treeModel ) {

					//root node
					if( attrs.angularTreeview ) {
					
						//create tree object if not exists
						scope[treeId] = scope[treeId] || {};

						//if node head clicks,
						scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function( selectedNode ){

							//Collapse or Expand
							selectedNode.collapsed = !selectedNode.collapsed;
							
							var changeFun = scope[treeId+ "SelectNodeHead"];
							if(changeFun) {
								changeFun(selectedNode);
							}
						};

						//if node label clicks,
						scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function( selectedNode ){

							//remove highlight from previous node
							if( scope[treeId].currentNode && scope[treeId].currentNode.selected ) {
								scope[treeId].currentNode.selected = undefined;
							}

							//set highlight to selected node
							selectedNode.selected = 'selected';

							//set currentNode
							scope[treeId].currentNode = selectedNode;
							
							var changeFun = scope[treeId+ "Change"];
							if(changeFun) {
								changeFun(selectedNode);
							}

						};
					}

					//Rendering template.
					element.html('').append( $compile( template )( scope ) );
				}
			}
		};
	}]);
})( angular );