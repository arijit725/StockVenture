
var companyDeatilsUrl ='http://localhost:8080/fundamental/companyDetails';
function startTab(){
    document.getElementById("defaultOpen").click();
}
function openCity(evt, cityName) {
  var i, tabcontent, tablinks;
  console.log("Selected tab: "+cityName)
  if(cityName == "Balance-Sheet"){
    balancesheetTable();
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
}

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
    createTable("Balance-Sheet","bltbl",headerList,datapoints);
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
        balanceSheetDto['Date']=headers[y];
        for(var i=0;i<datapoints.length;i++){
            id  = getCellID(datapoints[i][1],headers[y]);
            var value = document.getElementById(id).value;
            console.log("Balancesheet details"+ id+" : "+value);
            balanceSheetDto[datapoints[i][1]] = value;
            }
        console.log("Balance sheet for year:");
        console.log(balanceSheetDto);
        balanceSheetDtoList.push(balanceSheetDto);
    }
    console.log("Stored records: "+balanceSheetDtoList.length)
    console.log(balanceSheetDtoList)
    var balanceSheetJson = generateJsonString(balanceSheetDtoList);
    console.log(balanceSheetJson);
}



/*============================  Http Request Functions ===================================*/
function postRequest(url, requestBody){
        var Httpreq = new XMLHttpRequest(); // a new request
        Httpreq.open("POST",url,false);
        var md5Sum = md5(requestBody);
        Httpreq.setRequestHeader('x-md5sum',md5Sum);
        Httpreq.setRequestHeader('Content-type','application/json; charset=utf-8');
        console.log("requestBody: "+requestBody);
        Httpreq.send(requestBody);
        return Httpreq.responseText;
}

/*============================  Utility Functions ===================================*/


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

function createTable(divID,tableID,headerList,datapoints){

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
    var btn = createSubmitButton("blSubmit","submit","submit-button",submitBalancesheetDetails);
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
