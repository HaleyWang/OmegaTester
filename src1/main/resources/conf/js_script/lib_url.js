
//import "https://cdn.jsdelivr.net/npm/md5.js@1.3.5/index.js";
// import  "https://cdn.jsdelivr.net/npm/base-64@1.0.0/base64.min.js" ;

    function parseJson(jsonStr) {
        try {
            return JSON.parse(jsonStr);
        }catch(e) {
            return {};
        }
    }