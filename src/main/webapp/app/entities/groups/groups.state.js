(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('groups', {
            parent: 'entity',
            url: '/groups',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.groups.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/groups/groups.html',
                    controller: 'GroupsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groups');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('groups-detail', {
            parent: 'groups',
            url: '/groups/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.groups.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/groups/groups-detail.html',
                    controller: 'GroupsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groups');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Groups', function($stateParams, Groups) {
                    return Groups.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'groups',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('groups-detail.edit', {
            parent: 'groups-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groups/groups-dialog.html',
                    controller: 'GroupsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Groups', function(Groups) {
                            return Groups.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('groups.new', {
            parent: 'groups',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groups/groups-dialog.html',
                    controller: 'GroupsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('groups', null, { reload: 'groups' });
                }, function() {
                    $state.go('groups');
                });
            }]
        })
        .state('groups.edit', {
            parent: 'groups',
            url: '/{id}/edit',
            params: {
                group: null
            },
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groups/groups-dialog.html',
                    controller: 'GroupsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg'
                }).result.then(function() {
                    $state.go('groups', null, { reload: 'groups' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('groups.delete', {
            parent: 'groups',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/groups/groups-delete-dialog.html',
                    controller: 'GroupsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Groups', function(Groups) {
                            return Groups.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('groups', null, { reload: 'groups' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
