angular.module('easySettleApp')
    .directive('easyInput', function() {
        return {
            restrict: 'E',
            scope: {
                name: '=',
                inputModel: '=',
                label: '=',
                placeholder: '=',
                type: '=?'
            },
            templateUrl: 'app/components/easyInput/easyInput.html',
            link: function (scope) {

                if(scope.type === null){
                    scope.type = "text";
                }

            }
        };
    });
