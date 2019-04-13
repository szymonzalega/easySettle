(function() {
    'use strict';
    angular
        .module('easySettleApp')
        .factory('Members', Members);

    Members.$inject = ['$resource'];

    function Members ($resource) {
        var resourceUrl =  'api/members/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
