1 

{program HelloWorld;

{$EXTENDED}

procedure say_Hello_World();
begin
    write('H');
    write('e');
    write('l');
    write('l');
    write('o');
    write(' ');
    write('W');
    write('o');
    write('r');
    write('l');
    write('d');
    write('!');
    writeln();
end;

var i,j: integer,
    c: char, { komentar { ki je {gnezden} } }
    is_This_A_Weird_Name_That_is_valid: boolean;
    
begin
    c := 'd';
    c := ''''; 
    i := 10;
    is_This_A_Weird_Name_That_is_valid := true;

    for j := 1 to i do
    begin
        say_Hello_World(i);
    end;
end.}