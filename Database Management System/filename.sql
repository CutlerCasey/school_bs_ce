create or replace procedure printWhat is
begin
DBMS_OUTPUT.PUT_LINE('What book# is beign checked out by Patron#?');
end;
/

create or replace procedure checkOutBook(refBid in char, refPid in char) is
interDate date;
error varchar(1);
interBid char(6);
interPid char(6);

begin
error := 1;
select books.id into interBid from books where id = refBid;

error := 2;
select patrons.id into interPid from patrons where id = refPid;
select books.ckout_date into interDate from books where id = refBid;

if interDate is null then
update books set ckout_date = sysdate where id = refBid;
update books set p_id = refPid where id = refBid;
DBMS_OUTPUT.PUT_LINE('Checked out book.');
--print 'Book check out success';
--commit;
else DBMS_OUTPUT.PUT_LINE('Book already checked out.');
end if;

exception
when no_data_found then
if (error = 1) then DBMS_OUTPUT.PUT_LINE('Lack of book.');
else DBMS_OUTPUT.PUT_LINE('Lack of patron.');
end if;
when others then DBMS_OUTPUT.PUT_LINE('Unknown Error.');

end;
/
execute printWhat;
execute checkOutBook(&Book, &Patron);