CREATE OR REPLACE FUNCTION public.deletar_cliente_v1(p_id BIGINT)
RETURNS VOID
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	DELETE FROM cliente WHERE id = p_id;
END;
$BODY$;