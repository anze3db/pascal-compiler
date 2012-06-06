program queens;
    const
        chessboard_size = 8;
    var used_cols : array [1..chessboard_size] of integer;
begin
    used_cols[1] := 1;
    putint(used_cols[1])
end.