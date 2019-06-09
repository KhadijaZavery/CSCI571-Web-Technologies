<?php
    include "encode_hash.php";
   
    ini_set('display_errors', 1);
    ini_set('display_startup_errors', 1);
    error_reporting(E_ALL);
    
    //Checking whether location is set using ip-api.com and retrieving "Here" addresss
    $api_ticket_key = "ZA2wHGFGfj8l9RjHFH5BysxNyuPgViW0"; //For ticketmaster
 
    $data = file_get_contents('php://input');
    $data = json_decode($data,true);
    if($_SERVER['REQUEST_METHOD'] == 'POST') {
        if($data['type'] == 'ipAPI'){
            $lat = $data['lat'];
            $lng = $data['lon'];
            $geohash = encode($lat, $lng);
            echo $geohash;
            exit();
        }
    else if($data['type'] == 'eventSearch'){
        $keyword = $data['keyword'];
        $category = $data['category'];
        $miles = $data['miles'];
        $flag = $data['flag'];
        if($flag==true)
            $geohash = $data['location'];
        else
        {
            
            $api_google_key = "AIzaSyBYJYNAGMF5MEC-7txG2Hb3xpJBdSsWu6o";
            $geocoding_url = 'https://maps.googleapis.com/maps/api/geocode/json?address=' .urlencode($data['location']). '&key=' .urlencode($api_google_key);
            $geocoding_json = file_get_contents($geocoding_url);
            $geocoding_array = json_decode($geocoding_json,true);
            $lat = $geocoding_array['results'][0]['geometry']['location']['lat'];
            $lng = $geocoding_array['results'][0]['geometry']['location']['lng'];
            $geohash = encode($lat, $lng);
        }
         $eventsearch_url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" .urlencode($api_ticket_key).
             "&keyword=" .urlencode($keyword). "&segmentId=".urlencode($category)."&radius=" .urlencode($miles). "&unit=miles&geoPoint=" .urlencode($geohash);
        $eventsearch_json = file_get_contents($eventsearch_url);
        echo $eventsearch_json;
        exit();
        
    }
    else if($data['type'] == 'eventDetails'){
        $id = $data['eventId'];
        $eventdetails_url = "https://app.ticketmaster.com/discovery/v2/events/".urlencode($id)."?apikey=".urlencode($api_ticket_key)."&";
        $eventdetails_json = file_get_contents($eventdetails_url);
        echo $eventdetails_json;
        exit();
    }

    else if($data['type'] == 'venueDetails'){
        $keyword = $data['eventName'];
        $venue_url = "https://app.ticketmaster.com/discovery/v2/venues?apikey=".urlencode($api_ticket_key)."&keyword=".$keyword;
        $venuedetails_json = file_get_contents($venue_url);
        echo $venuedetails_json;
        exit();
    }
        
        
    else if($data['type'] == 'geocode'){
        //$location = $data['location'];
        $api_google_key = "AIzaSyBYJYNAGMF5MEC-7txG2Hb3xpJBdSsWu6o";
            $geocoding_url = 'https://maps.googleapis.com/maps/api/geocode/json?address=' .urlencode($data['location']). '&key=' .urlencode($api_google_key);
            $geocoding_json = file_get_contents($geocoding_url);
        echo $geocoding_json;
        exit();
    }
    }
    
?>

<!DOCTYPE html>
<html>
    <head>
    <style>
        #caption1, #caption2{
            color: #C1C1C1;
        }
        #myform{
            margin: 0 auto;
            border-style: solid;
            border-width: thick;
            border-color: #CBCBCB;
            padding: 10px;
            background-color: #F9F9F9;
        }
        #norecords{
            border-style: solid;
            border-width: thick;
            border-color: #CBCBCB;
            padding: 5px;
            background-color: #F9F9F9;
            width:75%;
            margin-left:auto;
            margin-right: auto;
            padding: 0px;
        }
        .nophotos{
             border-color: #CBCBCB;
            border-style: solid;
            border-width: 1px;
            width:75%;
            margin-left:auto;
            margin-right: auto;
            padding: 0px;
            font-weight: bold;
        }
        #keyword{
            padding: 0px;
        }
        #firsttable table, #firsttable td,#firsttable th {
            border: 1px solid black;
            padding: 10px;  
        }
        
        #firsttable {
            border-collapse: collapse;
            min-width: 1000px;
            
        }
        
        #firsttable td{
            z-index: 5;
        }
        
        #event_link:hover{
            color: lightgray;
        }
        #addtable{
            position: relative;
            margin-top: 50px;
            
        }
        #image_absolute{
            position: absolute;
            top: 350px;
            right: 200px;
            
        }
        #formheading{
            border-bottom: 2px solid #CBCBCB;
        }
        
        h3{
            text-align: center;
        }
        
        .hidden{
           width: 300px;
            height:250px;
            position: absolute;
            z-index: 15;
            display: none;
        }
        
        .show{
            display: block;
        }
        
        a{
           text-decoration: none;
           color: black;
        }
        
        #arrow_down, #arrow_up{
            margin-left: 48%;
            
            
        }
        
        #upDiv, #downDiv {
            display: none;
            margin: 0 auto;
            width:85%;
        }
        
        #map {
        width: 80%;
        height: 300px;
       }
        
        #test{
            width: 75%;
            border-collapse: collapse;
        }
        .modes{
            padding: 0px;
            margin: 0px;
            background-color: #F0F0F0;
        }
        
        .modes:hover{
            color: #909090;
        }
        
        #modes_div{
            margin-top: -120px;
            margin-left: 10px;
            width: 75px;
            margin-right: 10px;
        }
        
        .overlap_modes{
            background-color: #F0F0F0;
            padding: 5px 5px 5px 5px;
            margin:0;
        }
        
        .overlap_modes:hover{
            background-color:#DCDCDC;
            color: #B0B0B0;
        }
       
        .modes_map{
            position: absolute;
            display: none;
            z-index: 30; 
           
        }
        a:hover{
            color: #909090;
            text-decoration: none;
        }
        
        a:visited{
            text-decoration: none;
            color: black;
        }
        
        .modes_show{
            display: block;
        }
        #event_name:hover{
            color: 	#909090;
        }
        .hover_links:hover{
            color: #909090;
        }
