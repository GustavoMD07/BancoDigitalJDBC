CREATE OR REPLACE FUNCTION public.deletar_todos_contas_v1(
	)
    RETURNS boolean
    LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	DELETE FROM conta;
	IF FOUND THEN
		RETURN true;
	ELSE
		RETURN false;
	END IF;
END;
$BODY$;