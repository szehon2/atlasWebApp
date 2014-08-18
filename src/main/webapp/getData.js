function getGeoUnitTypeData() {
  var geoUnitTypeData = [];

  var doneFunction = function(geoUnitTypeServer) {
    for (var i = 0; i < geoUnitTypeServer.length; i++) {
      var geoUnitType = geoUnitTypeServer[i];
      var geoUnitTypeChildrenData = [];
      for (var j = 0; j < geoUnitType.geoObjects.length; j++) {
        var geoUnitTypeChild = geoUnitType.geoObjects[j];
        var geoUnitTypeChildData = {
          id : geoUnitTypeChild.name.name,
          text : geoUnitTypeChild.name.name,
          translation : geoUnitTypeChild.name.translation ? geoUnitTypeChild.name.translation.translation
              : "",
          transliteration : geoUnitTypeChild.name.translation ? geoUnitTypeChild.name.translation.transliteration
              : "",
          language : geoUnitTypeChild.name.translation ? geoUnitTypeChild.name.translation.language
              : "",
          isNative : geoUnitTypeChild.name.translation ? geoUnitTypeChild.name.translation.isNative
              : "",
          aliases : []
        }

        if (geoUnitTypeChild.geoAliases) {
          for (var k = 0; k < geoUnitTypeChild.geoAliases.length; k++) {
            var geoAliasServer = geoUnitTypeChild.geoAliases[k];
            var geoAliasData = {
              name : geoAliasServer.otherName.name,
              translation : geoAliasServer.otherName.translation ? geoAliasServer.otherName.translation.translation
                  : "",
              transliteration : geoUnitTypeChild.name.translation ? geoUnitTypeChild.name.translation.transliteration
                  : "",
              language : geoAliasServer.otherName.translation ? geoAliasServer.otherName.translation.language
                  : "",
              isNative : geoAliasServer.otherName.translation ? geoAliasServer.otherName.translation.isNative
                  : "",
            };
            geoUnitTypeChildData.aliases.push(geoAliasData);
          }
        }
        geoUnitTypeChildrenData.push(geoUnitTypeChildData);
      }
      var geoUnitTypeDataUnit = {
        id : geoUnitType.uniqueId,
        text : geoUnitType.uniqueId,
        children : geoUnitTypeChildrenData
      };
      geoUnitTypeData.push(geoUnitTypeDataUnit);
    }

  }

  $.ajax({
    dataType : "json",
    url : "historyAtlas",
    data : {
      "method" : "getGeoTypes"
    },
    success : doneFunction,
    async : false
  });

  return geoUnitTypeData;
}

function getGeoUnitType(parent, child, geoUnitTypeData) {
  for (var i = 0; i < geoUnitTypeData.length; i++) {
    var geoUnitTypeParent = geoUnitTypeData[i];
    if (geoUnitTypeParent.id == parent) {
      for (var k = 0; k < geoUnitTypeParent.children.length; k++) {
        var geoUnitTypeChild = geoUnitTypeParent.children[k];
        if (geoUnitTypeChild.id == child) {
          return geoUnitTypeChild;
        }
      }
    }
  }
}


function clearGeoUnitTypeForm() {
  $("#geounit-type-form #aliasFieldSet #subFieldSet").empty();
  $("#geounit-type-form #aliasFieldSet #add").unbind();
}



/*
This code looks-up the selected js-tree value from the map (geoUnitTypeData),
and then populates the form using the selected value.ß
*/
function fillSelectedForm(geoUnitTypeData) {
  var ref = $('#geounit-type-tree').jstree(true);
  var sel = ref.get_selected();
  if (!sel.length) {
    return false;
  }
  sel = sel[0]; // id of node

  var parent = ref.get_parent(sel);
  var childJson = getGeoUnitType(parent, sel, geoUnitTypeData);
  console.log(childJson);
  fillSelectedFormMethod(childJson);
  
}

function fillSelectedFormMethod(childJson) {
  $("#geounit-type-form [name=id]").val(childJson.id);
  $("#geounit-type-form [name=name]").val(childJson.text);
  $("#geounit-type-form [name=translation]").val(childJson.translation);
  $("#geounit-type-form [name=transliteration]").val(childJson.transliteration);
  $("#geounit-type-form [name=language]").val(childJson.language);

  var counter = $("#geounit-type-form").find("#aliasFieldSet").length + 1;
  var addButton = $("#geounit-type-form #aliasFieldSet #add");

  
  //clear the children.
  addButton.click(
      function() {
        var divClass = $('<div class="multi-fields" name="aliases" id="alias'
            + counter + '"/>');
        $("#geounit-type-form #aliasFieldSet #subFieldSet").append(divClass);
        var inputCol1 = '<label2>otherName</label2> <input type="text" name="otherName"/>';
        var inputCol2 = '<label2>language</label2> <input type="text" name="language"/>'
        var inputCol3 = '<label2>beginYear</label2> <input type="number" name="beginYear"/>'
        var inputCol4 = '<label2>endYear</label2> <input type="number" name="endYear"/>'
        var remove = $('<a href="#step2">Remove</a>');
        $(divClass).append(inputCol1);
        $(divClass).append(inputCol2);
        $(divClass).append(inputCol3);
        $(divClass).append(inputCol4);
        $(divClass).append(remove);
        $(divClass).append("<br>");
        counter++;
        remove.click(function() {
          $(this).parent().remove();
        });
      });
}