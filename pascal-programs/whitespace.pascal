program a; 
  var i: integer;
  procedure b(); 
    var j: integer;
    function c(absdf:integer):integer;
      var k: integer;
    begin
      i:=j;
      j:=k {ali so tukaj lokalne j in k, ali samo k?}
    end;
  begin
    i:=j {i = globalna, j = lokalna}
  end;
begin
  {nimamo lokalnih spremenljivk}
end.