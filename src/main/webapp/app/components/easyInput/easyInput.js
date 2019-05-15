angular.module('easySettleApp')
    .directive('easyInput', function() {
        return {
            restrict: 'E',
            scope: {
                name: '=',
                inputModel: '=',
                label: '=',
                placeholder: '='
            },
            templateUrl: 'app/components/easyInput/easyInput.html',
            link: function (scope) {
            }
        };
    });
