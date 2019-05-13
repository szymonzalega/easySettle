angular.module('easySettleApp').factory('PaymentsService',
    ['$resource',
        function ($resource) {
            var paymentsEvents = $resource('api/payments/',
                {},
                {
                    "getAllPayments": {
                        method: 'GET', isArray: true, url: 'api/payments/getAllPayments/:id'
                    },
                    "create": {
                        method: 'POST', isArray: false, url: 'api/payments/newPayment'
                    }
                });

            return {
                getAllPayments: function (id) {
                    return paymentsEvents.getAllPayments({id: id});
                },
                create: function (payment) {
                    return paymentsEvents.create(payment);
                }
            };
        }
    ]);

