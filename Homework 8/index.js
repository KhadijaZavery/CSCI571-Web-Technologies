const express = require('express');
const request = require('request');
var path = require('path');
var geohash = require('ngeohash');
var SpotifyWebApi = require('spotify-web-api-node');
var sortJsonArray = require('sort-json-array');
var ProgressBar = require('progressbar.js')

const app = express();


app.use(express.static(path.join(__dirname, 'public')));

/*app.get('/ip-api', function(req, res){
    console.log(req.query.latitude);//Gets the latitude value for testing
    });*/

//Calls autocompelete API
app.get('/autocomplete', function(req, res){
    request('https://app.ticketmaster.com/discovery/v2/suggest?apikey='+req.query.api_key+
    	'&keyword='+req.query.keyword, function (error, response, body) 
		{
        if (!error && response.statusCode == 200) 
        {
            res.send(body);
        } 
       
 		else {
            res.send(error);
        }
    });
    });

var firstMethod = function() {
   var promise = new Promise(function(resolve, reject){
      setTimeout(function() {
         app.get('/ip-api', function(req, res){
         console.log('IP - API method completed');
    	 //console.log(req.query.latitude);
    	 resolve(geohash.encode(req.query.latitude, req.query.longitude));
      }); }, 2000);
   });
   return promise;
};
 
var secondMethod = function(encoded) {
   var promise = new Promise(function(resolve, reject){
      setTimeout(function() {
      	console.log('TM Event Search completed for here');
      	app.get('/ticketmastersearch', function(req, res) {
		request('https://app.ticketmaster.com/discovery/v2/events.json?apikey=ZA2wHGFGfj8l9RjHFH5BysxNyuPgViW0'+
			"&keyword="+req.query.keyword+"&segmentId="+req.query.category+"&radius="+req.query.radius+
			"&unit="+req.query.unit+'&geoPoint='+encoded+'&sort=date,asc',
		function (error, response, body) 
			{
				
        		if (!error && response.statusCode == 200) 
        		{
            		res.send(body);
            		console.log('https://app.ticketmaster.com/discovery/v2/events.json?apikey='+req.query.api_key+
			"&keyword="+req.query.keyword+"&segmentId="+req.query.category+"&radius="+req.query.radius+
			"&unit="+req.query.unit+'&geoPoint='+encoded);
            		//resolve("Success");
        		} 
       
 				else {
            		//res.send(error);
   					
            		console.log("Error");
            		reject(error);
        			}
    			});
			}); }, 3000);
   });
   return promise;
};

firstMethod()
   .then(secondMethod);


var callGeocoding = function() {
   var promise = new Promise(function(resolve, reject){
      setTimeout(function() {
      	//console.log('TM Event Search completed');
      	app.get('/geocoding', function(req, res) {
		request('https://maps.googleapis.com/maps/api/geocode/json?address='+req.query.location+'&key='+req.query.api_key,
		function (error, response, body)
			{
				
        		if (!error && response.statusCode == 200) 
        		{
            		var geocoding = JSON.parse(body);
            		var lat = geocoding.results[0].geometry.location.lat;
            		var lon = geocoding.results[0].geometry.location.lng;
            		console.log("TAKING GEOCODING VALUE");
            		resolve(geohash.encode(lat, lon));
            		//resolve("Success");
        		} 
       
 				else {
            		console.log("Error");
            		reject(error);
        			}
    			});
			}); }, 3000);
   });
   return promise;
};
 
var callEventSearch = function(encoded) {
   var promise = new Promise(function(resolve, reject){
      setTimeout(function() {
      	//console.log('TM Event Search completed for custom');
      	app.get('/ticketmastersearch_custom', function(req, res) {
		request('https://app.ticketmaster.com/discovery/v2/events.json?apikey=ZA2wHGFGfj8l9RjHFH5BysxNyuPgViW0'+
			"&keyword="+req.query.keyword+"&segmentId="+req.query.category+"&radius="+req.query.radius+
			"&unit="+req.query.unit+'&geoPoint='+encoded+'&sort=date,asc',
		function (error, response, body) 
			{
				
        		if (!error && response.statusCode == 200) 
        		{
            		res.send(body);
            		
            		resolve("Success");
        		} 
       
 				else {
            		//res.send(error);
   					console.log('https://app.ticketmaster.com/discovery/v2/events.json?apikey='+req.query.api_key+
			"&keyword="+req.query.keyword+"&segmentId="+req.query.category+"&radius="+req.query.radius+
			"&unit="+req.query.unit+'&geoPoint='+encoded);
            		console.log("Error");
            		reject(error);
        			}
    			});
			}); }, 3000);
   });
   return promise;
};

