const queryString = window.location.search;
console.log(queryString);
const urlParams = new URLSearchParams(queryString);
const stockID = urlParams.get('stockID')
console.log(stockID);

var companyDetailsURL = '/fundamental/companydetails/'+stockID;

var generateReportUrl = '/fundamental/generateReport/'+stockID;

var getBalancesheetUrl ='/fundamental/balancesheet/'+stockID;
var getBalancesheetAnalysisUrl ='/fundamental/balancesheetAnalysis/'+stockID;

var getProfitAndLossUrl ='/fundamental/profitAndLoss/'+stockID;
var getProfitAndLossAnalysisUrl='/fundamental/profitAndLossAnalysis/'+stockID;


var getYearlyReportUrl ='/fundamental/yearlyreport/'+stockID;

var getQuarerlyReportUrl ='/fundamental/quarterlyreport/'+stockID;

var getRatiosUrl = '/fundamental/ratios/'+stockID;
var getRatiosAnalysusUrl='/fundamental/ratioAnalysis/'+stockID;

var getCashFlowUrl = '/fundamental/cashflow/'+stockID;

var stockValuationUrl = '/fundamental/stockvaluation/'+stockID;
var getStockValuationUrl='/fundamental/getStockValuation/'+stockID;

var getAnalysisReport = '/fundamental/analysis/'+stockID;

var targetPriceUrl = '/fundamental/targetPrice/'+stockID;


var currentSharePrice = null;
function onReportLoad(){

    createCompanyTable();

    showBalancesheetDetails(5);

    showProfitAndLossDetails(5);
//    profitAndLossTable(5);
//    profitAndLossAnalysis(5);

    showYearlyReportDetails(5);
    try {
      showQuarterlyReportDetails(10);
    }
    catch(err) {
        console.log("Error while analysing quarterly result"+err.message);
        }

    try{
        showRatioDetails(5);
     }catch(err){
        console.log("Error while analysing quarterly result"+err.message);
    }
    try{
        showCashFlowDetails(12);
    }catch(err){
        console.log("Error while analysing quarterly result"+err.message);
    }
//    ratiosAnalysis(5);
//    console.log("onReportLoad triggered: url: "+generateReportUrl);
//    GetRawBookContent(generateReportUrl);
    try{
        showEVEbitdaValue(5);
    } catch(err){
        console.log("Error while analysing quarterly result"+err.message);
    }

    futureaverageGrowthRate();
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

    var currentPV = document.createElement("TH");
    currentPV.innerHTML="Current PB";
    row.appendChild(currentPV);
    var companyBeta = document.createElement("TH");
    companyBeta.innerHTML="Company Beta";
    row.appendChild(companyBeta);

    var row2 = table.insertRow();
    var sharepriceVal = document.createElement("TD");
    sharepriceVal.innerHTML=companyDetails.currentSharePrice;
    row2.appendChild(sharepriceVal);
    currentSharePrice = companyDetails.currentSharePrice;

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

      var currentPVVal = document.createElement("TD");
       currentPVVal.innerHTML=companyDetails.currentpv;
        row2.appendChild(currentPVVal);
    var companyBetaVal = document.createElement("TD");
    companyBetaVal.innerHTML=companyDetails.cmpBeta;
    row2.appendChild(companyBetaVal);

    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);
}

function getCompanyDetails(){
    var jsonResonse = GetRawBookContent(companyDetailsURL);
    console.log("CompanyDetails: "+jsonResonse);
    var companyDetails = JSON.parse(jsonResonse);
    return companyDetails;
}


/*====================== Get Balance Sheet Details =========================*/
function balancesheetDataPoints(){
    var datapoints = new Array();
    datapoints.push(["Total Share Capital","total_share_capital","Continuous Dilution of Total Share Capital is a bad sign, as it will reduce earning per share which would play a role to reduce stock price."]);
    datapoints.push(["Equity Share Capital","equity_share_capital","Continuous Dilution of Equity Share Capital is a bad sign, as it will reduce earning per share which would play a role to reduce stock price."]);
    datapoints.push(["Reserves and Surplus","reserves","Total amount of reserve and slurplus"]);
    datapoints.push(["Total Debt","debt","Total debts "]);
    return datapoints;
}


function showBalancesheetDetails(years){
    var url = getBalancesheetUrl+'/'+years;
    console.log("Getting data from url: "+url);
    var jsonResonse = GetRawBookContent(url);
    console.log("BalanceSheet Json res: "+jsonResonse);
    var headerList = createHeaderFromResponse(jsonResonse);
    var balancesheetFY = createBalancesheetDataPoints(jsonResonse);
    balancesheetTable(headerList,balancesheetFY);
    balancesheetAnalysis(years);
}

function createBalancesheetDataPoints(balancesheetJson){
    var balancesheetList = JSON.parse(balancesheetJson);
    console.log("inside createBalancesheetDataPoints: datapoints count: "+balancesheetList.length);

    var balancesheetFY = new Map();
    for(var i =0;i<balancesheetList.length;i++){
        var balancesheet = balancesheetList[i];
        var bl = new Map();
        bl['total_share_capital']=balancesheet.total_share_capital;
        bl['equity_share_capital']=balancesheet.equity_share_capital;
        bl['reserves']=balancesheet.reserves;
        bl['debt']=balancesheet.debt;
        balancesheetFY[balancesheet.date] = bl;
    }
    return balancesheetFY;
}

function balancesheetTable(headerList, balancesheetFY){
    console.log("creaing Balancesheet table: ");
    var datapoints = balancesheetDataPoints();
//    var headerList =createHeaders(years);
    console.log("Generated Headers: "+headerList);
    var tableID="bltbl";
    var tableEle = document.getElementById(tableID);
    if(tableEle!=null){
            tableEle.remove();
        }
//    createTable("balancesheet-tbl",tableID,headerList,datapoints,balancesheetFY);
    createTableWithToolTips("balancesheet-tbl",tableID,headerList,datapoints,balancesheetFY);
}


function getBalancesheetData(years){
    var url = getBalancesheetUrl+'/'+years;
    console.log("Getting data from url: "+url);
    var jsonResonse = GetRawBookContent(url);
    console.log("BalanceSheet Json res: "+jsonResonse);
    var balancesheetList = JSON.parse(jsonResonse);
    console.log("Individual item: "+balancesheetList[2].total_share_capital);
    var balancesheetFY = createBalancesheetDataPoints(jsonResonse);
    return balancesheetFY;
}




function getBalancesheetAnalysis(years){
    var url = getBalancesheetAnalysisUrl+'/'+years;
    console.log("Getting data from url: "+url);
    var jsonResonse = GetRawBookContent(url);
    console.log("BalanceSheetAnalysis Json res: "+jsonResonse);
    var balancesheetAnalysis = JSON.parse(jsonResonse);
    console.log("Balancesheet Analysis info: "+balancesheetAnalysis.totalShareChangePercentage);
    return balancesheetAnalysis;
}

function balanceSheetTips(){
    var tips= "Good Company Balancesheet"+"<br>"+
               "=============================="+"<br>"+
               "share capital = constant/almost constant" +"<br>"+
               "Reserves = increasing" +"<br>"+
               "Debt = decreasing" +"<br><br>"+

               "Average Company Balancesheet"+"<br>"+
                "=============================="+"<br>"+
                "share capital = dilutes" +"<br>"+
                "Reserves = almost constant/ slow growth" +"<br>"+
                "Debt = almost constant/increasing" +"<br><br>"+

                 "BAD Company Balancesheet"+"<br>"+
                "=============================="+"<br>"+
                "share capital = dilutes" +"<br>"+
                "Reserves = almost constant/ slow growth" +"<br>"+
                "Debt = Increasing" +"<br><br>"+
                "Avoid Companies instantly if balancesheet shows poor."
return tips;
}
/*
{"totalShareChangePercentage":91.45862051360596,"increaseIncidentInTotalShare":2,"reservesChangePercentage":73.64442774604566,"debtChangePercentage":-100.0}
*/
function balancesheetAnalysis(years){
    var balAnal = getBalancesheetAnalysis(years);
    console.log("totalShareChangePercentage "+balAnal.totalShareChangePercentage)
    var divID = "balancesheet_analysis_div";
    var tableID = "balancesheet-analysis-tbl";

    var ele = document.getElementById(tableID);
    if(ele!=null){
        ele.remove();
    }

    var table = document.createElement("TABLE");
    table.setAttribute('id', tableID);
    var row0 = table.insertRow();

    createToolTip(row0,balanceSheetTips());

    var row1 = table.insertRow();
    var grossShareChangeMargin = 5;
    var cell1 = document.createElement("TD");
    cell1.innerHTML = "Total Share Capital Change: "+ balAnal.totalShareChangePercentage;
    if(balAnal.totalShareChangePercentage>grossShareChangeMargin){
        cell1.setAttribute("class", "analyzedbad");
    }
    else if(balAnal.totalShareChangePercentage==0){
        cell1.setAttribute("class", "analyzedgood");
    }
    row1.appendChild(cell1);

    var row2 = table.insertRow();
    var grossincidentmargin = 5;
    var cell2 = document.createElement("TD");
    cell2.innerHTML = "Increase Incident in Total Share Capital: "+ balAnal.increaseIncidentInTotalShare;
    if(balAnal.increaseIncidentInTotalShare>grossincidentmargin){
        cell2.setAttribute("class", "analyzedbad");
    }
    row2.appendChild(cell2);

    var row3 = table.insertRow();
    var grossReserveChangeMargin = 5;
    var cell3 = document.createElement("TD");
    cell3.innerHTML = "Change Percentage in Reserves: "+ balAnal.reservesChangePercentage;
    if(balAnal.reservesChangePercentage>grossReserveChangeMargin){
        cell3.setAttribute("class", "analyzedgood");
    }
    else if(balAnal.reservesChangePercentage<0){
        cell3.setAttribute("class", "analyzedbad");
    }
    row3.appendChild(cell3);


    var row4 = table.insertRow();
    var grossDebtChangeMargin = 1;
    var cell4 = document.createElement("TD");
    cell4.innerHTML = "Change Percentage in Debt: "+ balAnal.debtChangePercentage;
    if(balAnal.debtChangePercentage>grossDebtChangeMargin){
        console.log("Setting className: analyzedbad");
        cell4.setAttribute("class", "analyzedbad");
    }
    else if(balAnal.debtChangePercentage<0){
        cell4.setAttribute("class", "analyzedgood");
    }
    row4.appendChild(cell4);

    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);

    balancesheetCalcAnalysis(balAnal);
    showBalancesheetGrowthRate(balAnal,years);

    showAnalysisStatement(divID,balAnal);

}


