(function() {
    'use strict';

    angular
        .module('easySettleApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payments', {
            parent: 'entity',
            params: {
                id: null,
                name: null
            },
            url: '/payments/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.payments.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payments/payments.html',
                    controller: 'PaymentsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('payments');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('payments-detail', {
            parent: 'payments',
            url: '/payments/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'easySettleApp.payments.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payments/payments-detail.html',
                    controller: 'PaymentsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('payments');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Payments', function($stateParams, Payments) {
                    return Payments.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payments',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payments-detail.edit', {
            parent: 'payments-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payments/payments-dialog.html',
                    controller: 'PaymentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Payments', function(Payments) {
                            return Payments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payments.new', {
            parent: 'payments',
            url: '/new',
            params: {
                id: null,
                name: null
            },
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payments/payment-create-new.html',
                    controller: 'PaymentsAddController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('payments');
                    return $translate.refresh();
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payments',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payments.edit', {
            parent: 'payments',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payments/payments-dialog.html',
                    controller: 'PaymentsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Payments', function(Payments) {
                            return Payments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payments', null, { reload: 'payments' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payments.delete', {
            parent: 'payments',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payments/payments-delete-dialog.html',
                    controller: 'PaymentsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Payments', function(Payments) {
                            return Payments.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payments', null, { reload: 'payments' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
