
var companyDeatilsUrl ='http://localhost:8080/fundamental/companyDetails';
var balancesheetDeatilsUrl ='http://localhost:8080/fundamental/balancesheetDetails';
var profitAndLossDetailUrl = 'http://localhost:8080/fundamental/profitAndLossDetails';
var yearlyReportDetailUrl = 'http://localhost:8080/fundamental/yearlyReportDetails';
var quarterlyReportDetailUrl = 'http://localhost:8080/fundamental/quarterlyReportDetails';
var cashFlowDetailUrl = 'http://localhost:8080/fundamental/cashFlowDetails';
var ratioDetailUrl = 'http://localhost:8080/fundamental/ratioDetails';
var blUploadlUrl = 'http://localhost:8080/fundamental/uploadbl';
var plUploadUrl = 'http://localhost:8080/fundamental/uploadpl';
var uploadPDFuRL = 'http://localhost:8080/fundamental/uploadPDF';

var getDataUrl = 'http://localhost:8080/fundamental/getData';

var getBalancesheetUrl ='http://localhost:8080/fundamental/balancesheet/';


var stockID=null;

function tabopen(tabid){
    document.getElementById(tabid).click();
}
function startTab(){
    document.getElementById("defaultOpen").click();
}
function openCity(evt, cityName) {
  var i, tabcontent, tablinks;
  console.log("Selected tab: "+cityName)
  if(cityName == "Balance-Sheet"){
    balancesheetTable();

  }
  else if(cityName == "Profit-And-Loss"){
    porfitAndLossTable();
  }
  else if(cityName == "Yearly-Report"){
    yearlyReportsTable();
  }
  else if(cityName == "Quarterly-Report"){
    quarterlyReportTable();
  }
   else if(cityName == "Ratios"){
      ratiosTable();
    }
   else if(cityName == "CashFlow"){
         cashFlowTable();
       }




  tabcontent = document.getElementsByClassName("tabcontent");
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }
  tablinks = document.getElementsByClassName("tablinks");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }
  document.getElementById(cityName).style.display = "block";
  evt.currentTarget.className += " active";
}


/*======================================Functions for Company Details Handle================================*/
function submitCompanyDetails(){
    startTab();
    var companyName = document.getElementById("companyName").value;
    var marketCap = document.getElementById("marketCap").value.replace(',','');
    marketCap = marketCap.replace(',','');
    var industry = document.getElementById("industry").value;
    var currentSharePrice = document.getElementById("currentSharePrice").value;
    var industryPE = document.getElementById("industryPE").value;
    var faceValue = document.getElementById("faceValue").value;
    var ttmpe = document.getElementById("ttmpe").value;
    var ttmeps = document.getElementById("ttmeps").value;
    var years = document.getElementById("years").value;
    var seasonal = document.getElementById("snl").value;
    var currentpv = document.getElementById("currentpv").value;
    var cmpBeta = document.getElementById("cmpBeta").value;
    console.log("CompanyName: "+companyName+" marketCap: "+marketCap+" industry: "+industry+" currentSharePrice: "+currentSharePrice+" industryPE: "+industryPE+" faceValue: "+faceValue+" ttmeps: "+ttmeps+" years: "+years+" seasonal: "+seasonal+" currentpv: "+currentpv+" cmpBeta: "+cmpBeta);

//    localStorage.setItem("companyName", companyName);
    var companyDetails={
        "companyName":companyName,
        "marketCap":marketCap,
        "industry":industry,
        "currentSharePrice":currentSharePrice,
        "industryPE":industryPE,
        "faceValue":faceValue,
        "ttmpe":ttmpe,
        "ttmeps":ttmeps,
        "years":years,
        "currentpv":currentpv,
        "cmpBeta":cmpBeta,
//        "cars":cars,
        "seasonal":seasonal
    };
    var companyDetailsJson = generateJsonString(companyDetails);
    console.log("Company Details: "+companyDetailsJson)
    var responseText = postRequest(companyDeatilsUrl,companyDetailsJson)
    console.log(responseText);
    stockID = responseText;
    disableCompaneSection();

}