function showBalancesheetGrowthRate(balAnal,years){
    console.log("Calculating  Balancesheet GrowthRate:");
    balancesheetGrowthsDtoMap = balAnal.balancesheetGrowthsDtoMap;
    headerList = Object.keys(balancesheetGrowthsDtoMap);
//    console.log("Fetched ratioGrowthsDtoMap: ");
    console.log(balancesheetGrowthsDtoMap);
    var dataPoints = balancesheetDataPoints();
    console.log("showGrowthRate datapoints: "+dataPoints);
    console.log("headerList:" +headerList );
    for(var i=0;i<dataPoints.length;i++){
        //we can not calculate growth for the very first year. .
        for(var j=0;j<headerList.length;j++){
                   var cellid = dataPoints[i][1]+"-"+headerList[j];
//                   console.log(cellid);
                   var growthMap = balancesheetGrowthsDtoMap[headerList[j]];
//                   console.log("cellid: "+cellid+" growthMap: "+growthMap );
                   createGrowthLabel(cellid, growthMap[dataPoints[i][1]]);
            }
    }
   }

function balancesheetCalcAnalysis(balAnal){
    console.log("balancesheet score: "+balAnal.balanceSheetScore);
    console.log("debtToReserveRatioMap: "+balAnal.debtToReserveRatioMap);
    var debtToResrveRatioMap = balAnal.debtToReserveRatioMap;
    console.log(debtToResrveRatioMap);

    var headers = new Array();
    headers.push("Analysis");
    dates = Object.keys(debtToResrveRatioMap);
    for(var i=0;i<dates.length;i++){
        headers.push(dates[i]);
    }

    var datapoints = new Array();
    datapoints.push(["Debt-To-Reserve Ratio","Less Debt-To-Reserve Ratio means chances of getting bancrrupt for the company is less"]);
    for(var i=1;i<headers.length;i++){
        datapoints[0].push(debtToResrveRatioMap[headers[i]]);
    }

    console.log(datapoints);
    console.log("Headers: "+headers);

    var divID = "balanceshee_calc_div";
    var tableID = "balancesheet-calc-tbl";

    var ele = document.getElementById(tableID);
    if(ele!=null){
        ele.remove();
    }
    var table = document.createElement("TABLE");
    table.setAttribute('id', tableID);

    var row1 = table.insertRow();
    for(var i=0;i<headers.length;i++){

       var celli = document.createElement("TH");
       celli.innerHTML = headers[i];
       row1.appendChild(celli);
    }
    for(var i=0;i<datapoints.length;i++){
        var rowi = table.insertRow();
        var cellj = null;
        for(var j=0;j<datapoints[i].length;j++){
            if(j==1){
                            cellj.setAttribute("class","itemtooltip");
                            var span = document.createElement("SPAN");
                            span.setAttribute("class","tooltiptext");
                            span.innerHTML=datapoints[i][j];
                            cellj.appendChild(span);
                            continue;
                     }
             cellj = document.createElement("TD");
             cellj.innerHTML =datapoints[i][j];
             rowi.appendChild(cellj);

              if((i%2)==0){
                         bgColor = "white";
                 }
                 else{
                     bgColor="lightgray";
                 }

               rowi.style.backgroundColor=bgColor;
        }

    }
    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);

//    var scoreLbl = document.getElementById("bl_score_val");
//    scoreLbl.innerHTML = balAnal.balanceSheetScore;

    showBLScore(balAnal);
}


function createBLScroreDataPoints(dataJson){
//    var dataJsonList = JSON.parse(dataJson);
    console.log(dataJson);
    var plFY = new Map();
    var datamap = new Map();
    datamap['equityCapitalScore'] = dataJson.equityCapitalScore;
    datamap['reserveScore'] = dataJson.reserveScore;
    datamap['debtScore'] = dataJson.debtScore;
    datamap['balanceSheetScore'] = dataJson.balanceSheetScore;
//    console.log(datamap);
    plFY["Value"] = datamap;

    return plFY;
}


function showBLScore(jsonResonse){
    var parentDiv = document.getElementById("bl_score_div");
    var tableID = "bl_score_tbl";
    var dataFY = createBLScroreDataPoints(jsonResonse);
    var header = new Array();
    header.push("Score");
    header.push("Value");

     var tmpdatapoints = new Array();
     tmpdatapoints.push(["Equity Share Capital Score","equityCapitalScore"," This score is calcualted based on last N years performance. More recent year more impact, More previous year less impact. Higher value means less Diluton of share. <br> Over 5 years: Score = 20 -> No dilution over 5 years, value >20 means share capital buy back happened, value<20 means share capital dilution happened."]);
     tmpdatapoints.push(["Reserve Score","reserveScore","This score is calcualted based on last N years performance. More recent year more impact, More previous year less impact. More reserve is a good sign"]);
     tmpdatapoints.push(["Debt Score","debtScore","This score is calcualted based on last N years performance. More recent year more impact, More previous year less impact. Positive value means overall debt is reduced. This is a good sign"]);
     tmpdatapoints.push(["Total Balancesheet Score","balanceSheetScore","This score is calcualted based on last N years performance. "]);

    var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

    createTableWithToolTips("bl_score_div",tableID,header,tmpdatapoints,dataFY);
    var valrow1 = document.getElementById("equityCapitalScore-Value");
    var value1 = valrow1.innerHTML;
    if(value1>0)
        valrow1.style.color='green';
    else
        valrow1.style.color='red';

    var valrow2 = document.getElementById("reserveScore-Value");
    var value2 = valrow2.innerHTML;
    if(value2>0)
        valrow2.style.color='green';
    else
        valrow2.style.color='red';

    var valrow3 = document.getElementById("debtScore-Value");
    var value3 = valrow3.innerHTML;
    if(value3>0)
        valrow3.style.color='green';
    else
        valrow3.style.color='red';

    var valrow4 = document.getElementById("balanceSheetScore-Value");
    var value4 = valrow4.innerHTML;
    if(value4>0)
        valrow4.style.color='green';
    else
        valrow4.style.color='red';
}

function onBLYearSelection(){
   var years = document.getElementById("blyears").value;
   console.log("Years selection : "+years);
   showBalancesheetDetails(years);
//   balancesheetTable(years);

//   balancesheetAnalysis(years);
}



/*====================== Get Profit And Loss Details =========================*/
function plDataPoints(){
    var datapoints = new Array();
    datapoints.push(["Net Sales","netSales"," Net Sales"]);
    datapoints.push(["Consumption Of Raw Material","consumptionRawMaterial"," Increasing Raw Material cost with increasing sales means company has a vison to grow"]);
    datapoints.push(["Employee Cost","employeeCost"," Increasing Employee cost with increasing sales means company has a vison to grow"]);
    datapoints.push(["PBIT","pbit","PBIT measures an enterprise???s profitability by subtracting operating expenses from profit, while excluding tax and interest costs"]);
    datapoints.push(["Interest","interest",". It represents interest payable on any borrowings ??? bonds, loans, convertible debt or lines of credit"]);
    datapoints.push(["Net Profit","netProfit","Net Profit Reported for the financial Year. For an Ideal company Net Profit should be <= Cash Flow from Operating Activity."]);
    return datapoints;
}

function showProfitAndLossDetails(years){

    var jsonResonse = getDataFY(getProfitAndLossUrl,years);
    console.log("Profit And Loss Json res: "+jsonResonse);
    var headerList = createHeaderFromResponse(jsonResonse);
    var dataFY = createPlDataPoints(jsonResonse)
    profitAndLossTable(headerList,dataFY);
    profitAndLossAnalysis(years);
}

function createPlDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createPlDataPoints: datapoints count: "+dataJsonList.length);

    var plFY = new Map();
    for(var i =0;i<dataJsonList.length;i++){
        var data = dataJsonList[i];
        var datamap = new Map();
        datamap['netSales']=data.netSales;
        datamap['consumptionRawMaterial']=data.consumptionRawMaterial;
        datamap['employeeCost']=data.employeeCost;
        datamap['pbit']=data.pbit;
        datamap['interest']=data.interest;
        datamap['netProfit']=data.netProfit;
        plFY[data.date] = datamap;
    }
    return plFY;
}

function profitAndLossTable(headerList,dataFY){
//     var jsonResonse = getDataFY(getProfitAndLossUrl,years);
//    var dataFY = createPlDataPoints(jsonResonse);
    console.log("creaing profitAndLossTable: ");
    var datapoints = plDataPoints();

//    var headerList =createHeaders(years);
//    console.log("Generated Headers: "+headerList);
//    console.log(dataFY)
     var tableID="pltbl";
        var tableEle = document.getElementById(tableID);
        if(tableEle!=null){
                tableEle.remove();
            }
//    createTable("pl-tbl",tableID,headerList,datapoints,dataFY);
    createTableWithToolTips("pl-tbl",tableID,headerList,datapoints,dataFY);
}


function getProfitAndLossAnalysis(years){
    var url = getProfitAndLossAnalysisUrl+'/'+years;
    console.log("Getting data from url: "+url);
    var jsonResonse = GetRawBookContent(url);
    console.log("ProfitAndLossAnalysis Json res: "+jsonResonse);
    var profitAndLossAnalysis = JSON.parse(jsonResonse);
    return profitAndLossAnalysis;
}


function profitAndLossTips(){
    var tips= "Ideal Company Profit And Loss Statement"+"<br>"+
               "==================================="+"<br>"+
               "Net Sales = Grow" +"<br>"+
               "Consumption of material = Grow" +"<br>"+
               "Employee Cost = Grow" +"<br><br>"+
               "PBIT = Grow" +"<br><br>"+
               "Interest = Decrease" +"<br><br>"+
               "Net Profit = Grow" +"<br><br>"+

                "PBIT: Profit before Interest and Tax"+"<br>"+
                 "This is known as P/L Before other inc., int, except items & tax."
return tips;
}
/*
{"netSalesGrowthPercentage":-13.218246467423631,"isSalesGrowthContinuous":false,"rawMaterialGrowthPercentage":100.0,"PBITGrowthPercentage":0.0,"interestDecreasePercentage":1.364256480218281,"netProfitGrowthPercentage":0.12040939193257075}
*/
function profitAndLossAnalysis(years){
    var plAnal = getProfitAndLossAnalysis(years);
    console.log("netSalesGrowthPercentage "+plAnal.netSalesGrowthPercentage)
    var divID = "profitAndLoss_analysis_div";
    var tableID = "profitAndLoss-analysis-tbl";
    try{
        showPLGrowthRate(plAnal);
    }catch(err){
        console.log(err);
    }
    showAnalysisStatement(divID,plAnal);

    profitAndLossCalcAnalysis(plAnal);
}


