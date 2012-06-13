program kr_neki;
	const 
		f = 2;
		t = f*5;
	type
		int = integer;
		r = record
			a:integer;
			bb:integer;
			c : record
				a:int
			end
		end; 
	var 
		a : r;
		b : integer;
begin
	a.c.a := b
end.