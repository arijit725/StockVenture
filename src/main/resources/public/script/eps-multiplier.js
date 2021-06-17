const queryString = window.location.search;
console.log(queryString);
const urlParams = new URLSearchParams(queryString);
const stockID = urlParams.get('stockID')
console.log(stockID);

var companyDetailsURL = '/fundamental/companydetails/'+stockID;

var generateReportUrl = '/fundamental/generateReport/'+stockID;

var stockValuationUrl = '/fundamental/stockvaluation/'+stockID;
var getStockValuationUrl='/fundamental/getStockValuation/'+stockID;

var inputDataMap = new Map();

function onReportLoad(){

    createCompanyTable();

    waacdisountrate();
    forwardpe();
}



function waacdisountrate(){
    var fetchedValue = 11;
    console.log("Fetched value "+fetchedValue);
    id = "discountrate";
    document.getElementById(id).value = fetchedValue;
}

function forwardpe(){
    id = "forwardpe";
    fetchedValue = 23.32;
    document.getElementById(id).value = fetchedValue;
}


/*====================== Company Details =========================*/

/*
{"companyName":"TCS","years":0,"industry":"Computers- software","marketCap":1250500,"faceValue":1.0,"currentSharePrice":3460.0,"industryPE":35.04}
*/

function createCompanyTable(){
     var companyDetails = getCompanyDetails();
     var companyEle = document.getElementById("companyName");
     companyEle.innerHTML = companyDetails.companyName;

     var industryELe = document.getElementById("industry");
     industryELe.innerHTML=companyDetails.industry;

     var divID="Company-Details";
     var table = document.createElement("TABLE");
     var tableID = "cDetailsID";
     table.setAttribute('id', tableID);
     var row = table.insertRow();

    var sharepriceELe = document.createElement("TH");
    sharepriceELe.innerHTML="Current Share Price";
    row.appendChild(sharepriceELe);

    var industryPEEle = document.createElement("TH");
    industryPEEle.innerHTML="Industry PE";
    row.appendChild(industryPEEle);

    var marketCapEle = document.createElement("TH");
    marketCapEle.innerHTML="Marketcap";
    row.appendChild(marketCapEle);

    var faceValueEle = document.createElement("TH");
    faceValueEle.innerHTML="Facevalue";
    row.appendChild(faceValueEle);

      var ttmpeEle = document.createElement("TH");
      ttmpeEle.innerHTML="TTM PE";
      row.appendChild(ttmpeEle);

      var ttmepsEle = document.createElement("TH");
      ttmepsEle.innerHTML="TTM EPS";
      row.appendChild(ttmepsEle);

    var row2 = table.insertRow();
    var sharepriceVal = document.createElement("TD");
        sharepriceVal.innerHTML=companyDetails.currentSharePrice;
        row2.appendChild(sharepriceVal);

        var industryPEVal = document.createElement("TD");
        industryPEVal.innerHTML=companyDetails.industryPE;
        row2.appendChild(industryPEVal);

        var marketCapVal = document.createElement("TD");
        marketCapVal.innerHTML=companyDetails.marketCap;
        row2.appendChild(marketCapVal);

        var faceValueVal = document.createElement("TD");
        faceValueVal.innerHTML=companyDetails.faceValue;
        row2.appendChild(faceValueVal);

         var ttmpeVal = document.createElement("TD");
         ttmpeVal.innerHTML=companyDetails.ttmpe;
         row2.appendChild(ttmpeVal);

         var ttmepsVal = document.createElement("TD");
         ttmepsVal.innerHTML=companyDetails.ttmeps;
          row2.appendChild(ttmepsVal);

     var dvTable = document.getElementById(divID);
     dvTable.appendChild(table);
}


function getCompanyDetails(){
    var jsonResonse = GetRawBookContent(companyDetailsURL);
    console.log("CompanyDetails: "+jsonResonse);
    var companyDetails = JSON.parse(jsonResonse);
    return companyDetails;
}



function generateReport(){
    inputDataMap['growthRate'] =document.getElementById("growthrate").value;
    inputDataMap['discountRate']=document.getElementById("discountrate").value;
    inputDataMap['estimatedPE']=document.getElementById("forwardpe").value;
    console.log(inputDataMap);
    var requestBody = generateJsonString(inputDataMap);
    console.log(requestBody);
    var url = stockValuationUrl+'/epsmultiplier';
        console.log("Posting data to url: "+url);
        var jsonResonse = postRequest(url,requestBody, showValuationValue);
    }


var currentSharePrice = null;