function disableCompaneSection(){
    document.getElementById("companyName").disabled=true;
    document.getElementById("marketCap").disabled=true;
    document.getElementById("industry").disabled=true;
    document.getElementById("currentSharePrice").disabled=true;
    document.getElementById("industryPE").disabled=true;
    document.getElementById("faceValue").disabled=true;
    document.getElementById("faceValue").disabled=true;
    document.getElementById("years").disabled=true;
    document.getElementById("submitCompany").style.backgroundColor="lightgray";
}

/*======================================Functions for BalanceSheet Handle================================*/

function chooseBlWay(){
    var divID = "Balance-Sheet";
    var table = document.createElement("TABLE");
    var row = table.insertRow();
    var cell1 = document.createElement("TD");
     var label1 = document.createElement("LABEL");
        label1.innerHTML="Upload Balancesheet";
        var x = document.createElement("INPUT");
        x.setAttribute("type", "radio");
        x.setAttribute('id', "ubl");
        cell1.appendChild(label1);
        cell1.appendChild(x);
    row.appendChild(cell1);

    var cell2 = document.createElement("TD");
         var label2 = document.createElement("LABEL");
            label2.innerHTML="Manual Insert Balancesheet";
            var x2 = document.createElement("INPUT");
            x2.setAttribute("type", "radio");
            x2.setAttribute('id', "mbl");
            cell2.appendChild(label2);
            cell2.appendChild(x2);
        row.appendChild(cell2);

        var dvTable = document.getElementById(divID);
         dvTable.appendChild(table);


}
function balancesheetDatPoints(){
    var datapoints = new Array();
    datapoints.push(["FY Date","date"]);
    datapoints.push(["Total Share Capital","total_share_capital"]);
    datapoints.push(["Equity Share Capital","equity_share_capital"]);
    datapoints.push(["Reserves","reserves"]);
    datapoints.push(["TOTAL SHAREHOLDERS FUNDS","total_shareholder_funds"]);
    datapoints.push(["Debt","debt"]);
    return datapoints;
}

function balancesheetTable(){
    console.log("creaing Balancesheet table");
        var datapoints = balancesheetDatPoints();
        var bltbl = document.getElementById("bltbl");
        if(bltbl!=null){
            bltbl.remove();
        }
        var headerList =createGenericHeader();
        console.log("Generated Headers: "+headerList);
        createTable2("Balance-Sheet","bltbl",headerList,datapoints,submitBalancesheetDetails);
}

function submitBalancesheetDetails(){
    console.log("Submit Balancesheet action is triggered");
    var bltbl = "bltbl";
    var datapoints = balancesheetDatPoints();
    var headers = getTableHeader(bltbl)
    var balanceSheetDtoList = new Array();
    for(var y=0;y<headers.length;y++){
        var year = headers[y];
        var balanceSheetDto =  new Map();
//        balanceSheetDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var key = datapoints[i][1];
            var value = document.getElementById(id).value.trim();
            if(key=='date'){
                var tmpValue = value.split(" ");
                value = tmpValue[0].trim()+"-20"+tmpValue[1];
               }
            value = value.replace(',','');
//            console.log("Balancesheet details"+ id+" : "+value);
            balanceSheetDto[datapoints[i][1]] = value;
            }
        balanceSheetDtoList.push(balanceSheetDto);
    }
    console.log("Stored records: "+balanceSheetDtoList.length)
    console.log(balanceSheetDtoList)
    var balanceSheetJson = generateJsonString(balanceSheetDtoList);
    console.log(balanceSheetJson);
    var responseText = postRequest(balancesheetDeatilsUrl,balanceSheetJson);
    tabopen("plopen");
}