function showPLGrowthRate(plAnal){
    console.log("Calculating  ProfitAndLoss GrowthRate:");
    growthsDtoMap = plAnal.growthsDtoMap;
    headerList = Object.keys(growthsDtoMap);
//    console.log("Fetched ratioGrowthsDtoMap: ");
    console.log(growthsDtoMap);
    var dataPoints = plDataPoints();
    console.log("showGrowthRate datapoints: "+dataPoints);
    console.log("headerList:" +headerList );
    for(var i=0;i<dataPoints.length;i++){
        //we can not calculate growth for the very first year. .
        for(var j=0;j<headerList.length;j++){
                   var cellid = dataPoints[i][1]+"-"+headerList[j];
//                   console.log(cellid);
                   var growthMap = growthsDtoMap[headerList[j]];
//                   console.log("cellid: "+cellid+" growthMap: "+growthMap );
                   createGrowthLabel(cellid, growthMap[dataPoints[i][1]]);
            }
    }
   }

function profitAndLossCalcAnalysis(plAnal){
    console.log("netProfitVsSalesRatio: "+plAnal.netProfitVsSalesRatio);
    var netProfitVsSalesRatio = plAnal.netProfitVsSalesRatio;
    console.log(netProfitVsSalesRatio);

    var headers = new Array();
    headers.push("Analysis");
    dates = Object.keys(netProfitVsSalesRatio);
    for(var i=0;i<dates.length;i++){
        headers.push(dates[i]);
    }

    var datapoints = new Array();
    // here each row will contain all years record for particular analysis.
    datapoints.push(["NetProfit-Vs-Sales Ratio","Higher the number means company is making more profit."]);
    for(var i=1;i<headers.length;i++){
        datapoints[0].push(netProfitVsSalesRatio[headers[i]]);
    }

    console.log(datapoints);
    console.log("Headers: "+headers);

    var divID = "pl_calc_div";
    var tableID = "pl-calc-tbl";

    var ele = document.getElementById(tableID);
    if(ele!=null){
        ele.remove();
    }
    var table = document.createElement("TABLE");
    table.setAttribute('id', tableID);

    var row1 = table.insertRow();
    for(var i=0;i<headers.length;i++){

       var celli = document.createElement("TH");
       celli.innerHTML = headers[i];
       row1.appendChild(celli);
    }
    for(var i=0;i<datapoints.length;i++){
        var rowi = table.insertRow();
        var cellj = null;
        for(var j=0;j<datapoints[i].length;j++){
            if(j==1){
                            cellj.setAttribute("class","itemtooltip");
                            var span = document.createElement("SPAN");
                            span.setAttribute("class","tooltiptext");
                            span.innerHTML=datapoints[i][j];
                            cellj.appendChild(span);
                            continue;
                     }
             cellj = document.createElement("TD");
             cellj.innerHTML =datapoints[i][j];
             rowi.appendChild(cellj);

              if((i%2)==0){
                         bgColor = "white";
                 }
                 else{
                     bgColor="lightgray";
                 }

               rowi.style.backgroundColor=bgColor;
        }

    }
    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);
    showPLScore(plAnal);
//    var scoreLbl = document.getElementById("pl_score_val");
//    scoreLbl.innerHTML = plAnal.profiAndLossScore;

}

function createPLScroreDataPoints(dataJson){
//    var dataJsonList = JSON.parse(dataJson);
    console.log(dataJson);
    var plFY = new Map();
    var datamap = new Map();
    datamap['netProfitScore'] = dataJson.netProfitScore;
    datamap['netSalesScore'] = dataJson.netSalesScore;
    datamap['interestScore'] = dataJson.interestScore;
    datamap['profiAndLossScore'] = dataJson.profiAndLossScore;

    plFY["Value"] = datamap;

    return plFY;
}


function showPLScore(jsonResonse){
    var parentDiv = document.getElementById("pl_score_div");
    var tableID = "pl_score_tbl";
    var dataFY = createPLScroreDataPoints(jsonResonse);
    var header = new Array();
    header.push("Score");
    header.push("Value");

     var tmpdatapoints = new Array();
     tmpdatapoints.push(["Net Profit Score","netProfitScore"," This score is calcualted based on last N years performance. More recent year more impact, More previous year less impact"]);
     tmpdatapoints.push(["Net Sales Score","netSalesScore","This score is calcualted based on last N years performance. More recent year more impact, More previous year less impact"]);
     tmpdatapoints.push(["Interest Score","interestScore","This score is calcualted based on last N years performance. More recent year more impact, More previous year less impact. Positive value means overall interest is reduced. This is a good sign"]);
     tmpdatapoints.push(["Total Profit And Loss Score","profiAndLossScore","This score is calcualted based on last N years performance. Score importance order Net Profit Score>Net Sales Score>Interest Score"]);

    var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

    createTableWithToolTips("pl_score_div",tableID,header,tmpdatapoints,dataFY);
    var valrow1 = document.getElementById("netProfitScore-Value");
    var value1 = valrow1.innerHTML;
    if(value1>0)
        valrow1.style.color='green';
    else
        valrow1.style.color='red';

    var valrow2 = document.getElementById("netSalesScore-Value");
    var value2 = valrow2.innerHTML;
    if(value2>0)
        valrow2.style.color='green';
    else
        valrow2.style.color='red';

    var valrow3 = document.getElementById("interestScore-Value");
    var value3 = valrow3.innerHTML;
    if(value3>0)
        valrow3.style.color='green';
    else
        valrow3.style.color='red';

    var valrow4 = document.getElementById("profiAndLossScore-Value");
    var value4 = valrow4.innerHTML;
    if(value4>0)
        valrow4.style.color='green';
    else
        valrow4.style.color='red';
}


function onPLYearSelection(){
   var years = document.getElementById("plyears").value;
   console.log("Years selection : "+years);
//   profitAndLossTable(years);
//   profitAndLossAnalysis(years);
    showProfitAndLossDetails(years);

}


/*====================== Get Yearly Report Details =========================*/
function showYearlyReportDetails(years){
        var jsonResonse = getDataFY(getYearlyReportUrl,years);
        console.log("Yearly Report Json res: "+jsonResonse);
        var headerList = createHeaderFromResponse(jsonResonse);
        var dataFY = createYlDataPoints(jsonResonse)
        yearlyReportTable(headerList,dataFY);

        var url = getAnalysisReport+'/yearlyreport/'+years;
        console.log("Getting data from url: "+url);
        var analyzedjsonResonse = GetRawBookContent(url);
        console.log("Yearly Report Analysis Json response: "+jsonResonse);
        yearlyReportAnalysis(years,headerList,analyzedjsonResonse);
}

function yearltReportDataPoints(){
    var datapoints = new Array();
     datapoints.push(["Basic EPS","basicEPS"," PE Ratio ToolTips PlaceHolder"]);
     datapoints.push(["PBIT","pbit"," PE Ratio ToolTips PlaceHolder"]);
    return datapoints;
}

function createYlDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createYlDataPoints: datapoints count: "+dataJsonList.length);

    var plFY = new Map();
    for(var i =0;i<dataJsonList.length;i++){
        var data = dataJsonList[i];
        var datamap = new Map();
        datamap['basicEPS']=data.basicEPS;
        datamap['pbit']=data.pbit;
        datamap['epsGrowthRate'] = data.epsGrowthRate;
        plFY[data.date] = datamap;
    }
    return plFY;
}



function yearlyReportTable(headerList,dataFY){
    console.log("creaing yearlyReportTable: ");
    var datapoints = yearltReportDataPoints();
     var tableID="yltbl";
        var tableEle = document.getElementById(tableID);
        if(tableEle!=null){
                tableEle.remove();
            }
    createTableWithToolTips("yearly-tbl",tableID,headerList,datapoints,dataFY);
}
function yearlyReportAnalysis(years,headerList,analyzedjsonResonse){
    console.log("analyzing Yearly Report: "+analyzedjsonResonse);
    var analysis = JSON.parse(analyzedjsonResonse);
    console.log(analysis);
    showYearlyGrowthRate(analysis,headerList,years);
//    highlightedPoints(ratioAnalysis,years);
    yearlyReportEstmation(analyzedjsonResonse);
}


function showYearlyGrowthRate(analysis,headerList,years){
    console.log("Calculating  YearlyGrowthRate:");
    epsGrowthRate = analysis.epsGrowthRate;
//    console.log("Fetched ratioGrowthsDtoMap: ");
    console.log(epsGrowthRate);
//    var headerList =createHeaders(years);
//    var dataPoints = new Array();
//    dataPoints.push(["Basic EPS","basicEPS"," PE Ratio ToolTips PlaceHolder"]);
    console.log("headerList:" +headerList );
        //we can not calculate growth for the very first year. .
        for(var j=1;j<headerList.length-1;j++){
                   var cellid = "basicEPS-"+headerList[j];
                   var growthMap = epsGrowthRate[headerList[j]];
                   console.log("growthmap: "+growthMap);
                   createGrowthLabel(cellid, growthMap);
            }
    }
function createYearlyReportAnalysisDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createYearlyReportAnalysisDataPoints: datapoints count: "+dataJsonList.length);
    console.log(dataJsonList.estimatedEPSCAGR);
//    var afwpe = dataJsonList.pegRatioAnalysis;
    var plFY = new Map();
    var datamap = new Map();
    datamap['estimatedEPSCAGR'] = dataJsonList.estimatedEPSCAGR;
//    datamap['averageEPS'] = dataJsonList.averageEPS;
    datamap['avgGrowthEstimatedEPS'] = dataJsonList.avgGrowthEstimatedEPS;
    datamap['cagrGrowthEstimatedEPS'] = dataJsonList.cagrGrowthEstimatedEPS;
    plFY["Value"] = datamap;

    return plFY;
}
function yearlyReportEstmation(jsonResonse){
    console.log("inside yearlyReportEstmation:");
    var parentDivID = "yearly_analysis_div";
    var parentDiv = document.getElementById(parentDivID);
    var tableID = "yl_analysis_tbl";
    var dataFY = createYearlyReportAnalysisDataPoints(jsonResonse);
    var header = new Array();
    header.push("Analysis");
    header.push("Value");

     var tmpdatapoints = new Array();
     tmpdatapoints.push(["Estimated EPS (CAGR)","estimatedEPSCAGR"," EPS estimated using CAGR technique."]);
    tmpdatapoints.push(["EPS AVG growth","avgGrowthEstimatedEPS","Average EPS Growth over period of years"]);
    tmpdatapoints.push(["EPS CAGR growth","cagrGrowthEstimatedEPS","CAGR EPS Growth over period of years"]);

    var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

    createTableWithToolTips(parentDivID,tableID,header,tmpdatapoints,dataFY);
}

