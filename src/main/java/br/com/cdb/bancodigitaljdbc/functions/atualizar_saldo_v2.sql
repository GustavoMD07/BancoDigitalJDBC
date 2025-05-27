CREATE OR REPLACE FUNCTION public.atualizar_saldo_v2(
	p_saldo numeric,
	p_id bigint)
    RETURNS VOID
    LANGUAGE 'plpgsql'
    
AS $BODY$
BEGIN
	UPDATE saldo_moeda
	SET saldo = p_saldo
	WHERE id = p_id;
END;
$BODY$;