/*======================================Functions for Profit And Loss Handle================================*/
function porfiAndLossDataPoints(){
    var datapoints = new Array();
    datapoints.push(["FY Date","pldate"]);
    datapoints.push(["Net Sales","netSales"]);
    datapoints.push(["Consumption Of Raw Material","consumptionRawMaterial"]);
    datapoints.push(["Employee Cost","employeeCost"]);
//    datapoints.push(["PBIT","pbit"]);
    datapoints.push(["Interest","interest"]);
    datapoints.push(["Net Profit","netProfit"]);
    return datapoints;
}


function getProfitAndLossData(){
    var url = getDataUrl+'/profitandloss/'+stockID;
    console.log("Getting data from url: "+url);
    var jsonResonse = getContent(url);
    console.log("ProfitANdLoss Json res: "+jsonResonse);
    var profitTableMap = JSON.parse(jsonResonse);
    console.log("creaing ProfitAndLoss table");
        var datapoints = porfiAndLossDataPoints();
        var tableID = document.getElementById("pftbl");
        if(tableID!=null){
            tableID.remove();
        }
        var headerList =createHeaders();
        console.log("Generated Headers: "+headerList);
        createTable1("Profit-And-Loss","pftbl",headerList,datapoints,submitPofitAndLossDetails,profitTableMap);
}

function porfitAndLossTable(){
//    uploadProfitAndLossPDF("Profit-And-Loss","plpdf");
    console.log("creaing ProfitAndLoss table");
    var datapoints = porfiAndLossDataPoints();
    var tableID = document.getElementById("pftbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createGenericHeader();
    console.log("Generated Headers: "+headerList);
    createTable2("Profit-And-Loss","pftbl",headerList,datapoints,submitPofitAndLossDetails);
}


function submitPofitAndLossDetails(){
    console.log("Submit ProfitAndLoss action is triggered");
    var tableID = "pftbl";
    var datapoints = porfiAndLossDataPoints();
    var headers = getTableHeader(tableID)
    var profitAndLossDtoList = new Array();
    for(var y=0;y<headers.length;y++){
        var year = headers[y];
        var balanceSheetDto =  new Map();
//        balanceSheetDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var key = datapoints[i][1];
            var value = document.getElementById(id).value.trim();
            if(key=='pldate'){
                var tmpValue = value.split(" ");
                value = tmpValue[0].trim()+"-20"+tmpValue[1];
               }
            value = value.replace(',','');
//            console.log("ProfitAndLoss details"+ id+" : "+value);
            if(key=='pldate')
                key='date';
            balanceSheetDto[key] = value;
            }
//        console.log("PorfitAndLossDetails for year:");
//        console.log(balanceSheetDto);
        profitAndLossDtoList.push(balanceSheetDto);
    }
    console.log("Stored profitAndLoss records: "+profitAndLossDtoList.length)
    console.log(profitAndLossDtoList)
    var profitAndLossJson = generateJsonString(profitAndLossDtoList);
    console.log(profitAndLossJson);
    var responseText = postRequest(profitAndLossDetailUrl,profitAndLossJson)
    tabopen("yropen");
}


/*======================================Functions for Yearly Report Handle================================*/
function yearlyReportsDataPoints(){
    var datapoints = new Array();
    datapoints.push(["FY Date","yldate"]);
    datapoints.push(["PBIT","pbit"]);
    datapoints.push(["Basic EPS","basicEPS"]);

    return datapoints;
}