function onYLYearSelection(){
   var years = document.getElementById("ylyears").value;
   console.log("Years selection : "+years);
    showYearlyReportDetails(years);
}

/*====================== Get Quarterly Report Details =========================*/
function showQuarterlyReportDetails(years){
        var jsonResonse = getDataFY(getQuarerlyReportUrl,years);
        console.log("Quarterly Report Json res: "+jsonResonse);
        var headerList = createHeaderFromResponse(jsonResonse);
        var dataFY = createQlDataPoints(jsonResonse)
        quarterlyReportTable(headerList,dataFY);

        var url = getAnalysisReport+'/quarterlyreport/'+years;
        console.log("Getting data from url: "+url);
        var analyzedjsonResonse = GetRawBookContent(url);
        console.log("Quarterly Report Analysis Json response: "+analyzedjsonResonse);
        quarterlyReportAnalysis(years,headerList,analyzedjsonResonse);
}

function quarterlyReportDataPoints(){
    var datapoints = new Array();
     datapoints.push(["EPS","eps","Quarterly Earning per share"]);
     datapoints.push(["YOY Sales Growth","yoySalesGrowth","YOY sales growth"]);
     datapoints.push(["Net Profit","netprofit","Quarterly Net Profit. Growing is always a good sign"]);
    return datapoints;
}

function createQlDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createQlDataPoints: datapoints count: "+dataJsonList.length);

    var plFY = new Map();
    for(var i =0;i<dataJsonList.length;i++){
        var data = dataJsonList[i];
        var datamap = new Map();
        datamap['eps']=data.eps;
        datamap['yoySalesGrowth']=data.yoySalesGrowth;
        datamap['netprofit']=data.netprofit;
        plFY[data.date] = datamap;
    }
    return plFY;
}



function quarterlyReportTable(headerList,dataFY){
    console.log("creaing yearlyReportTable: ");
    var datapoints = quarterlyReportDataPoints();
     var tableID="qltbl";
        var tableEle = document.getElementById(tableID);
        if(tableEle!=null){
                tableEle.remove();
            }
    createTableWithToolTips("quarterly-tbl",tableID,headerList,datapoints,dataFY);
}
function quarterlyReportAnalysis(years,headerList,analyzedjsonResonse){
    console.log("analyzing Quarterly Report: "+analyzedjsonResonse);
    var analysis = JSON.parse(analyzedjsonResonse);
    console.log(analysis);
    showQuarterlyGrowthRate(analysis,headerList,years);
    quarterlyReporthighlightedPoints(analysis,years);
    quarterlyReportEstmation(analyzedjsonResonse);
}

function quarterlyReporthighlightedPoints(quarterlyAnalysis,years){

    var divID = "quarterly_analysis_stmt_div";

    showAnalysisStatement(divID,quarterlyAnalysis);
}



function showQuarterlyGrowthRate(analysis,headerList,years){
    console.log("Calculating  QuarterlyGrowthRate:");
    analyzedgrowthMap = analysis.quarterlyReportGrowthsDtoMap;
//    console.log("Fetched ratioGrowthsDtoMap: ");
    console.log(analyzedgrowthMap);
//    var headerList =createHeaders(years);
//    var dataPoints = new Array();
//    dataPoints.push(["Basic EPS","basicEPS"," PE Ratio ToolTips PlaceHolder"]);
//    console.log("headerList:" +headerList );
        //we can not calculate growth for the very first year. .
        for(var j=1;j<headerList.length-1;j++){
                   var cellid = "eps-"+headerList[j];
                   var growthMap = analyzedgrowthMap[headerList[j]];
                   console.log(growthMap);
                   createGrowthLabel(cellid, growthMap.eps);

                  var cellid = "netprofit-"+headerList[j];
                  var growthMap = analyzedgrowthMap[headerList[j]];
                  console.log(growthMap);
                  createGrowthLabel(cellid, growthMap.netprofit);
            }
    }
function createQuarterlyReportAnalysisDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createQuarterlyReportAnalysisDataPoints: datapoints count: "+dataJsonList.length);
    console.log(dataJsonList.estimatedEPSCAGR);
//    var afwpe = dataJsonList.pegRatioAnalysis;
    var plFY = new Map();
    var datamap = new Map();
    datamap['qestimatedEPSCAGR'] = dataJsonList.estimatedEPSCAGRStr;
    datamap['qttmEPS'] = dataJsonList.ttmEPSStr;

    plFY["Value"] = datamap;

    return plFY;
}
function quarterlyReportEstmation(jsonResonse){
    console.log("inside yearlyReportEstmation:");
    var parentDivID = "quarterly_analysis_div";
    var parentDiv = document.getElementById(parentDivID);
    var tableID = "ql_analysis_tbl";
    var dataFY = createQuarterlyReportAnalysisDataPoints(jsonResonse);
    var header = new Array();
    header.push("Analysis");
    header.push("Value");

     var tmpdatapoints = new Array();
     tmpdatapoints.push(["Estimated EPS (CAGR)","qestimatedEPSCAGR"," EPS estimated using CAGR technique."]);
     tmpdatapoints.push(["TTM EPS","qttmEPS","Average EPS over period of years"]);

    var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

    createTableWithToolTips(parentDivID,tableID,header,tmpdatapoints,dataFY);
}

function onQLYearSelection(){
   var years = document.getElementById("qlyears").value;
   console.log("Quarters selection : "+years);
    showQuarterlyReportDetails(years);
}
/*====================== Get Ratios Details =========================*/

function ratiosDataPoints(){
    var datapoints = new Array();
     datapoints.push(["PE Ratio","peRatio","PE ratio = (Current Share price/EPS). This ratio determine how much X times we are paying for a share. Higer PE ratio means price of share is high compare to earning per share(EPS). Though we should avoid stock with high PE ratio but if its peers are also trading in high PE ratio, there are chance that this industry has growth opportunity."]);
    datapoints.push(["PB Ratio","pbRatio",PBRatioToolTips()]);
    datapoints.push(["Return On Equity","roe",ROEToolTips()]);
    datapoints.push(["Enterprise Value","ev","EV calculates a company's total value or assessed worth. This includes market capitalization, debt and exclude cash. This gives true value of an enterprise."]);
    datapoints.push(["EV/EBITDA","evEbitda","EV/EBITDA compares the value of a company???debt included???to the company???s cash earnings less non-cash expenses.<br> EV/EBITDA Range: <0 = avoid.<br> between 0 to 2 = Future Growth uncertain <br> 4 to 5 = Neutral <br> 6 to 10 = Ideal for investment <br> 10 to 16 = Fair Valued <br> >20 = Over Valued<br> Compare with peers for actual valuation"]);
    datapoints.push(["Debt-To-Equity Ratio","debtToEquityRatio"," Debt to equity ratio placeholder"]);
    return datapoints;
}

function PBRatioToolTips(){
    var toolTips = "PB Ratio Shows how much (X times) premium we are paying for stock value."+"<br>"+
                    "Low PBRatio =  UnderValued - not much difference between stock price and book value"+"<br>"+
                    "High PBRatio = OverValued - stock price is much higher than book value"+"<br>"+
                    "For a good company PBRatio ranges 3 to 6"+"<br><br>"+
                    "Compare PBRatio with peers for valuation estimation";
    return toolTips;

}

function ROEToolTips(){
 var toolTips = "ROE = (net profit after tax)/(total shareholder's fund)."+"<br>"+
  "A rising ROE suggests that a company is increasing its profit generation without needing as much capital."+"<br>"+
   "It also indicates how well a company's management deploys shareholder capital."+"<br>"+
    "A higher ROE is usually better while a falling ROE may indicate a less efficient usage of equity capital. Company with Continuous rising ROE is a parameter of potential multibagger."
 return toolTips;
}

function createRatiosDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createRatiosDataPoints: datapoints count: "+dataJsonList.length);

    var plFY = new Map();
    for(var i =0;i<dataJsonList.length;i++){
        var data = dataJsonList[i];
        console.log(data);
        var datamap = new Map();
        datamap['peRatio']=data.peRatio;
        datamap['pbRatio']=data.pbRatio;
        datamap['roe']=data.roe;
        datamap['ev']=data.ev;
        datamap['evEbitda']=data.evEbitda;
        datamap['debtToEquityRatio']=data.debtToEquityRatio;
        plFY[data.date] = datamap;
    }
    return plFY;
}
function showRatioDetails(years){
    var jsonResonse = getDataFY(getRatiosUrl,years);
    console.log("Ratio Json res: "+jsonResonse);
    var headerList = createHeaderFromResponse(jsonResonse);
    var dataFY = createRatiosDataPoints(jsonResonse);
    ratiosTable(headerList,dataFY);

    var url = getRatiosAnalysusUrl+'/'+years;
    console.log("Getting data from url: "+url);
    var analyzedjsonResonse = GetRawBookContent(url);
    console.log("RatioAnalysis Json response: "+jsonResonse);
    ratiosAnalysis(years,headerList,analyzedjsonResonse);
    showforwardPE(analyzedjsonResonse);
    showPEG(analyzedjsonResonse);
}
function ratiosTable(headerList,dataFY){
//     var jsonResonse = getDataFY(getRatiosUrl,years);
//    var dataFY = createRatiosDataPoints(jsonResonse);
//    console.log("creating RatiosTable: "+dataFY.peRatio);
    var datapoints = ratiosDataPoints();

//    var headerList =createHeaders(years);
//    console.log("Generated Headers: "+headerList);
    console.log(dataFY)
     var tableID="rtbl";
        var tableEle = document.getElementById(tableID);
        if(tableEle!=null){
                tableEle.remove();
            }
    createTableWithToolTips("ratio-tbl-div",tableID,headerList,datapoints,dataFY);
}

function ratiosAnalysis(years,headerList,jsonResonse){
//    var url = getRatiosAnalysusUrl+'/'+years;
//    console.log("Getting data from url: "+url);
//    var jsonResonse = GetRawBookContent(url);
    console.log("Analyzing ratio:");
    var ratioAnalysis = JSON.parse(jsonResonse);
    showGrowthRate(ratioAnalysis,headerList,years);
    highlightedPoints(ratioAnalysis,years);
}


function showGrowthRate(ratioAnalysis,headerList,years){
    console.log("Calculating  GrowthRate:");
    ratioGrowthsDtoMap = ratioAnalysis.ratioGrowthsDtoMap;
//    console.log("Fetched ratioGrowthsDtoMap: ");
    console.log(ratioGrowthsDtoMap);
//    var headerList =createHeaders(years);
    var dataPoints = ratiosDataPoints();
    console.log("showGrowthRate datapoints: "+dataPoints);
    console.log("headerList:" +headerList );
    for(var i=0;i<dataPoints.length;i++){
        //we can not calculate growth for the very first year. .
        for(var j=1;j<headerList.length-1;j++){
                   var cellid = dataPoints[i][1]+"-"+headerList[j];
                   var growthMap = ratioGrowthsDtoMap[headerList[j]];
                   createGrowthLabel(cellid, growthMap[dataPoints[i][1]]);
            }
    }
}

