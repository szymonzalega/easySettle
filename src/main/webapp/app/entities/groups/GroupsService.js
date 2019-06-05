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
                    },
                    "getOneGroup": {
                        method: 'GET', isArray: false, url: 'api/groups/:id'
                    },
                    "getGroupsWithMembers": {
                        method: 'GET', isArray: true, url: 'api/groups/getGroupsWithMembers'
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
                getOneGroup: function (id) {
                    return groupsEvents.getOneGroup({id: id});
                },
                remove: function (id) {
                    return groupsEvents.remove({id: id});
                },
                getGroupsWithMembers: function () {
                    return groupsEvents.getGroupsWithMembers();
                }
            };
        }
    ]);

