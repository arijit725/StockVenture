var industryListURL = '/fundamental/listindustry';
var stockListURL = '/fundamental/liststock';
var loadReportUrl = '/fundamental/loadanalysis';

function onIndustryLoad(){
    var jsonResponse = GetContent(industryListURL);
    var industryList = JSON.parse(jsonResponse);
    console.log(industryList);
    generateIndustryTable(industryList);
}

/*====================== Generate Industrt List =========================*/

function generateIndustryTable(industryList){
    var divId = "industrylist";
    var tableId = "industry-tbl";

    var tableEle = document.getElementById(tableId);
    if(tableEle!=null){
            tableEle.remove();
        }

    createTable(divId,tableId,industryList,industryRowSelected);
}

function industryRowSelected(){
    console.log("row selected");
    if (this.parentNode.nodeName == 'THEAD') {
         return;
      }

     var cells = this.cells;
     console.log(cells)
     var industry = cells[0].innerHTML;
     console.log(industry);
     generateStocklist(industry);

}


/*====================== Generate Stock List =========================*/

function generateStocklist(industry){
    var url = stockListURL+"/"+industry;
    var jsonResponse = GetContent(url);
    console.log("Stock list json: "+jsonResponse);
    var stockList = JSON.parse(jsonResponse);

    generateStockTable(industry,stockList);
}


function generateStockTable(industry,stockList){
    var divId = "stock-tbl";
    var tableId = "st-tbl";
    var industryNameELe = document.getElementById("industry-name");
    industryNameELe.innerHTML = industry;
    var tableEle = document.getElementById(tableId);
    if(tableEle!=null){
            tableEle.remove();
        }

    createTable(divId,tableId,stockList,stockRowSelected);

}

function stockRowSelected(){
    if (this.parentNode.nodeName == 'THEAD') {
         return;
      }
     var cells = this.cells;
     var stock = cells[0].innerHTML;
    console.log("stock Row selected: "+stock);
    var industry = document.getElementById("industry-name").innerHTML;
    var stockID = loadFundamentalData(industry,stock);

    generateReport(stockID);
}

function loadFundamentalData(industry,stock){
    url = loadReportUrl+"/"+industry+"/"+stock;
    var stockID = GetContent(url);
    console.log("loaded stock :: stockID = "+stockID);
    return stockID;
}
/*============================  Function for report generation ===================================*/
function generateReport(stockID){
    console.log("generateReport action is triggered");
    window.open("stock-report.html?stockID="+stockID);
    window.focus();
}

/*====================== Http Request Utility =========================*/
function GetContent(yourUrl){
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}


function createTable(divId,tableID, elementList, action){
     var table = document.createElement("TABLE");
        table.setAttribute('id', tableID);
    //    var rowCount = document.getElementById("years").value;
        var row = table.insertRow(0);

        var bgColor="white";
        for(var i=0;i<elementList.length;i++){
            console.log("Element: "+elementList[i]);
            var tmprow = table.insertRow();
//            tmprow.setAttribute("onclick",action);
            tmprow.onclick = action;
            if((i%2)==0){
                bgColor = "white";
            }
            else{
                bgColor="lightgray";
            }

            tmprow.style.backgroundColor=bgColor;

            var cell = document.createElement("TD");
            cell.innerHTML = elementList[i];

                tmprow.appendChild(cell);
            }

    var divEle = document.getElementById(divId);
    divEle.appendChild(table);
    divEle.style.display="block";
}