function highlightedPoints(ratioAnalysis,years){

    var divID = "ratio_analysis_div";
    var tableID = "ratio_analysis-tbl";

    var ele = document.getElementById(tableID);
    if(ele!=null){
        ele.remove();
    }

    var table = document.createElement("TABLE");
    table.setAttribute('id', tableID);
    var row0 = table.insertRow();

    createToolTip(row0,profitAndLossTips());

    var row1 = table.insertRow();
    var grossSellChangeMargin = 5;
    var cell1 = document.createElement("TD");
    cell1.innerHTML = "Possibility for MultiBagger Stock: "+ ratioAnalysis.possibilityOfMultiBagger;
    if(ratioAnalysis.possibilityOfMultiBagger){
        cell1.setAttribute("class", "analyzedgood");
    }
    row1.appendChild(cell1);

     var row2 = table.insertRow();
        var grossSellChangeMargin = 5;
        var cell2 = document.createElement("TD");
        cell2.innerHTML = ratioAnalysis.ttmPEAnalysis;
        if(ratioAnalysis.ttmPEAnalysis.includes("Fair")){
            cell2.setAttribute("class", "analyzedgood");
        }
        else{
            cell2.setAttribute("class", "analyzedbad");
        }
        row2.appendChild(cell2);

    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);

    console.log(ratioAnalysis);
    showAnalysisStatement(divID,ratioAnalysis);
}

function createForwardPEDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
//    console.log("inside createForwardPEDataPoints: datapoints count: "+dataJsonList.length);
//    console.log(dataJsonList.forwardPEAnalysis);
    var afwpe = dataJsonList.forwardPEAnalysis;
    var plFY = new Map();
    var datamap = new Map();
    datamap['currentPE'] = afwpe.currentPE;
    datamap['yearlyForwardPE'] = afwpe.yearlyForwardPE;
    datamap['quarterlyForwardPE'] = afwpe.quarterlyForwardPE;
    datamap['forwardPE'] = afwpe.forwardPE;
    datamap['fwpevaluation'] = afwpe.valuation;

    plFY["Value"] = datamap;

    return plFY;
}

function showforwardPE(jsonResonse){
    var parentDiv = document.getElementById("forwardPE_div");
    var tableID = "forwardpetbl";
    var dataFY = createForwardPEDataPoints(jsonResonse);
    var header = new Array();
    header.push("Forward PE");
    header.push("Value");

     var tmpdatapoints = new Array();
     tmpdatapoints.push(["Current/TTM PE Ratio","currentPE"," Current TTM PE Ratio"]);
     tmpdatapoints.push(["Yearly Forward PE Ratio","yearlyForwardPE","Forward PE Ratio=Current Market Price/( Estimated EPS based on last 3 years CAGR Growth)"]);
     tmpdatapoints.push(["Quarterly Forward PE Ratio","quarterlyForwardPE","Forward PE Ratio = Current Market Price/(TTM EPS for last 4 quarters)"]);
     tmpdatapoints.push(["Forward PE Ratio","forwardPE","Forward PE gives idea about future valuation of stock for 1 down the line. Forward PE = (Yearly Forward PE + Quarterly Forward PE)/2."]);
     tmpdatapoints.push(["Forward PE Valuation","fwpevaluation","We should consider to invest in FAIR_VALUED stock. Avoid under valued stock or over valued stock. Consider to sell stock when it shows Over valued."]);

    var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

    createTableWithToolTips("forwardPE_div",tableID,header,tmpdatapoints,dataFY);
    var valrow = document.getElementById("fwpevaluation-Value");
    console.log("valrow value: "+valrow.innerHTML);
    var value = valrow.innerHTML;
    if(value=='FAIR_VALUED')
        valrow.style.color='green';
    else
        valrow.style.color='red';
}



function createPEGDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
//    console.log("inside createForwardPEDataPoints: datapoints count: "+dataJsonList.length);
//    console.log(dataJsonList.forwardPEAnalysis);
    var afwpe = dataJsonList.pegRatioAnalysis;
    var plFY = new Map();
    var datamap = new Map();
    datamap['pegRatio'] = afwpe.pegRatio;
    datamap['pegvaluation'] = afwpe.valuation;

    plFY["Value"] = datamap;

    return plFY;
}


function showPEG(jsonResonse){
    var parentDiv = document.getElementById("peg_div");
    var tableID = "peg_tbl";
    var dataFY = createPEGDataPoints(jsonResonse);
    var header = new Array();
    header.push("PEG Ratio");
    header.push("Value");

     var tmpdatapoints = new Array();
     tmpdatapoints.push(["PEG Ratio","pegRatio"," PEG Ratio = (PE Ratio)/(Growth in EPS). This helps to understand if growth in EPS justify PE. This is a stock valuation parameter."]);
     tmpdatapoints.push(["PEG Valuation","pegvaluation","For India Market, PEG <3 considered to be good for investment. PEG<1 is Ideal investment and could be invested.PEG negative means growth of earning per share (EPS) is negative. One should not consider such stock to invest"]);

    var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

    createTableWithToolTips("peg_div",tableID,header,tmpdatapoints,dataFY);
    var valrow = document.getElementById("pegvaluation-Value");
    var value = valrow.innerHTML;
    if(value=='FAIR_VALUED')
        valrow.style.color='green';
    else
        valrow.style.color='red';
}

function onRYearSelection(){
    var years = document.getElementById("ryears").value;
   console.log("Years selection : "+years);
//   ratiosTable(years);
//   ratiosAnalysis(years)
    showRatioDetails(years);
}

/*====================== Get CashFlow Details =========================*/
function showCashFlowDetails(years){
        var jsonResonse = getDataFY(getCashFlowUrl,years);
        console.log("Cashflow Json res: "+jsonResonse);
        var headerList = createHeaderFromResponse(jsonResonse);
        console.log("CashFLow HeaderList :" + headerList);
        var dataFY = createCashFlowDataPoints(jsonResonse);
        cashFlowTable(headerList,dataFY);

        var url = getAnalysisReport+'/cashflow/'+years;
        console.log("Getting data from url: "+url);
        var analyzedjsonResonse = GetRawBookContent(url);
        console.log("CashFlow Analysis Json response: "+analyzedjsonResonse);
        cashFlowAnalysis(years,headerList,analyzedjsonResonse);
}

function cashFlowDataPoints(){
    var datapoints = new Array();
     datapoints.push(["Cash From Operating Activity","cashFromOperatingActivity","Cash From Original Activity"]);
     datapoints.push(["Fixed Asset Purchased","fixedAssestsPurchased","Capital Expendeture"]);
     datapoints.push(["Net CashFlow","netCashFlow","Net CashFlow"]);
     datapoints.push(["Free CashFlow","freeCashFlow","Free CashFlow"]);
    return datapoints;
}

function createCashFlowDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createCashFlowDataPoints: datapoints count: "+dataJsonList.length);

    var plFY = new Map();
    for(var i =0;i<dataJsonList.length;i++){
        var data = dataJsonList[i];
        var datamap = new Map();
        datamap['cashFromOperatingActivity']=data.cashFromOperatingActivity;
        datamap['fixedAssestsPurchased']=data.fixedAssestsPurchased;
        datamap['netCashFlow']=data.netCashFlow;
        datamap['freeCashFlow']=data.freeCashFlow;
        plFY[data.date] = datamap;
    }
    return plFY;
}


//function quarterlyReportTable(headerList,dataFY){
//    console.log("creaing yearlyReportTable: ");
//    var datapoints = quarterlyReportDataPoints();
//     var tableID="qltbl";
//        var tableEle = document.getElementById(tableID);
//        if(tableEle!=null){
//                tableEle.remove();
//            }
//    createTableWithToolTips("quarterly-tbl",tableID,headerList,datapoints,dataFY);
//}
function cashFlowTable(headerList,dataFY){
    console.log("creaing cashFlowTable: ");
    var datapoints = cashFlowDataPoints();
     var tableID="cftbl";
        var tableEle = document.getElementById(tableID);
        if(tableEle!=null){
                tableEle.remove();
            }
    createTableWithToolTips("cf-tbl",tableID,headerList,datapoints,dataFY);
}
function cashFlowAnalysis(years,headerList,analyzedjsonResonse){
    console.log("analyzing CashFLow: "+analyzedjsonResonse);
    var analysis = JSON.parse(analyzedjsonResonse);
    console.log(analysis);
    showCashFlowGrowthRate(analysis,headerList,years);
    highlightedCashFlowPoints(analysis,years);
//    cashFlowEstmation(analyzedjsonResonse);
}



function showCashFlowGrowthRate(analysis,headerList,years){
//    console.log("Calculating  QuarterlyGrowthRate:");
//    epsGrowthRate = analysis.epsGrowthRate;
////    console.log("Fetched ratioGrowthsDtoMap: ");
//    console.log(epsGrowthRate);
////    var headerList =createHeaders(years);
////    var dataPoints = new Array();
////    dataPoints.push(["Basic EPS","basicEPS"," PE Ratio ToolTips PlaceHolder"]);
////    console.log("headerList:" +headerList );
//        //we can not calculate growth for the very first year. .
//        for(var j=1;j<headerList.length-1;j++){
//                   var cellid = "eps-"+headerList[j];
//                   var growthMap = epsGrowthRate[headerList[j]];
//                   console.log("growthmap: "+growthMap);
//                   createGrowthLabel(cellid, growthMap);
//            }
    }

function highlightedCashFlowPoints(cashFlowAnalysis,years){

    var divID = "cf_analysis_div";
    var tableID = "cf_analysis-tbl";

    var ele = document.getElementById(tableID);
    if(ele!=null){
        ele.remove();
    }

    var table = document.createElement("TABLE");
    table.setAttribute('id', tableID);
    var row0 = table.insertRow();

    createToolTip(row0,profitAndLossTips());

    var row1 = table.insertRow();
    var grossSellChangeMargin = 5;
    var cell1 = document.createElement("TD");
    cell1.innerHTML = cashFlowAnalysis.operatingCashFlowVsNetProficCmp;
    if(cashFlowAnalysis.operatingCashFlowVsNetProficCmp.includes("Avoid")){
        cell1.setAttribute("class", "analyzedbad");
    }
    else{
        cell1.setAttribute("class", "analyzedgood");
    }
    row1.appendChild(cell1);

     var row2 = table.insertRow();
        var grossSellChangeMargin = 5;
        var cell2 = document.createElement("TD");
        cell2.innerHTML = "CFO/PAt Ratio: "+cashFlowAnalysis.cfoPatRatio;
        if(cashFlowAnalysis.cfoPatRatio<1){
            cell2.setAttribute("class", "analyzedbad");
        }
        else{
            cell2.setAttribute("class", "analyzedgood");
        }
        row2.appendChild(cell2);

    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);

}

function createCashFLowAnalysisDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createQuarterlyReportAnalysisDataPoints: datapoints count: "+dataJsonList.length);
    console.log(dataJsonList.estimatedEPSCAGR);
    var plFY = new Map();
    var datamap = new Map();
    datamap['qestimatedEPSCAGR'] = dataJsonList.estimatedEPSCAGRStr;
    datamap['qttmEPS'] = dataJsonList.ttmEPSStr;

    plFY["Value"] = datamap;

    return plFY;
}
function cashFlowEstmation(jsonResonse){
    console.log("inside yearlyReportEstmation:");
    var parentDivID = "quarterly_analysis_div";
    var parentDiv = document.getElementById(parentDivID);
    var tableID = "ql_analysis_tbl";
    var dataFY = createQuarterlyReportAnalysisDataPoints(jsonResonse);
    var header = new Array();
    header.push("Analysis");
    header.push("Value");

     var tmpdatapoints = new Array();
     tmpdatapoints.push(["Estimated EPS (CAGR)","qestimatedEPSCAGR"," EPS estimated using CAGR technique."]);
     tmpdatapoints.push(["TTM EPS","qttmEPS","Average EPS over period of years"]);

    var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

    createTableWithToolTips(parentDivID,tableID,header,tmpdatapoints,dataFY);
}

function onCFYearSelection(){
   var years = document.getElementById("cfyears").value;
   console.log("Cashflow year selection : "+years);
    showCashFlowDetails(years);
}




function futureaverageGrowthRate(){
    var url = getStockValuationUrl+'/averageGrowthRate';
    var jsonResonse = GetRawBookContent(url);
    console.log("futureaverageGrowthRate: "+jsonResonse);
    var data = JSON.parse(jsonResonse);
    var fcfGrowthDto = data.fcfGrowthDto;
    var netProfitGrowthDto = data.netProfitGrowthDto;
    document.getElementById("fcfgr").innerHTML = fcfGrowthDto.growthRate;
    document.getElementById("npr").innerHTML = netProfitGrowthDto.growthRate;

}

/*====================== Quarterly Intrinsic Stock Valuation  =========================*/

function requestQtrIntriscStockValuation(){
    var peAvg = document.getElementById("peAvg").value;
    console.log("peAvg: "+peAvg);

    var request={
        "peAvg":peAvg
    };
    var requestBody = generateJsonString(request);
    console.log("requestQtrIntriscStockValuation request body:  "+requestBody);
    var url = stockValuationUrl+'/quarterlyIntrinsic';
    console.log("Posting data to url: "+url);
    var jsonResonse = postRequest(url,requestBody, showQtrIntrinsicStockValue);
}

function showQtrIntrinsicStockValue(){
    var url = getStockValuationUrl+'/quarterlyIntrinsic';
    var jsonResonse = GetRawBookContent(url);
    console.log("showQtrIntrinsicStockValue: response: "+jsonResonse)
    var targetPriceLbl = document.createElement("LABEL");
    targetPriceLbl.innerHTML="Calculated Target Price : "+ jsonResonse;
    targetPriceLbl.style.fontSize="large";
     var qtInEval = document.getElementById("qtInEval");
     qtInEval.appendChild(targetPriceLbl);
}


/*====================== Economic DCF Stock Valuation  =========================*/

function requestEconomicDCF(){
    var growR = document.getElementById("growR").value;
    console.log("Growth Rate: "+peAvg);

//    var iefy = document.getElementById("iefy").value;
//    console.log("Interest Expense(Last FY): "+iefy);

    var itefy = document.getElementById("itefy").value;
    console.log("Income Tax Expense(Last FY): "+itefy);

    var ibtfy = document.getElementById("ibtfy").value;
    console.log("Income Before Tax (Last FY): "+ibtfy);

    var rfr = document.getElementById("rfr").value;
    console.log("Risk Free Rate: "+rfr);

//    var cbeta = document.getElementById("cbeta").value;
//    console.log("Company Beta: "+cbeta);

    var mktret = document.getElementById("mktret").value;
    console.log("Market Return: "+mktret);


    var margR = document.getElementById("margR").value;
    console.log("margR: "+margR);

    var cashEQDCF = document.getElementById("cashEQDCF").value;
    console.log("cashEQDCF: "+cashEQDCF);

//    var debtDCF = document.getElementById("debtDCF").value;
//    console.log("debtDCF: "+debtDCF);

    var request={
        "growR":growR,
//        "iefy":iefy,
        "itefy":itefy,
        "ibtfy":ibtfy,
        "rfr":rfr,
//        "cbeta":cbeta,
        "mktret":mktret,
        "margR":margR,
        "cashEQDCF":cashEQDCF,
//        "debtDCF":debtDCF
    };

    var requestBody = generateJsonString(request);
    console.log("requestEconomicDCF request body:  "+requestBody);
    var url = stockValuationUrl+'/economicDCF';
    console.log("Posting data to url: "+url);
    var jsonResonse = postRequest(url,requestBody, showEconomicDCFValue);
}


function showEconomicDCFValue(){
    var url = getStockValuationUrl+'/economicDCF';
    var jsonResonse = GetRawBookContent(url);
    console.log("showEconomicDCFValue: response: "+jsonResonse);
    showEconomicDCFProjection(jsonResonse);
}


function createEconomicDCFDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
//    console.log("inside createForwardPEDataPoints: datapoints count: "+dataJsonList.length);
//    console.log(dataJsonList.forwardPEAnalysis);
//    var afwpe = dataJsonList.pegRatioAnalysis;
    var plFY = new Map();
    var datamap = new Map();
    datamap['emdcfdiscountrate'] = dataJsonList.discountRate;
    datamap['emdcftargetPrice'] = dataJsonList.targetPrice;
    datamap['emdcfpriceAfterMarginOfSafty'] = dataJsonList.priceAfterMarginOfSafty;
    datamap['emdcfupside'] = dataJsonList.upside;
    datamap['emdcfdecision'] = dataJsonList.decision;

    plFY["Value"] = datamap;

    return plFY;
}

function showEconomicDCFProjection(jsonResonse){
       var parentDiv = document.getElementById("ecmDcf");
       var tableID = "ecm_dcf_tbl";
       var dataJsonList = createEconomicDCFDataPoints(jsonResonse);
       console.log(dataJsonList);
       var header = new Array();
       header.push("DCF Model Estimation");
       header.push("Value");

        var tmpdatapoints = new Array();
        tmpdatapoints.push(["Discount Rate (WACC)","emdcfdiscountrate","The weighted average cost of capital (WACC) is a financial metric that shows what the total cost of capital (the interest rate paid on funds used for financing operations) is for a firm."]);
        tmpdatapoints.push(["Intrinsic Value","emdcftargetPrice"," True value of company based on free cash flow. For good investment Projected Intrinsic value should be higher than current Market value."]);
        tmpdatapoints.push(["Intrinsic Value After Margin of Safty","emdcfpriceAfterMarginOfSafty","Considering target price after provided margin of safty"]);
        tmpdatapoints.push(["Upside","emdcfupside","upside = estimatedPricePerShare/currentSharePrice. Consider to invest for DCF calculation years if upside>discount rate"]);
        tmpdatapoints.push(["Valuation","emdcfdecision","Valuation for buy or no buy for DCF calculation years. Again Market value runs with sentiment while intrinsic value runs with actual assets and free cashflow of company. If company is not generating cashflow in a stable manner, DCF might not give proper intrinsic valuation."]);

       var ele = document.getElementById(tableID);
           if(ele!=null){
               ele.remove();
           }

       createTableWithToolTips("ecmDcf",tableID,header,tmpdatapoints,dataJsonList);
       var valrow = document.getElementById("emdcfdecision-Value");
       var value = valrow.innerHTML;
       if(value=='NO_BUY')
           valrow.style.color='red';
       else
           valrow.style.color='green';


}
function createDCFHeader(nextNYearsFreeCashFlow){
    var yearcount = nextNYearsFreeCashFlow.length;
    for(var i=1;i<=yearcount;i++){

    }
}

/*====================== Calculate WAAC  =========================*/

function calculateWaac(){

    var itefy = document.getElementById("itefy").value;
    itefy = itefy.replace(",","");
    console.log("Income Tax Expense(Last FY): "+itefy);

    var ibtfy = document.getElementById("ibtfy").value;
    ibtfy = ibtfy.replace(",","");
    console.log("Income Before Tax (Last FY): "+ibtfy);

    var rfr = document.getElementById("rfr").value;
    rfr = rfr.replace(",","");
    console.log("Risk Free Rate: "+rfr);

    var mktret = document.getElementById("mktret").value;
    mktret = mktret.replace(",","");
    console.log("Market Return: "+mktret);

    var request={
        "incomeTaxExpense":itefy,
        "incomeBeforeTax":ibtfy,
        "riskFreeRate":rfr,
        "marketReturn":mktret
    };

    var requestBody = generateJsonString(request);
    console.log("calculateWaac request body:  "+requestBody);
    var url = stockValuationUrl+'/waac';
    console.log("Posting data to url: "+url);
    var jsonResonse = postRequest(url,requestBody, showWaacValue);
}

function showWaacValue(){
    var url = getStockValuationUrl+'/waac';
    var jsonResonse = GetRawBookContent(url);
    console.log("showEconomicDCFValue: response: "+jsonResonse);
    var data = JSON.parse(jsonResonse);
    document.getElementById("discRate").innerHTML = data.discountRate+"%";
}

/*====================== EV/EBITDA Valuation =========================*/

function createEVEBITDADataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log(dataJsonList);
    console.log(dataJsonList.forcastedEV+" "+dataJsonList.expectedEBITDA+" "+dataJsonList.targetPrice);
    var plFY = new Map();
    var datamap = new Map();
    datamap['forcastedEV'] = dataJsonList.forcastedEV;
    datamap['expectedEBITDA'] = dataJsonList.expectedEBITDA;
    datamap['targetPrice'] = dataJsonList.targetPrice;
    datamap['targetPriceAfterMarginOfSafty'] = dataJsonList.targetPriceAfterMarginOfSafty;
    datamap['evebitdaupside'] = dataJsonList.upside;
    plFY["Value"] = datamap;
    console.log(datamap);
    return plFY;
}

