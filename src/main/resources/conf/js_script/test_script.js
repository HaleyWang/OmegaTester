

function test($response, $preReqResultStr, $envString) {

    var $log = [];
    var console = {
        log: function(e) {
            $log.push(e);
        }
    }

    function parseJson(jsonStr) {
        try {
            return JSON.parse(jsonStr);
        }catch(e) {
            return {};
        }
    }

    function $assertThat(msg, actual, expect, condition) {
        if (!condition) {
            condition = function(a, e) {
                return a === e;
            };
        }
        try {
            $tests[msg] = condition(actual, expect);
            if (!$tests[msg]) {
                $tests[msg] = JSON.stringify(arguments);
            }
        } catch (e) {
            $tests[msg] = $tests[msg] + " " + JSON.stringify(arguments) + " " + e.toString();
        }
    }

    var $preReqResult = parseJson($preReqResultStr);
    var $env = parseJson($envString);
    var $tests = {};

    try {

        {{my_code}};

        $tests.$env = $env;
    }
    catch (e) {
        $tests.error = e.toString();
        console.log(e.toString());
    }
    $tests.$log = $log;
    return JSON.stringify($tests);
}