function showValuationValue(){
    var url = getStockValuationUrl+'/epsmultiplier';
    var jsonResonse = GetRawBookContent(url);
    console.log("showPEValuationFValue: response: "+jsonResonse);
    var data = JSON.parse(jsonResonse);
    projectedMap = data.projectionDtoMap;
    console.log(projectedMap)
    document.getElementById("projeps1").innerHTML = projectedMap[1].ttmEPS;
    document.getElementById("projeps2").innerHTML = projectedMap[2].ttmEPS;
    document.getElementById("projeps3").innerHTML = projectedMap[3].ttmEPS;
    document.getElementById("projeps4").innerHTML = projectedMap[4].ttmEPS;
    document.getElementById("projeps5").innerHTML = projectedMap[5].ttmEPS;

    document.getElementById("intrn1").innerHTML = projectedMap[1].intrinsicValue;
    document.getElementById("intrn2").innerHTML = projectedMap[2].intrinsicValue;
    document.getElementById("intrn3").innerHTML = projectedMap[3].intrinsicValue;
    document.getElementById("intrn4").innerHTML = projectedMap[4].intrinsicValue;
    document.getElementById("intrn5").innerHTML = projectedMap[5].intrinsicValue;

    var finalIntrinsicValue = data.finalIntrinsicValue;
    var currentSharePrice = data.currentPrice;
    document.getElementById("finalIntrinsicValue").innerHTML = finalIntrinsicValue;


    var upside = (finalIntrinsicValue-currentSharePrice)/currentSharePrice*100;
    document.getElementById("upside").innerHTML = upside;

    if(upside<0)
     document.getElementById("upside").style.color = "red";
    else
      document.getElementById("upside").style.color = "green";
 }

function showCalcPE(stockpriceList){
    for(var i=0;i<stockpriceList.length;i++){
        var stockPriceRaw = stockpriceList[i];
        console.log(stockPriceRaw);
//        var stockPrice = JSON.parse(stockPriceRaw);
        var stockPrice = stockPriceRaw;
        var id = "cp-FY-EPS-"+stockPrice.date;
        var ele = document.getElementById(id);
        ele.innerHTML = stockPrice.fyEPS;

        var id2 = "cp-PE-"+stockPrice.date;

        var ele2 = document.getElementById(id2);
        ele2.innerHTML = stockPrice.pe;
    }
}

function marginofsafty(ele){

    var targetPrice = document.getElementById("fvtrgprice").innerHTML;
    targetPrice = parseFloat(targetPrice);
    var marginofsafty = ele.value;
//    console.log("Target Price: "+targetPrice+" Margin of Safty: "+marginofsafty);
    var targetPriceMrgn = targetPrice*(1-(marginofsafty/100));
    document.getElementById("mgmtp").innerHTML = targetPriceMrgn;

        if(targetPriceMrgn>currentSharePrice)
                document.getElementById("mgmtp").style.color = "green";
            else
               document.getElementById("mgmtp").style.color = "red";

    var upside = (targetPriceMrgn-currentSharePrice)/currentSharePrice*100;
    document.getElementById("upside").innerHTML = upside;

    if(upside<0)
     document.getElementById("upside").style.color = "red";
    else
      document.getElementById("upside").style.color = "green";
}
/*====================== Http Request Utility =========================*/
function GetRawBookContent(yourUrl){
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}

function postRequest(url, requestBody,onloadfunc){
        var Httpreq = new XMLHttpRequest(); // a new request
        Httpreq.open("POST",url,true);
        Httpreq.setRequestHeader('x-stockid',stockID);
        Httpreq.setRequestHeader('Content-type','application/json; charset=utf-8');
        Httpreq.send(requestBody);

        Httpreq.onload =onloadfunc;

        return Httpreq.responseText;
}

/*============================  Utility Functions ===================================*/




function splitAndFill(ele){
    console.log("inside fragmentYearlyFill: ")
    console.log(ele.id);
    value = ele.value;
    idsplit = ele.id.split('-');
    valuesplit = value.split('\t');
    console.log("after split:")
    console.log(idsplit);
    console.log(valuesplit);
    var start = parseInt(idsplit[1]);
    for(var i=0;i<valuesplit.length;i++){
        var tmpIndex = start+i;
        var nexid = idsplit[0]+'-'+tmpIndex;
        var nextvalue = valuesplit[i];
        console.log(nexid+" = "+nextvalue);
        var nextele = document.getElementById(nexid);
        if(nextele!=null){
            nextele.value = nextvalue;
        }
    }


    console.log(value)
}

function createGenericHeader(years){
    var header = new Array();
    header.push("DataPoints");
    for(var i=0;i<years;i++){
    header.push("Year-"+(i+1));
    }
    return header;
}

