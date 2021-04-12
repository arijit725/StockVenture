const queryString = window.location.search;
console.log(queryString);
const urlParams = new URLSearchParams(queryString);
const stockID = urlParams.get('stockID')
console.log(stockID);

var generateReportUrl = 'http://localhost:8080/fundamental/generateReport/'+stockID;

function onReportLoad(){
    console.log("onReportLoad triggered: url: "+generateReportUrl);
    GetRawBookContent(generateReportUrl);
}






/*====================== Http Request Utility =========================*/
function GetRawBookContent(yourUrl){
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}