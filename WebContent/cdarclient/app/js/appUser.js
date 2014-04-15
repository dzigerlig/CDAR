app.controller("LoginController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$.removeCookie('cdar');
	UserService.user = '';
	
	$scope.chkbKnowledgeProducer;

	$scope.credentials = {
		username : '',
		password : ''
	};
	
	$scope.login = function() {
		AuthenticationService.login.loginuser({username:$scope.credentials.username,password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1 && response.accesstoken.length == 40) {
				response.isProducer = $scope.chkbKnowledgeProducer;
				$.cookie('cdar', response, {
					expires : 7
				});
				UserService.user = $.cookie('cdar');
				if ($scope.chkbKnowledgeProducer) {
					$location.path('/homeproducer');
				} else {
					$location.path('/homeconsumer');
				}
			} else {
				alert("wrong username/password combination");
			}
		});
	};
}]);

app.controller("RegistrationController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$.removeCookie('cdar');
	UserService.user = '';

	$scope.credentials = {
		username : "",
		password : ""
	};

	$scope.register = function() {
		AuthenticationService.addUser.post({username: $scope.credentials.username, password: md5.createHash($scope.credentials.password)}, function(response) {
			if (response.id != -1) {
				alert("User created!");
				$location.path('/login');
			} else {
				alert("User already exists!");
			}
		});
	};
}]);

app.controller("AccountController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	$scope.UserService = UserService;
	$scope.newPw = '';
	
	$scope.changePw = function() {
		console.log($scope.user);
		$scope.UserService.user.password = md5.createHash($scope.newPw);
		AuthenticationService.edit.changepw($scope.UserService.user, function(response) {
			if (response.id != -1) {
				alert("pw changed!");
				$scope.newPw = '';
			} else {
				alert("pw change failed!");
			}
		});
	};
}]);


app.controller("SortableController", ['$scope', '$location', 'AuthenticationService', 'md5', 'UserService', function($scope, $location,
		AuthenticationService, md5, UserService) {
	//http://johnny.github.io/jquery-sortable/
	var adjustment;
	$("#sortedList").sortable({
	  pullPlaceholder: false,
	  // prevent header from being moved
	  isValidTarget: function  (item, container) {
		  return false;
	  },
	  // animation on drop
	  onDrop: function  (item, targetContainer, _super) {
	    var clonedItem = $('<li/>').css({height: 0});
	    item.before(clonedItem);
	    clonedItem.animate({'height': item.height()});
	    
	    item.animate(clonedItem.position(), function  () {
	      clonedItem.detach();
	      _super(item);
	    });
	  },
	  

	  // set item relative to cursor position
	  onDragStart: function ($item, container, _super) {
		if(container.options.drop) {
	    var offset = $item.offset(),
	    pointer = container.rootGroup.pointer;

	    adjustment = {
	      left: pointer.left - offset.left,
	      top: pointer.top - offset.top
	    };

	    _super($item, container);
		}
	  },
	  onDrag: function ($item, position) {
	    $item.css({
	      left: position.left - adjustment.left,
	      top: position.top - adjustment.top
	    });
	  }
	});
	
	$("#cdarLiHeader").sortable({
		  drop: true
	});
	
	$('#cdarLiItem').sortable({
		drop: false
	});
	
}]);