</style>
</head>
    
<body>
   
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBYJYNAGMF5MEC-7txG2Hb3xpJBdSsWu6o&callback=initMap">
    </script>
<script>
    

    function resetForm(){
    document.getElementById("myform").reset();
    document.getElementById("addtable").innerHTML = "";
    document.getElementById("imgDiv").innerHTML = "";
    document.getElementById("upDiv").innerHTML = "";
    document.getElementById("downDiv").innerHTML = "";
    if(location_here == true)
        document.getElementById("location_here").checked = true;
    else
        document.getElementById("location_text").disabled = true;
    }
    
    var req, resultJSON;
    window.onload = function() {
        var source="http://ip-api.com/json";
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var result = this.responseText;
                resultJSON = JSON.parse(result);
                console.log(resultJSON.lat);
                if(resultJSON.status == "success"){
                    document.getElementById('search_button').disabled = false;
                }
                callPHP(resultJSON);
            }};
        xhr.open("POST", source, true);
        xhr.send();
        
    }
    var result_latlong;
    
    function callPHP(resultJSON1) {
        console.log("between");
        var xhttp = new XMLHttpRequest();
        req = {
            lat : resultJSON1.lat,
            lon : resultJSON1.lon,
            status : resultJSON1.status,
            type : "ipAPI"
        };
         xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                result_latlong = this.responseText;
//                result_latlong= JSON.parse(result_latlong);
                console.log("Encoded"+result_latlong);
               
            }};
        xhttp.open("POST", "main.php", true);
        xhttp.send(JSON.stringify(req));
        console.log(req);
    }
    
    var result_geocode;
    function callGeocoding(){
        var xhttp = new XMLHttpRequest();
        req = {
            location : document.getElementById("location_text").value,
            type : "geocode"
        };
         xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var geocode = this.responseText;
                result_geocode = JSON.parse(geocode);
                
               
            }};
        xhttp.open("POST", "main.php", true);
        xhttp.send(JSON.stringify(req));
        //console.log(req);
    }

    
    function submitData(){
        var keyword = document.getElementById('keyword').value;
        var category = document.getElementById('category').value;
        var miles = document.getElementById('miles').value;
        if(miles == "")
            miles = 10;
        var location_here = document.getElementById('location_here').checked;
        if(location_here == true)
            {
                var flag = true;
                var location = result_latlong;    
            }
        else{
             var location = document.getElementById('location_text').value;
             flag = false;
             callGeocoding();
        }
        var form_req = {
            keyword : keyword,
            category : category,
            miles : miles,
            flag : flag,
            location : location,
            type : 'eventSearch'
        }
        
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var resp = xhttp.responseText;
                var json_resp = JSON.parse(resp);
                console.log(json_resp);   
                generateHtml(json_resp);
            }
        };
        xhttp.open("POST", "main.php",true);
        xhttp.send(JSON.stringify(form_req));  
        
    }
        
        
       function generateHtml(eventsearch_array){
           var txt;
           document.getElementById("imgDiv").innerHTML = "";
           document.getElementById("upDiv").innerHTML = "";
           document.getElementById("downDiv").innerHTML = "";
           if(eventsearch_array.page.totalElements == 0)
               {
                   var txt1 = "<p align = 'center' id='norecords'>No records has been found</p>";
                   console.log("In no records");
                   document.getElementById("addtable").innerHTML = txt1;
               }
            
            else 
           {
                var eventsList = eventsearch_array._embedded.events;
            
                txt="<html><head><title></title></head><body>";
                txt += "<table border='1' id = 'firsttable' align = 'center'>";
                //txt += "col width=";

                txt += "<tr>";
                txt += "<th>Date</th>";
                txt += "<th>Icon</th>";
                txt += "<th>Event</th>";
                txt += "<th>Genre</th>";
                txt += "<th>Venue</th>";
                txt+= "</tr>";  
                for(var i=0;i<eventsList.length;i++) //do for all companies (one per row)
                    {    
                        eventsNodeList=eventsList[i]; //get properties of a Company (an object)
                        txt+="<tr>"; 
                        if(eventsNodeList.hasOwnProperty('dates')){
                            if(eventsNodeList.dates.hasOwnProperty('start'))
                                if((eventsNodeList.dates.start.hasOwnProperty('localDate')) && ((eventsNodeList.dates.start.localDate) != 'Undefined' && eventsNodeList.dates.start.localDate != undefined))
                            txt+="<td align = 'center'>" +eventsNodeList.dates.start.localDate+"<br>";
                        }
                        if(eventsNodeList.hasOwnProperty('dates')){
                            if(eventsNodeList.dates.hasOwnProperty('start'))
                                if((eventsNodeList.dates.start.hasOwnProperty('localTime')) && ((eventsNodeList.dates.start.localTime) != 'Undefined' && eventsNodeList.dates.start.localTime != undefined))
                            txt+=eventsNodeList.dates.start.localTime+"<br></td>";
                        }

                        if(eventsNodeList.hasOwnProperty('images')){
                            if((eventsNodeList.images[0].hasOwnProperty('url')) && ((eventsNodeList.images[0].url) != 'Undefined' && eventsNodeList.images[0].url != undefined))
                                txt+="<td align = 'center'><img src='"+ eventsNodeList.images[0].url +"' width= 50px height = 30px/></td>";
                        }
                        else
                            {
                            txt+="<td>N/A</td>";}
                        
                        if((eventsNodeList.hasOwnProperty('name')) && (eventsNodeList.name != undefined)){
                            txt+="<td><a id = 'event_name' style='cursor:pointer' onclick = callEventAPI('"+eventsNodeList.id+"')>" +eventsNodeList.name+ "</a></td>";}
                        else
                            {
                            txt+="<td>N/A</td>";}

                        if(eventsNodeList.hasOwnProperty('classifications'))
                        {
                            if(eventsNodeList.classifications[0].hasOwnProperty('segment'))
                                if((eventsNodeList.classifications[0].segment.hasOwnProperty('name')) && ((eventsNodeList.classifications[0].segment.name) != 'Undefined' && (eventsNodeList.classifications[0].segment.name) != undefined)) 
                                txt+="<td>"+eventsNodeList.classifications[0].segment.name+"</td>";
                        }
                        else
                            {
                            txt+="<td>N/A</td>";}

                        if((eventsNodeList._embedded.venues[0].hasOwnProperty('name')) && (eventsNodeList._embedded.venues[0].name) != 'Undefined' && (eventsNodeList._embedded.venues[0].name) != undefined ){
                            txt+="<td><a style='cursor:pointer' onclick = createMap('"+eventsNodeList._embedded.venues[0].id+i+"','"+eventsNodeList._embedded.venues[0].location.latitude+"','"+eventsNodeList._embedded.venues[0].location.longitude+"','"+i+"')>"+eventsNodeList._embedded.venues[0].name+"</a><div class = 'hidden' id='"+eventsNodeList._embedded.venues[0].id+i+"'></div><div class = 'modes_map' id ='"+i+"'><p onclick= directions('WALKING','"+eventsNodeList._embedded.venues[0].id+i+"','"+eventsNodeList._embedded.venues[0].location.latitude+"','"+eventsNodeList._embedded.venues[0].location.longitude+"') class='overlap_modes'>Walk there</p><p onclick=directions('BICYCLING','"+eventsNodeList._embedded.venues[0].id+i+"','"+eventsNodeList._embedded.venues[0].location.latitude+"','"+eventsNodeList._embedded.venues[0].location.longitude+"')  class='overlap_modes'>Bike there</p><p onclick=directions('DRIVING','"+eventsNodeList._embedded.venues[0].id+i+"','"+eventsNodeList._embedded.venues[0].location.latitude+"','"+eventsNodeList._embedded.venues[0].location.longitude+"') class='overlap_modes'>Drive there</p></div></td>";

                            }
                        else{
                             txt+="<td>N/A</td>";
                        }
                    txt+="</tr>";
                    }
                 
                txt += "</table></body></html>";
               document.getElementById("addtable").innerHTML = txt;
              
   }}
    
    function callEventAPI(id){
        var req = {
            eventId : id,
            type : "eventDetails"
        };
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                resp = xhttp.responseText;
                json_resp = JSON.parse(resp);
                console.log("Below is the JSON response for event details");
                console.log(json_resp);
                createEventDetails(json_resp);
            }
        };
        xhttp.open("POST", "main.php",true);
        xhttp.send(JSON.stringify(req));
       
    } 
        
    function createEventDetails(eventdetails_array){
            txt=' ';
            txt+="<html><head></head><body>";
            txt += "<table align = 'center'>";
            txt+="<h3>"+eventdetails_array.name+"</h3>";
            txt+="<tr>"; 
            if(eventdetails_array.hasOwnProperty('dates'))
            if(eventdetails_array.dates.hasOwnProperty('start'))
               if(eventdetails_array.dates.start.hasOwnProperty('localDate')){
            txt+="<td><b>Date</b><br>" + eventdetails_array.dates.start.localDate;
            }
        
            if(eventdetails_array.hasOwnProperty('dates'))
            if(eventdetails_array.dates.hasOwnProperty('start'))
               if(eventdetails_array.dates.start.hasOwnProperty('localTime')){
            txt+=" "+eventdetails_array.dates.start.localTime;
            }
    
            if(eventdetails_array.hasOwnProperty('seatmap'))
                {
                    if((eventdetails_array.seatmap.hasOwnProperty('staticUrl')) && (eventdetails_array.seatmap.staticUrl) != 'Undefined' && eventdetails_array.seatmap.staticUrl != 'undefined')
                    txt+="<td rowspan = '7'> <img src='" +eventdetails_array.seatmap.staticUrl+ "' width= 400px height = 250px/></td>";
                }
            else{
                txt+="<td rowspan = '7' width=0px height=0px></td>";
            }
             txt+="</tr>";
            
            //Checking elements in attractions array
            var attractions = [];
            if(eventdetails_array._embedded.hasOwnProperty('attractions')){
            txt+="<tr>"; 
            txt+="<td><b>Artist/Team</b><br>";
            for(var i=0; i<eventdetails_array._embedded.attractions.length; i++)
                {
                    if(i!=eventdetails_array._embedded.attractions.length-1 && (eventdetails_array._embedded.attractions[i].hasOwnProperty('url')) && (eventdetails_array._embedded.attractions[i].url != 'Undefined'))
                        txt += "<a class = 'hover_links' href='"+eventdetails_array._embedded.attractions[i].url+"' target='_blank'>"+eventdetails_array._embedded.attractions[i].name+ " | </a>";
                    else if((eventdetails_array._embedded.attractions[i].hasOwnProperty('url')) && (eventdetails_array._embedded.attractions[i].url != 'Undefined'))
                        txt += "<a class = 'hover_links' href='"+eventdetails_array._embedded.attractions[i].url+"'target='_blank'>"+eventdetails_array._embedded.attractions[i].name+ "</a>";
                }
                    
            txt+="</td></tr>";
            }
            if(eventdetails_array._embedded.hasOwnProperty('venues'))
            if(eventdetails_array._embedded.venues[0].hasOwnProperty('name'))
            {
                txt+="<tr>"; 
                txt+="<td><b>Venue</b><br>" + eventdetails_array._embedded.venues[0].name+ "</td>";
                txt+="</tr>";
            }

            if(eventdetails_array.hasOwnProperty('classifications')){
                txt+="<tr>"; 
                txt+="<td><b>Genre</b><br>";
                if(eventdetails_array.classifications[0].hasOwnProperty('subGenre'))
                    if(eventdetails_array.classifications[0].subGenre.hasOwnProperty('name') && (eventdetails_array.classifications[0].subGenre.name != 'Undefined') && eventdetails_array.classifications[0].subGenre.name != undefined)
                    txt+= eventdetails_array.classifications[0].subGenre.name; 

                if(eventdetails_array.classifications[0].hasOwnProperty('genre'))
                    if(eventdetails_array.classifications[0].genre.hasOwnProperty('name') && (eventdetails_array.classifications[0].genre.name != 'Undefined') && eventdetails_array.classifications[0].genre.name != undefined)
                    txt+= " | "+eventdetails_array.classifications[0].genre.name;

                if(eventdetails_array.classifications[0].segment.hasOwnProperty('name') && (eventdetails_array.classifications[0].segment.name != 'Undefined'))

                    txt+= " | "+eventdetails_array.classifications[0].segment.name;

                if(eventdetails_array.classifications[0].hasOwnProperty('subType'))
                    if(eventdetails_array.classifications[0].subType.hasOwnProperty('name') && (eventdetails_array.classifications[0].subType.name != 'Undefined') && eventdetails_array.classifications[0].subType.name != undefined)
                    txt+= " | "+ eventdetails_array.classifications[0].subType.name;

                if(eventdetails_array.classifications[0].hasOwnProperty('type'))
                    if(eventdetails_array.classifications[0].type.hasOwnProperty('name') && (eventdetails_array.classifications[0].type.name != 'Undefined') && eventdetails_array.classifications[0].type.name != undefined)
                    txt+= " | "+ eventdetails_array.classifications[0].type.name+"</td>";

                txt+="</tr>";
            }

            if(eventdetails_array.hasOwnProperty('priceRanges')){
            txt+="<tr>"; 
            //Has min and max and currency
            if(eventdetails_array.priceRanges[0].hasOwnProperty('min') && (eventdetails_array.priceRanges[0].min != 'Undefined') && eventdetails_array.priceRanges[0].hasOwnProperty('max')){
                txt+="<td><b>Price Ranges</b><br>" + eventdetails_array.priceRanges[0].min+ "-"+ eventdetails_array.priceRanges[0].max;
                if(eventdetails_array.priceRanges[0].hasOwnProperty('currency') && (eventdetails_array.priceRanges[0].currency != 'Undefined'))
                 txt+= " "+eventdetails_array.priceRanges[0].currency+"</td>";
                else
                    txt+="</td>";
                
            }
            //Has only min and currency
            else if(eventdetails_array.priceRanges[0].hasOwnProperty('min') && (eventdetails_array.priceRanges[0].min != 'Undefined'))
            {
                txt+="<td><b>Price Ranges</b><br>" + eventdetails_array.priceRanges[0].min;
                if(eventdetails_array.priceRanges[0].hasOwnProperty('currency') && (eventdetails_array.priceRanges[0].currency != 'Undefined'))
                 txt+= " "+eventdetails_array.priceRanges[0].currency+"</td>";
                else
                    txt+="</td>";
            }
            
            //Has only max and currency
            else if(eventdetails_array.priceRanges[0].hasOwnProperty('max') && (eventdetails_array.priceRanges[0].max != 'Undefined')){
                txt+="<td><b>Price Ranges</b><br>" + eventdetails_array.priceRanges[0].max;
                if(eventdetails_array.priceRanges[0].hasOwnProperty('currency') && (eventdetails_array.priceRanges[0].currency != 'Undefined'))
                 txt+= " "+eventdetails_array.priceRanges[0].currency+"</td>";
                else
                    txt+="</td>";
            }
            txt+="</tr>";
            }

            if(eventdetails_array.hasOwnProperty('dates'))
            if(eventdetails_array.dates.hasOwnProperty('status')){
            txt+="<tr>"; 
            if(eventdetails_array.dates.status.hasOwnProperty('code') && (eventdetails_array.dates.status.code != 'Undefined'))
                txt+="<td><b>Ticket Status</b><br>" + eventdetails_array.dates.status.code+ "</td>";
            txt+="</tr>";}

            if(eventdetails_array.hasOwnProperty('url') && (eventdetails_array.url != 'Undefined')){
            txt+="<tr>"; 
            txt+="<td><b>Buy Ticket at</b><br><a class = 'hover_links' href='"+eventdetails_array.url+"'target='_blank'>Ticketmaster</a></td>";
            txt+="</tr>";}
            
            txt+="</table>"

            txt+="<p id = 'caption1'  style = 'text-align:center;'>Click to show venue info</p>";
            callVenueAPI(encodeURIComponent(eventdetails_array._embedded.venues[0].name));
           
            txt+="<img src='http://csci571.com/hw/hw6/images/arrow_down.png' onclick =createVenueDetails() id = 'arrow_down' width = '40px' height = '20px' style = 'align : center;'   >";
            document.getElementById("addtable").innerHTML = txt;
            down_arrow_div = ' ';
            down_arrow_div+="<p id = 'caption2' style = 'text-align:center;'>Click to show venue photos</p>";
            down_arrow_div += "<img onclick='toggle_down()' id = 'arrow_up' src='http://csci571.com/hw/hw6/images/arrow_down.png' width = 40px height = 20px>";
            document.getElementById("imgDiv").innerHTML = down_arrow_div;
        }
     
     var venueDetails_array; 
     function callVenueAPI(name){
        var req = {
            eventName : name,
            type : "venueDetails"
        };
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                resp = xhttp.responseText;
                venueDetails_array = JSON.parse(resp);
                console.log("Below - Venue");
                console.log(venueDetails_array);
                //createVenueDetails(venueDetails_array);
            }
        };
        xhttp.open("POST", "main.php",true);
        xhttp.send(JSON.stringify(req));
     }
    
    var count_down = 0;
    function createVenueDetails(){
            var venue_info = ' ';
            var x = document.getElementById("upDiv");
            if (document.getElementById("arrow_down").src == "http://csci571.com/hw/hw6/images/arrow_up.png") 
                {
                    //will enter this loop only when I want to close the div and hide the table
                    document.getElementById("arrow_down").src = "http://csci571.com/hw/hw6/images/arrow_down.png";
                    document.getElementById("caption1").innerHTML = "Click to show venue info";
                    x.style.display = "none";
                }
            else 
                {
                    //will enter this loop when I am first creating the table
                    document.getElementById("arrow_down").src = "http://csci571.com/hw/hw6/images/arrow_up.png";
                    document.getElementById("caption1").innerHTML = "Click to hide venue info";
                    x.style.display = "block";
                    
                    if(venueDetails_array.page.totalElements != 0)
                    {
                        venue_info+="<table id = 'test' align = 'center' border = '1'>";
                        venue_info+="<tr align ='center'><td align = right><b>Name</b></td><td colspan = '2'>";
                        if(venueDetails_array.hasOwnProperty('_embedded'))
                           {
                               if(venueDetails_array._embedded.hasOwnProperty('venues'))
                                if(venueDetails_array._embedded.venues[0].hasOwnProperty('name') && venueDetails_array._embedded.venues[0].name!='Undefined' && venueDetails_array._embedded.venues[0].name!=undefined)
                                    venue_info+= venueDetails_array._embedded.venues[0].name+"</td></tr>";
                           }
                        else
                            {
                                venue_info+= "N/A</td></tr>";
                            }
                        venue_info+="<tr align = 'center'><td align = right><b>Map</b></td><td style='border-right:0px solid;'><div id = 'modes_div'><p class = 'modes' onclick = directions('WALKING','map','"+venueDetails_array._embedded.venues[0].location.latitude+"','"+venueDetails_array._embedded.venues[0].location.longitude+"') margin-top = '20px;'>Walk there</p><p onclick=directions('BICYCLING','map','"+venueDetails_array._embedded.venues[0].location.latitude+"','"+venueDetails_array._embedded.venues[0].location.longitude+"') class = 'modes'>Bike there</p><p onclick = directions('DRIVING','map','"+venueDetails_array._embedded.venues[0].location.latitude+"','"+venueDetails_array._embedded.venues[0].location.longitude+"') class = 'modes'> Drive there</p></div></td><td align = 'left' style = 'width: 500px; border-left:0px solid;'><div id='map' width = '200px'></div></td></tr>";

                        venue_info+="<tr align='center'><td align = right><b>Address<b></td><td  colspan = '2'>";
                        
                        if(venueDetails_array.hasOwnProperty('_embedded'))
                           {
                               if(venueDetails_array._embedded.hasOwnProperty('venues'))
                                if(venueDetails_array._embedded.venues[0].hasOwnProperty('address'))
                                if(venueDetails_array._embedded.venues[0].address.hasOwnProperty('line1') && venueDetails_array._embedded.venues[0].address.line1 !='Undefined' && venueDetails_array._embedded.venues[0].address.line1 !=undefined)
                                    venue_info+=venueDetails_array._embedded.venues[0].address.line1+"</td></tr>";
                           }
                        else
                            {
                                venue_info+= "N/A</td></tr>";
                            }
                        
                        venue_info+="<tr align='center'><td align = right><b>City<b></td><td  colspan = '2'>";
                         if(venueDetails_array.hasOwnProperty('_embedded'))
                           {
                               if(venueDetails_array._embedded.hasOwnProperty('venues'))
                                if(venueDetails_array._embedded.venues[0].hasOwnProperty('city'))
                                if(venueDetails_array._embedded.venues[0].city.hasOwnProperty('name') && venueDetails_array._embedded.venues[0].city.name != 'Undefined' && venueDetails_array._embedded.venues[0].city.name != undefined)
                                venue_info+= venueDetails_array._embedded.venues[0].city.name+"</td></tr>";
                           }
                        else
                            {
                                venue_info+= "N/A</td></tr>";
                            }
                        
                        venue_info+="<tr align='center'><td align = right><b>Postal code<b></td><td  colspan = '2'>";
                        if(venueDetails_array.hasOwnProperty('_embedded'))
                           {
                               if(venueDetails_array._embedded.hasOwnProperty('venues'))
                                if(venueDetails_array._embedded.venues[0].hasOwnProperty('postalCode') && venueDetails_array._embedded.venues[0].postalCode != 'Undefined' && venueDetails_array._embedded.venues[0].postalCode != undefined)
                                venue_info+= venueDetails_array._embedded.venues[0].postalCode+"</td></tr>";
                           }
                        else
                            {
                                venue_info+= "N/A</td></tr>";
                            }
                        
                        venue_info+="<tr align='center'><td align = right><b>Upcoming Events<b></td><td  colspan = '2'>";
                         if(venueDetails_array.hasOwnProperty('_embedded'))
                           {
                               if(venueDetails_array._embedded.hasOwnProperty('venues'))
                                if(venueDetails_array._embedded.venues[0].hasOwnProperty('name') && venueDetails_array._embedded.venues[0].namee != 'Undefined' && venueDetails_array._embedded.venues[0].name != undefined)
                                venue_info+= "<a class = 'hover_links' href='"+venueDetails_array._embedded.venues[0].url +"' target='_blank'>"+venueDetails_array._embedded.venues[0].name+" Tickets</a></td></tr>";
                        venue_info+="</table>";
                           }
                        else
                            {
                                venue_info+= "N/A</td></tr>";
                            }
                       
                        document.getElementById('upDiv').innerHTML = venue_info;

                    var lat = parseFloat(venueDetails_array._embedded.venues[0].location.latitude);
                    var lng = parseFloat(venueDetails_array._embedded.venues[0].location.longitude);
                    var venue = {lat: lat, lng: lng};
                    var map = new google.maps.Map(
                    document.getElementById('map'), {zoom: 13, center: venue});
                    var marker = new google.maps.Marker({position: venue, map: map}); 
                    }
                else{
                    var venue_info_norecords = "<p align = 'center' class='nophotos'>No Venue Info Found</p>";
                    document.getElementById("upDiv").innerHTML = venue_info_norecords;
                }
                }
        
        //Checking if the Venue Photos div is already open, if it is then close it    
        var y = document.getElementById("downDiv");  
        if (y.style.display == "block") {
            y.style.display = 'none';
            document.getElementById("arrow_up").src = "http://csci571.com/hw/hw6/images/arrow_down.png";
            document.getElementById("caption2").innerHTML = "Click to show venue photos";
        }
    }
    
    count_up = 0;
    function toggle_down(){
        var x = document.getElementById("downDiv");
        if (document.getElementById("arrow_up").src == "http://csci571.com/hw/hw6/images/arrow_up.png")
                {
                    //will enter this loop only to close the div and hide the table
                    document.getElementById("arrow_up").src = "http://csci571.com/hw/hw6/images/arrow_down.png";
                    document.getElementById("caption2").innerHTML = "Click to show venue photos";
                    x.style.display = "none";
                }
            else 
                {
                    //will enter this table first to create table of venue photos
                    document.getElementById("arrow_up").src = "http://csci571.com/hw/hw6/images/arrow_up.png";
                    document.getElementById("caption2").innerHTML = "Click to hide venue photos";
                    x.style.display = "block";
                    if(venueDetails_array.hasOwnProperty('_embedded'))
                {
                    if(venueDetails_array._embedded.venues[0].hasOwnProperty('images'))
                    {
                        var venue_image_text = ' ';
                        venue_image_text+="<table border = '1' style= 'border-collapse: collapse;' align = 'center' width = '900px'";
                        for(var i = 0; i<venueDetails_array._embedded.venues[0].images.length; i++)
                            venue_image_text+="<tr align = 'center'><td align = 'center'><img src='"+ venueDetails_array._embedded.venues[0].images[i].url +"' width= 700px height = 200px /></td></tr>";
                        venue_image_text+="</table>";
                        document.getElementById("downDiv").innerHTML = venue_image_text;
                    }}
                    else{
                        var venue_image_text1 = "<p align = 'center' class='nophotos'>No Venue Photos Found</p>";
                        document.getElementById("downDiv").innerHTML = venue_image_text1;
                    }
                } 
        //Checking if Venue Info div is already open, if it is then close it
         var y = document.getElementById("upDiv");  
         if (y.style.display == "block") {
            y.style.display = 'none';
            document.getElementById("arrow_down").src = "http://csci571.com/hw/hw6/images/arrow_down.png";
            document.getElementById("caption1").innerHTML = "Click to show venue info";
        }
        
    }
    
    //To handle the case with no records
    function print_no_records(){
        var new_txt = ' ';
        new_txt += "<p>No records found</p>";
        document.getElementById("addtable").innerHTML = new_txt;
    }
    
 function initMap() {
            
}
    var new_id, toggle_bit, new_div_id;
    toggle_bit = 0;
    
    function createMap(id,latitude,longitude, divId){
        
        if(toggle_bit!=0 && document.getElementById(id) != new_id && document.getElementById(divId) != new_div_id)
            //&& (document.getElementById(id) == new_id || document.getElementById(id) != new_id)) //different and not 0
        {
            new_id.classList.toggle("show"); 
            new_div_id.classList.toggle("modes_show");
            console.log("in If");
            var lat = parseFloat(latitude);
            var lng = parseFloat(longitude);
            var venue = {lat: lat, lng: lng};
            // The map, centered at Uluru
            var map = new google.maps.Map(
            document.getElementById(id), {zoom: 13, center: venue});
            // The marker, positioned at Uluru
            var marker = new google.maps.Marker({position: venue, map: map});
            new_id = document.getElementById(id);
            new_div_id = document.getElementById(divId);

            new_id.classList.toggle("show");
            new_div_id.classList.toggle("modes_show");
            toggle_bit++;
            
        }
        
        else if(toggle_bit!=0 && document.getElementById(id) == new_id && document.getElementById(divId) == new_div_id)
        {
            console.log("in else If"); 
            new_id.classList.toggle("show"); 
            new_div_id.classList.toggle("modes_show");
            toggle_bit = 0;
        }
        
        else {
        var lat = parseFloat(latitude);
        var lng = parseFloat(longitude);
        var venue = {lat: lat, lng: lng};
        // The map, centered at Uluru
        var map = new google.maps.Map(
        document.getElementById(id), {zoom: 13, center: venue});
        // The marker, positioned at Uluru
        var marker = new google.maps.Marker({position: venue, map: map});
        new_id = document.getElementById(id);
        new_div_id = document.getElementById(divId);
        
        new_id.classList.toggle("show");
        new_div_id.classList.toggle("modes_show");
        toggle_bit++;
        console.log("in else");
       }
    }
    
    function directions(travel_mode, mapId, latitude, longitude){
        console.log("In directions");
        var directionsDisplay = new google.maps.DirectionsRenderer;
        var directionsService = new google.maps.DirectionsService;
        var map = new google.maps.Map(document.getElementById(mapId), {
            zoom: 14,
            center: {lat: 37.77, lng: -122.447}
            });
        directionsDisplay.setMap(map);
        calculateAndDisplayRoute(directionsService, directionsDisplay,travel_mode, latitude, longitude);
    }
    
    function calculateAndDisplayRoute(directionsService, directionsDisplay,mode, lat_dest, lng_dest) {
        console.log("In calc and disp");
        var location_here = document.getElementById('location_here').checked;
        if(location_here == true)
            {
             //Get result from IP-API  
                var lat_origin = parseFloat(resultJSON.lat); 
                var lng_origin = parseFloat(resultJSON.lon);
            }
        else{
            console.log(result_geocode);
            var lat_origin = result_geocode.results[0].geometry.location.lat;
            var lng_origin = result_geocode.results[0].geometry.location.lng;
             //Get result from geocoded API
        }
//        var lat_dest = parseFloat(venueDetails_array._embedded.venues[0].location.latitude);
//    var lng_dest = parseFloat(venueDetails_array._embedded.venues[0].location.longitude);
    
    console.log(lng_origin);
    console.log(lat_origin);
    directionsService.route({  
                origin: {lat: lat_origin, lng: lng_origin}, 
                destination: {lat: parseFloat(lat_dest), lng: parseFloat(lng_dest)},  
                travelMode: google.maps.TravelMode[mode]}, 
                function(response, status) {
                    if (status == 'OK') {
                        directionsDisplay.setDirections(response);
                    } else {
                        window.alert('Directions request failed due to ' + status);
                    }
  });
}
    
