angular.module('easySettleApp')
    .directive('easyLabel', function() {
        return {
            restrict: 'E',
            scope: {
                label: '=',
            },
            templateUrl: 'app/components/easyLabel/easyLabel.html',
            link: function (scope) {
            }
        };
    });
