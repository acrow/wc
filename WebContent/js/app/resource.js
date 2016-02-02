angular.module('resource', [ 'ngResource' ]).config([ '$resourceProvider', '$httpProvider', function($resourceProvider, $httpProvider) {
    // Don't strip trailing slashes from calculated URLs
    // $resourceProvider.defaults.stripTrailingSlashes = false;

    /***************************************************************************
     * Fix IE8 ajax caching issue *
     **************************************************************************/
    // $httpProvider.defaults.headers.get = $httpProvider.defaults.headers.get
    // || {};
    // $httpProvider.defaults.headers.get['If-Modified-Since'] = '0';
} ]).factory('ExpenseAccount', [ '$resource', function($resource, appConfig) {
    return $resource('./expenseAccount', null, {
        'save' : {
            method : 'POST'
        },
        'del' : {
            method : 'DELETE'
        },
        'all' : {
            method : 'GET',
            url : './expenseAccount/all'
        },
        'query' : {
            method : 'GET',
            url : './expenseAccount/query'
        },
        'getOnePage' : {
            method : 'GET',
            url : 'data/activity/Search/:count/:page',
            params : {
                SearchTerm : "",
                Status : "",
                count : 5,
                page : 1
            }
        }
    });
} ]).factory('Staff', [ '$resource', function($resource, appConfig) {
    return $resource('./Staff', null, {
        'save' : {
            method : 'POST'
        },
        'del' : {
            method : 'DELETE'
        },
        'all' : {
            method : 'GET',
            url : './staff/all'
        },
        'login' : {
            method : 'GET',
            url : './staff/login'
        },
        'currentUsr' : {
            method : 'GET',
            url : './staff/currentUsr'
        },
        'changePassword' : {
            method : 'GET',
            url : './staff/changePassword'
        },
        'logout' : {
            method : 'GET',
            url : './staff/logout'
        }
    });
} ]).factory('Dict', [ '$resource', function($resource, appConfig) {
    return $resource('./Dict', null, {
        'save' : {
            method : 'POST'
        },
        'del' : {
            method : 'DELETE'
        },
        'all' : {
            method : 'GET',
            url : './dict/all'
        },
        'options' : {
            method : 'GET',
            url : './dict/options',
            isArray : true
        }
    });
} ]);