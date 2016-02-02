var reimburse = angular.module('reimburse', [ 'ngRoute', 'ui.bootstrap', 'resource', 'service', 'ui.grid', 'ui.grid.selection', 'ui.grid.autoResize', 'ui.grid.i18n', 'ui.grid.pagination', 'ui.grid.grouping' ]);

reimburse.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/expenseAccount', {
        templateUrl : 'view/expenseAccount.html',
        controller : 'expenseAccountCtl'
    }).when('/borrow', {
        templateUrl : 'view/borrow.html',
        controller : 'borrowCtl'
    }).when('/staff', {
        templateUrl : 'view/staff.html',
        controller : 'staffCtl'
    }).when('/dict', {
        templateUrl : 'view/dict.html',
        controller : 'dictCtl'
    }).when('/report/:type', {
        templateUrl : 'view/report.html',
        controller : 'reportCtl'
    }).otherwise({
        redirectTo : '/expenseAccount'
    });
} ]);

reimburse.filter('toDate', function() {
    return function(input) {
        var newInput = input.replace(/-/g,"/");
        var date = new Date(newInput );
        if (date) {
            return date;
        } else {
            return input;
        }
    };
});

reimburse.controller('expenseAccountCtl', function($scope, $window, ExpenseAccount, $rootScope, uiGridConstants, dialog, loading, utils) {
    $rootScope.activeMenu = 'expenseAccount';
    utils.checkLogin($rootScope).then(function(usr) {
        loading.show();
        ExpenseAccount.query({
            entity : {
                state : 1
            }
        }, function(result) {
            if (result.rows) {
                $scope.gridOptions.data = result.rows;
            } else {
                $scope.gridOptions.data = [];
            }
            loading.hide();
        });
    }, function() {
        $scope.$emit('login');
    });
    $scope.gridOptions = {
        enableSorting : true,
        enableFiltering : true,
        enableColumnMenus : false,
        enableVerticalScrollbar : uiGridConstants.scrollbars.NEVER,
        showGridFooter : true,
        minRowsToShow : 10,
        paginationPageSizes : [ 10, 15, 20 ],
        paginationPageSize : 10,
        columnDefs : [ {
            name : '录入日期',
            field : 'inputDate',
            headerCellClass : 'text-center text-primary',
            cellFilter: 'toDate | date:"yyyy-MM-dd"',
            filterCellFiltered: true,
            sortCellFiltered: true,
            cellClass : 'text-center'
        }, {
            name : '部门编号',
            field : 'deptCode',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '项目财务编号',
            field : 'financeProjCode',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '科目',
            field : 'subject',
            maxWidth : 50,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '摘要',
            field : 'summary',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '支出金额',
            field : 'amount',
            cellFilter : 'currency:""',
            headerCellClass : 'text-center text-primary',
            cellClass : 'text-right'
        }, {
            name : '支出方式',
            field : 'paymethod',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '币种',
            field : 'currencyType',
            maxWidth : 60,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '发票单位',
            field : 'toCompany',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '课题负责人',
            field : 'projManager',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '实际项目编号',
            field : 'projCode',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '报销人',
            field : 'submitter',
            maxWidth : 70,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '录入人',
            field : 'inputer',
            maxWidth : 70,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '处理结果',
            field : 'result',
            maxWidth : 80,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '备注',
            field : 'memo',
            cellTooltip : true,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '借款人',
            field : 'borrower',
            maxWidth : 70,
            visible : false,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '借款日期',
            field : 'borrowDate',
            cellFilter : 'date',
            visible : false,
            headerCellClass : 'text-center text-primary',
            cellClass : 'text-center'
        }, {
            name : '状态',
            field : 'state',
            maxWidth : 50,
            visible : false,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '操作',
            maxWidth : 50,
            headerCellClass : 'text-center text-primary',
            enableFiltering : false,
            cellTemplate : '<div class="text-center"><a href="javascript:void(0);" ng-click=" grid.appScope.modify(row.entity)"><span class="glyphicon glyphicon-pencil text-success" ></span></a> <a href="javascript:void(0);" ng-click=" grid.appScope.del(row.entity)"><span class="glyphicon glyphicon-remove text-danger"></span></a></div>'
        } ]
    };
    // 引入常量
    $scope.constExpenseAccountStatus = utils.constExpenseAccountStatus;
    
    $scope.addNew = function() {
        dialog.showEditor({
            type : utils.constExpenseAccountStatus.REIMBURSE,
            entity : {
                payMethod : $rootScope.dict.paymethod[0].value,
                state : utils.constExpenseAccountStatus.REIMBURSE,
                accountSet : $rootScope.dict.accountSet[0].value
            },
            yes : function(entity) {
                ExpenseAccount.save(entity, function(result) {
                    if (result.success) {
                        $scope.gridOptions.data.push(result.attr.data);
                    } else {
                        $window.alert(result.msg);
                    }
                });
            }
        });
    };
    $scope.modify = function(entity) {
        // 备份修改前的状态
        $scope.modifyBack = angular.copy(entity);
        dialog.showEditor({
            type : utils.constExpenseAccountStatus.REIMBURSE,
            entity : entity,
            yes : function(entity) {
                ExpenseAccount.save(entity, function(result) {
                    if (!result.success) {
                        $window.alert(result.msg);
                     // 原记录恢复到修改前状态
                        angular.copy($scope.modifyBack, entity);
                    } else {
                        if (!entity.id) { // id不存在说明是另存
                            // 原记录恢复到修改前状态
                            angular.copy($scope.modifyBack, entity);
                            $scope.gridOptions.data.push(result.attr.data);
                        } else {
                            entity.inputer = result.attr.data.inputer;
                            entity.inputerId = result.attr.data.inputerId;
                        }
                    }

                });
            },
            no : function() {
                // 取消修改，记录恢复到修改前状态
                angular.copy($scope.modifyBack, entity);
            }
        });
    };
    $scope.del = function(entity) {
        // 备份要删除的数据
        $scope.delBack = entity;
        dialog.showPrompt({
            title : '删除确认',
            body : '确实要删除当前记录吗？',
            yes : function() {
                ExpenseAccount.del({
                    id : $scope.delBack.id
                }, function(result) {
                    if (result.success) {
                        // 从本地列表中删除数据
                        $scope.gridOptions.data.splice($scope.gridOptions.data.indexOf($scope.delBack), 1);
                    } else {
                        $window.alert(result.msg);
                    }
                });
            }
        });
    };
    $scope.toggleFilter = function() {
        // $scope.gridOptions.enableFiltering =
        // !$scope.gridOptions.enableFiltering;
    };
    // 自动调整高度
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.getTableHeight = function() {
        var rowHeight = 30; // your row height
        var headerHeight = 150; // your header height
        return {
//           height: ($scope.gridApi.core.getVisibleRows().length * rowHeight + headerHeight) + "px"
            height: ($scope.gridOptions.paginationPageSize * rowHeight + headerHeight) + "px"
        };
     };
});
reimburse.controller('borrowCtl', function($scope, $window, ExpenseAccount, $rootScope, uiGridConstants, dialog, loading, utils) {
    $rootScope.activeMenu = 'borrow';
    utils.checkLogin($rootScope).then(function(usr) {
        loading.show();
        ExpenseAccount.query({
            entity : {
                state : -1
            }
        }, function(result) {
            if (result.rows) {
                $scope.gridOptions.data = result.rows;
            } else {
                $scope.gridOptions.data = [];
            }
            loading.hide();
        });
    }, function() {
        $scope.$emit('login');
    });
    
    $scope.gridOptions = {
        enableSorting : true,
        enableFiltering : true,
        enableColumnMenus : false,
        enableVerticalScrollbar : uiGridConstants.scrollbars.NEVER,
        showGridFooter : true,
        minRowsToShow : 10,
        paginationPageSizes : [ 10, 15, 20 ],
        paginationPageSize : 10,
        columnDefs : [ {
            name : '借款日期',
            field : 'borrowDate',
            cellFilter : 'date',
            headerCellClass : 'text-center text-primary',
            cellFilter : 'toDate | date:"yyyy-MM-dd"',
            filterCellFiltered : true,
            sortCellFiltered : true,
            cellClass : 'text-center'
        }, {
            name : '部门编号',
            field : 'deptCode',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '项目财务编号',
            field : 'financeProjCode',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '摘要',
            field : 'summary',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '借款金额',
            field : 'amount',
            cellFilter : 'currency:""',
            headerCellClass : 'text-center text-primary',
            cellClass : 'text-right'
        }, {
            name : '支出方式',
            field : 'paymethod',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '币种',
            field : 'currencyType',
            maxWidth : 60,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '发票单位',
            field : 'toCompany',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '课题负责人',
            field : 'projManager',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '借款人',
            field : 'borrower',
            maxWidth : 70,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '录入人',
            field : 'inputer',
            maxWidth : 70,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '冲账日期',
            field : 'inputDate',
            headerCellClass : 'text-center text-primary',
            cellFilter : 'toDate | date: "yyyy-MM-dd" ',
            filterCellFiltered : true,
            sortCellFiltered : true,
            cellClass : 'text-center'
        }, {
            name : '备注',
            field : 'memo',
            cellTooltip : true,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '状态',
            field : 'state',
            maxWidth : 50,
            visible : false,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '操作',
            maxWidth : 70,
            headerCellClass : 'text-center text-primary',
            enableFiltering : false,
            cellTemplate : '<div class="text-center"> <a href="javascript:void(0);" ng-click=" grid.appScope.reimburse(row.entity)"><span class="glyphicon glyphicon-copy text-primary"></span></a> <a href="javascript:void(0);" ng-click=" grid.appScope.modify(row.entity)"><span class="glyphicon glyphicon-pencil text-success" ></span></a> <a href="javascript:void(0);" ng-click=" grid.appScope.del(row.entity)"><span class="glyphicon glyphicon-remove text-danger"></span></a></div>'
        }, {
            name : '实际项目编号',
            field : 'projCode',
            visible : false,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '科目',
            field : 'subject',
            maxWidth : 50,
            visible : false,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '报销人',
            field : 'submitter',
            maxWidth : 70,
            visible : false,
            headerCellClass : 'text-center text-primary'
        }, {
            name : '处理结果',
            field : 'result',
            maxWidth : 80,
            visible : false,
            headerCellClass : 'text-center text-primary'
        } ]
    };
    
    // 引入常量
    $scope.constExpenseAccountStatus = utils.constExpenseAccountStatus;
    
    $scope.addNew = function() {
        dialog.showEditor({
            type : utils.constExpenseAccountStatus.BORROW,
            entity : {
                payMethod : $rootScope.dict.paymethod[0].value,
                state : utils.constExpenseAccountStatus.BORROW
            },
            yes : function(entity) {
                ExpenseAccount.save(entity, function(result) {
                    if (result.success) {
                        $scope.gridOptions.data.push(result.attr.data);
                    } else {
                        $window.alert(result.msg);
                    }

                });
            }
        });
    };
    $scope.modify = function(entity) {
        // 备份修改前的状态
        $scope.modifyBack = angular.copy(entity);
        dialog.showEditor({
            type : utils.constExpenseAccountStatus.BORROW,
            entity : entity,
            yes : function(entity) {
                // 如果是另存为i，需要清空已报销信息
                if (!entity.id) {
                    entity.subject = null;
                    entity.projCode = null;
                    entity.submitter = null;
                }
                ExpenseAccount.save(entity, function(result) {
                    if (!result.success) {
                        $window.alert(result.msg);
                     // 原记录恢复到修改前状态
                        angular.copy($scope.modifyBack, entity);
                    } else {
                        if (!entity.id) { // id不存在说明是另存
                            // 原记录恢复到修改前状态
                            angular.copy($scope.modifyBack, entity);
                            $scope.gridOptions.data.push(result.attr.data);
                        } else {
                            entity.inputer = result.attr.data.inputer;
                            entity.inputerId = result.attr.data.inputerId;
                        }
                    }

                });
            },
            no : function() {
                // 取消修改，记录恢复到修改前状态
                angular.copy($scope.modifyBack, entity);
            }
        });
    };
    // 冲账
    $scope.reimburse = function(entity) {
        // 备份修改前的状态
        $scope.modifyBack = angular.copy(entity);
        dialog.showEditor({
            type : utils.constExpenseAccountStatus.BOTH,
            entity : entity,
            yes : function(entity) {
                entity.state = utils.constExpenseAccountStatus.BOTH;
                ExpenseAccount.save(entity, function(result) {
                    if (!result.success) {
                        $window.alert(result.msg);
                     // 原记录恢复到修改前状态
                        angular.copy($scope.modifyBack, entity);
                    } 
                });
            },
            no : function() {
                // 取消修改，记录恢复到修改前状态
                angular.copy($scope.modifyBack, entity);
            }
        });
    };
    $scope.del = function(entity) {
        // 备份要删除的数据
        $scope.delBack = entity;
        dialog.showPrompt({
            title : '删除确认',
            body : '确实要删除当前记录吗？',
            yes : function() {
                ExpenseAccount.del({
                    id : $scope.delBack.id
                }, function(result) {
                    if (result.success) {
                        // 从本地列表中删除数据
                        $scope.gridOptions.data.splice($scope.gridOptions.data.indexOf($scope.delBack), 1);
                    } else {
                        $window.alert(result.msg);
                    }
                });
            }
        });
    };
    $scope.toggleFilter = function() {
        // $scope.gridOptions.enableFiltering =
        // !$scope.gridOptions.enableFiltering;
    };
    // 自动调整高度
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.getTableHeight = function() {
        var rowHeight = 30; // your row height
        var headerHeight = 150; // your header height
        return {
//           height: ($scope.gridApi.core.getVisibleRows().length * rowHeight + headerHeight) + "px"
            height: ($scope.gridOptions.paginationPageSize * rowHeight + headerHeight) + "px"
           
        };
     };
});
reimburse.controller('staffCtl', function($scope, $window, Staff, $rootScope, $location, uiGridConstants, dialog, loading, utils) {
    $rootScope.activeMenu = 'setting';
    utils.checkLogin($rootScope).then(function(usr) {
        if (!utils.hasRole(usr, '管理员')) {
            $location.url("/expenseAccount");
            return;
        }
        loading.show();
        Staff.all(function(result) {
            if (result.rows) {
                $scope.gridOptions.data = result.rows;
            } else {
                $scope.gridOptions.data = [];
            }
            loading.hide();
        });
    }, function() {
        $scope.$emit('login');
    });
    $scope.gridOptions = {
        enableSorting : true,
        enableFiltering : true,
        enableColumnMenus : false,
        enableVerticalScrollbar : uiGridConstants.scrollbars.NEVER,
        showGridFooter : true,
        minRowsToShow : 10,
        paginationPageSizes : [ 10, 15, 20 ],
        paginationPageSize : 10,
        columnDefs : [ {
            name : '用户名',
            field : 'name',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '登录名',
            field : 'logName',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '角色',
            field : 'role',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '操作',
            maxWidth : 50,
            headerCellClass : 'text-center text-primary',
            enableFiltering : false,
            cellTemplate : '<div class="text-center"><a href="javascript:void(0);" ng-click=" grid.appScope.modify(row.entity)"><span class="glyphicon glyphicon-pencil text-success" ></span></a> <a href="javascript:void(0);" ng-click=" grid.appScope.del(row.entity)"><span class="glyphicon glyphicon-remove text-danger"></span></a></div>'
        } ]
    };
    
    $scope.addNew = function() {
        dialog.showEditor({
            templateUrl : './view/staffEditor.html',
            yes : function(entity) {
                Staff.save(entity, function(result) {
                    if (result.success) {
                        $scope.gridOptions.data.push(result.attr.data);
                    } else {
                        $window.alert(result.msg);
                    }

                });
            }
        });
    };
    $scope.modify = function(entity) {
        // 备份修改前的状态
        $scope.modifyBack = angular.copy(entity);
        dialog.showEditor({
            templateUrl : './view/staffEditor.html',
            entity : entity,
            yes : function(entity) {
                Staff.save(entity, function(result) {
                    if (!result.success) {
                        $window.alert(result.msg);
                    }
                });
            },
            no : function() {
                // 取消修改，记录恢复到修改前状态
                angular.copy($scope.modifyBack, entity);
            }
        });
    };

    $scope.del = function(entity) {
        // 备份要删除的数据
        $scope.delBack = entity;
        dialog.showPrompt({
            title : '删除确认',
            body : '确实要删除当前记录吗？',
            yes : function() {
                Staff.del({
                    id : $scope.delBack.id
                }, function(result) {
                    if (result.success) {
                        // 从本地列表中删除数据
                        $scope.gridOptions.data.splice($scope.gridOptions.data.indexOf($scope.delBack), 1);
                    } else {
                        $window.alert(result.msg);
                    }
                });
            }
        });
    };
    // 自动调整高度
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.getTableHeight = function() {
        var rowHeight = 30; // your row height
        var headerHeight = 150; // your header height
        return {
//           height: ($scope.gridApi.core.getVisibleRows().length * rowHeight + headerHeight) + "px"
            height: ($scope.gridOptions.paginationPageSize * rowHeight + headerHeight) + "px"
        };
     };
});
reimburse.controller('dictCtl', function($scope, $window, Dict, $rootScope, $location, loading, dialog, uiGridConstants, utils) {
    $rootScope.activeMenu = 'setting';
    $scope.currentDictType = 'paymethod';
    $scope.dictTypeChanged = function() {
        loading.show();
        Dict.all({
            type : $scope.currentDictType
        }, function(result) {
            if (result.rows) {
                $scope.gridOptions.data = result.rows;
            } else {
                $scope.gridOptions.data = [];
            }
            loading.hide();
        });
    };
    utils.checkLogin($rootScope).then(function(usr) {
        if (!utils.hasRole(usr, '管理员')) {
            $location.url("/expenseAccount");
            return;
        }
        $scope.dictTypeChanged();
    }, function() {
        $scope.$emit('login');
    });
    $scope.gridOptions = {
        enableSorting : true,
        enableFiltering : true,
        enableColumnMenus : false,
        enableVerticalScrollbar : uiGridConstants.scrollbars.NEVER,
        showGridFooter : true,
        minRowsToShow : 10,
        paginationPageSizes : [ 10, 15, 20 ],
        paginationPageSize : 10,
        columnDefs : [ {
            name : '类型',
            field : 'type',
            headerCellClass : 'text-center text-primary',
            visible : false
        }, {
            name : '名称',
            field : 'name',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '值',
            field : 'value',
            headerCellClass : 'text-center text-primary',
            visible : false
        }, {
            name : '排序',
            field : 'sortIndex',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '说明',
            field : 'description',
            headerCellClass : 'text-center text-primary'
        }, {
            name : '操作',
            maxWidth : 50,
            headerCellClass : 'text-center text-primary',
            enableFiltering : false,
            cellTemplate : '<div class="text-center"><a href="javascript:void(0);" ng-click=" grid.appScope.modify(row.entity)"><span class="glyphicon glyphicon-pencil text-success" ></span></a> <a href="javascript:void(0);" ng-click=" grid.appScope.del(row.entity)"><span class="glyphicon glyphicon-remove text-danger"></span></a></div>'
        } ]
    };

    $scope.addNew = function() {
        dialog.showEditor({
            templateUrl : './view/dictEditor.html',
            yes : function(entity) {
                entity.type = $scope.currentDictType;
                entity.value = entity.name;
                Dict.save(entity, function(result) {
                    if (result.success) {
                        $scope.gridOptions.data.push(result.attr.data);
                    } else {
                        $window.alert(result.msg);
                    }

                });
            }
        });
    };
    $scope.modify = function(entity) {
        // 备份修改前的状态
        $scope.modifyBack = angular.copy(entity);
        dialog.showEditor({
            templateUrl : './view/dictEditor.html',
            entity : entity,
            yes : function(entity) {
                Dict.save(entity, function(result) {
                    if (!result.success) {
                        $window.alert(result.msg);
                    }
                });
            },
            no : function() {
                // 取消修改，记录恢复到修改前状态
                angular.copy($scope.modifyBack, entity);
            }
        });
    };

    $scope.del = function(entity) {
        // 备份要删除的数据
        $scope.delBack = entity;
        dialog.showPrompt({
            title : '删除确认',
            body : '确实要删除当前记录吗？',
            yes : function() {
                Dict.del({
                    id : $scope.delBack.id
                }, function(result) {
                    if (result.success) {
                        // 从本地列表中删除数据
                        $scope.gridOptions.data.splice($scope.gridOptions.data.indexOf($scope.delBack), 1);
                    } else {
                        $window.alert(result.msg);
                    }
                });
            }
        });
    };
    // 自动调整高度
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.getTableHeight = function() {
        var rowHeight = 30; // your row height
        var headerHeight = 150; // your header height
        return {
//           height: ($scope.gridApi.core.getVisibleRows().length * rowHeight + headerHeight) + "px"
            height: ($scope.gridOptions.paginationPageSize * rowHeight + headerHeight) + "px"
        };
     };
});
reimburse.controller('reportCtl', function($scope, $window, ExpenseAccount, $rootScope, $routeParams, $filter, uiGridConstants,uiGridGroupingConstants, dialog, loading, utils) {
    $rootScope.activeMenu = 'report';
    utils.checkLogin($rootScope).then(function(usr) {
        loading.show();
        ExpenseAccount.query({
            entity : {
                state : 1
            }
        }, function(result) {
            if (result.rows) {
                $scope.gridOptions.data = $filter("orderBy")(result.rows, 'projManager', 'inputDate', 'financeProjCode');
            } else {
                $scope.gridOptions.data = [];
            }
            loading.hide();
            $scope.gridApi.grouping.aggregateColumn('支出', uiGridGroupingConstants.aggregation.SUM, '小计');
        });
    }, function() {
        $scope.$emit('login');
    });
    $scope.gridOptions = {
        enableSorting : false,
        enableFiltering : false,
        enableColumnMenus : false,
        enableVerticalScrollbar : uiGridConstants.scrollbars.NEVER,
        showGridFooter : true,
        minRowsToShow : 10,
        paginationPageSizes : [ 10, 15, 20 ],
        paginationPageSize : 10,
        columnDefs : [ {
            name : '课题负责人',
            field : 'projManager',
            headerCellClass : 'text-center text-primary',
            maxWidth : 100,
           grouping:  {
                groupPriority: 4
              }
        }, {
            name : '日期',
            field : 'inputDate',
            headerCellClass : 'text-center text-primary',
            maxWidth : 100
        }, {
            name : '项目号',
            field : 'financeProjCode',
            headerCellClass : 'text-center text-primary',
            maxWidth : 100
        }, {
            name : '摘要',
            field : 'summary',
            headerCellClass : 'text-center text-primary',
            visible : true
        }, {
            name : '支出',
            field : 'amount',
            headerCellClass : 'text-center text-primary',
            maxWidth : 200
        }, {
            name : '支出方式',
            field : 'paymethod',
            headerCellClass : 'text-center text-primary',
            maxWidth : 100
        } ]
    };
    $scope.print = function() {
        var pageSize = 35;
        var managers = [];
        var manager = null;
        angular.forEach($scope.gridOptions.data, function(data, index, array) {
            if (manager == null) {
                manager ={name: data.projManager, count : 1};
            } else if (data.projManager == manager.name) {
                manager.count++;
            } else {
                managers.push(manager);
                manager ={name: data.projManager, count : 1};                
            }
        });
        if (manager != null) {
            managers.push(manager);
        }

        LODOP.PRINT_INIT("test");
        LODOP.SET_PRINT_STYLE("FontSize",11);
        LODOP.SET_PRINT_STYLE("Bold",0);
        angular.forEach(managers, function(data, index, array) {
            LODOP.ADD_PRINT_HTM(50,0,'100%',39,"<p><h3 style='text-align:center'>报销审批单</h3></p>");
            var mn = encodeURIComponent(data.name);
            LODOP.ADD_PRINT_TABLE(88,'2%','96%',900,'URL:http://localhost:8081/Reimburse/expenseAccount/reportAuditApply?manager=' + mn);
            var i = 0;
            while(data.count >= i*pageSize) {
                LODOP.ADD_PRINT_HTM(10,0,'100%', 20,'<p style="text-align:center;font-size:11px;border-bottom:1px solid gray;padding:3px;">清华大学WMC组</p>');
                LODOP.ADD_PRINT_HTM(1060,0,'100%', 20,'<p style="text-align:center;font-size:11px;border-top:1px solid gray;padding:3px;">第' + (i+1) + '页 / 共' + Math.ceil(data.count/pageSize) + '页</p>');
                if (data.count <=( i+1)*pageSize) {
                    LODOP.ADD_PRINT_TEXT(1010,'2%',300, 20, '经办人签字：');
                    LODOP.ADD_PRINT_TEXT(1010,'37%',300, 20, '课题负责人签字：');
                    LODOP.ADD_PRINT_TEXT(1010,'72%',300, 20, '负责人签字：');
                }
                LODOP.NewPage();
                i++;
            } 

        });
        LODOP.PREVIEW();
    };
    // 自动调整高度
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };

    $scope.getTableHeight = function() {
        var rowHeight = 30; // your row height
        var headerHeight = 120; // your header height
        return {
//           height: ($scope.gridApi.core.getVisibleRows().length * rowHeight + headerHeight) + "px"
            height: ($scope.gridOptions.paginationPageSize * rowHeight + headerHeight) + "px"
        };
     };
});
reimburse.run(function($window, $rootScope, Dict, dialog, $route, Staff, utils) {
    $rootScope.dict = {};
    Dict.options({
        type : 'dictType'
    }, function(options) {
        $rootScope.dict.dictType = options;
    });
    Dict.options({
        type : 'accontSet'
    }, function(options) {
        $rootScope.dict.accountSet = options;
    });
    Dict.options({
        type : 'role'
    }, function(options) {
        $rootScope.dict.role = options;
    });
    Dict.options({
        type : 'paymethod'
    }, function(options) {
        $rootScope.dict.paymethod = options;
    });

    $rootScope.password = function() {
        dialog.showPassword({
            yes : function(entity) {

            },
            no : function() {

            }
        });
    };
    
    $rootScope.logout = function() {
        dialog.showPrompt({
            title : '登出确认',
            body : '确实要退出登录吗？',
            yes : function() {
                Staff.logout( function(result) {
                    if (result.success) {
                        $rootScope.usr = null;
//                        $route.reload();
                        $window.location.href = "./";
                    } else {
                        $window.alert(result.msg);
                    }
                });
            }
        });
    };

    $rootScope.$on('login', function() {
        // 显示登录框
        dialog.showLogin({
            yes : function(usr) {
                $rootScope.usr = usr;
                $route.reload();
            },
            no : function() {
                
            }
        });
    });
});