var thirdMethod = function(someStuff) {
   var promise = new Promise(function(resolve, reject){
      setTimeout(function() {
         //console.log('third method completed');
         //console.log(someStuff);
         //resolve({result: someStuff.newData});
         resolve(someStuff);
      }, 3000);
   });
   return promise;
};
 
callGeocoding()
   .then(callEventSearch)
   .then(thirdMethod);

app.get('/eventdetails', function(req, res){
    request("https://app.ticketmaster.com/discovery/v2/events/" +req.query.id+ "?apikey=ZA2wHGFGfj8l9RjHFH5BysxNyuPgViW0&", 
    	function (error, response, body) 
		{
        if (!error && response.statusCode == 200) 
        {
            res.send(body);
            console.log("EVENT");
            
        } 
       
 		else {
            res.send(error);
        }
    });
    });

app.get('/venuedetails', function(req, res){
    request("https://app.ticketmaster.com/discovery/v2/venues?apikey=ZA2wHGFGfj8l9RjHFH5BysxNyuPgViW0&keyword="+req.query.keyword, 
    	function (error, response, body) 
		{
        if (!error && response.statusCode == 200) 
        {
            res.send(body);
            console.log("VENUE");
            
        } 
       
 		else {
            res.send(error);
        }
    });
    });



// credentials are optional
var spotifyApi = new SpotifyWebApi({
  clientId: 'd70b82b4ed14458bb971bd8c7f465ce1',
  clientSecret: 'cd4a5ec1e1244d2a975df582d900277d',
  //redirectUri: 'http://www.example.com/callback'
});

app.get('/spotifydetails', function(req, res) {
                var artistName = req.query.keyword;
                spotifyApi.clientCredentialsGrant().then(
                    function(data) {
                        accessToken = data.body['access_token'];
                        console.log("The access token expires in "+data.body['expires_in']);
                        console.log('The access token is ' + data.body['access_token']);
                        spotifyApi.setAccessToken(data.body['access_token']);
                        spotifyApi.searchArtists(artistName)
                        .then(function(data) {
                            console.log('Search artists', data.body);
                            res.send(data.body);
                        }, function(err) {
                            console.error(err);
                        });
                    },
                    function(err) {
                        console.log('Something went wrong!', err);
                    }
                );
        });

app.get('/customsearch', function(req, res){
    request('https://www.googleapis.com/customsearch/v1?q='+req.query.keyword+'&cx='+req.query.cx+'&imgSize=small&imgType=news&num=9&searchType=image&key='+req.query.api_key, 
    	function (error, response, body) 
		{
        if (!error && response.statusCode == 200) 
        {
            res.send(body);
            //console.log(body);
        } 
 		else {
            res.send(error);
        }
    });
    });

app.get('/songkicksearch', function(req, res){
    request('https://api.songkick.com/api/3.0/search/venues.json?query='+req.query.venue+'&apikey='+req.query.api_key, 
    	function (error, response, body) 
		{
        if (!error && response.statusCode == 200) 
        {
            res.send(body);
            //console.log("sogkick worked");
            //console.log(body);
        } 
 		else {
            res.send(error);
        }
    });
    });

app.get('/songkickupcoming', function(req, res){
    request('https://api.songkick.com/api/3.0/venues/'+req.query.id+'/calendar.json?apikey='+req.query.api_key, 
    	function (error, response, body) 
		{
        if (!error && response.statusCode == 200) 
        {
            res.send(body);
            console.log("songkick worked");
            //console.log(body);
        } 
 		else {
            res.send(error);
        }
    });
    });

app.get('/sortArray', function(req, res){
	console.log(req.query.array);
	//console.log(JSON.parse(req.query.array[0]));
 	res.send(sortJsonArray(JSON.parse(req.query.array), req.query.property,req.query.sortOrder));
    });

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server listening on port ${PORT}...`);
});