//function getYearlyReportData(){
//    var url = getDataUrl+'/yearlyreport/'+stockID;
//    console.log("Getting data from url: "+url);
//    var jsonResonse = getContent(url);
//    console.log("YearlyReport Json res: "+jsonResonse);
//    var yearlyTableMap = JSON.parse(jsonResonse);
//    console.log("creating Yearly-Report table");
//        var datapoints = yearlyReportsDataPoints();
//        var tableID = document.getElementById("yrtbl");
//        if(tableID!=null){
//            tableID.remove();
//        }
//        var headerList =createHeaders();
//        console.log("Generated Headers: "+headerList);
////        createTable("Yearly-Report","yrtbl",headerList,datapoints,submitYearlyReportDetails);
//        createTable1("Yearly-Report","yrtbl",headerList,datapoints,submitYearlyReportDetails,yearlyTableMap);
//}
//
//function submitYearlyReportPDF(){
//    var filePath = document.getElementById("yrpdf").value;
//    console.log("Submitting Yearly Report PDF from:"+filePath);
//    var fileUpload = document.getElementById("yrpdf");
//        if (typeof (FileReader) != "undefined") {
//            var reader = new FileReader();
//            console.log(reader);
//            reader.onload = function (e) {
//                console.log(e.target.result);
//            }
//            var file  = fileUpload.files[0];
////            var responseText =  postFile(uploadPDFuRL,file,'yearlyreport');
//            var responseText =  postFile(uploadPDFuRL,file,'yearlyreport',getYearlyReportData);
//            console.log("File Upload Response: "+responseText);
//
//        } else {
//            alert("This browser does not support HTML5.");
//        }
//}
//
//function uploadYearlyReportPDF(parentid,id){
//    uploadFile(parentid,id,"yrsubmit",submitYearlyReportPDF)
//}

function yearlyReportsTable(){
//    uploadYearlyReportPDF("Yearly-Report","yrpdf");
    console.log("creaing Yearly-Report table");
    var datapoints = yearlyReportsDataPoints();
    var tableID = document.getElementById("yrtbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createGenericHeader();
    console.log("Generated Headers: "+headerList);
    createTable2("Yearly-Report","yrtbl",headerList,datapoints,submitYearlyReportDetails);
}


function submitYearlyReportDetails(){
    console.log("submitYearlyReportDetails action is triggered");
    var tableID = "yrtbl";
    var datapoints = yearlyReportsDataPoints();
    var headers = getTableHeader(tableID)
    var yearlyReportDtoList = new Array();
    for(var y=0;y<headers.length;y++){
        var year = headers[y];
        var yearlyReportDto =  new Map();
//        yearlyReportDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var key = datapoints[i][1];
            var value = document.getElementById(id).value.trim();
            value = value.replace(',','');
            if(key=='yldate'){
                value = value.replace('\'','');
                var tmpValue = value.split(" ");
                value = tmpValue[0].trim()+"-20"+tmpValue[1];
               }
//            console.log("ProfitAndLoss details"+ id+" : "+value);
            if(key=='yldate')
                key='date';
            yearlyReportDto[key] = value;
            }
//        console.log("PorfitAndLossDetails for year:");
//        console.log(balanceSheetDto);
        yearlyReportDtoList.push(yearlyReportDto);
    }
    console.log("Stored yearlyReportDto records: "+yearlyReportDtoList.length)
    console.log(yearlyReportDtoList)
    var yearlyReportJson = generateJsonString(yearlyReportDtoList);
    console.log(yearlyReportJson);
    var responseText = postRequest(yearlyReportDetailUrl,yearlyReportJson);
    tabopen("qropen");
}

/*======================================Functions for Quarterly Report Handle================================*/

var qtrDataSource = "screener.com"
function quarterReprtDataPoints(){
    var datapoints = new Array();
    datapoints.push(["FY Date","qrdate"]);
    datapoints.push(["Basic EPS","eps"]);
    datapoints.push(["YOY Sales Growth","yoySalesGrowth"]);
    return datapoints;
}

function createQuarterlyReportHeaders(){
    var header = new Array();
    header.push("DataPoints");
    var today = new Date();
    var currentYear = today.getFullYear();
    var month = today.getMonth();
    var years = 12;
    for(var i=0;i<years;i++){
    header.push("QTR-"+(i+1));
    }
    return header;
}

