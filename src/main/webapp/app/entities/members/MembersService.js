angular.module('easySettleApp').factory('MembersService',
    ['$resource',
        function ($resource) {
            let membersEvents = $resource('api/members/',
                {},
                {
                    "get": {
                        method: 'GET', isArray: true
                    },
                    "saveMembers": {
                        method: 'POST', isArray: false, url: 'api/members/saveMembers'
                    },
                    "create": {
                        method: 'POST', isArray: false
                    },
                    "update": {
                        method: 'PUT', isArray: false
                    },
                    "remove": {
                        method: 'DELETE', isArray: false, url: 'api/members/:id'
                    },
                    "getMembersByGroup": {
                        method: 'GET', isArray: true, url: 'api/members/getMembersByGroup/:groupId'
                    },
                    "settleDebt": {
                        method: 'GET', isArray: true, url: 'api/members/settleDebt/:groupId'
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
                },
                getMembersByGroup: function (groupId) {
                    return membersEvents.getMembersByGroup({groupId: groupId});
                },
                settleDebt: function (groupId) {
                    return membersEvents.settleDebt({groupId: groupId})
                }
            };
        }
    ]);

