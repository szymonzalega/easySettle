angular.module('easySettleApp').factory('MembersService',
    ['$resource',
        function ($resource) {
            var membersEvents = $resource('api/members/',
                {},
                {
                    "get": {
                        method: 'GET', isArray: true
                    },
                    "saveMembers": {
                        method: 'POST', isArray: true, url: 'api/members/saveMembers'
                    },
                    "create": {
                        method: 'POST', isArray: false
                    },
                    "update": {
                        method: 'PUT', isArray: false
                    },
                    "remove": {
                        method: 'DELETE', isArray: false, url: 'api/members/:id'
                    }
                });

            return {
                get: function () {
                    return membersEvents.get();
                },
                saveMembers: function (membersList){
                  return membersEvents.saveMembers(membersList);
                },
                create: function (task) {
                    return membersEvents.create(task);
                },
                update: function (task) {
                    return membersEvents.update(task);
                },
                remove: function (id) {
                    return membersEvents.remove({id: id});
                }
            };
        }
    ]);

