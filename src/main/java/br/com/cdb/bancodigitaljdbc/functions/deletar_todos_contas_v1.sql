CREATE OR REPLACE FUNCTION public.deletar_todos_contas_v1()
RETURNS void
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	DELETE FROM conta;
END;
$BODY$;