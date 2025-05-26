CREATE OR REPLACE FUNCTION public.atualizar_saldo_v1(p_saldo NUMERIC, p_id BIGINT)
RETURNS VOID
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	UPDATE saldo_moeda
	SET saldo = p_saldo
	WHERE id = p_id;
END;
$BODY$;