function showEVEBITDAProjection(jsonResonse){
        console.log("inside showEVEBITDAProjection: ")
       var parentDiv = document.getElementById("ev_ebitda_div");
       var tableID = "ev_ebitda_div_tbl";
       var dataJsonList = createEVEBITDADataPoints(jsonResonse);
       console.log(dataJsonList);
       var header = new Array();
       header.push("EV/EBITDA Model Estimation");
       header.push("Value");

        var tmpdatapoints = new Array();
        tmpdatapoints.push(["Expected EBITDA","expectedEBITDA","Expected EBITDA over next 1 year forward"]);
        tmpdatapoints.push(["Forcasted EV","forcastedEV","Forcasted Enterprise value over next 1 year forward"]);
        tmpdatapoints.push(["Target Price ","targetPrice","Calculated Target Price"]);
        tmpdatapoints.push(["Target Price After Margin of Safty","targetPriceAfterMarginOfSafty","Target price after margin of safty"]);
        tmpdatapoints.push(["Upside","evebitdaupside","Upside"]);

       var ele = document.getElementById(tableID);
           if(ele!=null){
               ele.remove();
           }

       createTableWithToolTips("ev_ebitda_div",tableID,header,tmpdatapoints,dataJsonList);
       var valrow = document.getElementById("evebitdaupside-Value");
       var value = valrow.innerHTML;
       if(value>1)
           valrow.style.color='green';
       else
           valrow.style.color='red';



}
function evebitdavaluation(){
    var url = getStockValuationUrl+'/evebitda/5';
//    var url = targetPriceUrl+'/'+years;
    var jsonResonse = GetRawBookContent(url);
    var data = JSON.parse(jsonResonse);
    var targetPrice = data.targetPrice;
    document.getElementById("evebitdaestimation").innerHTML = targetPrice;
    targetPrice = parseFloat(targetPrice);
    if(targetPrice<currentSharePrice)
         document.getElementById("evebitdaestimation").style.color = "red";
        else
          document.getElementById("evebitdaestimation").style.color = "green";
}

function evEbitdaMrgnOfSfty(mrgnOfSfty){
    var targetPrice = document.getElementById("evebitdaestimation").innerHTML;
    targetPrice = parseFloat(targetPrice);
    console.log("[EV/EBITDA] mrgnOfSfty : "+mrgnOfSfty+" currentSharePrice: "+currentSharePrice+" targetPrice: "+targetPrice);
    var targetPriceMrgn = targetPrice*(1-(mrgnOfSfty/100));
    document.getElementById("evebitdamrgnsfty").innerHTML = targetPriceMrgn;
    console.log("[PEValuation] targetPriceMrgn: "+targetPriceMrgn);
    if(targetPriceMrgn>currentSharePrice)
            document.getElementById("evebitdamrgnsfty").style.color = "green";
    else
       document.getElementById("evebitdamrgnsfty").style.color = "red";

    var upside = (targetPriceMrgn-currentSharePrice)/currentSharePrice*100;
        document.getElementById("evebitdaupside").innerHTML = upside;

    if(upside<0)
     document.getElementById("evebitdaupside").style.color = "red";
    else
      document.getElementById("evebitdaupside").style.color = "green";
}

function showEVEbitdaValue(years){
    var url = getStockValuationUrl+'/evebitda/'+years;
//    var url = targetPriceUrl+'/'+years;
    var jsonResonse = GetRawBookContent(url);

    console.log("showEVEbitdaValue: response: "+jsonResonse);
    showEVEBITDAProjection(jsonResonse);
}


function marginofsafty(ele){
        var marginofsafty = ele.value;
        marginofsafty = parseFloat(marginofsafty);
        pevaluationMrgnOfSfty(marginofsafty);
        epsmultipliervaluationMrgnOfSfty(marginofsafty);
        netprofitvaluationMrgnOfSfty(marginofsafty);
        twophasedcfMrgnOfSfty(marginofsafty);
        evEbitdaMrgnOfSfty(marginofsafty);
        grahamMrgnOfSfty(marginofsafty);
}

/*====================== PE Valuation Model =========================*/


function peValuationMode(){
    console.log("peValuationMode action is triggered");
    window.open("pe-valuation-model.html?stockID="+stockID);
    window.focus();

    fetchPeValuationEstimation();
}
var pevaltm;
function fetchPeValuationEstimation() {
        var url = getStockValuationUrl+'/pevaluation';
        var jsonResonse = GetRawBookContent(url);
        console.log("showPEValuationFValue: response: "+jsonResonse);
        var data = JSON.parse(jsonResonse);
        console.log(" Fetching PE value: "+data);
        pevaltm = setTimeout(fetchPeValuationEstimation, 5000);
        if(data.evluated){
            var ele = document.getElementById("pevaluationestimation");
            ele.innerHTML = data.fairValuedTargetPrice;
//            document.getElementById("pevaluationestimationrgnsfty").innerHTML =data.fairValuedTargetPrice;
            var targetPrice = parseFloat(data.fairValuedTargetPrice);
            if(targetPrice<currentSharePrice)
                     document.getElementById("pevaluationestimation").style.color = "red";
                    else
                      document.getElementById("pevaluationestimation").style.color = "green";
            clearTimeout(pevaltm);
        }
}

function pevaluationMrgnOfSfty(mrgnOfSfty){
    var targetPrice = document.getElementById("pevaluationestimation").innerHTML;
    targetPrice = parseFloat(targetPrice);
    console.log("[PEValuation] mrgnOfSfty : "+mrgnOfSfty+" currentSharePrice: "+currentSharePrice+" targetPrice: "+targetPrice);
    var targetPriceMrgn = targetPrice*(1-(mrgnOfSfty/100));
    document.getElementById("pevaluationestimationrgnsfty").innerHTML = targetPriceMrgn;
    console.log("[PEValuation] targetPriceMrgn: "+targetPriceMrgn);
    if(targetPriceMrgn>currentSharePrice)
            document.getElementById("pevaluationestimationrgnsfty").style.color = "green";
    else
       document.getElementById("pevaluationestimationrgnsfty").style.color = "red";

    var upside = (targetPriceMrgn-currentSharePrice)/currentSharePrice*100;
        document.getElementById("pevaluationupside").innerHTML = upside;

    if(upside<0)
     document.getElementById("pevaluationupside").style.color = "red";
    else
      document.getElementById("pevaluationupside").style.color = "green";
}


/*====================== EPS Multiplier Valuation Model =========================*/

function epsmultiplierModel(){
    console.log("epsmultiplierModel action is triggered");
    window.open("eps-multiplier.html?stockID="+stockID);
    window.focus();

    fetchEPSMultiplerValuationEstimation();
}

var epsvaltm;
function fetchEPSMultiplerValuationEstimation(){
     var url = getStockValuationUrl+'/epsmultiplier';
            var jsonResonse = GetRawBookContent(url);
            console.log("showepsmultiplier: response: "+jsonResonse);
            var data = JSON.parse(jsonResonse);
            console.log(" Fetching epsmultiplier value: "+data);
            epsvaltm = setTimeout(fetchEPSMultiplerValuationEstimation, 5000);
            if(data.evluated){
                var ele = document.getElementById("epsmultiplierestimation");
                ele.innerHTML = data.finalIntrinsicValue;
//                document.getElementById("epsmultiplierestimationrgnsfty").innerHTML =data.fairValuedTargetPrice;
                var targetPrice = parseFloat(data.finalIntrinsicValue);
                if(targetPrice<currentSharePrice)
                    document.getElementById("epsmultiplierestimation").style.color = "red";
                else
                    document.getElementById("epsmultiplierestimation").style.color = "green";
                clearTimeout(epsvaltm);
            }
}


function epsmultipliervaluationMrgnOfSfty(mrgnOfSfty){
    var targetPrice = document.getElementById("epsmultiplierestimation").innerHTML;
    targetPrice = parseFloat(targetPrice);
    console.log("[PEValuation] mrgnOfSfty : "+mrgnOfSfty+" currentSharePrice: "+currentSharePrice+" targetPrice: "+targetPrice);
    var targetPriceMrgn = targetPrice*(1-(mrgnOfSfty/100));
    document.getElementById("epsmultiplierestimationrgnsfty").innerHTML = targetPriceMrgn;
    console.log("[PEValuation] targetPriceMrgn: "+targetPriceMrgn);
    if(targetPriceMrgn>currentSharePrice)
            document.getElementById("epsmultiplierestimationrgnsfty").style.color = "green";
    else
       document.getElementById("epsmultiplierestimationrgnsfty").style.color = "red";

    var upside = (targetPriceMrgn-currentSharePrice)/currentSharePrice*100;
        document.getElementById("epsmultiplierupside").innerHTML = upside;

    if(upside<0)
     document.getElementById("epsmultiplierupside").style.color = "red";
    else
      document.getElementById("epsmultiplierupside").style.color = "green";
}





/*====================== Net Profit Valuation Model =========================*/

function netprofitvaluationmodel(){
    console.log("netprofitvaluationmodel action is triggered");
    window.open("netprofitvaluation.html?stockID="+stockID);
    window.focus();

    fetchNetProfitValuationEstimation();
}

var netprofitvaltm;
function fetchNetProfitValuationEstimation(){
     var url = getStockValuationUrl+'/netprofitvaluation';
            var jsonResonse = GetRawBookContent(url);
            console.log("showe Net Profit Valuation: response: "+jsonResonse);
            var data = JSON.parse(jsonResonse);
            console.log(" Fetching Net Profit Valuation value: "+data);
            netprofitvaltm = setTimeout(fetchNetProfitValuationEstimation, 5000);
            if(data.evluated){
                var ele = document.getElementById("netprofitestimation");
                ele.innerHTML = data.finalIntrinsicValue;
//                document.getElementById("netprofitmationrgnsfty").innerHTML =data.finalIntrinsicValue;
                var targetPrice = parseFloat(data.finalIntrinsicValue);
                if(targetPrice<currentSharePrice)
                    document.getElementById("netprofitestimation").style.color = "red";
                else
                    document.getElementById("netprofitestimation").style.color = "green";
                clearTimeout(netprofitvaltm);
            }
}


function netprofitvaluationMrgnOfSfty(mrgnOfSfty){
    var targetPrice = document.getElementById("netprofitestimation").innerHTML;
    targetPrice = parseFloat(targetPrice);
    console.log("[NetProfitValuation] mrgnOfSfty : "+mrgnOfSfty+" currentSharePrice: "+currentSharePrice+" targetPrice: "+targetPrice);
    var targetPriceMrgn = targetPrice*(1-(mrgnOfSfty/100));
    document.getElementById("netprofitmationrgnsfty").innerHTML = targetPriceMrgn;
    console.log("[NetProfitValuation] targetPriceMrgn: "+targetPriceMrgn);
    if(targetPriceMrgn>currentSharePrice)
            document.getElementById("netprofitmationrgnsfty").style.color = "green";
    else
       document.getElementById("netprofitmationrgnsfty").style.color = "red";

    var upside = (targetPriceMrgn-currentSharePrice)/currentSharePrice*100;
        document.getElementById("netprofitupside").innerHTML = upside;

    if(upside<0)
     document.getElementById("netprofitupside").style.color = "red";
    else
      document.getElementById("netprofitupside").style.color = "green";
}