</script>

<form id ="myform" style="width:600px; margin:0 auto;" onsubmit="event.preventDefault(); submitData(); ">
    <p id = "formheading" style="width:600px; margin:0 auto; text-align:center; font-size: 30px; font-weight: 200;"><i>Events Search</i></p>
    <br><b>Keyword</b>
    <input id = "keyword" type="text" name="keyword" value="" style = 'margin-bottom:4px;' required>
    <br>
    <b>Category</b>
    <select id = "category"name="category" style = 'margin-bottom:4px;'>
      <option selected = "selected" value="">Default</option>
      <option value="KZFzniwnSyZfZ7v7nJ">Music</option>
      <option value="KZFzniwnSyZfZ7v7nE">Sports</option>
      <option value="KZFzniwnSyZfZ7v7na">Arts & Theater</option>
      <option value="KZFzniwnSyZfZ7v7nn">Film</option>
      <option value="KZFzniwnSyZfZ7v7n1">Miscellaneous</option>
    </select><br>

    <b>Distance(miles)</b>
    <input id="miles" type="number" name="miles" placeholder="10" style = 'margin-bottom:4px;' value="">
    from 
    <input id = "location_here" type="radio" name="location" value="here" checked = "checked" <?php if(isset($_POST['location'])  == 'Yes')  echo ' checked="checked"'; ?> onClick = "location_text.disabled = true"> Here<br>
    
    <input id = "location_custom" type="radio" name="location" value="" onClick = "location_text.disabled =false" style="margin-left:310px;" ><input disabled="true" id="location_text" type="text" name="custom_location" placeholder ="location" required><br>
    
    <input style='margin-left:60px; margin-top:20px; margin-right:3px;' id = "search_button" type="submit" name="submit" value="Search" disabled>
    
    <input id = "clear" type="button" value="Clear" onClick = "resetForm()">
</form>
    
<div id ="addtable">
</div>
    
<div id="upDiv">
</div>
    
<div id="imgDiv">
</div>
    
<div id="downDiv">
</div> 
  </body>
</html>



