angular.module('easySettleApp').factory('PaymentsService',
    ['$resource',
        function ($resource) {
            var paymentsEvents = $resource('api/payments/',
                {},
                {
                    "getAllPayments": {
                        method: 'GET', isArray: true, url: 'api/payments/getAllPayments'
                    },
                    "create": {
                        method: 'POST', isArray: false, url: 'api/payments/newPayment'
                    }
                });

            return {
                getAllPayments: function () {
                    return paymentsEvents.getAllPayments();
                },
                create: function (payment) {
                    return paymentsEvents.create(payment);
                }
            };
        }
    ]);

