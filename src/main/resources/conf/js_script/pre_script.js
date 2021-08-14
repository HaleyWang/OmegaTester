function runPreRequestScript($envString) {
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

    var $preReqResult = {};
    var $env = parseJson($envString);

    try {

         {{my_code}};

    } catch (e) {
        $preReqResult.error = e.toString();
        console.log(e.toString());

    }
    $preReqResult.$log = $log;
    return JSON.stringify($preReqResult);
}