//set and get Default Description set in property-file (Server) 
app.service('DescriptionService', ['$resource', function($resource) {
	var cdarDescriptions = {};
	//initialize local variables on startup of service
	var descriptionResource = $resource('../webapi/descriptions');
	descriptionResource.get().$promise.then(function(response) {
		cdarDescriptions.directory = response.directoryDescription;
		cdarDescriptions.node = response.nodeDescription;
		cdarDescriptions.subnode = response.subnodeDescription;
		cdarDescriptions.wikiurl = response.wikiUrl;
		cdarDescriptions.expandedLevel= response.expandedLevel;
		cdarDescriptions.consumerDescription = response.consumerDescription;
		cdarDescriptions.producerDescription = response.producerDescription;
	});
	
	cdarDescriptions.getDirectoryDescription = function() {
		return cdarDescriptions.directory;
	};
	cdarDescriptions.getNodeDescription = function() {
		return cdarDescriptions.node;
	};
	
	cdarDescriptions.getSubnodeDescription = function() {
		return cdarDescriptions.subnode;
	};
	
	cdarDescriptions.getWikiUrl = function() {
		return cdarDescriptions.wikiurl;
	};
	
	cdarDescriptions.getExpandedLevel = function() {
		return cdarDescriptions.expandedLevel;
	};
	
	cdarDescriptions.getProducerDescription = function() {
		return cdarDescriptions.producerDescription;
	};
	
	cdarDescriptions.getConsumerDescription = function() {
		return cdarDescriptions.consumerDescription;
	};
	
	return cdarDescriptions;
} ]);