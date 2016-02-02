angular.module('service', [ 'ui.bootstrap' ])
.factory("utils", ['$q', '$log', 'Staff', function($q, $log, Staff) {
    return {
        checkLogin: function (con) {
            var deferred = $q.defer();
            Staff.currentUsr(function(result) {
                if (result.success) {
                    con.usr = result.data;
                    deferred.resolve(result.data);
                } else {
                    deferred.reject(result.msg);
                }
            });
            return deferred.promise;
        },
        hasRole: function(usr, role) {
            if (usr.role == '管理员') {
                return true;
            }
            return false;
        },
        constExpenseAccountStatus : {
                REIMBURSE : 1,
                BORROW : -1,
                BOTH : 0
            }
    };
}])
.factory("dialog", [ '$modal', '$log', 'Staff', 'utils', function($modal, $log, Staff, utils) {

    return {
        /***********************************************************************
         * Confirme popup window with 50% opacity black backgrount layer.
         * 
         * Usage example :
         * 
         * confirmPopup({ title : 'someTitle', body : 'someBody', yes :
         * function(){ console.log('Yes, i am'); console.log($scope.aa); }, no :
         * function(){ console.log('No, i am not'); } });
         **********************************************************************/
        showPrompt : function(param) {
            var templateUrl = './view/prompt.html';
            var modalInstance = $modal.open({
                templateUrl : templateUrl,
                controller : [ '$scope', '$modalInstance', 'title', 'body', function($scope, $modalInstance, title, body) {
                    $scope.title = title;
                    $scope.body = body;

                    $scope.ok = function() {
                        $modalInstance.close($scope.entity);
                    };

                    $scope.cancel = function() {
                        $modalInstance.dismiss();
                    };
                } ],
                size : 'sm',
                resolve : {
                    title : function() {
                        return param.title || '请确认';
                    },
                    body : function() {
                        return param.body || '要继续吗？';
                    }
                }
            });

            modalInstance.result.then(param.yes || function() {
            }, param.no || function() {
            }, function() {
            });

            return modalInstance;
        },
        /***********************************************************************
         * Confirme popup window with 50% opacity black backgrount layer.
         * 
         * Usage example :
         * 
         * confirmPopup({ type : 'ea', entity : {}, templateUrl:
         * './view/expenseAccountEditor.html', yes : function(){
         * console.log('Yes, i am'); console.log($scope.aa); }, no : function(){
         * console.log('No, i am not'); } });
         **********************************************************************/
        showEditor : function(param) {
            var templateUrl = './view/editor.html';
            if (param.templateUrl) {
                templateUrl = param.templateUrl;
            }
            var modalInstance = $modal.open({
                templateUrl : templateUrl,
                backdrop : 'static',
                controller : [ '$scope', '$modalInstance', 'type', 'entity', function($scope, $modalInstance, type, entity) {
                    $scope.type = type;
                    $scope.saveAs = true;
                    if (entity) {
                        $scope.entity = entity;
                    } else {
                        $scope.entity = {};
                    }

                    $scope.ok = function() {
                        $modalInstance.close($scope.entity);
                    };

                    $scope.cancel = function() {
                        $modalInstance.dismiss();
                    };

                    $scope.saveNew = function() {
                        $scope.entity.id = null;
                        $modalInstance.close($scope.entity);
                    };

                    $scope.resetPassword = function() {
                        $scope.entity.password = null;
                        $modalInstance.close($scope.entity);
                    };
                } ],
                size : 'md',
                resolve : {
                    type : function() {
                        return param.type || '';
                    },
                    entity : function() {
                        return param.entity || '';
                    }
                }
            });

            modalInstance.result.then(param.yes || function() {
            }, param.no || function() {
            }, function() {
            });

            return modalInstance;
        },
        /***********************************************************************
         * Confirme popup window with 50% opacity black backgrount layer.
         * 
         * Usage example :
         * 
         * confirmPopup({ type : 'ea', entity : {}, templateUrl:
         * './view/expenseAccountEditor.html', yes : function(){
         * console.log('Yes, i am'); console.log($scope.aa); }, no : function(){
         * console.log('No, i am not'); } });
         **********************************************************************/
        showLogin : function(param) {
            var templateUrl = './view/login.html';
            var modalInstance = $modal.open({
                templateUrl : templateUrl,
                backdrop : 'static',
                controller : ['$window', '$scope', '$modalInstance', 'Staff', function($window, $scope, $modalInstance) {
                    $scope.entity = {};
                    $scope.ok = function() {
                        Staff.login(
                                {logName: $scope.entity.logName,
                                    password: $scope.entity.password},
                                function(result) {
                                        if (result.success) {
                                            $modalInstance.close(result.data);
                                        } else {
                                            $scope.entity.errMsg = result.msg;
                                        }
                                });
                    };
                    $scope.keyup = function(e){
                        var keycode = window.event?e.keyCode:e.which;
                        if(keycode==13){
                            $scope.ok();
                        }
                    };
                    $scope.cancel = function() {
//                        $modalInstance.dismiss();
                        $window.location.href="./";
                    };
                } ],
                size : 'md'
            });

            modalInstance.result.then(param.yes || function() {
            }, param.no || function() {
            }, function() {
            });

            return modalInstance;
        },
        /***********************************************************************
         * Confirme popup window with 50% opacity black backgrount layer.
         * 
         * Usage example :
         * 
         * confirmPopup({ type : 'ea', entity : {}, templateUrl:
         * './view/expenseAccountEditor.html', yes : function(){
         * console.log('Yes, i am'); console.log($scope.aa); }, no : function(){
         * console.log('No, i am not'); } });
         **********************************************************************/
        showPassword : function(param) {
            var templateUrl = './view/password.html';
            var modalInstance = $modal.open({
                templateUrl : templateUrl,
                backdrop : 'static',
                controller : [ '$scope', '$modalInstance', 'Staff', function($scope, $modalInstance) {
                    $scope.entity = {};
                    $scope.ok = function() {
                        Staff.changePassword(
                                {oldPassword: $scope.entity.oldPassword,
                                    newPassword: $scope.entity.newPassword},
                                function(result) {
                                        if (result.success) {
                                            $modalInstance.close(result.data);
                                        } else {
                                            $scope.entity.errMsg = result.msg;
                                        }
                                });
                    };
                    $scope.cancel = function() {
                        $modalInstance.dismiss();
                    };
                } ],
                size : 'md'
            });

            modalInstance.result.then(param.yes || function() {
            }, param.no || function() {
            }, function() {
            });

            return modalInstance;
        }
    };

} ])

.factory("loading", [ '$modal', '$log', function($modal, $log) {

    /***************************************************************************
     * * The show/hide methods call be called multiple times. * The loading
     * layer will stay there when "count" is bigger than 0 * * Loading sign with
     * 50% opacity black backgrount layer.
     * 
     * Usage example: loading.show(); //show the loading layer loading.hide();
     * //hide the loading layer
     **************************************************************************/
    var Spin = function() {};
    Spin.prototype.count = 0;
    Spin.prototype.show = function() {
        this.count++;
        if (this.count === 1) {// it hasn't been started, yet.
            this.modalInstance = $modal.open({
                template : '<div class="text-center" style="color:white;"><img src="./img/loading.gif" /></div>',
                windowClass : 'loading-with-mask',
                backdrop : 'static',
                keyboard : false,
                controller : [ '$scope', '$modalInstance', function($scope, $modalInstance) {
                } ],
                size : 'sm'
            });
        }
    };
    Spin.prototype.hide = function() {
        this.count--;
        if (this.count === 0) {
            this.modalInstance.close();
        }
    };

    return new Spin();
} ]);
