{
        Testna datoteka s čimveč kombinacijami za testiranje
}
program kr_neki;
const a=5;
          n=true;
          k='A';
          j=false;
type
        neki=integer;
        janez=char;
        janez=boolean;
        neki = array[1..5] of integer;
        neki = array[1..5] of janez;

var n:integer;
        neki : array[1..5] of integer;
        neki: record
                        neki : (integer);
                        pojdi: boolean;
                        neki : array[1..5] of integer;
                        neki : record
                                                testni:integer;
                                                neki : array[1..5] of integer
                                   end
                  end;
        pointer: ^neki;


begin
        {komentar...}
        neki:=5+6+generacija(1988)*6+5;
        neki+-1^.neki:=1
        
end.