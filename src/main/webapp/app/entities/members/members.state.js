(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('members', {
            parent: 'entity',
            url: '/members',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.members.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/members/members.html',
                    controller: 'MembersController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('members');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('members-detail', {
            parent: 'members',
            url: '/members/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.members.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/members/members-detail.html',
                    controller: 'MembersDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('members');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Members', function($stateParams, Members) {
                    return Members.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'members',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('members-detail.edit', {
            parent: 'members-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/members/members-dialog.html',
                    controller: 'MembersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Members', function(Members) {
                            return Members.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('members.new', {
            parent: 'members',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/members/members-dialog.html',
                    controller: 'MembersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                balance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('members', null, { reload: 'members' });
                }, function() {
                    $state.go('members');
                });
            }]
        })
        .state('members.edit', {
            parent: 'members',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/members/members-dialog.html',
                    controller: 'MembersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Members', function(Members) {
                            return Members.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('members', null, { reload: 'members' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('members.delete', {
            parent: 'members',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/members/members-delete-dialog.html',
                    controller: 'MembersDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Members', function(Members) {
                            return Members.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('members', null, { reload: 'members' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
