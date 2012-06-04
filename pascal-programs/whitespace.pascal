program queens;
    const
        chessboard_size = 8;
    var used_cols : array [1..9] of integer;
begin
    used_cols[1] := 13;
    putint(used_cols[1])
end.