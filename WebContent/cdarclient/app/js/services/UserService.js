//store data to cookie
app.factory('UserService', [ '$location', '$cookieStore',  function($location, $cookieStore) {
	return {
		getUsername : function() {
			return $cookieStore.get('cdarUsername');
		},
		
		getAccesstoken : function() {
			return $cookieStore.get('cdarAccesstoken');
		},
		
		getUserId : function() {
			return $cookieStore.get('cdarId');
		},
		
		getDrillHierarchy : function() {
			return $cookieStore.get('cdarDrillHierarchy');
		},
		
		getIsProducer : function() {
			return $cookieStore.get('cdarProducer');
		},
		
		isLoggedIn : function() {
			return this.getUserId() !== -1;
		},
		
		setUsername : function(val) {
			$cookieStore.put('cdarUsername', val);
		},
		
		setAccesstoken : function(val) {
			$cookieStore.put('cdarAccesstoken', val);
		},
		
		setUserId : function(val) {
			$cookieStore.put('cdarId', val);
		},
		
		setDrillHierarchy : function(val) {
			$cookieStore.put('cdarDrillHierarchy', val);
		},
		
		setIsProducer : function(val) {
			$cookieStore.put('cdarProducer', val);
		},
		
		checkResponseUnauthorized : function(error) {
			if (error.status === 401) {
				$location.path('/login');
			}
		},
		
		removeCookies : function() {
			CDAR.setCustomHeader('none', 'no-token');
			$cookieStore.remove('cdarUsername');
			$cookieStore.remove('cdarAccesstoken');
			$cookieStore.remove('cdarId');
			$cookieStore.remove('cdarProducer');
		},
		
		redirectUrl : function() {
			if (this.getIsProducer() == 'true') {
				this.setIsProducer('false');
				$location.path('/homeconsumer');
			} else {
				this.setIsProducer('true');
				$location.path('/homeproducer');
			}
		}
	};
} ]);