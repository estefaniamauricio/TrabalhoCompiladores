  function onAddTable() {
    $('#createTableDialog').dialog();
  }
 

  function onCreateTable() {
    var nrofCols = $(':flxtblNrOfCols').val();
    if ( nrofCols < 1) nrofCols = 1;
    	var nrofRows = $(':flxtblNrOfRows').val();
    if ( nrofRows < 1) nrofRows = 1;
    	nrofRows++;
    $('#createTableDialog').dialog('close');
    // Init table
    var content = [];
    for (var r = 0; r < nrofRows; r++ ) {
      var row = [];
      for (var c = 0; c < nrofCols; c++) {
        row.push("");
      }
      content.push(row)
    }
    $('#ownTableUnAvailPrompt').hide();
    $('#ownTableAvailPrompt').show();
    $('#ownTable').flextabledit({
      content: content,
      addTableClass: "myTable",
      texts: { cut: 'Couper', copy: 'Copier', paste: 'Coller', insert: 'Insérer', remove: 'Supprimer', columnName: 'Nom' }
    });
  }
 
  function onDeleteTable() {
    $('#ownTable').flextabledit('destroy');
    $('#ownTableAvailPrompt').hide();
    $('#ownTableUnAvailPrompt').show();
  }