function createTable1(divID,tableID,headerList,datapoints, submitAction, datamap){

    var table = document.createElement("TABLE");
    table.setAttribute('id', tableID);
//    var rowCount = document.getElementById("years").value;
    var row = table.insertRow(0);
    var columnCount = headerList.length;
    console.log("Column count: "+columnCount)
    for (var i = 0; i < columnCount; i++) {
                var headerCell = document.createElement("TH");
                headerCell.innerHTML = headerList[i];
                row.appendChild(headerCell);

          }
    //adding datapoints row
    for(var i=0;i<datapoints.length;i++){
        var tmprow = table.insertRow(i+1);
        var isDPCell = true;
        var splitenable = true;
        for(var j=0;j<headerList.length;j++){
            var datapointsCell = document.createElement("TD");
            if(isDPCell){
                 datapointsCell.innerHTML = datapoints[i][0];
                 isDPCell = false;
            }
            else{
                  var tmpInput = document.createElement("INPUT");
                  tmpInput.setAttribute("type", "text");
                  dataPerYear = datamap[headerList[j]];
                  try{
                    console.log("setting datapoint: "+datapoints[i][1]+" "+headerList[j]+" val: "+dataPerYear[datapoints[i][1]]);
                  tmpInput.value=dataPerYear[datapoints[i][1]]
                  }catch(e){
                    console.log(e);
                  }
                  var id = datapoints[i][1]+'-'+headerList[j];
                  tmpInput.setAttribute("id", id);
                  if(splitenable){
                      datapoint = datapoints[i][1];
                      tmpInput.oninput=function(){fragmentLines(this.id,headerList, this.value)};
                      splitenable = false;
                    }
                  datapointsCell.appendChild(tmpInput);
            }

            tmprow.appendChild(datapointsCell);
        }

    }
    var btnRow = table.insertRow();
    var btncell = document.createElement("TD");
    btncell.setAttribute("colspan",headerList.length);
    btncell.setAttribute("align","right");
    var btn = createSubmitButton("blSubmit","submit","submit-button",submitAction);
    btncell.appendChild(btn);
    btnRow.appendChild(btncell);

    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);
}

function fragmentLines(datapointid, headers, data){
//    for(var i=0;i<datapoints.length;i++){
        console.log("fragmentLines: datapoint: "+datapointid+" data: "+data);
        datapoint = datapointid.split('-')[0];

        var tmpdata = data.split('\t');
        if(tmpdata.length>1){
            var tmpI = 0;
            for(var y=1;y<headers.length;y++){
                id  = getCellID(datapoint,headers[y]);
//                console.log("fragmentLines input id: "+id);
                document.getElementById(id).value=tmpdata[tmpI].trim();
                tmpI++;
                }
             }
//             else{
//                    console.log("This is single valued: "+tmpdata)
//                }

    }


function generateJsonString(obj){
    var jsonStr = JSON.stringify(obj);
    return jsonStr
}


function createGrowthLabel(cellid, growthVal){
    var parent = document.getElementById(cellid);
    var label = document.createElement("LABEL");
    label.innerHTML="("+growthVal+"%)";
    if(growthVal>0)
        label.style.color='green';
    else if(growthVal<0)
        label.style.color='red';
    else
        label.style.color='blue';
    parent.appendChild(label);
}

function createToolTip(parentrow, tipsText){

    var cell0 = document.createElement("TD");
    cell0.innerHTML="Tips";
    cell0.setAttribute("class","tooltip");
    var span = document.createElement("SPAN");
    span.setAttribute("id","balTips");
    span.setAttribute("class","tooltiptext");
    span.innerHTML=tipsText;
    cell0.appendChild(span);
    parentrow.appendChild(cell0);
}

function getDataFY(url,years){
    var url = url+'/'+years;
    console.log("Getting data from url: "+url);
    var jsonResonse = GetRawBookContent(url);
    console.log("Json res: "+jsonResonse);
    return jsonResonse;
}

function createHeaderFromResponse(jsonResonse){
    var datalist = JSON.parse(jsonResonse);
    var header = new Array();
    header.push("DataPoints");
     for(var i =0;i<datalist.length;i++){
       var data = datalist[i];
       var d = data.date;
       header.push(d);
      }
      console.log("createHeaderFromResponse: "+header);
      return header;
}

function createHeaders(years){
    var header = new Array();
    header.push("DataPoints");
    var today = new Date();
    var currentYear = today.getFullYear();
    var month = today.getMonth();
    if(month<4)
      currentYear = currentYear-1;
//    var years = document.getElementById("years").value;
    for(var i=0;i<years;i++){
            header.push("Mar-"+currentYear);
            currentYear = currentYear-1;
    }
    return header;

}

