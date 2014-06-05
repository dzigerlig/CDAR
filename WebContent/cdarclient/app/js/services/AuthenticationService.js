//authentication restcalls
app.factory('AuthenticationService', [ '$log', '$resource', '$location', 'UserService', 
                                       function($log, $resource, $location, UserService) {
	return $resource('../webapi/users/:userid/:action', {}, {
		'addUser' : {
			method : 'POST'
		},
		
		'updateUser' : {
			method : 'POST'
		},
		
		'loginUser':{
			method: 'GET',
			params:{
				action: 'login'
			}
		},
		
		'logoutUser':function(){
			UserService.removeCookies();
			$location.path('/login'); 
		}
	}				
	);
} ]);