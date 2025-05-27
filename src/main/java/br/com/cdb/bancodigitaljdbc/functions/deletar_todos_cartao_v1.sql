CREATE OR REPLACE FUNCTION public.deletar_todos_cartao_v1()
RETURNS void
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	DELETE FROM cartao;
END;
$BODY$;