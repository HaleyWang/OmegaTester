var app = angular.module('TodoApp', [ 'angularTreeview', 'ui.ace', 'ui.tree' ]);

function log() {
    //console.log.apply(this, arguments);
}
var editors =   [];

app.controller('TodoController', function($rootScope, $scope, $http, $timeout) {

	$scope.reqTabs = [];

	$scope.groupEditObj = {};


	// key: groupId, value: if true means expanded else false.
	$scope.navCollapseRecords = {};
	
	$scope.collectionsOrHistory = true;
	$scope.onClickcollectionsOrHistory = function(flag) {
		if(flag == 'history') {
			$scope.collectionsOrHistory = false;
		}else {
			$scope.collectionsOrHistory = true;
		}
	};

	     $scope.collapseAll = function () {
            $scope.$broadcast('angular-ui-tree:collapse-all');
          };

          $scope.expandAll = function () {
            $scope.$broadcast('angular-ui-tree:expand-all');
          };

/*
	$scope.$watch('currentReqTabData.name',function(){
        log($scope.currentReqTabData.name);
    })*/

    //
    $scope.$watch('currentReqTabData.meta.request',function(){


        if(!$scope.currentReqTabData.meta) {
           return ;
        }
        log($scope.currentReqTabData.meta.request);

        var reqTextData = $scope.currentReqTabData.meta.request.trim();

        var reqData = null;
        if(reqTextData.indexOf("var") == 0) {
            reqData = eval("(function(){" + reqTextData + " ; return req;})()");
        }else {
            reqData = JSON.parse($scope.currentReqTabData.meta.request);
        }

        $scope.currentReqTabData.name = reqData.name;
		$scope.currentReqTabData.method = reqData.method;
		$scope.currentReqTabData.url = reqData.url;

		$scope.currentReqTabData.swaggerId = reqData.swaggerId;
        //description
        $scope.currentReqTabData.meta.description = reqData.description;

    });

    $scope.$watch('currentReqTabData.response',function(){

            $scope.drawBodyJson();

    });


    $scope.showAce =true;
    $scope.resTabDblclick = function() {
    var me = $scope;
         log('dblclick');
         if(me.resMaxBox == "max-box") {
             me.resMaxBox = "";
             me.showAce =true;
         }else {
             me.resMaxBox = "max-box"
             me.showAce =false;
             $timeout(function() {
                me.showAce =true
             }, 100);
         }
    };

    $scope.reqTabDblclick = function() {
         var me = $scope;
         log('dblclick');
         if(me.reqMaxBox == "max-box") {
             me.reqMaxBox = ""
             me.showAce =true;
         }else {
             me.reqMaxBox = "max-box"
             me.showAce =false;
              $timeout(function() {
                 me.showAce =true
              }, 100);
         }
    };



    $scope.drawBodyJson = function() {
        /*
        log('drawBodyJson');
        if(!$scope.currentReqTabData.response) {
            drawJSONView({});
            return ;
        }
        var jsonData = $scope.currentReqTabData.response.body;
        drawJSONView(jsonData);
        */
    }



	$scope.reqSearchText = "";
	
	function isMatch(searchText, text) {
		// new RegExp('.*By.*ron.*','i').test('Hi Byron');
		var exp = ".*" + searchText.replace(/\s+/, ".+") + ".*";
		return new RegExp(exp, 'i').test(text);
	}

	function eachReq(reqGroupItem, reqSearchText) {
		log("eachReq", reqGroupItem);
		reqGroupItem.hide = false;
		if(reqSearchText && reqGroupItem.id) {
			var show = isMatch(reqSearchText , reqGroupItem.name);
			if(!show) {
				show = isMatch(reqSearchText , reqGroupItem.url);
			}
			reqGroupItem.hide = !show;
		}
		if(reqGroupItem.groupId) {
			reqGroupItem.hide = false;
		}
		if(!reqSearchText) {
			reqGroupItem.hide = false;
		}
		var child = reqGroupItem.children;
		if(child && child.length > 0) {
			for(var i in child) {
				eachReq(child[i], reqSearchText);
			}
		}
	}

	$scope.onReqSearchTextChange = function() {
		log("onReqSearchTextChange " , $scope.reqSearchText);

		
		log("$scope.reqList " , $scope.reqList);
		var root = {groupId: -1, children: $scope.reqList}
		eachReq(root, $scope.reqSearchText);

	};

	
	
	function loadNavCollapseRecords() {
		try{
			if(localStorage.navCollapseRecords) {
				$scope.navCollapseRecords = JSON.parse(localStorage.navCollapseRecords);
			}
		}catch(e) {
		}
		if(!$scope.navCollapseRecords) {
			$scope.navCollapseRecords = {};
		}
	}
	loadNavCollapseRecords();
	
	function changeNavCollapseRecords(navCollapseRecords) {
		localStorage.navCollapseRecords = JSON.stringify(navCollapseRecords);
	}
	

	function loadReqTabs(){
		try{
			$scope.reqTabs = JSON.parse(localStorage.reqTabs);

			for ( var i in $scope.reqTabs) {
				var t = $scope.reqTabs[i];
				if(!t.tabId) {
					t.tabId = _.uniqueId("tab_");
				}

			}
			
		}catch (e) {
			log(e);
		}
	};
	loadReqTabs();
	
	function changeReqTabsStorage(reqTabs) {
		localStorage.reqTabs = JSON.stringify(reqTabs);
	}

	$scope.onReqTabsChange = function(){
		log("onReqTabsChange");
		changeReqTabsStorage($scope.reqTabs);
	};

	$scope.usageLogs = {
		reqTabs : $scope.reqTabs,
		currentRequestDataTabIndex : 0,
		currentResponseDataTabIndex : 0
	};

	$scope.currentReqTabData = {};


	$scope.loginModel = {
		email : localStorage.myEmail,
		pass : ''
	};
	$scope.registerModel = {
		name : '',
		email : '',
		pass : ''
	};

	$scope.currentAccount = {};

    $scope.searchReqByNameOrUrl = function() {


    };

    $scope.findReqByReqId = function() {


    };

	$scope.login = function() {

		$http({
			method : 'POST',
			url : '/v1/account/publicLogin',
			data : $.param($scope.loginModel),

			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(res) {
			log(res);
			if(res.data.accountId) {
			    localStorage.myEmail = res.data.email;
				// $scope.fetchReqList();
				// $scope.currentAccount = res.data.data;
				// $(".at-login-modal-sm").removeClass("in");
				window.location.reload();
			}
		})
	};
	
	$scope.fetchAccountInfo = function() {

		$http({
			method : 'GET',
			url : '/v1/account/info'
		}).success(function(res) {
			log(res);
			if(res.data && res.data.accountId) {
				$scope.currentAccount = res.data;
				$scope.fetchGroupList();
			}else {
				//pop up login
				$(".at-login-modal-sm").modal("show");
			}
        }).error(function(data, status, headersFn, config) {
            $(".at-login-modal-sm").modal("show");
        });
	};
	$scope.fetchAccountInfo();
	$scope.importTypes = [];
	$scope.importObject = {type : "cURL"};
	$scope.exportTypes = [];
	$scope.exportObject = {type : "cURL"};

	$scope.fetchImportType = function() {

    		$http({
    			method : 'GET',
    			url : '/v1/req/importType'
    		}).success(function(res) {
    		    log('fetchImportType');
    			log(res);
    			$scope.importTypes = res.data;
    		})
    	};

    $scope.fetchExportType = function() {

        		$http({
        			method : 'GET',
        			url : '/v1/req/exportType'
        		}).success(function(res) {
        		    log('fetchImportType');
        			log(res);
        			$scope.exportTypes = res.data;
        		})
        	};


	$scope.importRequest = function() {

        if($scope.importObject.codeId) {
            $scope.importObject.codeObj = _.find($scope.settingList, {id: parseInt($scope.importObject.codeId)});
            $scope.importObject.code = $scope.importObject.codeObj.content;
        }


		$http({
			method : 'POST',
			url : '/v1/req/importRequest',
			data : $scope.importObject
		}).success(function(res) {
			log(res);
			if(!$scope.currentReqTabData.meta) {
			    $scope.currentReqTabData.meta = {};
			}
			$scope.currentReqTabData.meta.request = res.data;
            console.log(123);
			$scope.formatReqData();

		})
	};
	$scope.exportRequestData = "";

	$scope.exportRequest = function() {

	        if($scope.exportObject.codeId) {
                $scope.exportObject.codeObj = _.find($scope.settingList, {id: parseInt($scope.exportObject.codeId)});
                $scope.exportObject.code = $scope.exportObject.codeObj.content;
            }

            $scope.exportObject.value = $scope.currentReqTabData.meta.request;


    		$http({
    			method : 'POST',
    			url : '/v1/req/exportRequest?type=' + $scope.exportObject.type,
    			data : $scope.exportObject
    		}).success(function(res) {
    			log(res);
    			$scope.exportRequestData = res.data;

    		})
    	};


	$scope.register = function() {

		$http({
			method : 'POST',
			url : '/v1/account/publicRegister',
			data : $.param($scope.registerModel),
			headers : {
				"Content-Type" : "application/x-www-form-urlencoded"
			}
		}).success(function(res) {
			log(res);
			if(res.data.accountId) {
			    localStorage.myEmail = res.data.email;
			    window.location.reload();
			}

		})
	};


	$scope.testClick = function() {

    		alert(1);

    }


    $scope.toggleNode = function(el, node) {

        node.collapsed = !node.collapsed;

        $scope.changeNodeCollapse(node);

    }

    $scope.changeNodeCollapse = function(node) {

        $scope.navCollapseRecords[node.groupId] = node.collapsed;

        log($scope.navCollapseRecords);

        changeNavCollapseRecords($scope.navCollapseRecords);
    }

    $scope.showReqSettings = false;
    $scope.toggleShowReqSettings = function() {

         $scope.showReqSettings  = !$scope.showReqSettings ;
    };
     $scope.showResSettings = false;
          $scope.toggleShowResSettings = function() {

               $scope.showResSettings  = !$scope.showResSettings ;
          };

    $scope.clickReqExItem = function(item) {
        if(!$scope.currentReqTabData.meta) {
            $scope.currentReqTabData.meta = {};
        }
        $scope.currentReqTabData.meta.request = item.value;

    };

    $scope.clickCaseExItem = function(item) {
            if(!$scope.currentReqTabData.meta) {
                $scope.currentReqTabData.meta = {};
            }
            $scope.currentReqTabData.meta.cases = item.value;

    };

    $scope.clickPreScriptExItem = function(item) {
            if(!$scope.currentReqTabData.meta) {
                $scope.currentReqTabData.meta = {};
            }
            $scope.currentReqTabData.meta.pre_request_script = item.value;

    };

    $scope.clickTestExItem = function(item) {
            if(!$scope.currentReqTabData.meta) {
                $scope.currentReqTabData.meta = {};
            }
            $scope.currentReqTabData.meta.test_script = item.value;

    };


	$scope.configs = {};
	$scope.levels = [1,2,3,4,5,6,7,8];
	$scope.fetchReqMethods = function() {

		$http.get("/v1/config/list").success(function(res) {
			log(res);
			$scope.reqMethods = res.data.methods;
			$scope.configs = res.data;
		});

	}

	$scope.fetchReqList = function() {

		$http.get("/v1/req/list").success(function(res) {
			log(res);
			
			var result = res.data;
			fillEachReqCollapsed({children: result}, $scope.navCollapseRecords);

			$scope.reqList = result;
		});

	};

	function openReqGroupList(reqGroupList, open) {
	    if(!reqGroupList || reqGroupList.length === 0) {
	    return
	    }
        for(var i in reqGroupList) {
            openReqGroupItem(reqGroupList[i], open);
        }


	}

	function openReqGroupItem(reqGroupItem, open) {
    	    reqGroupItem.collapsed = open;
    }

	function fillEachReqCollapsed(reqGroupItem, navCollapseRecords) {
		
		if(reqGroupItem.groupId) {
			if(typeof navCollapseRecords[reqGroupItem.groupId] === "undefined") {
				reqGroupItem.collapsed = true;
			}else {
				reqGroupItem.collapsed = navCollapseRecords[reqGroupItem.groupId];
			}
		}
		
		log("eachReq", reqGroupItem);
		
		var child = reqGroupItem.children;
		if(child && child.length > 0) {
			for(var i in child) {
				fillEachReqCollapsed(child[i], navCollapseRecords);
			}
		}
	}
	
	
	

	$scope.newReqTabModel = function() {
		log("====>");
		for ( var i in $scope.reqTabs) {
			var t = $scope.reqTabs[i];
			t.active = "";
		}
		var reqTabModel = {
			id : null,
			name : 'New Tab',
			active : 'active',
			tabId : _.uniqueId("tab_")
		};
		$scope.reqTabs.push(reqTabModel);

		$scope.onReqTabsChange();

		$scope.clickReqTab(reqTabModel);
	};
	if($scope.reqTabs == null || $scope.reqTabs.length === 0) {
		$scope.newReqTabModel();
	}

	$scope.fetchGroupList = function() {
		$http.get("/v1/req/groupList").success(function(res) {
			log(res);

			$scope.groupList = res.data;
	        $scope.fetchImportType();
            $scope.fetchExportType();
	        $scope.fetchReqMethods();

	        $scope.findReqHistory();
            $scope.fetchReqList();
            $scope.fetchSettingList();
		});
	}

	$scope.reqSave = function() {
	    changeReqTabsStorage($scope.reqTabs);

		$http({
			method : 'POST',
			url : '/v1/req/update',
			data : $scope.currentReqTabData
		}).success(function(res) {

			log(res);
			$scope.fetchReqList();
			toastr.success('Success.');
		});
	};
	
	$scope.reqSaveAs = function() {

		// var idx = $scope.getCurrentReqTabIdx();
		// $scope.currentReqTabData = res.data;
		// $scope.reqTabs[idx]

		var newReqObj = $.extend({}, $scope.currentReqTabData, $scope.newReqObj);
		$scope.addReq(newReqObj);
	};

	$scope.addReq = function(newReqObj1, callback) {

		var newReqObj = $.extend({}, newReqObj1);

		delete newReqObj.id;
		$http({
			method : 'POST',
			url : '/v1/req/add',
			data : newReqObj,
		}).success(function(res) {
			log(res);
			if(res && res.data) {
			    res.data.collapsed = false;
            	$scope.changeNodeCollapse(res.data);
			}

            $scope.fetchGroupList();
			$scope.fetchReqList();

			callback && callback(res);

			toastr.success('Success.');

		})
	};
	
	function removeItem(children, i, node) {
		var reqGroupItem = children[i];
			log("eachReq", reqGroupItem);
			if(reqGroupItem.id && reqGroupItem.id == node.id) {
				children.splice(i, 1);
			}
			if(reqGroupItem.groupId == node.id && reqGroupItem.groupId == node.groupId
			    && reqGroupItem.children.length == 0
			) {
				children.splice(i, 1);
			}

			var subChildren = reqGroupItem.children;
			if(subChildren && subChildren.length > 0) {
				for(var k in subChildren) {
					removeItem(subChildren, k, node);
				}
			}
	}


	$scope.reqDelete = function(node) {
		log("===", node);
		var url = '/v1/req/delete';
		var isReqGroup = node.group;
        if(isReqGroup) {
            url = '/v1/req/groupDelete'
        }

		$http({
			method : 'POST',
			url : url + "?id=" +  node.id,
			//data : {id: node.id},
			transformRequest: function(obj) {
		        var str = [];
		        for(var p in obj)
		        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
		        return str.join("&");
		    },
		    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function(res) {
			log(res);
			if(isReqGroup) {
			    $scope.fetchReqList();
			    return;
			}
			for(var i in $scope.reqList) {
				removeItem($scope.reqList, i, node);
			}
		})
	};

	$scope.beforeUpdateGroup = function(item) {
    		//$scope.groupEditObj
    		$scope.groupEditObj = item.node;
    		console.log(item);

    	};

	$scope.updateGroup = function() {
		//$scope.groupEditObj
		var node = $scope.groupEditObj;

		$http({
			method : 'POST',
			url : '/v1/req/groupUpdate',
			data : {groupId: node.groupId, name: $scope.groupEditObj.name},
		}).success(function(res) {
			log(res);


			$scope.fetchGroupList();
		})

	};

	$scope.reqEdit = function(node) {



		var isReqGroup = node.group;
		if(isReqGroup) {
			$scope.groupEditObj = {groupId: node.groupId, name: node.name, obj: node};
			return ;
		}


		$http({
			method : 'POST',
			url : '/v1/req/update',
			data : node,
		}).success(function(res) {
			log(res);
			toastr.success('Success.');

		})

	};
	$scope.reqHistoryList = [];
	$scope.findReqHistory = function() {
		log("findReqHistory " );
		$http.get("/v1/reqHistory/list").success(function(res) {
			log(res);
			$scope.reqHistoryList = res.data;
		});
	};

	$scope.reqDuplicate = function(node) {
		log("reqDuplicate : " , node);


		$http.get("/v1/req/detail?id=" +node.id).success(function(res) {
			log(res);
			var name = res.data.name;
			
			var copyIndex = name.lastIndexOf(" copy");
			if(copyIndex > 0) {
				name = name.substring(0 ,copyIndex);
			}
			
			res.data.name = _.uniqueId(name + " copy");

			$scope.addReq(res.data, function() {

			});

		});


	};
	
	// SelectNodeHead
	$scope.mytreeSelectNodeHead = function(node) {
		$scope.navCollapseRecords[node.groupId] = node.collapsed;
		
		log($scope.navCollapseRecords);
		log($scope.reqList);
		
		changeNavCollapseRecords($scope.navCollapseRecords);
	};

	$scope.onClickHistoryItem = function(his) {

		var id = his.taskHistoryId;
		$scope.fectReqHistoryDetail(id, function() {
			$scope.onReqTabsChange();

		});

	};
	
	$scope.mytreeChange = function() {

		log("=======", $scope.mytree.currentNode);
		if($scope.mytree.currentNode.groupId) {
			return;
		}
		
		var isReq = !$scope.mytree.currentNode.groupId;
		if(isReq) {
			var idx = $scope.getCurrentReqTab();
		}
		var id = $scope.mytree.currentNode.id;
		$scope.fetchCurrentReqTabDetail(id, function() {
			$scope.onReqTabsChange();

		});

	};

    $scope.clickReqItem = function(node) {

        log("click", node);

        if(node.group) {
            return;
        }

        var isReq = !node.group;
        if(isReq) {
            var idx = $scope.getCurrentReqTab();
        }
        var id = node.id;
        $scope.fetchCurrentReqTabDetail(id, function() {
            $scope.onReqTabsChange();

        });

    };
	
	$scope.getCurrentReqTabIdx = function() {
		var idx = 0;
		var reqTabs = $scope.reqTabs;
		for(var i in reqTabs) {
			if(reqTabs[i].active === "active") {
				idx = i;
				break;
			}
		}
		
		
		return idx;
	};

	$scope.getCurrentReqTab = function() {
		var idx = $scope.getCurrentReqTabIdx();
		var reqTabs = $scope.reqTabs;

		log("====>idx ",$(".req-tabs .active"), idx);

		var reqTab = $scope.reqTabs[idx];
		// $scope.reqTabs[idx] = $scope.mytree.currentNode;
		// reqTab = $scope.mytree.currentNode;
		$scope.currentReqTabData = reqTab;

		return reqTab;
	};
	
	$scope.changeReqMethod = function(method) {
		$scope.currentReqTabData.method = method;
	};
	
	$scope.fectReqHistoryDetail =  function(id, callback) {
		$http.get("/v1/reqHistory/detail?id=" +id).success(function(res) {
			log(res);
			var idx = $scope.getCurrentReqTabIdx();

			$scope.currentReqTabData = res.data.req;
			
			$scope.currentReqTabData.response = JSON.parse(res.data.reqTaskHistoryMeta.content);

			var currentResponse = $scope.currentReqTabData.response ;

            var enterStrReg = new RegExp(String.fromCharCode(10),"gm") ;
            var enterStrReg1 = new RegExp('/n',"gm") ;
            var enterStrReg2 = new RegExp('/r',"gm") ;
            var tReg = new RegExp('/t',"gm") ;


			currentResponse.headers = $scope.formatJsonCode (currentResponse.headers);
			currentResponse.body = $scope.formatJsonCode (currentResponse.body);


			if(res.data.reqTaskHistoryMeta.testReport) {
			    var testResult = JSON.parse(res.data.reqTaskHistoryMeta.testReport);
                var testResultArr = _.map(testResult, function(val, key){
                    var   success = _.isBoolean(val) ? val : false;
                    var res = {i: key, v: val, success: success};
                    return res;
                });

                var testErrorCount = _.filter(testResultArr, function(o){ return !o.success; }).length;
                var testError = !!testErrorCount;

                currentResponse.testResult = testResult;
                currentResponse.testResultArr = testResultArr;
                currentResponse.testErrorCount = testErrorCount;
                currentResponse.testError = testError;
			}


			$scope.reqTabs[idx] = res.data.req;
			$scope.reqTabs[idx].active = "active";
			
			if(!$scope.reqTabs[idx].tabId) {
				$scope.reqTabs[idx].tabId = _.uniqueId("tab_");

			}

			callback && callback(res.data.req);

		});
	};
	
	$scope.fetchCurrentReqTabDetail = function(id, callback) {

	    if(!id) {
	        callback && callback(null);
	        return;
	    }

		$http.get("/v1/req/detail?id=" +id).success(function(res) {
			log(res);
			var idx = $scope.getCurrentReqTabIdx();

			$scope.currentReqTabData = res.data;

			$scope.reqTabs[idx] = res.data;
			$scope.reqTabs[idx].active = "active";
			
			if(!$scope.reqTabs[idx].tabId) {
				$scope.reqTabs[idx].tabId = _.uniqueId("tab_");

			}

			callback && callback(res.data);

		});

	};
	
	function beautifyCode(js_source) {
	    if(!js_source) {
	        return js_source;
	    }
		var tabsize = null;
		var tabchar = null;
		var c = "";
        if (js_source && js_source.charAt(0) === '<') {
            // document.getElementById('result').value = style_html(js_source,
			// tabsize, tabchar, 80);
            c = style_html(js_source, tabsize, tabchar, 80);
        } else {
            // document.getElementById('result').value = js_beautify(js_source,
			// tabsize, tabchar);
            c = js_beautify(js_source, tabsize, tabchar);
        }
        return c;
	}
	
	$scope.sendCurrentReq = function(node) {
	    var currentReqTabData = $scope.currentReqTabData;
		currentReqTabData.loading = true;
		currentReqTabData.response = {};
		currentReqTabData.response.body = "loading";
		currentReqTabData.responseMeta = {};
		$http({
			method : 'POST',
			url : '/v1/req/send',
			data : currentReqTabData
		}).success(function(res) {
		    log(' success --- ');
			if(!res || !res.data) {
				currentReqTabData.loading = false;
				currentReqTabData.response = "";

				return;
			}

            res.data.headers = beautifyCode(res.data.headers);

            res.data.body = beautifyCode(res.data.body);

            if(res.data.testResult) {
                res.data.testResult = JSON.parse(res.data.testResult);

                res.data.testResultArr = _.map(res.data.testResult, function(val, key){
                    var   success = _.isBoolean(val) ? val : false;
                    var res = {i: key, v: val, success: success};
                    return res;
                });

                res.data.testErrorCount = _.filter(res.data.testResultArr, function(o){ return !o.success; }).length;
                res.data.testError = !!res.data.testErrorCount;
            }

            //res.data.testResult =
            
			currentReqTabData.response = res.data;
			currentReqTabData.responseMeta = res.meta;
			// $scope.currentReqTabData.response.body = "loading";

			log(res);

        }).error(function(data, status, headersFn, config) {
            log(' error ---');

            if(data && data.meta) {
                currentReqTabData.response .body = "_Ω_:" + data.meta.msg;
            }
            currentReqTabData.response .status = status;

			currentReqTabData.responseMeta = {response_status: status};

			toastr.error('Error.');

			
		}).finally(function() {
		    log(' finally ---');
			currentReqTabData.loading = false;
			$scope.findReqHistory();
		})
	};


	$scope.logout = function() {
		$http({
			method : 'GET',
			url : '/v1/account/publicLogout'
		}).success(function(res) {
			log(res);
			window.location.reload();
		})
	};

	$scope.clickReqTab = function(tab) {
	    if(!tab) {
	        return;
	    }
		for ( var i in $scope.reqTabs) {
			var t = $scope.reqTabs[i];
			t.active = "";
		}
		tab.active = "active";

		$scope.onReqTabsChange();

		var reqTab = $scope.getCurrentReqTab();
		log("clickReqTab", reqTab);

		if(!reqTab.meta || !reqTab.meta.request) {
			$scope.fetchCurrentReqTabDetail(reqTab.id);
		}
	};
	
	$scope.clickReqTab($scope.getCurrentReqTab());

	$scope.removeReqTabs = function() {

        log("removeReqTabs");

        $scope.reqTabs = [];

        if($scope.reqTabs.length == 0) {
            $scope.newReqTabModel();
        }

        $scope.clickReqTab($scope.reqTabs[activeId]);
    };

	$scope.removeReqTab = function(tab) {

		log("removeReqTab");

		var removeIdx = null;
		var activeId = null;
		var  n = $scope.reqTabs.length;
		for(var  i in $scope.reqTabs) {
			if($scope.reqTabs[i].tabId == tab.tabId) {
				removeIdx = i;
				break;
			}
		}

		var reqTabs = _.filter($scope.reqTabs, function(reqTab){
			return reqTab.tabId != tab.tabId ;

			});

		log(reqTabs);

		$scope.reqTabs = reqTabs;

		if(removeIdx) {
			if(removeIdx == 0) {
				activeId = 1;
			}else {
				activeId = removeIdx -1;
			}
		}

		if($scope.reqTabs.length == 0) {
		    $scope.newReqTabModel();
		}

		$scope.clickReqTab($scope.reqTabs[activeId]);
	};

	$scope.formatReqData = function() {
		var idx =  $(".req-data-tab .active").data("idx");
        if(idx == 0) {
                //ajax
            $http({
                    method : 'POST',
                    url : '/v1/req/format',
                    data: $scope.currentReqTabData.meta.request,
                    headers : {
                    				"Content-Type" : "text/plain"
                    			}
                }).success(function(res) {

                    $scope.currentReqTabData.meta.request = beautifyCode(res.data);


                    changeReqTabsStorage($scope.reqTabs);
                });

        }

	};

    $scope.folderClose = false;
    $scope.doFolderOpen = function() {

        $scope.folderClose = false;
        log("expandAll");
        openReqGroupList($scope.reqList, true);

        $scope.expandAll();

    };
    $scope.doFolderClose = function() {
        $scope.folderClose = true;
        log("collapseAll");
        openReqGroupList($scope.reqList, false);
        $scope.collapseAll();

    };


	$scope.fetchSettingList =  function() {
		$http.get("/v1/setting/list?t=" + (new Date().getTime())).success(function(res) {
		    log("fetchSettingList", res);

		    $scope.settingList = res.data;
		});
	};


	$scope.settingView = "list";
	$scope.editEnv = {};
	$scope.currentSettingType = "ENV";

    $scope.changeCurrentSettingType =  function(settingType) {
        $scope.currentSettingType = settingType;
    }


	$scope.saveEnvSetting =  function(setting) {
        if(!setting.name) {
            return;
        }

	    setting.type= $scope.currentSettingType;
        $http({
            method : 'POST',
            url : '/v1/setting/save',
            data : setting,
        }).success(function(res) {
            log(res);

            $scope.fetchSettingList();
            toastr.success('Success.');

        });
    };

    $scope.gotoEditEnvView =  function(envSetting) {
        log("gotoEditEnvView");
        $scope.settingView = "save";
        $scope.editEnv = envSetting;

    };

    $scope.gotoAddEnvView =  function() {
        log("gotoAddEnvView");
        $scope.settingView = "save";
        $scope.editEnv = {};

    };

    $scope.gotoEnvListView =  function() {
        log("gotoEnvListView");
        $scope.settingView = "list";
        $scope.editEnv = {};
    };

    $scope.duplicateEnv =  function(envSetting) {
        log("duplicateEnv");
        var newEnv = $.extend(true, {}, envSetting);
        newEnv.current = 0;
        newEnv.name = newEnv.name + " copy";
        delete newEnv.id;
        $scope.editEnv = newEnv;
        var setting = $scope.editEnv;

        var setting = $scope.editEnv;

        $scope.saveEnvSetting(setting);
    };

    $scope.deleteEnv =  function(envSetting) {
        log("deleteEnv");

        $http({
                    method : 'POST',
                    url : '/v1/setting/delete?id=' + envSetting.id
                }).success(function(res) {
                    log(res);

                    $scope.fetchSettingList();
                });

    };

    $scope.changeEnv =  function(envSetting) {
        log("changeEnv");
        if($scope.settingList) {
            for(var  i = 0, n= $scope.settingList.length; i < n ; i++) {

                if($scope.settingList[i].current != 0) {
                    $scope.settingList[i].current = 0;
                    $scope.saveEnvSetting($scope.settingList[i]);
                }
            }
        }
        envSetting.current = 1;
        $scope.saveEnvSetting(envSetting);
    };




    $scope.saveEnv =  function() {
        log("saveEnv");

        var setting = $scope.editEnv;

        $scope.saveEnvSetting(setting);

    };

    $scope.aceLoaded = function(_editor) {
         //log(' ============> aceLoaded ');
         //var _session = _editor.getSession();
         //var _renderer = _editor.renderer;

         //$scope.formatCode(_session);
         log(_editor);
		_editor.$blockScrolling = 'Infinity';
         editors.push(_editor);
     };

    $scope.formatJsonCode = function(jsonCode) {
           log(' ============> formatCode1 ');
          try {

              return JSON.stringify(JSON.parse(jsonCode), null, '\t');
          } catch (e) {
              log('something wrong with your json.');
              return jsonCode; //show message that json is malformed
          }
    };




		        $scope.treeOptions = {
                    accept: function(sourceNodeScope, destNodesScope, destIndex) {
                        log("accept-->1", sourceNodeScope, destIndex);
                        log("accept-->2", destNodesScope);


                        var sourceValue = sourceNodeScope.$modelValue.value;
                        if(!destNodesScope || !destNodesScope.$nodesScope) {
                            return true;
                        }
                        var destValue = destNodesScope.$nodesScope.$modelValue ? destNodesScope.$nodesScope.$modelValue.url : null;
                        log("accept-->3", destNodesScope.$nodesScope);

                        return destValue ? false: true;
                    },

            beforeDrop : function (e) {
              var sourceValue = e.source.nodeScope.$modelValue.value,
                destValue = e.dest.nodesScope.node ? e.dest.nodesScope.node.value : undefined,
                modalInstance;

                if(e.dest.nodesScope.node) {
                    log("accept-->4", e.dest.nodesScope.node);
                    if(!e.dest.nodesScope.node.groupId) {
                        return false;
                    }
                }



              // display modal if the node is being dropped into a smaller container
              if (sourceValue > destValue) {
                modalInstance = $modal.open({
                  templateUrl: 'drop-modal.html'
                });
                // or return the simple boolean result from $modal
                if (!e.source.nodeScope.$treeScope.usePromise) {
                  return modalInstance.result;
                } else { // return a promise
                  return modalInstance.result.then(function (allowDrop) {
                    if (!allowDrop) {
                      return $q.reject();
                    }
                    return allowDrop;
                  });
                }
              }
            }
                  };



		  $scope.visible = function (item) {
        return !($scope.query && $scope.query.length > 0
        && item.name.indexOf($scope.query) == -1);

      };





	  $scope.visibleGroup = function (item) {
        return true; //todo

      };

      $scope.changeGroupName = function (item) {
              return true; //todo

            };

      $scope.foldAtDepth = function(depth) {
           var editor = editors[4];
            editor.session.unfold()

           if(depth > 0) {
           foldAtDepth(editor, depth);

           }


      }


});


/*
editor.session.foldAll()
editor.session.unfold()
*/
function foldAtDepth(editor, depth) {
    var range, fold;
    var session = editor.session;
    var indent = depth * session.getTabString().length;


        for (var row = 0; row < session.getLength(); row++) {
            if (session.foldWidgets[row] === undefined) {
                session.foldWidgets[row] = session.getFoldWidget(row);
            }


            if (session.foldWidgets[row] === "start") {
                range = session.getFoldWidgetRange(row);
                if (range !== null && session.getLine(row).search(/\S/) === indent) {


                    fold = session.addFold("...", range);


                    row = range.end.row;
                }
            }
        }


};


function resizeEditors() {
        if(!editors) {
            return;
        }
        for(var i = 0, n = editors.length; i < n ; i++) {
            editors[i].resize();
        }

    }

	$(function() {
	    var respBox = $(".response-box .tab-content");
		Split([ '#a', '#b' ], {
			gutterSize : 8,
			sizes : [ 28, 72 ],
			minSize : [ 240, 500 ],
			cursor : 'col-resize',
			onDragStart : function() {
				respBox.hide();
				resizeEditors();
			},
			onDragEnd : function() {
				respBox.show();
				resizeEditors();

			}

		})
		Split(['#c', '#d'], {
          direction: 'vertical',
          sizes: [55, 45],
          gutterSize: 8,
          cursor: 'row-resize',
          onDragStart : function() {
                respBox.hide();
                resizeEditors();
            },
            onDragEnd : function() {
                respBox.show();
                resizeEditors();
            }
        });


tabs_unlimited();

toastr.options.positionClass = 'toast-bottom-right';




	});