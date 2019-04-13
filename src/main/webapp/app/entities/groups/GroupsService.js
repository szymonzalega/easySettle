angular.module('easySettleApp').factory('GroupsService',
    ['$resource',
        function ($resource) {
            var groupsEvents = $resource('api/groups/',
                {},
                {
                    "get": {
                        method: 'GET', isArray: true
                    },
                    "create": {
                        method: 'POST', isArray: false
                    },
                    "update": {
                        method: 'PUT', isArray: false
                    },
                    "remove": {
                        method: 'DELETE', isArray: false, url: 'api/groups/:id'
                    }
                });

            return {
                get: function () {
                    return groupsEvents.get();
                },
                create: function (task) {
                    return groupsEvents.create(task);
                },
                update: function (task) {
                    return groupsEvents.update(task);
                },
                remove: function (id) {
                    return groupsEvents.remove({id: id});
                }
            };
        }
    ]);

