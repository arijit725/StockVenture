const queryString = window.location.search;
console.log(queryString);
const urlParams = new URLSearchParams(queryString);
const stockID = urlParams.get('stockID')
console.log(stockID);

var companyDetailsURL = 'http://localhost:8080/fundamental/companydetails/'+stockID;

var generateReportUrl = 'http://localhost:8080/fundamental/generateReport/'+stockID;



var getBalancesheetUrl ='http://localhost:8080/fundamental/balancesheet/'+stockID;
var getBalancesheetAnalysisUrl ='http://localhost:8080/fundamental/balancesheetAnalysis/'+stockID;

var getProfitAndLossUrl ='http://localhost:8080/fundamental/profitAndLoss/'+stockID;
var getProfitAndLossAnalysisUrl='http://localhost:8080/fundamental/profitAndLossAnalysis/'+stockID;

var getRatiosUrl = 'http://localhost:8080/fundamental/ratios/'+stockID;
var getRatiosAnalysusUrl='http://localhost:8080/fundamental/ratioAnalysis/'+stockID;

var targetPriceUrl = 'http://localhost:8080/fundamental/targetPrice/'+stockID;

function onReportLoad(){

    createCompanyTable();

    balancesheetTable(5);
    balancesheetAnalysis(5);

    profitAndLossTable(5);
    profitAndLossAnalysis(5);

    ratiosTable(5);
    ratiosAnalysis(5);
//    console.log("onReportLoad triggered: url: "+generateReportUrl);
//    GetRawBookContent(generateReportUrl);
    targetPriceValuation(5);

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


/*====================== Get Balance Sheet Details =========================*/
function balancesheetDataPoints(){
    var datapoints = new Array();
    datapoints.push(["Total Share Capital","total_share_capital"]);
    datapoints.push(["Equity Share Capital","equity_share_capital"]);
    datapoints.push(["Reserves","reserves"]);
    datapoints.push(["Debt","debt"]);
    return datapoints;
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


function balancesheetTable(years){
     var balancesheetFY = getBalancesheetData(years);
    console.log("creaing Balancesheet table: ");
    var datapoints = balancesheetDataPoints();
    var headerList =createHeaders(years);
    console.log("Generated Headers: "+headerList);
    var tableID="bltbl";
    var tableEle = document.getElementById(tableID);
    if(tableEle!=null){
            tableEle.remove();
        }
    createTable("balancesheet-tbl",tableID,headerList,datapoints,balancesheetFY);
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
}

function balancesheetCalcAnalysis(balAnal){
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

}

function onBLYearSelection(){
   var years = document.getElementById("blyears").value;
   console.log("Years selection : "+years);
   balancesheetTable(years);
   balancesheetAnalysis(years);
}



/*====================== Get Profit And Loss Details =========================*/
function plDataPoints(){
    var datapoints = new Array();
     datapoints.push(["Net Sales","netSales"]);
    datapoints.push(["Consumption Of Raw Material","consumptionRawMaterial"]);
    datapoints.push(["Employee Cost","employeeCost"]);
    datapoints.push(["PBDIT","pbit"]);
    datapoints.push(["Interest","interest"]);
    datapoints.push(["Net Profit","netProfit"]);
    return datapoints;
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

function profitAndLossTable(years){
     var jsonResonse = getDataFY(getProfitAndLossUrl,years);
    var dataFY = createPlDataPoints(jsonResonse);
    console.log("creaing profitAndLossTable: ");
    var datapoints = plDataPoints();

    var headerList =createHeaders(years);
//    console.log("Generated Headers: "+headerList);
//    console.log(dataFY)
     var tableID="pltbl";
        var tableEle = document.getElementById(tableID);
        if(tableEle!=null){
                tableEle.remove();
            }
    createTable("pl-tbl",tableID,headerList,datapoints,dataFY);
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
    cell1.innerHTML = "Growth in Net Sales Percentage: "+ plAnal.netSalesGrowthPercentage;
    if(plAnal.netSalesGrowthPercentage>grossSellChangeMargin){
        cell1.setAttribute("class", "analyzedgood");
    }
    else if(plAnal.netSalesGrowthPercentage<0){
        cell1.setAttribute("class", "analyzedbad");
    }
    row1.appendChild(cell1);

    var row2 = table.insertRow();
    var grossincidentmargin = 5;
    var cell2 = document.createElement("TD");
    cell2.innerHTML = "Net Sales Growth Continuous?: "+ plAnal.isSalesGrowthContinuous;
    if(plAnal.isSalesGrowthContinuous){
        cell2.setAttribute("class", "analyzedgood");
    }

    row2.appendChild(cell2);

    var row3 = table.insertRow();
    var grossRawMaterialChangeMargin = 5;
    var cell3 = document.createElement("TD");
    cell3.innerHTML = "Change Percentage in Raw Material Consumption: "+ plAnal.rawMaterialGrowthPercentage;
    if(plAnal.rawMaterialGrowthPercentage>grossRawMaterialChangeMargin){
        cell3.setAttribute("class", "analyzedgood");
    }
    row3.appendChild(cell3);


    var row4 = table.insertRow();
    var grossPBITChangeMargin = 1;
    var cell4 = document.createElement("TD");
    cell4.innerHTML = "Growth in PBIt: "+ plAnal.PBITGrowthPercentage;
    if(plAnal.PBITGrowthPercentage>grossPBITChangeMargin){
        console.log("Setting className: analyzedgood");
        cell4.setAttribute("class", "analyzedbad");
    }
    row4.appendChild(cell4);


    var row5 = table.insertRow();
    var grossInterestChangeMargin = 0;
    var cell5 = document.createElement("TD");
    cell5.innerHTML = "Decrease in Debt Interest : "+ plAnal.interestDecreasePercentage;
    if(plAnal.interestDecreasePercentage<=grossInterestChangeMargin){
        cell5.setAttribute("class", "analyzedgood");
    }
    else{
        cell5.setAttribute("class", "analyzedbad");
    }
    row5.appendChild(cell5);

      var row6 = table.insertRow();
        var grossProfitChangeMargin = 1;
        var cell6 = document.createElement("TD");
        cell6.innerHTML = "Growth in Net Profit: "+ plAnal.netProfitGrowthPercentage;
        if(plAnal.netProfitGrowthPercentage>grossProfitChangeMargin){
            console.log("Setting className: analyzedbad");
            cell6.setAttribute("class", "analyzedgood");
        }
        else if(plAnal.netProfitGrowthPercentage<0){
            cell6.setAttribute("class", "analyzedbad");
        }
        row6.appendChild(cell6);

    var dvTable = document.getElementById(divID);
    dvTable.appendChild(table);
    profitAndLossCalcAnalysis(plAnal);
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

}

function onPLYearSelection(){
   var years = document.getElementById("plyears").value;
   console.log("Years selection : "+years);
   profitAndLossTable(years);
   profitAndLossAnalysis(years);
}


/*====================== Get Ratios Details =========================*/

function ratiosDataPoints(){
    var datapoints = new Array();
     datapoints.push(["PE Ratio","peRatio"," PE Ratio ToolTips PlaceHolder"]);
    datapoints.push(["PB Ratio","pbRatio",PBRatioToolTips()]);
    datapoints.push(["Return On Equity","roe"," PE Ratio ToolTips PlaceHolder"]);
    datapoints.push(["Enterprise Value","ev"," PE Ratio ToolTips PlaceHolder"]);
    datapoints.push(["EV/EBITDA","evEbitda"," PE Ratio ToolTips PlaceHolder"]);
    return datapoints;
}

function PBRatioToolTips(){
    var toolTips = "PB Ratio Shows how much (X times) premium we are paying for stock value."+"<br>"+
                    "Low PBRatio =  UnderValued"+"<br>"+
                    "High PBRatio = OverValued"+"<br>"+
                    "For a good company PBRatio ranges 3 to 6"+"<br><br>"+
                    "Compare PBRatio with peers for valuation estimation";
    return toolTips;

}

function ROEToolTips(){
 var toolTips = "ROE is same as RONE"
}

function createRatiosDataPoints(dataJson){
    var dataJsonList = JSON.parse(dataJson);
    console.log("inside createPlDataPoints: datapoints count: "+dataJsonList.length);

    var plFY = new Map();
    for(var i =0;i<dataJsonList.length;i++){
        var data = dataJsonList[i];
        var datamap = new Map();
        datamap['peRatio']=data.peRatio;
        datamap['pbRatio']=data.pbRatio;
        datamap['roe']=data.roe;
        datamap['ev']=data.ev;
        datamap['evEbitda']=data.evEbitda;
        plFY[data.date] = datamap;
    }
    return plFY;
}

function ratiosTable(years){
     var jsonResonse = getDataFY(getRatiosUrl,years);
    var dataFY = createRatiosDataPoints(jsonResonse);
    console.log("creaing RatiosTable: "+dataFY.peRatio);
    var datapoints = ratiosDataPoints();

    var headerList =createHeaders(years);
//    console.log("Generated Headers: "+headerList);
    console.log(dataFY)
     var tableID="rtbl";
        var tableEle = document.getElementById(tableID);
        if(tableEle!=null){
                tableEle.remove();
            }
    createTableWithToolTips("ratio-tbl-div",tableID,headerList,datapoints,dataFY);
}

function ratiosAnalysis(years){
    var url = getRatiosAnalysusUrl+'/'+years;
    console.log("Getting data from url: "+url);
    var jsonResonse = GetRawBookContent(url);
    console.log("RatioAnalysis Json res: "+jsonResonse);
    var ratioAnalysis = JSON.parse(jsonResonse);
    showGrowthRate(ratioAnalysis,years);
    highlightedPoints(ratioAnalysis,years);
}


function showGrowthRate(ratioAnalysis,years){
    ratioGrowthsDtoMap = ratioAnalysis.ratioGrowthsDtoMap;
    console.log("Fetched ratioGrowthsDtoMap: ");
    console.log(ratioGrowthsDtoMap);
    var headerList =createHeaders(years);
    var dataPoints = ratiosDataPoints();

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

}
function onRYearSelection(){
    var years = document.getElementById("ryears").value;
   console.log("Years selection : "+years);
   ratiosTable(years);
   ratiosAnalysis(years);
}



/*====================== Target Price Estimation =========================*/


function targetPriceHeaders(){
    headers = new Array();
    headers.push(["Model","model"])
    headers.push(["Target Price","targetPrice"]);
    headers.push(["Entry Price","entryPrice"]);
    return headers;
}
function targetPriceDataPoints(){
    var datapoints = new Array();
     datapoints.push(["EV/EBITDA","evebitda"," PE Ratio ToolTips PlaceHolder"]);
//    datapoints.push(["Price TO Sales","priceTOSell",PBRatioToolTips()]);
//    datapoints.push(["F-Score","fscore"," PE Ratio ToolTips PlaceHolder"]);

    return datapoints;
}


function targetPriceValuation(years){
    var url = targetPriceUrl+'/'+years;
    console.log("Getting data from url: "+url);
    var jsonResonse = GetRawBookContent(url);
    console.log("TargetPrice Json res: "+jsonResonse);
    var targetPriceEstimation = JSON.parse(jsonResonse);
    createTargetPriceTable(targetPriceHeaders(),targetPriceDataPoints(),targetPriceEstimation);

}

function createTargetPriceTable(headers,datapoints,targetPriceEstimation){

    targetPriceMap = targetPriceEstimation.targetPriceMap;
    var divID = "targetprice-tbl-div";
    var tableID = "targetprice-tbl-id";

     var ele = document.getElementById(tableID);
        if(ele!=null){
            ele.remove();
        }

     var table = document.createElement("TABLE");
     var rowh = table.insertRow();
     for(var i=0;i<headers.length;i++){
        var cellH = document.createElement("TH");
        cellH.innerHTML=headers[i][0];
        rowh.appendChild(cellH);
     }

    for(var i=0;i<datapoints.length;i++){
        var row = table.insertRow();
        var cell1 = document.createElement("TD");
        cell1.innerHTML = datapoints[i][0];
        row.appendChild(cell1);
        targetPriceModel = targetPriceMap[datapoints[i][1]].priceMap;
        console.log(targetPriceModel);
        for(var j=1;j<headers.length;j++){
            var cellj = document.createElement("TD");
            var param = headers[j][1];
            console.log("Param Name : "+param);

            cellj.innerHTML = targetPriceModel[param];
            row.appendChild(cellj);
        }
         if((i%2)==0){
                    bgColor = "white";
                }
                else{
                    bgColor="lightgray";
                }
        row.style.backgroundColor=bgColor;

    }
     var div = document.getElementById(divID);
     div.appendChild(table);

}

function onTYearSelection(){
    var years = document.getElementById("tyears").value;
   console.log("Years selection : "+years);
   targetPriceValuation(years);
}

/*====================== Http Request Utility =========================*/
function GetRawBookContent(yourUrl){
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}

/*============================  Utility Functions ===================================*/

function createGrowthLabel(cellid, growthVal){
    var parent = document.getElementById(cellid);

    var label = document.createElement("LABEL");
    label.innerHTML="("+growthVal+"%)";
    if(growthVal>0)
        label.style.color='green';
    else if(growthVal<0)
        label.style.color='red';

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

function createHeaders(years){
    var header = new Array();
    header.push("DataPoints");
    var today = new Date();
    var currentYear = today.getFullYear();
//    var years = document.getElementById("years").value;
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

function createTableWithToolTips(divID,tableID,headerList,datapoints,dataFY){

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
                 datapointsCell.setAttribute("class","itemtooltip");
                 var span = document.createElement("SPAN");
                 span.setAttribute("class","tooltiptext");
                 span.innerHTML=datapoints[i][2];
                 datapointsCell.appendChild(span);
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