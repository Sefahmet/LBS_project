 var map;
 var coord1Input = document.getElementById("coord1");
 var coord2Input = document.getElementById("coord2");
 var marker1;
 var marker2;
 var line;
 var pathId;
 var minx = ol.proj.transform([7.081, 50.723], "EPSG:4326", "EPSG:3857")[0];
 var miny = ol.proj.transform([7.081, 50.723], "EPSG:4326", "EPSG:3857")[1];
 var maxx = ol.proj.transform([7.093, 50.730], "EPSG:4326", "EPSG:3857")[0];
 var maxy = ol.proj.transform([7.093, 50.730], "EPSG:4326", "EPSG:3857")[1];


tile_layer = new ol.layer.Tile({ source: new ol.source.OSM() });
var oldZoom = 17;

var map = new ol.Map({
	target: 'map', 
	layers: [
		tile_layer
	], 
	view: new ol.View({
		center: ol.proj.fromLonLat([7.086, 50.727]),
		zoom: oldZoom,
		maxZoom: 20,
		minZoom: 14,
		extent: [minx, miny, maxx, maxy],
	}) 
});
map.on("click", function (e) {
      var position = ol.proj.toLonLat(e.coordinate);
      if (!marker1) {
        marker1 = new ol.Feature({
          geometry: new ol.geom.Point(e.coordinate)
        });
        var markerLayer = new ol.layer.Vector({
          source: new ol.source.Vector({
            features: [marker1]
          })
        });
        map.addLayer(markerLayer);
        coord1Input.value = position[0].toFixed(7) + "," + position[1].toFixed(7);
      } else if (!marker2) {
        marker2 = new ol.Feature({
          geometry: new ol.geom.Point(e.coordinate)
        });
        var markerLayer = new ol.layer.Vector({
          source: new ol.source.Vector({
            features: [marker2]
          })
        });
        map.addLayer(markerLayer);
        coord2Input.value = position[0].toFixed(7) + "," + position[1].toFixed(7);
      } else {
        marker1.getGeometry().setCoordinates(e.coordinate);
        coord1Input.value = position[0].toFixed(7) + "," + position[1].toFixed(7);
      }
    });

  function findShortestPath() {
    console.log("this function is findShortestPath version 2")

    var coord1 = coord1Input.value.split(",");
    var coord2 = coord2Input.value.split(",");
    var lat1 = parseFloat(coord1[0]);
    var lon1 = parseFloat(coord1[1]);
    var lat2 = parseFloat(coord2[0]);
    var lon2 = parseFloat(coord2[1]);
      console.log(lat1,
                  lon1,
                  lat2,
                  lon2)
    const url = `/routing2023_ASA/api/shortestPath/shortest-path-params?lat1=${lat1}&lon1=${lon1}&lat2=${lat2}&lon2=${lon2}`;

    console.log(url)
    fetch(url)
            .then(response => {
              if (response.ok) {
                return response.json();
              } else {
                throw new Error('Connection is unsuccessful.');
              }
            })
            .then(data => {
            
              console.log('Server respond:', data);
              console.log(url);

              pathId = data.pathId;
              drawPath(data.coordinates);
            })
            .catch(error => {
              console.error('Connection is unsuccessful.', error);
            });
    document.getElementById("selectPointButton").disabled = false;
  }

  function drawPath(path) {
    if (line) {
      map.removeLayer(line);
    }

    var points = [];
    for (var i = 0; i < path.length; i++) {
      var coord = path[i];
      if (coord.valid) {
        var point = ol.proj.fromLonLat([coord.x, coord.y]);
        points.push(point);
      }
    }

    var lineString = new ol.geom.LineString(points);
    var lineFeature = new ol.Feature({
      geometry: lineString
    });

    var lineStyle = new ol.style.Style({
      stroke: new ol.style.Stroke({
        color: '#0022ff',
        width: 2,
        opacity: 1
      })
    });
    lineFeature.setStyle(lineStyle);

    var vectorSource = new ol.source.Vector({
      features: [lineFeature]
    });

    line = new ol.layer.Vector({
      source: vectorSource
    });

    map.addLayer(line);
    document.getElementById("selectPointButton").style.display = "block";
  }

  var selectPointButton = document.getElementById("selectPointButton");
  selectPointButton.addEventListener("click", function () {
    // Kullanıcıdan nokta seçmesini isteyin
    map.on("click", function (e) {
      var position = ol.proj.toLonLat(e.coordinate);
      var lat = position[0];
      var lon = position[1];

      // PathId ve kullanıcının seçtiği noktanın koordinatlarıyla yeni bir API isteği gönderin
      const infoUrl = `/routing2023_ASA/api/building/infoLatLon?pathId=${pathId}&lat=${lat}&lon=${lon}`;
      console.log(infoUrl);
      fetch(infoUrl)
              .then(response => {
                if (response.ok) {
                  return response.text();
                } else {
                  //throw new Error('Connection is unsuccessful.');
                }
              })
              .then(data => {
                // Dönen bilgiyi konsola yazdır
                console.log('Connection is successfull:', data);
                speak(data);
              })
              .catch(error => {
                  console.log(error)
                console.error('Connection is unsuccessful.', error);
              });
    });

  });
  function speak(text) {
    if ('speechSynthesis' in window) {
      var msg = new SpeechSynthesisUtterance();
      msg.text = text;
      msg.lang = "en-GB"; // Set the language to English (UK)
      window.speechSynthesis.speak(msg);
    } else {
      console.error('Your browser doesnt support voice features.');
    }
  }