function generateJsonString(obj){
    var jsonStr = JSON.stringify(obj);
    return jsonStr
}

function getCellID(datapoint , fy){
    return datapoint+'-'+fy;
}

function getTableHeader(tableID){
    var headerList = new Array();
    var table = document.getElementById(tableID);
    var headers = table.rows[0].cells;
    for(var i=1;i<headers.length;i++ ){
        headerList.push(headers[i].innerHTML);
    }
    console.log("getTableHeader:: headers: "+headerList);
    return headerList;
}



/**
"analysisStatement":["{\"statement\":\" Average Dilution in Equity Share Capital: 0.0\",\"color\":\"greeen\"}","{\"statement\":\" Average Dilution in Equity Share Capital: 0.0\",\"color\":\"green\"}","{\"statement\":\" Average Reserve Growth: 51.14\",\"color\":\"green\"}","{\"statement\":\" Average Reserve Growth: 0.0\",\"color\":\"green\"}"]
**/
function showAnalysisStatement(parentDiv, balAnal){
    analysisStatementList = balAnal.analysisStatement;
    console.log("Showing analysis statement: "+analysisStatementList.length);
    var parentDivEle = document.getElementById(parentDiv);
    var tableID = parentDiv+"_stmt_tbl";
    var children = parentDivEle.childNodes;
    console.log("children:" +children);
    console.log(children);
    var tableEle = document.getElementById(tableID);
    console.log(tableEle);
    if(tableEle!=null){
        console.log("removing: "+tableID);
        tableEle.remove();
        parentDivEle.removeChild(tableEle);
    }

    var table = document.createElement("TABLE");
    table.setAttribute("id",tableID);
    for(var i=0;i<analysisStatementList.length;i++){
        var row = table.insertRow();
        var cell = document.createElement("TD");
        var jsonString = analysisStatementList[i];
        console.log(jsonString);
        var parsedObj = JSON.parse(jsonString);
        console.log(parsedObj);
        cell.innerHTML = parsedObj.statement;
        cell.setAttribute("class", parsedObj.style);

        row.appendChild(cell);
    }

    parentDivEle.appendChild(table);

}

function createTableWithToolTips(divID,tableID,headerList,datapoints,dataFY){

    var table = document.createElement("TABLE");
    table.setAttribute('id', tableID);
//    var rowCount = document.getElementById("years").value;
    var row = table.insertRow(0);
    var columnCount = headerList.length;
//    console.log("Column count: "+columnCount)
    for (var i = 0; i < columnCount; i++) {
                var headerCell = document.createElement("TH");
                headerCell.innerHTML = headerList[i];
                row.appendChild(headerCell);
          }

//    console.log("DataFY: ");
//    console.log(dataFY);
    //adding datapoints row
    var bgColor="white";
    for(var i=0;i<datapoints.length;i++){
        var tmprow = table.insertRow(i+1);
        if((i%2)==0){
            bgColor = "white";
        }
        else{
            bgColor="lightgray";
        }

        tmprow.style.backgroundColor=bgColor;
        var isDPCell = true;

        for(var j=0;j<headerList.length;j++){
            var datapointsCell = document.createElement("TD");
            if(isDPCell){
                 datapointsCell.innerHTML = datapoints[i][0];
                 datapointsCell.setAttribute("class","itemtooltip");
                 var span = document.createElement("SPAN");
                 span.setAttribute("class","tooltiptext");
                 span.innerHTML=datapoints[i][2];
                 datapointsCell.appendChild(span);
                 isDPCell = false;
            }
            else{
                  dataPerYear = dataFY[headerList[j]];
//                  console.log("dataPerYear for: "+headerList[j]+" = "+dataPerYear);
                  var valueEle = document.createElement("LABEL");
                  valueEle.innerHTML = dataPerYear[datapoints[i][1]];
                  var id = datapoints[i][1]+'-'+headerList[j];
//                  console.log("Setting cell ID: "+id);
                  valueEle.setAttribute("id", id);
                  datapointsCell.appendChild(valueEle);
            }

            tmprow.appendChild(datapointsCell);
        }

    }

    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);
}


function createSubmitButton(id,value,className, onclickAction){
    var btn = document.createElement("INPUT");
    btn.setAttribute("id",id);
    btn.setAttribute("type","button");
    btn.setAttribute("value", value);
    btn.setAttribute("class",className);
    btn.onclick=onclickAction;
    return btn;
}