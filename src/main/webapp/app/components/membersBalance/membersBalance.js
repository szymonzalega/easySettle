angular.module('easySettleApp')
    .directive('membersBalance', function() {
        return {
            restrict: 'E',
            scope: {
                members: '='
            },
            templateUrl: 'app/components/membersBalance/membersBalance.html',
            link: function (scope) {

                scope.$watch('members', function(newValue, oldValue) {
                    calculateElementWidth();
                });

                function calculateElementWidth() {
                    angular.element(document).ready(function () {

                        let positiveBalance = 0;
                        angular.forEach(scope.members, function (member) {
                            if(member.balance > 0){
                                positiveBalance += member.balance;
                            }
                        });

                        angular.forEach(scope.members, function (member) {
                            let el = document.getElementById('member__' + member.id);
                            let parentElWidth = el.parentElement.clientWidth;
                            let elWidth = el.clientWidth;
                            let emptySpaceWidth = parentElWidth - elWidth;
                            let percentageOfBalance = (Math.abs(member.balance) * 100) / positiveBalance;
                            let percentageOfBalanceInPixel = (percentageOfBalance * emptySpaceWidth) / 100;
                            let finalWidth = elWidth + percentageOfBalanceInPixel;
                            el.style.width = finalWidth + 'px';
                        });

                        console.log(scope.members);

                    });
                }
            }
        };
    });