function quarterlyReportTable(){
    console.log("creaing quarterly report table");
    var datapoints = quarterReprtDataPoints();
    var tableID = document.getElementById("qrtbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createQuarterlyReportHeaders();

    console.log("Generated Headers: "+headerList);
//    createTable("Quarterly-Report","qrtbl",headerList,datapoints,submitQuarterlyReportDetails);
    createTable2("Quarterly-Report","qrtbl",headerList,datapoints,submitQuarterlyReportDetails);
}


function submitQuarterlyReportDetails(){
    console.log("submitQuarterlyReportDetails action is triggered");
    var tableID = "qrtbl";
    var datapoints = quarterReprtDataPoints();
    var headers = getTableHeader(tableID);
    var ratioDtoList = new Array();
    for(var y=0;y<headers.length;y++){
        var year = headers[y];
        var yearlyReportDto =  new Map();
        yearlyReportDto['date']=headers[y];
        var dontPush = false;
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var ele = document.getElementById(id);
            var key = datapoints[i][1];
            var value = document.getElementById(id).value.trim();
            console.log("value: "+value);
            if(!value){
                dontPush = true;
                break;
              }
            value = value.replace(',','');
            value = value.replace('%','');
             if(key=='qrdate'){
                value = value.replace('\'','');
                var tmpValue = value.split(" ");
                console.log("value: "+value+" tmpvalue: "+tmpValue[0]+" "+tmpValue[1]);
                if(tmpValue[1].trim().length==4)
                    value =  tmpValue[0].trim() +"-"+tmpValue[1].trim();
                else
                    value = tmpValue[0].trim()+"-20"+tmpValue[1];
               }
//            console.log("ProfitAndLoss details"+ id+" : "+value);
            if(key=='qrdate')
                key='date';
            yearlyReportDto[key] = value;
            }
            if(!dontPush){
                ratioDtoList.push(yearlyReportDto);
                }
    }
    console.log("Stored Ratio Details records: "+ratioDtoList.length)
    console.log(ratioDtoList)
    var ratioDtoJson = generateJsonString(ratioDtoList);
    console.log(ratioDtoJson);
    var responseText = postRequest(quarterlyReportDetailUrl,ratioDtoJson);
    tabopen("ratioopen");
}


/*======================================Functions for Ratios Handle================================*/
function ratiosDataPoints(){
    var datapoints = new Array();
    datapoints.push(["FY Date","rdate"]);
    datapoints.push(["PE Ratio","peRatio"]);
    datapoints.push(["PB Ratio","pbRatio"]);
    datapoints.push(["ROE Ratio","roe"]);
    datapoints.push(["Enterprise Value","ev"]);
    datapoints.push(["EV/EBITDA","evEbitda"]);
    datapoints.push(["Debt-to-Equity","debtToEquityRatio"]);
    return datapoints;
}

function ratiosTable(){
    console.log("creaing ratios table");
    var datapoints = ratiosDataPoints();
    var tableID = document.getElementById("ratbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createGenericHeader();
    console.log("Generated Headers: "+headerList);
//    createTable("Ratios","ratbl",headerList,datapoints,submitRatiosDetails);

      createTable2("Ratios","ratbl",headerList,datapoints,submitRatiosDetails);
}


function submitRatiosDetails(){
    console.log("submitRatiosDetails action is triggered");
    var tableID = "ratbl";
    var datapoints = ratiosDataPoints();
    var headers = getTableHeader(tableID)
    var ratioDtoList = new Array();
    for(var y=0;y<headers.length;y++){
        var year = headers[y];
        var yearlyReportDto =  new Map();
//        yearlyReportDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){

            id  = getCellID(datapoints[i][1],headers[y]);
            var key = datapoints[i][1];
            var value = document.getElementById(id).value.trim();
            value = value.replace(',','');
            if(key=='rdate'){
                value = value.replace('\'','');
                var tmpValue = value.split(" ");
                value = tmpValue[0].trim()+"-20"+tmpValue[1];
               }
//            console.log("ProfitAndLoss details"+ id+" : "+value);
            if(key=='rdate')
                key='date';
            yearlyReportDto[key] = value;


            }
        ratioDtoList.push(yearlyReportDto);
    }
    console.log("Stored Ratio Details records: "+ratioDtoList.length)
    console.log(ratioDtoList)
    var ratioDtoJson = generateJsonString(ratioDtoList);
    console.log(ratioDtoJson);
    var responseText = postRequest(ratioDetailUrl,ratioDtoJson);

    tabopen("cashflowopen");
}


