(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfers', {
            parent: 'entity',
            url: '/transfers',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.transfers.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfers/transfers.html',
                    controller: 'TransfersController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transfers');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('transfers-detail', {
            parent: 'transfers',
            url: '/transfers/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.transfers.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfers/transfers-detail.html',
                    controller: 'TransfersDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transfers');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Transfers', function($stateParams, Transfers) {
                    return Transfers.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfers',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfers-detail.edit', {
            parent: 'transfers-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfers/transfers-dialog.html',
                    controller: 'TransfersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transfers', function(Transfers) {
                            return Transfers.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfers.new', {
            parent: 'transfers',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfers/transfers-dialog.html',
                    controller: 'TransfersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transfers', null, { reload: 'transfers' });
                }, function() {
                    $state.go('transfers');
                });
            }]
        })
        .state('transfers.edit', {
            parent: 'transfers',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfers/transfers-dialog.html',
                    controller: 'TransfersDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transfers', function(Transfers) {
                            return Transfers.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfers', null, { reload: 'transfers' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfers.delete', {
            parent: 'transfers',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfers/transfers-delete-dialog.html',
                    controller: 'TransfersDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Transfers', function(Transfers) {
                            return Transfers.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfers', null, { reload: 'transfers' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