/*====================== 2 Phase DCF Valuation Model =========================*/

function twophasedcfvaluation(){
    console.log("twophasedcfvaluation action is triggered");
    window.open("dcf-2-phase-valuation.html?stockID="+stockID);
    window.focus();

    fetch2PhaseDCFValuationEstimation();
}

var twophasedcfvaluationvaltm;
function fetch2PhaseDCFValuationEstimation(){
     var url = getStockValuationUrl+'/twophasedcfvaluation';
            var jsonResonse = GetRawBookContent(url);
            console.log("showe Net Profit Valuation: response: "+jsonResonse);
            var data = JSON.parse(jsonResonse);
            console.log(" Fetching Net Profit Valuation value: "+data);
            twophasedcfvaluationvaltm = setTimeout(fetch2PhaseDCFValuationEstimation, 5000);
            if(data.evluated){
                var ele = document.getElementById("twophasedcfestimation");
                ele.innerHTML = data.finalIntrinsicValue;
//                document.getElementById("twophasedcfmationrgnsfty").innerHTML =data.finalIntrinsicValue;
                var targetPrice = parseFloat(data.finalIntrinsicValue);
                if(targetPrice<currentSharePrice)
                    document.getElementById("twophasedcfestimation").style.color = "red";
                else
                    document.getElementById("twophasedcfestimation").style.color = "green";
                clearTimeout(twophasedcfvaluationvaltm);
            }
}


function twophasedcfMrgnOfSfty(mrgnOfSfty){
    var targetPrice = document.getElementById("twophasedcfestimation").innerHTML;
    targetPrice = parseFloat(targetPrice);
    console.log("[Two Phase DCF Valuation] mrgnOfSfty : "+mrgnOfSfty+" currentSharePrice: "+currentSharePrice+" targetPrice: "+targetPrice);
    var targetPriceMrgn = targetPrice*(1-(mrgnOfSfty/100));
    document.getElementById("twophasedcfmationrgnsfty").innerHTML = targetPriceMrgn;
    console.log("[Two Phase DCF Valuation] targetPriceMrgn: "+targetPriceMrgn);
    if(targetPriceMrgn>currentSharePrice)
            document.getElementById("twophasedcfmationrgnsfty").style.color = "green";
    else
       document.getElementById("twophasedcfmationrgnsfty").style.color = "red";

    var upside = (targetPriceMrgn-currentSharePrice)/currentSharePrice*100;
        document.getElementById("twophasedcfupside").innerHTML = upside;

    if(upside<0)
     document.getElementById("twophasedcfupside").style.color = "red";
    else
      document.getElementById("twophasedcfupside").style.color = "green";
}


/*====================== Bejamin Graham Valuation Model =========================*/

function grahamValution(){
    console.log("Benjamin Graham action is triggered");
    window.open("benjamin-graham-valuation.html?stockID="+stockID);
    window.focus();

    grahamValuationEstimation();
}

var grahamvaluationvaltm;
function grahamValuationEstimation(){
     var url = getStockValuationUrl+'/graham';
            var jsonResonse = GetRawBookContent(url);
            console.log("show Graham valuation: response: "+jsonResonse);
            var data = JSON.parse(jsonResonse);
            console.log(" Fetching Net Profit Valuation value: "+data);
            grahamvaluationvaltm = setTimeout(grahamValuationEstimation, 5000);
            if(data.evluated){
                var ele = document.getElementById("grahamestimation");
                ele.innerHTML = data.finalIntrinsicValue;
//                document.getElementById("twophasedcfmationrgnsfty").innerHTML =data.finalIntrinsicValue;
                var targetPrice = parseFloat(data.finalIntrinsicValue);
                if(targetPrice<currentSharePrice)
                    document.getElementById("grahamestimation").style.color = "red";
                else
                    document.getElementById("grahamestimation").style.color = "green";
                clearTimeout(grahamvaluationvaltm);
            }
}


function grahamMrgnOfSfty(mrgnOfSfty){
    var targetPrice = document.getElementById("grahamestimation").innerHTML;
    targetPrice = parseFloat(targetPrice);
    console.log("[Graham] mrgnOfSfty : "+mrgnOfSfty+" currentSharePrice: "+currentSharePrice+" targetPrice: "+targetPrice);
    var targetPriceMrgn = targetPrice*(1-(mrgnOfSfty/100));
    document.getElementById("grahammrgnsfty").innerHTML = targetPriceMrgn;
    console.log("[Graham] targetPriceMrgn: "+targetPriceMrgn);
    if(targetPriceMrgn>currentSharePrice)
            document.getElementById("grahammrgnsfty").style.color = "green";
    else
       document.getElementById("grahammrgnsfty").style.color = "red";

    var upside = (targetPriceMrgn-currentSharePrice)/currentSharePrice*100;
        document.getElementById("grahamupside").innerHTML = upside;

    if(upside<0)
     document.getElementById("grahamupside").style.color = "red";
    else
      document.getElementById("grahamupside").style.color = "green";
}


/*====================== Stock Counter Model =========================*/
function stockCounter(){
    var totalInvestmentAmount = parseFloat(document.getElementById("invstAmt").value);
    var bidPrice1 = parseFloat(document.getElementById("stockBidPrice1").value);
    var bidPrice2 = parseFloat(document.getElementById("stockBidPrice2").value);
    var bidPrice3 = parseFloat(document.getElementById("stockBidPrice3").value);

    var bidList = new Array();
    bidList.push(bidPrice1);
    bidList.push(bidPrice2);
    bidList.push(bidPrice3);

    bidList.sort(function(a, b){
                   var x =parseFloat(a);
                   var y = parseFloat(b);
                   if (y < x) {return -1;}
                   if (y > x) {return 1;}
                   return 0;
                 });
    console.log(bidList);
        var highPrice = bidList[0];
        var middlePrice = bidList[1];
        var lowPrice = bidList[2];

        var highPriceSplit = .2;
        var middlePriceSplit = .5;
        var lowPriceSplit = .3;
        //keep highprice 0.25% less than price as stock
        highPrice = highPrice * (1-0.0025);

        document.getElementById("stockBidPriceDis1").innerHTML = highPrice;
        document.getElementById("stockBidPriceDis2").innerHTML = middlePrice;
        document.getElementById("stockBidPriceDis3").innerHTML = lowPrice;

         var highInvestmentAmount = totalInvestmentAmount * highPriceSplit;
         var middleInvestmentAmount = totalInvestmentAmount * middlePriceSplit;
         var lowInvestmentAmount = totalInvestmentAmount * lowPriceSplit;




         var actualStockCount = parseInt(totalInvestmentAmount / highPrice);
        console.log(" Only with Original Price Calculated Stock Count:  "+ actualStockCount);
         console.log(" High Price Investment: "+highInvestmentAmount+" Middle Price Investment: "+middleInvestmentAmount+" Low Price Investment: "+lowInvestmentAmount);
         var highStockCount = parseInt(highInvestmentAmount/highPrice);
        var middleStockCount = parseInt(middleInvestmentAmount/middlePrice);
        var lowStockCount = parseInt(lowInvestmentAmount/middlePrice);

        console.log("High Stock Count: "+highStockCount+" Middle Stock Count: "+middleStockCount+" Low Stock Count: "+lowStockCount +"  Total Stock: "+(highStockCount+lowStockCount+middleStockCount));



        var finalInvestmentAmount = highPrice * highStockCount + middlePrice*middleStockCount + lowPrice*lowStockCount;
        console.log(" Final Investment Amount : "+finalInvestmentAmount);
        var investmentDiff = totalInvestmentAmount - finalInvestmentAmount;
        console.log("investmetDiff: "+investmentDiff);

        if(investmentDiff>=middlePrice){
            var count = parseInt(investmentDiff/middlePrice);
            middleInvestmentAmount = middleInvestmentAmount+ (count * middlePrice);
            middleStockCount =middleStockCount +count;
            console.log("Adding extra middle stock: "+middleStockCount+" "+middleInvestmentAmount);
        }
        else if(investmentDiff>=lowPrice){
                         var count = parseInt(investmentDiff/lowPrice);
                         lowInvestmentAmount = lowInvestmentAmount+ (count * lowPrice);
                         lowStockCount =lowStockCount +count;
                         console.log("Adding extra low stock: "+lowStockCount+" "+lowInvestmentAmount);
                     }

        var adjustedStockCount = (highStockCount+lowStockCount+middleStockCount);

        finalInvestmentAmount = highPrice * highStockCount + middlePrice*middleStockCount + lowPrice*lowStockCount;

        investmentDiff = totalInvestmentAmount - finalInvestmentAmount;

        document.getElementById("bid1Investemnt").innerHTML = highInvestmentAmount;
        document.getElementById("bid2Investemnt").innerHTML = middleInvestmentAmount;
        document.getElementById("bid3Investemnt").innerHTML = lowInvestmentAmount;

        document.getElementById("bid1StockCount").innerHTML = highStockCount;
        document.getElementById("bid2StockCount").innerHTML = middleStockCount;
        document.getElementById("bid3StockCount").innerHTML = lowStockCount;
        document.getElementById("origStockCount").innerHTML = actualStockCount;
        document.getElementById("finalStockCount").innerHTML = adjustedStockCount;

        document.getElementById("finalInvestment").innerHTML = finalInvestmentAmount;
        document.getElementById("origInvestment").innerHTML = totalInvestmentAmount;
        document.getElementById("reducedInvestment").innerHTML = investmentDiff;


}


function onTYearSelection(){
    var years = document.getElementById("tyears").value;
   console.log("Years selection : "+years);
//   targetPriceValuation(years);
    showEVEbitdaValue(years);

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

function createTable(divID,tableID,headerList,datapoints,dataFY){

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
                 isDPCell = false;
            }
            else{
                  dataPerYear = dataFY[headerList[j]];
                  console.log("dataPerYear for: "+headerList[j]+" = "+dataPerYear);
                  var valueEle = document.createElement("LABEL");
                  valueEle.innerHTML = dataPerYear[datapoints[i][1]];
                  var id = datapoints[i][1]+'-'+headerList[j];
                  console.log("Setting cell ID: "+id);
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