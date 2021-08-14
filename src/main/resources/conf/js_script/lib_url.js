
// import  "https://cdn.jsdelivr.net/npm/base-64@1.0.0/base64.min.js" ;
// import  "https://cdn.jsdelivr.net/npm/base-64@1.0.0/base64.js" ;

    function parseJson(jsonStr) {
        try {
            return JSON.parse(jsonStr);
        }catch(e) {
            return {};
        }
    }