/*======================================Functions for Cashflow Report Handle================================*/

var cashFlowDataSource = "screener.com"
function cashFlowDataPoints(){
    var datapoints = new Array();
    datapoints.push(["FY Date","cfdate"]);
    datapoints.push(["Cash from Operating Activity","cashFromOperatingActivity"]);
    datapoints.push(["Fixed assets purchased","fixedAssestsPurchased"]);
    datapoints.push(["Net Cash Flow","netCashFlow"]);
    return datapoints;
}

function createCashFlowHeaders(){
    var header = new Array();
    header.push("DataPoints");
    var today = new Date();
    var currentYear = today.getFullYear();
    var month = today.getMonth();
    var years = 12;
    for(var i=0;i<years;i++){
    header.push("Year-"+(i+1));
    }
    return header;
}

function cashFlowTable(){
    console.log("creaing cash flow table");
    var datapoints = cashFlowDataPoints();
    var tableID = document.getElementById("cftbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createCashFlowHeaders();

    console.log("Generated Headers: "+headerList);
//    createTable("Quarterly-Report","qrtbl",headerList,datapoints,submitQuarterlyReportDetails);
    createTable2("CashFlow","cftbl",headerList,datapoints,submitCashFlowDetails);
}


function submitCashFlowDetails(){
    console.log("submitCashFlowDetails action is triggered");
    var tableID = "cftbl";
    var datapoints = cashFlowDataPoints();
    var headers = getTableHeader(tableID);
    var ratioDtoList = new Array();
    for(var y=0;y<headers.length;y++){
        var year = headers[y];
        var yearlyReportDto =  new Map();
        yearlyReportDto['date']=headers[y];
        var dontPush = false;
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var ele = document.getElementById(id);
            var key = datapoints[i][1];
            var value = document.getElementById(id).value.trim();
            console.log("value: "+value);
            if(!value){
                dontPush = true;
                break;
              }
            value = value.replace(',','');
            value = value.replace('%','');
             if(key=='cfdate'){
                value = value.replace('\'','');
                var tmpValue = value.split(" ");
                console.log("value: "+value+" tmpvalue: "+tmpValue[0]+" "+tmpValue[1]);
                if(tmpValue[1].trim().length==4)
                    value =  tmpValue[0].trim() +"-"+tmpValue[1].trim();
                else
                    value = tmpValue[0].trim()+"-20"+tmpValue[1];
               }
//            console.log("ProfitAndLoss details"+ id+" : "+value);
            if(key=='cfdate')
                key='date';
            yearlyReportDto[key] = value;
            }
            if(!dontPush){
                ratioDtoList.push(yearlyReportDto);
                }
    }
    console.log("Stored Ratio Details records: "+ratioDtoList.length)
    console.log(ratioDtoList)
    var ratioDtoJson = generateJsonString(ratioDtoList);
    console.log(ratioDtoJson);
    var responseText = postRequest(cashFlowDetailUrl,ratioDtoJson);

}

