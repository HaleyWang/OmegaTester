var app = angular.module('TodoApp', [ 'angularTreeview' ]);



app.filter('reqHistoryFilter', function () {
    return function (items, queryStr) {
        if(!queryStr) {
            return items;
        }
        var newItems = [];
        var qStr = queryStr.replace(/\s+/,  ".+");
        var patt =new RegExp(qStr,"i");
        for (var i = 0; i < items.length; i++) {
            var item = items[i];
            if (patt.test(item.req.name) || patt.test(item.req.url)) {
                newItems.push(item);
            }
        };

        return newItems;
    }
});

app.controller('TodoController', function($rootScope, $scope, $http) {
	
	$scope.fectSettingList =  function() {
		$http.get("/v1/setting/list?t=" + (new Date().getTime())).success(function(res) {
		    console.log("fectSettingList", res);

		    $scope.settingList = res.data;
		    $scope.fectchBatchs();
		});
	};
	$scope.fectSettingList();

	$scope.fectchAccountInfo = function() {

		$http({
			method : 'GET',
			url : '/v1/account/info'
		}).success(function(res) {
			console.log(res);
			if (res.data.accountId) {
				// $scope.fectchReqList();
				$scope.currentAccount = res.data;
			}
		})
	};
	$scope.fectchAccountInfo();

	$scope.fectchGroups = function() {

		$http({
			method : 'GET',
			url : '/v1/req/groupList'
		}).success(function(res) {
			console.log(res);
				// $scope.fectchReqList();
				$scope.groupList = res.data;
		})
	};
	$scope.fectchGroups();

	$scope.newBatchObj = {
		reqGroup : {
			groupId : 33
		}
	};
	$scope.batchAdd = function() {

		$http({
			method : 'POST',
			url : '/v1/batch/add',
			data : $scope.newBatchObj,
		}).success(function(res) {
			console.log(res);
			$scope.fectchBatchs();


		})
	};


	$scope.fectchBatchs = function() {

		$http({
			method : 'GET',
			url : '/v1/batch/list'
		}).success(function(res) {
			console.log(res);
			// $scope.fectchReqList();
			var batchList = res.data;
			var activeBatchId = localStorage.activeBatchId;
            if(!activeBatchId) {
                activeBatchId = 0;;
            }
            activeBatchId = Number(activeBatchId);
            var activeBatchIdx = 0;
			for(var i in batchList) {
				var item = batchList[i];
				if(!item.name) {
					item.name = item.reqGroup.name;
				}
                if(item.batchId == activeBatchId) {
                    item.active = "active";
                    activeBatchIdx = i;
                }else {
                    item.active = "";
                }
				//$scope.settingList
				item.envSetting = _.filter($scope.settingList, function(o){ return o.id == item.envSettingId; });
			}

			$scope.batchList = batchList;
			console.log("batchList", $scope.batchList);

			$scope.onClickBatchItem($scope.batchList[activeBatchIdx]);
		})
	};

	$scope.batchDetail = function(batch) {


	}

	$scope.batchDelete = function(batch) {

		$http({
			method : 'POST',
			url : '/v1/batch/delete',
			data : "batchId=" + batch.batchId,
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		}).success(function(res) {
			console.log(res);
			$scope.fectchBatchs();

		})
	};
	
	$scope.batchUpdate = function(batch) {

		$http({
			method : 'POST',
			url : '/v1/batch/update',
			data : batch,
			headers : {
				'Content-Type' : 'application/json'
			}
		}).success(function(res) {
			console.log(res);
			$scope.fectchBatchs();

		})
	};
	
	

	$scope.batchOrHistory = true;
	$scope.onClickHistoryTab = function() {
		$scope.batchOrHistory = false;

	};
	$scope.onClickBatchTab = function() {
		$scope.batchOrHistory = true;

	};
	
	$scope.onClickBatchItem = function(batch) {
		console.log(batch);
		if(!batch) {
			return;
		}
		localStorage.activeBatchId = batch.batchId;
		
		$http({
			method : 'GET',
			url : '/v1/batchHistory/list?batchId=' + batch.batchId
		}).success(function(res) {
			$scope.batchHistoryList = res.data;
			console.log(res);
			
			$scope.onClickbatchHistoryTotalNum($scope.batchHistoryList[$scope.batchHistoryList.length-1]);
		})

	};
	
	$scope.onClickbatchHistoryTotalNum = function(batchHistoryItem ) {
		if(!batchHistoryItem) {
			return;
		}
		var batchHistoryId = batchHistoryItem.batchHistoryId;
		console.log(batchHistoryId);
		
		
		$http.get("/v1/req/history/list?batchHistoryId=" + batchHistoryId).success(function(res) {
			console.log(res);
			$scope.reqHistoryList = res.data;
		});

	};
	
	$scope.onClickbatchHistoryErrorNum = function(batchHistoryItem ) {
		if(!batchHistoryItem) {
			return;
		}
		var batchHistoryId = batchHistoryItem.batchHistoryId;
		console.log(batchHistoryId);
		
		
		$http.get("/v1/req/history/list?batchHistoryId=" + batchHistoryId).success(function(res) {
			console.log(res);
			var result = res.data;
			var errorResults = [];
			if(result != null && result.length > 0) {
				for(var i = 0, n = result.length; i < n; i++) {
					if(!result[i].testSuccess) {
						errorResults.push(result[i]);
					}
				}
			}
			
			$scope.reqHistoryList = errorResults;
		});

	};



	$scope.fectReqHistoryDetail =  function(reqHistory) {
	    var id = reqHistory.historyId;
	    reqHistory.req.showDetail = !reqHistory.req.showDetail;
        $http.get("/v1/req/history/detail?id=" +id).success(function(res) {
            console.log(res);

            reqHistory.req.meta = res.data.req.meta;
			var resContent = JSON.parse(res.data.reqTaskHistoryMeta.content);
			resContent.body = JSON.parse(resContent.body);
			resContent.headers=resContent.headers.replace(/\r\n/g,"")
			resContent.headers=resContent.headers.replace(/\n/g,"");
			resContent.headers=resContent.headers.replace(/\t/g,"");

			
			resContent.headers = JSON.parse(resContent.headers);

			res.data.reqTaskHistoryMeta.content = JSON.stringify(resContent, null, 4) ;
            reqHistory.reqTaskHistoryMeta = res.data.reqTaskHistoryMeta;

        });
    };
	

});