(function() {
    'use strict';
    angular
        .module('easySettleApp')
        .factory('Transfers', Transfers);

    Transfers.$inject = ['$resource'];

    function Transfers ($resource) {
        var resourceUrl =  'api/transfers/:id';

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
