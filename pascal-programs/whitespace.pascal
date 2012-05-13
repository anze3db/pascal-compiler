program HelloWorld;

var
    a:boolean;
    b:^char;
    da:integer;

procedure p();
var
    a:integer;
begin
    a := 6
end;
function f(t:integer):boolean;
var
    a:char;
begin
    a := 'd';
    f(4);
    f(4)
end;
function f2():integer;
begin
end;
begin

    a := true and f(3);
    da := f2;
    f(da);
    b^ := 'f'
end.