/*============================  Http Request Functions ===================================*/
function postRequest(url, requestBody){
        var Httpreq = new XMLHttpRequest(); // a new request
        Httpreq.open("POST",url,false);
//        var md5Sum = md5(requestBody);
        Httpreq.setRequestHeader('x-stockid',stockID);
//        Httpreq.setRequestHeader('x-md5sum',md5Sum);
        Httpreq.setRequestHeader('Content-type','application/json; charset=utf-8');
        console.log("requestBody: "+requestBody);
        Httpreq.send(requestBody);
        return Httpreq.responseText;
}

function postFile(url, file, pdftype,onloadfunc){
        var Httpreq = new XMLHttpRequest(); // a new request
        Httpreq.open("POST",url,true);
//        var md5Sum = md5(requestBody);
        Httpreq.setRequestHeader('x-stockid',stockID);
        formData = new FormData();
        formData.append("pdftype",pdftype);
        formData.append('stockid', stockID);
        formData.append('balancesheet', file);
        console.log("File details: "+file.type);
        Httpreq.send(formData);

        Httpreq.onload =onloadfunc;

        return Httpreq.responseText;
}

function getContent(yourUrl){
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}

/*============================  Function for report generation ===================================*/
function generateReport(){
    updateStockDetails();
    console.log("generateReport action is triggered");
    window.open("stock-report.html?stockID="+stockID);
    window.focus();
}

function updateStockDetails(){

    var insertStockDetailsUrl = 'http://localhost:8080/fundamental/storeDetails/';
    postRequest(insertStockDetailsUrl,"updatestock");

}
/*============================  Utility Functions ===================================*/


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


function uploadFile(parentid,id, submitBtnId, submitAction){
    var prev = document.getElementById(id);
    if(prev!=null)
        prev.remove();
    var prevBtn = document.getElementById(submitBtnId);
    if(prevBtn!=null)
        prevBtn.remove();
    var fileu = document.createElement("INPUT");
    fileu.setAttribute("type", "file");
    fileu.setAttribute("id", id);
    var btn = createSubmitButton(submitBtnId,"submit","submit-button",submitAction);
    var div = document.getElementById(parentid);
    div.appendChild(fileu);
    div.appendChild(btn);
}

function createHeaders(){
    var header = new Array();
    header.push("DataPoints");
    var today = new Date();
    var currentYear = today.getFullYear();
    var month = today.getMonth();
    var years = document.getElementById("years").value;
    if(month<4)
        currentYear = currentYear-1;
    for(var i=0;i<years;i++){
        header.push("Mar-"+currentYear);
        currentYear = currentYear-1;
    }
    return header;

}

function createGenericHeader(){
    var header = new Array();
    header.push("DataPoints");
    var today = new Date();
    var currentYear = today.getFullYear();
    var month = today.getMonth();
    var years = document.getElementById("years").value;
    for(var i=0;i<years;i++){
    header.push("Year-"+(i+1));
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


function createTable(divID,tableID,headerList,datapoints, submitAction){

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
        for(var j=0;j<headerList.length;j++){
            var datapointsCell = document.createElement("TD");
            if(isDPCell){
                 datapointsCell.innerHTML = datapoints[i][0];
                 isDPCell = false;
            }
            else{
                  var tmpInput = document.createElement("INPUT");
                  tmpInput.setAttribute("type", "text");
                  var id = datapoints[i][1]+'-'+headerList[j];
                  tmpInput.setAttribute("id", id);

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

function createTable2(divID,tableID,headerList,datapoints, submitAction){

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
                  var id = datapoints[i][1]+'-'+headerList[j];
//                  console.log("input id: "+id);
                  tmpInput.setAttribute("id", id);
                  tmpInput.setAttribute("size","5");
                  if(splitenable){
                    console.log("splitter id: "+id);
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

function createSubmitButton(id,value,className, onclickAction){
    var btn = document.createElement("INPUT");
    btn.setAttribute("id",id);
    btn.setAttribute("type","button");
    btn.setAttribute("value", value);
    btn.setAttribute("class",className);
    btn.onclick=onclickAction;
    return btn;
}
