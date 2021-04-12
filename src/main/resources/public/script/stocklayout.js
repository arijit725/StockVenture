
var companyDeatilsUrl ='http://localhost:8080/fundamental/companyDetails';
var balancesheetDeatilsUrl ='http://localhost:8080/fundamental/balancesheetDetails';
var profitAndLossDetailUrl = 'http://localhost:8080/fundamental/profitAndLossDetails';
var yearlyReportDetailUrl = 'http://localhost:8080/fundamental/yearlyReportDetails';
var ratioDetailUrl = 'http://localhost:8080/fundamental/ratioDetails';

var stockID=null;
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
   else if(cityName == "Ratios"){
      ratiosTable();
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
    var marketCap = document.getElementById("marketCap").value;
    var industry = document.getElementById("industry").value;
    var currentSharePrice = document.getElementById("currentSharePrice").value;
    var industryPE = document.getElementById("industryPE").value;
    var faceValue = document.getElementById("faceValue").value;
    var years = document.getElementById("years").value;
    console.log("CompanyName: "+companyName+" marketCap: "+marketCap+" industry: "+industry+" currentSharePrice: "+currentSharePrice+" industryPE: "+industryPE+" faceValue: "+faceValue+" years: "+years)
//    localStorage.setItem("companyName", companyName);
    var companyDetails={
        "companyName":companyName,
        "marketCap":marketCap,
        "industry":industry,
        "currentSharePrice":currentSharePrice,
        "industryPE":industryPE,
        "faceValue":faceValue,
        "years":years
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
function balancesheetDatPoints(){
    var datapoints = new Array();
    datapoints.push(["Total Share Capital","total_share_capital"]);
    datapoints.push(["Equity Share Capital","equity_share_capital"]);
    datapoints.push(["Reserves","reserves"]);
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
    var headerList =createHeaders();
    console.log("Generated Headers: "+headerList);
    createTable("Balance-Sheet","bltbl",headerList,datapoints,submitBalancesheetDetails);
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
        balanceSheetDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var value = document.getElementById(id).value;
//            console.log("Balancesheet details"+ id+" : "+value);
            balanceSheetDto[datapoints[i][1]] = value;
            }
//        console.log("Balance sheet for year:");
//        console.log(balanceSheetDto);
        balanceSheetDtoList.push(balanceSheetDto);
    }
    console.log("Stored records: "+balanceSheetDtoList.length)
    console.log(balanceSheetDtoList)
    var balanceSheetJson = generateJsonString(balanceSheetDtoList);
    console.log(balanceSheetJson);
    var responseText = postRequest(balancesheetDeatilsUrl,balanceSheetJson)
}





/*======================================Functions for Profit And Loss Handle================================*/
function porfiAndLossDataPoints(){
    var datapoints = new Array();
    datapoints.push(["Net Sales","netSales"]);
    datapoints.push(["Consumption Of Raw Material","consumptionRawMaterial"]);
    datapoints.push(["Employee Cost","employeeCost"]);
    datapoints.push(["PBDIT","pbit"]);
    datapoints.push(["Interest","interest"]);
    datapoints.push(["Net Profit","netProfit"]);
    return datapoints;
}

function porfitAndLossTable(){
    console.log("creaing ProfitAndLoss table");
    var datapoints = porfiAndLossDataPoints();
    var tableID = document.getElementById("pftbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createHeaders();
    console.log("Generated Headers: "+headerList);
    createTable("Profit-And-Loss","pftbl",headerList,datapoints,submitPofitAndLossDetails);
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
        balanceSheetDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var value = document.getElementById(id).value;
//            console.log("ProfitAndLoss details"+ id+" : "+value);
            balanceSheetDto[datapoints[i][1]] = value;
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
}


/*======================================Functions for Yearly Report Handle================================*/
function yearlyReportsDataPoints(){
    var datapoints = new Array();
    datapoints.push(["Basic EPS","basicEPS"]);
    return datapoints;
}

function yearlyReportsTable(){
    console.log("creaing Yearly-Report table");
    var datapoints = yearlyReportsDataPoints();
    var tableID = document.getElementById("yrtbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createHeaders();
    console.log("Generated Headers: "+headerList);
    createTable("Yearly-Report","yrtbl",headerList,datapoints,submitYearlyReportDetails);
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
        yearlyReportDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var value = document.getElementById(id).value;
//            console.log("ProfitAndLoss details"+ id+" : "+value);
            yearlyReportDto[datapoints[i][1]] = value;
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
}


/*======================================Functions for Ratios Handle================================*/
function ratiosDataPoints(){
    var datapoints = new Array();
    datapoints.push(["PE Ratio","peRatio"]);
    datapoints.push(["PB Ratio","pbRatio"]);
    datapoints.push(["ROE Ratio","roe"]);
    datapoints.push(["Enterprise Value","ev"]);
    datapoints.push(["EV/EBITDA","evEbitda"]);
    return datapoints;
}

function ratiosTable(){
    console.log("creaing ratios table");
    var datapoints = ratiosDataPoints();
    var tableID = document.getElementById("ratbl");
    if(tableID!=null){
        tableID.remove();
    }
    var headerList =createHeaders();
    console.log("Generated Headers: "+headerList);
    createTable("Ratios","ratbl",headerList,datapoints,submitRatiosDetails);
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
        yearlyReportDto['date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var value = document.getElementById(id).value;
            yearlyReportDto[datapoints[i][1]] = value;
            }
        ratioDtoList.push(yearlyReportDto);
    }
    console.log("Stored Ratio Details records: "+ratioDtoList.length)
    console.log(ratioDtoList)
    var ratioDtoJson = generateJsonString(ratioDtoList);
    console.log(ratioDtoJson);
    var responseText = postRequest(ratioDetailUrl,ratioDtoJson);
}

/*============================  Http Request Functions ===================================*/
function postRequest(url, requestBody){
        var Httpreq = new XMLHttpRequest(); // a new request
        Httpreq.open("POST",url,false);
        var md5Sum = md5(requestBody);
        Httpreq.setRequestHeader('x-stockid',stockID);
        Httpreq.setRequestHeader('x-md5sum',md5Sum);
        Httpreq.setRequestHeader('Content-type','application/json; charset=utf-8');
        console.log("requestBody: "+requestBody);
        Httpreq.send(requestBody);
        return Httpreq.responseText;
}

/*============================  Utility Functions ===================================*/

function createHeaders(){
    var header = new Array();
    header.push("DataPoints");
    var today = new Date();
    var currentYear = today.getFullYear();
    var years = document.getElementById("years").value;

    for(var i=0;i<years;i++){
        currentYear = currentYear-1;
        header.push("Mar-"+currentYear)
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

function createSubmitButton(id,value,className, onclickAction){
    var btn = document.createElement("INPUT");
    btn.setAttribute("id",id);
    btn.setAttribute("type","button");
    btn.setAttribute("value", value);
    btn.setAttribute("class",className);
    btn.onclick=onclickAction;
    return btn;
}
